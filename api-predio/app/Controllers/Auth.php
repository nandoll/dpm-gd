<?php

namespace App\Controllers;

use App\Controllers\BaseController;
use App\Models\Persona;
use App\Models\Rol;
use CodeIgniter\API\ResponseTrait;
use Config\Services;
use Firebase\JWT\JWT;

class Auth extends BaseController
{

    use ResponseTrait;

	public function __construct()
	{
		helper('secure_password');
	}

    public function login()
	{
		try {
			$data = $this->request->getJSON();
			$usuario = $data->username;
			$password = $data->password;

			// Verificar que el usuario exista ...
			$model = new Persona();
			$personaData = $model->where(['correo' => $usuario])->first();
			if ($personaData == null) :
				return $this->failNotFound('Usuario no se encuentra registrado');
			endif;

			// Verificar que la contraseña sea igual al del usuario ... 
			if (verifyPassword($password, $personaData["password"])) :
				$rol = new Rol();
				$rolData = $rol->asObject()->find($personaData["rol_id"]);
				$personaData["rol"] = $rolData->nombres;
				$token = $this->tokenAuthJWT($personaData);
				return $this->respond(array("token" => $token));
			else :
				// return $this->failValidationError('La contraseña no es válida');
				return $this->failValidationErrors('La contraseña no es válida');
			endif;
			// Generar el Token

		} catch (\Exception $e) {
			return $this->failServerError('Ha ocurrido un  error en el servidor');
		}
	}

    private function tokenAuthJWT($usuario): string
	{
		$key = Services::getSecretKey();
		$time = time();
		$payload = array(
			"aud" => base_url(),
			"iat" => $time,
			// "exp" => $time + 60,
			"exp" => $time + 3600,
			"data" => array(
				"nombre" => $usuario["nombres"],
				"username" => $usuario["correo"],
				"rol" => $usuario["rol"]
			)
		);
		$jwt = JWT::encode($payload, $key);
		return $jwt;
	}
    
    public function index()
    {
        //
    }
}
