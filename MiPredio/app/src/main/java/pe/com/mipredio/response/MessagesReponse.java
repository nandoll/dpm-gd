package pe.com.mipredio.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessagesReponse {
    @SerializedName("error")
    @Expose
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
