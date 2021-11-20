package pe.com.mipredio.response;

import com.google.gson.annotations.SerializedName;

public class ProgramacionListaMapaResponse {

    @SerializedName("latitud")
    private String latitud;
    @SerializedName("longitud")
    private String longitud;

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
}
