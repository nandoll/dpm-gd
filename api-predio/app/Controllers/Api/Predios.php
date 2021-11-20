<?php

namespace App\Controllers\Api;

use App\Models\Predio;
use CodeIgniter\RESTful\ResourceController;

class Predios extends ResourceController
{


    public function __construct()
	{
		$this->model = $this->setModel(new Predio());
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
        try {
			$predio = $this->request->getJSON();
			if ($this->model->insert($predio)) :
				$predio->id = $this->model->insertID();
				return $this->respondCreated($predio);
			else :
				return $this->failValidationErrors($this->model->validation->listErrors());
			endif;
		} catch (\Exception $e) {
			return $this->failServerError('Ha ocurrido un error en el servidor' . $e);
		}
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

    public function getByNumber($nro=null){
        $predio = new Predio();
        $data = $predio->where(['nroMedidor'=>$nro])->first() ;
        if($data == null){
            // return $this->respondNoContent('No se ha encontrado el predio');
            return $this->fail('No se ha encontrado el predio');
        }
        return $this->respond($data);
    }
}
