package pe.com.mipredio.response;

import com.google.gson.annotations.SerializedName;

public class ProgramacionListaChartResponse {

    @SerializedName("nombres")
    private String nombres;
    @SerializedName("pendiente")
    private String pendiente;
    @SerializedName("registrado")
    private String registrado;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPendiente() {
        return pendiente;
    }

    public void setPendiente(String pendiente) {
        this.pendiente = pendiente;
    }

    public String getRegistrado() {
        return registrado;
    }

    public void setRegistrado(String registrado) {
        this.registrado = registrado;
    }
}
