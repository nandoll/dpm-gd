package pe.com.mipredio.model;

import android.graphics.drawable.Drawable;

public class TaskModel {

    private int id;
    private String direccion;
    private String contacto;
    private String latitud;
    private String longitud;
    private String nroDocumento;
    private String medicion;
    private String observacioj;
    private String fechaRegistro;
    private String horaRegistro;
    private String ubigeo;
    private String estado; // registrado, pediente, enviado
    private String personalId;

    public TaskModel(int id, String direccion, String fechaRegistro, String horaRegistro, String ubigeo) {
        this.id = id;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.horaRegistro = horaRegistro;
        this.ubigeo = ubigeo;
    }

    public TaskModel(){

    }
    public TaskModel(int id, String direccion, String contacto, String latitud, String longitud, String nroDocumento, String medicion, String observacioj, String fechaRegistro, String horaRegistro, String ubigeo, String estado, String personalId) {
        this.id = id;
        this.direccion = direccion;
        this.contacto = contacto;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nroDocumento = nroDocumento;
        this.medicion = medicion;
        this.observacioj = observacioj;
        this.fechaRegistro = fechaRegistro;
        this.horaRegistro = horaRegistro;
        this.ubigeo = ubigeo;
        this.estado = estado;
        this.personalId = personalId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getObservacioj() {
        return observacioj;
    }

    public void setObservacioj(String observacioj) {
        this.observacioj = observacioj;
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
