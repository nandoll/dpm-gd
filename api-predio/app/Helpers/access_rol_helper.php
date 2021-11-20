<?php

use App\Models\Rol;
use CodeIgniter\HTTP\Response;
use CodeIgniter\HTTP\ResponseInterface;
// use CodeIgniter\HTTP\RequestInterface;
use Config\Services;
use Firebase\JWT\JWT;

// function validateAccess($roles, $authHeader) : bool
function validateAccess($roles, $jwt) : bool
{
    helper('_jwt');

    if (!is_array($roles)) :
        return false;
    endif;

    /*
    $key = Services::getSecretKey();
    $arr = explode(' ', $authHeader);
    $jwt = $arr[1];
    $jwt = JWT::decode($jwt, $key, ['HS256']);
    */

    $rolmodel = new Rol();
    // $rol = $rolmodel->find($jwt->data->rol);
    // $rol = $rolmodel->where('nombres',$jwt->data->rol)->first();
    $rol = $rolmodel->where('nombres',$jwt->_rol)->first();
    if ($rol == null) :
        return false;
    endif;

    // foreach ($roles as $key => $value) {
    //     if ($value != $rol["nombre"])
    //         return false;
    // }

    if(!in_array($rol["nombres"] , $roles)): 
        return false;
    endif;

    return true;
}
