package pe.com.mipredio.services;

import pe.com.mipredio.request.LoginRequest;
import pe.com.mipredio.response.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("auth/login")
    Call<LoginResponse> loginPersona (@Body LoginRequest loginRequest);

}
