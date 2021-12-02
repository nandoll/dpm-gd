package pe.com.mipredio.model;

import android.graphics.drawable.Drawable;

import java.util.List;

public class TaskModel {

    private String id;
    private String direccion;
    private String contacto;
    private String latitud;
    private String longitud;
    private String nroDocumento;
    private String medicion;
    private String observacion;
    private String fechaRegistro;
    private String horaRegistro;
    private String ubigeo;
    private String estado; // registrado, pediente, enviado
    private String personalId;
    private String nroMedidor;
    private String situacion;
    private String foto;

    public TaskModel(String id, String direccion, String fechaRegistro, String horaRegistro, String ubigeo, String estado, String nroMedidor, String situacion) {
        this.id = id;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.horaRegistro = horaRegistro;
        this.ubigeo = ubigeo;
        this.estado = estado;
        this.nroMedidor = nroMedidor;
        this.situacion = situacion;
    }

    public TaskModel() {

    }


    public TaskModel(String id, String direccion, String contacto, String latitud, String longitud, String nroDocumento, String medicion, String observacion, String fechaRegistro, String horaRegistro, String ubigeo, String estado, String personalId, String nroMedidor, String situacion, String foto) {
        this.id = id;
        this.direccion = direccion;
        this.contacto = contacto;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nroDocumento = nroDocumento;
        this.medicion = medicion;
        this.observacion = observacion;
        this.fechaRegistro = fechaRegistro;
        this.horaRegistro = horaRegistro;
        this.ubigeo = ubigeo;
        this.estado = estado;
        this.personalId = personalId;
        this.nroMedidor = nroMedidor;
        this.situacion = situacion;
        this.foto = foto;
    }

    public TaskModel(String id, String direccion, String contacto, String latitud, String longitud, String nroDocumento, String medicion, String observacion, String fechaRegistro, String horaRegistro, String ubigeo, String estado, String personalId, String nroMedidor, String situacion) {
        this.id = id;
        this.direccion = direccion;
        this.contacto = contacto;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nroDocumento = nroDocumento;
        this.medicion = medicion;
        this.observacion = observacion;
        this.fechaRegistro = fechaRegistro;
        this.horaRegistro = horaRegistro;
        this.ubigeo = ubigeo;
        this.estado = estado;
        this.personalId = personalId;
        this.nroMedidor = nroMedidor;
        this.situacion = situacion;
    }


    public TaskModel(String id, String direccion, String horaRegistro, String ubigeo, String estado, String nroMedidor) {
        this.id = id;
        this.direccion = direccion;
        this.horaRegistro = horaRegistro;
        this.ubigeo = ubigeo;
        this.estado = estado;
        this.nroMedidor = nroMedidor;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getNroMedidor() {
        return nroMedidor;
    }

    public void setNroMedidor(String nroMedidor) {
        this.nroMedidor = nroMedidor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getMedicion() {
        return medicion;
    }

    public void setMedicion(String medicion) {
        this.medicion = medicion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(String horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}
