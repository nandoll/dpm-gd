<?php

namespace App\Controllers\Api;

use App\Models\Persona;
use CodeIgniter\RESTful\ResourceController;

class Personas extends ResourceController
{
    public function __construct()
	{
		helper('_jwt');
		helper('access_rol');
	}

    /**
     * Return an array of resource objects, themselves in array format
     *
     * @return mixed
     */
    public function index()
    {
        //
    }

    /**
     * Return the properties of a resource object
     *
     * @return mixed
     */
    public function show($id = null)
    {
        //
    }

    /**
     * Return a new resource object, with default properties
     *
     * @return mixed
     */
    public function new()
    {
        //
    }

    /**
     * Create a new resource object, from "posted" parameters
     *
     * @return mixed
     */
    public function create()
    {
        //
    }

    /**
     * Return the editable properties of a resource object
     *
     * @return mixed
     */
    public function edit($id = null)
    {
        //
    }

    /**
     * Add or update a model resource, from "posted" properties
     *
     * @return mixed
     */
    public function update($id = null)
    {
        //
    }

    /**
     * Delete the designated resource object from the model
     *
     * @return mixed
     */
    public function delete($id = null)
    {
        //
    }

    public function getObtenerTecnicos(){
        $jwt = getHeader($this->request);
        if(!validateAccess( array('jefe') , $jwt )){
            return $this->failServerError('El Rol no tiene permitido la petición realizada.');
        }
        $persona = new Persona();
        $personaData = $persona->asObject()->where("correo",$jwt->data->username)->first();
        $id = $personaData->id;
        $lista = $persona->where("idJefe",$id)->findAll();
        return $this->respond($lista);
    }
}
