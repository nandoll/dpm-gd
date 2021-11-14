<?php 
use CodeIgniter\HTTP\RequestInterface;
use Config\Services;
use Firebase\JWT\JWT;

function getHeader(RequestInterface $request){
    $header = $request->getServer('HTTP_AUTHORIZATION');
    $key = Services::getSecretKey();
    $jwt = JWT::decode($header,$key, array('HS256'));
    return $jwt;
}