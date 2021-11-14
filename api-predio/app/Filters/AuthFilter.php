<?php

namespace App\Filters;

use App\Models\Rol;
use CodeIgniter\API\ResponseTrait;
use CodeIgniter\Filters\FilterInterface;
use CodeIgniter\HTTP\RequestInterface;
use CodeIgniter\HTTP\ResponseInterface;
use Config\Services;
use Firebase\JWT\JWT;

class AuthFilter implements FilterInterface
{
    use ResponseTrait;
    
    /**
     * Do whatever processing this filter needs to do.
     * By default it should not return anything during
     * normal execution. However, when an abnormal state
     * is found, it should return an instance of
     * CodeIgniter\HTTP\Response. If it does, script
     * execution will end and that Response will be
     * sent back to the client, allowing for error pages,
     * redirects, etc.
     *
     * @param RequestInterface $request
     * @param array|null       $arguments
     *
     * @return mixed
     */
    public function before(RequestInterface $request, $arguments = null)
    {
        try {
			$key = Services::getSecretKey();
			$authHeader = $request->getServer('HTTP_AUTHORIZATION');
			if ($authHeader == null) :
				return Services::response()->setStatusCode(ResponseInterface::HTTP_UNAUTHORIZED, 'No se ha enviado el JWT requerido');
			endif;
			//$arr = explode(' ', $authHeader);
			//$jwt = $arr[1];
			// JWT::decode( $jwt , $key, ['HS256'] );
			//$jwt = JWT::decode($jwt, $key, ['HS256']);
            // $jwt = JWT::decode($authHeader,$key,['HS256']);
            $jwt = JWT::decode($authHeader,$key, array('HS256'));
			$rolmodel = new Rol();
            // $rol = $rolmodel->find($jwt->data->rol);
            $rol = $rolmodel->where('nombres',$jwt->data->rol)->first();
			if ($rol == null) :
				return Services::response()->setStatusCode(ResponseInterface::HTTP_UNAUTHORIZED, 'El Rol de JWT no es válido');
			endif;

			return true;
		} catch (\Firebase\JWT\ExpiredException $ee) {
			return Services::response()->setStatusCode(ResponseInterface::HTTP_UNAUTHORIZED, 'El token JWT ha expirado');
		} catch (\Exception $th) {
			return Services::response()->setStatusCode(ResponseInterface::HTTP_INTERNAL_SERVER_ERROR, 'Ocurrio un error en el servidor al validar el toke');
		}
    }

    /**
     * Allows After filters to inspect and modify the response
     * object as needed. This method does not allow any way
     * to stop execution of other after filters, short of
     * throwing an Exception or Error.
     *
     * @param RequestInterface  $request
     * @param ResponseInterface $response
     * @param array|null        $arguments
     *
     * @return mixed
     */
    public function after(RequestInterface $request, ResponseInterface $response, $arguments = null)
    {
        //
    }
}
