package pe.com.mipredio.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("messages")
    @Expose
    private MessagesReponse messages;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public MessagesReponse getMessages() {
        return messages;
    }

    public void setMessages(MessagesReponse messages) {
        this.messages = messages;
    }

}
