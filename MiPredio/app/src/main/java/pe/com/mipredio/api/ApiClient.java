package pe.com.mipredio.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pe.com.mipredio.services.LoginService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                // .baseUrl("http://localhost:8080/api/")
                .baseUrl("http://127.0.0.1/api/")
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public static LoginService getLoginService(){
        LoginService loginService = getRetrofit().create(LoginService.class);
        return loginService;
    }

    
}
