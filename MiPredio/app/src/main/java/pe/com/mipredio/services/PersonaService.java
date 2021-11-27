package pe.com.mipredio.services;

import java.util.List;

import pe.com.mipredio.response.PersonaListaResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PersonaService {

    @GET("api/personas/technicians")
    Call<List<PersonaListaResponse>> getListaPersonal(@Header("Authorization") String authHeader);

}
