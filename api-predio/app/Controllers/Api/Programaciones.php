<?php

namespace App\Controllers\Api;

use App\Models\Persona;
use App\Models\Programacion;
use CodeIgniter\RESTful\ResourceController;
use PhpParser\Node\Expr\FuncCall;

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
            if ($programacion == null) {
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
    }
    /**
     * Add or update a model resource, from "posted" properties
     *
     * @return mixed
     */
    public function update($id = null)
    {
        try {
            $json = $this->request->getJSON();
            if ($json == null) {
                return $this->fail("No hay información de envío");
            }
            $json->hora = date("d/m/Y h:i:s");
            $json->estado = STATUS_COMPLETE;
            if ($this->model->update($id, $json)) :
                return $this->respondCreated($json);
            else :
                return $this->failValidationErrors($this->model->validation->listErrors());
            endif;
        } catch (\Exception $e) {
            return $this->failServerError('Ha ocurrido un error en el servidor' . $e);
        }
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
    // public function getByDate($tecnico = null)
    public function getByDate()
    {
        $fecha  = $this->request->getVar("fecha");
        $tecnico  = $this->request->getVar("tecnico"); // correo

        if (!isValidDate($fecha)) {
            return $this->fail("La fecha no tiene un formato válido");
        }

        $jwt = getHeader($this->request);
        $persona = new Persona();
        // $personaData = $persona->asObject()->where("correo",$jwt->data->username)->first();
        $personaData = $persona->asObject()->where("correo", $jwt->_username)->first();
        $id = $personaData->id;

        $programacion = new Programacion();

        if (validateAccess(array('jefe'), $jwt)) {

            $data = $programacion->select('programaciones.id, hora, nroMedidor, ubigeo, direccion, programaciones.estado, DATE_FORMAT(fecha,"%d/%m/%Y") fecha, situacion ')
                ->join('predios', 'programaciones.idPredio=predios.id')
                ->join('personas', 'personas.id = programaciones.idPersona')
                ->where("fecha", $fecha)
                //->where("idPersona", $tecnico)
                ->where("correo", $tecnico)
                ->orderBy('1', 'desc')
                ->findAll();
        } else {

            $data = $programacion->select('programaciones.id, hora, nroMedidor, ubigeo, direccion, estado , DATE_FORMAT(fecha,"%d/%m/%Y") fecha, situacion ')
                ->join('predios', 'programaciones.idPredio=predios.id')
                ->where("fecha", $fecha)
                ->where("idPersona", $id)
                ->orderBy('1', 'desc')
                ->findAll();
        }
        return $this->respond($data);
    }
    /**
     * Obtiene las coordenadas de las programaciones
     */
    // public function getByDateAndMap($tecnico = null)
    public function getByDateAndMap()
    {
        $fecha  = $this->request->getVar("fecha");
        $correo  = $this->request->getVar("correo"); // correo del usuario
        if (!isValidDate($fecha)) {
            return $this->fail("La fecha no tiene un formato válido");
        }

        $jwt = getHeader($this->request);
        $persona = new Persona();
        // $personaData = $persona->asObject()->where("correo",$jwt->data->username)->first();
        $personaData = $persona->asObject()->where("correo", $jwt->_username)->first();
        $id = $personaData->id;

        $programacion = new Programacion();

        if (validateAccess(array('jefe'), $jwt)) {

            $data = $programacion->select(' predios.latitud, predios.longitud ')
                ->join('predios', 'programaciones.idPredio=predios.id')
                ->join('personas', 'personas.id = programaciones.idPersona')
                ->where("fecha", $fecha)
                // ->where("idPersona", $tecnico)
                ->where('correo', $correo)
                ->orderBy('1', 'desc')
                ->findAll();
        } else {

            $data = $programacion->select(' predios.latitud, predios.longitud ')
                ->join('predios', 'programaciones.idPredio=predios.id')
                ->where("fecha", $fecha)
                ->where("idPersona", $id)
                ->orderBy('1', 'desc')
                ->findAll();
        }
        return $this->respond($data);
    }
    /**
     * Completar actividad  por el tecnico de la programación que se le a asignado
     */
    public function fillActivity()
    {
        $dataJSON = $this->request->getJSON();
        $jwt = getHeader($this->request);
        $persona = new Persona();
        $personaData = $persona->asObject()->where("correo", $jwt->_username)->first();
        $id = $personaData->id;

        $programacion = new Programacion();
        $data = $programacion->where("id", $dataJSON->id)->where("idPersona", $id)->first();
        if ($data == null) {
            return $this->fail("No ha sido posible obtener información de la programación");
        }

        $data->foto = $dataJSON->foto;
        $data->latitud = $dataJSON->latitud;
        $data->longitud = $dataJSON->longitud;
        $data->medicion = $dataJSON->medicion;
        $data->comentario = $dataJSON->comentario;
        $data->update();
        return $this->respond(null, null, "Se ha guardado la actividad enviada");
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
            $this->model->update($id, array("foto" => $newfilename));
            return $this->respondUpdated(array(
                'file' => $newfilename
            ), "Archivo ha sido actualizado");
        } else {
            return $this->fail("Error al cargar el archivo");
        }
    }
    /** 
     * 
     */
    public function getById(Int $id)
    {
        $programacion = new Programacion();
        $data = $programacion->asObject()->select('foto, direccion, idPredio,  contactoNombre nombres, contactoApellido apellidos , IF(programaciones.latitud="",predios.latitud,programaciones.latitud) latitud, IF(programaciones.longitud="",predios.longitud,programaciones.longitud) longitud, dni, medicion, comentario, nroMedidor medidor ')
            ->join('predios', 'programaciones.idPredio=predios.id')
            ->where("programaciones.id", $id)
            ->first();
        $predioId = $data->idPredio;
        $dataAnt = $programacion->asObject()->where("idPredio", $predioId)->where("id<", $id)->orderBy('fecha', 'desc')->first();
        $medicionAnt = "No hay";
        if ($dataAnt) {
            $medicionAnt = $dataAnt->medicion;
        }
        $data->medicion_ant = $medicionAnt;
        return $this->respond($data);
    }

    public function dayClose()
    {
        $jwt = getHeader($this->request);

        $dataJSON = $this->request->getJSON(); // { Fecha }
        if ($dataJSON->fecha) {
            $dataJSON->situacion = STATUS_SEND;
            if ($this->model->where("fecha", $dataJSON->fecha)
                ->set(["situacion" => STATUS_SEND])->update()
            ) {
                $this->sendNotification("/topics/cierreDia", array( "title"=>"CIERRE DIARIO", "body" => "El usuario " . $jwt->_username . " ha realizado el cierre del día " . $dataJSON->fecha ));
                return $this->respondCreated($dataJSON);
            } else {
                return $this->failValidationErrors($this->model->validation->listErrors());
            }
            return $this->respond($dataJSON);
        } else {
            return $this->fail("No hay información de envío");
        }
    }

    private function sendNotification($to, $data)
    {
        $api_key = "AAAA7JI_gkA:APA91bHENkLUivNg9oW7OABzpmr5cEHKUUYNh5hHAG2nUM6vkZlb0fuVX_IWIwQDStISGa7d44YPwD7HWB0fkXXr4oYPXfOYdjyvJeiLfWt_ohoDCsc8DSqabXAjcs4pMB_RDvoF4pZB";
        $ch = curl_init();
        $url = "https://fcm.googleapis.com/fcm/send";
        $fields = json_encode(array("to" => $to, "notification" => $data));

        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);

        $headers = array();
        $headers[] = "Authorization:key=" . $api_key;
        $headers[] = "Content-Type:application/json";
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);

        $result = curl_exec($ch);
        if (curl_errno($ch)) {
            echo "Error: " . curl_error($ch);
        }
        curl_close($ch);
    }




    public  function getChartByDate()
    {
        $jwt = getHeader($this->request);
        $dataJSON = $this->request->getJSON(); // { fecha, usuario }
        $programacion = new Programacion();
        $data = null;
        if ($dataJSON) {
            if (validateAccess(array('jefe'), $jwt)) {
                $data = $programacion->select("concat(p2.nombres,' ',p2.apellidoPaterno ) nombres, SUM(programaciones.estado='pendiente') pendiente, SUM(programaciones.estado!='pendiente') registrado")
                    ->join("personas p2", "p2.id = programaciones.idPersona")
                    ->where("fecha", $dataJSON->fecha)
                    ->groupBy("idPersona")
                    ->findAll();
            } else {
                $data = $programacion->select("concat(p2.nombres,' ',p2.apellidoPaterno ) nombres, SUM(programaciones.estado='pendiente') pendiente, SUM(programaciones.estado!='pendiente') registrado")
                    ->join("personas p2", "p2.id = programaciones.idPersona")
                    ->where("fecha", $dataJSON->fecha)
                    ->where("correo", $dataJSON->usuario)
                    ->groupBy("idPersona")
                    ->findAll();
            }
        }
        return $this->respond($data);
    }
}
