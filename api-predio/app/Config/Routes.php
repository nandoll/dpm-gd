<?php

namespace Config;

// Create a new instance of our RouteCollection class.
$routes = Services::routes();

// Load the system's routing file first, so that the app and ENVIRONMENT
// can override as needed.
if (file_exists(SYSTEMPATH . 'Config/Routes.php')) {
    require SYSTEMPATH . 'Config/Routes.php';
}

/*
 * --------------------------------------------------------------------
 * Router Setup
 * --------------------------------------------------------------------
 */
$routes->setDefaultNamespace('App\Controllers');
$routes->setDefaultController('Home');
$routes->setDefaultMethod('index');
$routes->setTranslateURIDashes(false);
$routes->set404Override();
$routes->setAutoRoute(true);

/*
 * --------------------------------------------------------------------
 * Route Definitions
 * --------------------------------------------------------------------
 */

// We get a performance increase by specifying the default
// route since we don't have to scan directories.
$routes->get('/', 'Home::index');
//  API project path
$routes->post('/auth/login', 'Auth::login');
$routes->group('api',['namespace'=>'App\Controllers\api','filter'=>'authFilter'] , function($routes){
    $routes->group('predios',function($routes){
        $routes->post('/create','Predios::create');
        $routes->get('nro/(:num)', 'Predios::getObtenerPorNro/$1');
    });
    $routes->group('programaciones',function($routes){
        $routes->post('/create','Programaciones::create');
        $routes->get('fecha/mapa','Programaciones::getObtenerPorFechaMapa/$1');
        $routes->get('fecha/(:any)','Programaciones::getObtenerPorFecha/$1');
        $routes->get('fecha','Programaciones::getObtenerPorFecha');        
        $routes->put('validar','Programaciones::completarActividad');
        $routes->post('/upload/(:codigo)', 'Programaciones::upload/$1');
    });
    $routes->group('personas',function($routes){
        $routes->get('tecnicos','Personas::getObtenerTecnicos');
    });
    
});


/*
 * --------------------------------------------------------------------
 * Additional Routing
 * --------------------------------------------------------------------
 *
 * There will often be times that you need additional routing and you
 * need it to be able to override any defaults in this file. Environment
 * based routes is one such time. require() additional route files here
 * to make that happen.
 *
 * You will have access to the $routes object within that file without
 * needing to reload it.
 */
if (file_exists(APPPATH . 'Config/' . ENVIRONMENT . '/Routes.php')) {
    require APPPATH . 'Config/' . ENVIRONMENT . '/Routes.php';
}
