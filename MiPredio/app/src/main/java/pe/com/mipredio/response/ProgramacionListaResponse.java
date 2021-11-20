package pe.com.mipredio.response;

import com.google.gson.annotations.SerializedName;

public class ProgramacionListaResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("hora")
    private String hora;
    @SerializedName("nroMedidor")
    private String nroMedidor;
    @SerializedName("ubigeo")
    private String ubigeo;
    @SerializedName("direccion")
    private String direccion;
    @SerializedName("estado")
    private String estado;
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("situacion")
    private String situacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNroMedidor() {
        return nroMedidor;
    }

    public void setNroMedidor(String nroMedidor) {
        this.nroMedidor = nroMedidor;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }
}
