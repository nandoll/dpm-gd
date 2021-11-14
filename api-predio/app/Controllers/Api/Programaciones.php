<?php

namespace App\Controllers\Api;

use App\Models\Persona;
use App\Models\Programacion;
use CodeIgniter\RESTful\ResourceController;

class Programaciones extends ResourceController
{
    public function __construct()
	{
		helper('other');
        helper('_jwt');
        helper('access_rol');

        $this->model = $this->setModel(new Programacion());

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
			$programacion = $this->request->getJSON();
            if($programacion == null){
                return $this->fail("No hay información para el registro");
            }
            $programacion->estado = STATUS_PENDING;

			if ($this->model->insert($programacion)) :
				$programacion->id = $this->model->insertID();
				return $this->respondCreated($programacion);
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

    /**
     * Obtiene las programaciones por fecha del técnico
     */
    public function getObtenerPorFecha($tecnico=null){
        $fecha  = $this->request->getVar("fecha");
        if(!isValidDate($fecha)){
            return $this->fail("La fecha no tiene un formato válido");
        }

        $jwt = getHeader($this->request);
        $persona = new Persona();
        $personaData = $persona->asObject()->where("correo",$jwt->data->username)->first();
        $id = $personaData->id;

        $programacion = new Programacion();
                
        if(validateAccess( array('jefe') , $jwt )){
            
            $data = $programacion->select('programaciones.id, hora, nroMedidor, ubigeo, direccion, estado ')
            ->join('predios','programaciones.idPredio=predios.id')
            ->where("fecha",$fecha)
            ->where("idPersona",$tecnico)
            ->orderBy('1','desc')
            ->findAll();

        }else{

            $data = $programacion->select('programaciones.id, hora, nroMedidor, ubigeo, direccion, estado ')
            ->join('predios','programaciones.idPredio=predios.id')
            ->where("fecha",$fecha)
            ->where("idPersona",$id)
            ->orderBy('1','desc')
            ->findAll();

        }               
        return $this->respond($data);
    }

    /**
     * Obtiene las coordenadas de las programaciones
     */
    public function getObtenerPorFechaMapa($tecnico=null){
        $fecha  = $this->request->getVar("fecha");
        if(!isValidDate($fecha)){
            return $this->fail("La fecha no tiene un formato válido");
        }

        $jwt = getHeader($this->request);
        $persona = new Persona();
        $personaData = $persona->asObject()->where("correo",$jwt->data->username)->first();
        $id = $personaData->id;

        $programacion = new Programacion();
                
        if(validateAccess( array('jefe') , $jwt )){
            
            $data = $programacion->select(' predios.latitud, predios.longitud ')
            ->join('predios','programaciones.idPredio=predios.id')
            ->where("fecha",$fecha)
            ->where("idPersona",$tecnico)
            ->orderBy('1','desc')
            ->findAll();

        }else{

            $data = $programacion->select(' predios.latitud, predios.longitud ')
            ->join('predios','programaciones.idPredio=predios.id')
            ->where("fecha",$fecha)
            ->where("idPersona",$id)
            ->orderBy('1','desc')
            ->findAll();

        }               
        return $this->respond($data);
    }

    /**
     * Completar actividad  por el tecnico de la programación que se le a asignado
     */
    public function completarActividad(){
        $dataJSON = $this->request->getJSON();
        $jwt = getHeader($this->request);
        $persona = new Persona();
        $personaData = $persona->asObject()->where("correo",$jwt->data->username)->first();
        $id = $personaData->id;

        $programacion = new Programacion();
        $data = $programacion->where("id",$dataJSON->id)->where("idPersona",$id)->first();
        if($data == null){
            return $this->fail("No ha sido posible obtener información de la programación");
        }

        $data->foto = $dataJSON->foto;
        $data->latitud = $dataJSON->latitud;
        $data->longitud = $dataJSON->longitud;
        $data->medicion = $dataJSON->medicion;
        $data->comentario = $dataJSON->comentario;
        $data->update();
        return $this->respond(null,null,"Se ha guardado la actividad enviada");
        /*
         {
            "id" : 123,
            "foto" : "",
            "latitud" : "",
            "longitud" : "",
            "medicion" : "",
            "comentario": ""
        }
         */
        // return $this->respond($programacion);
    }

    /**
     * Cargar imagen
     */
    public function upload($id = null)
    {
        $file = $this->request->getFile('image');
        $profile_image = $file->getName();
        $temp = explode(".", $profile_image);
        $newfilename = 'programacion_' . round(microtime(true)) . '.' . end($temp);
        if ($file->move("images", $newfilename)) {
            $this->model->update($id,array("foto"=>$newfilename));
            return $this->respondUpdated(array(
                'file'=>$newfilename
            ),"Archivo ha sido actualizado");
        } else {
            return $this->fail("Error al cargar el archivo");
        }
    }

}