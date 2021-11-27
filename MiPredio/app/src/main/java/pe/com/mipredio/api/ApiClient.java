package pe.com.mipredio.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pe.com.mipredio.services.LoginService;
import pe.com.mipredio.services.PersonaService;
import pe.com.mipredio.services.ProgramacionService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String API_URL = "http://issadent.generics-pharm.pl/public/";
    public static final String API_URL_IMAGE = API_URL + "images/";
    public  static  final  String API_URL_IMAGE_PERSON = API_URL_IMAGE + "person/";


    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                // .baseUrl("http://localhost:8080/api/")
                //.baseUrl("https://predios.anteru.cloud/")
                // .baseUrl("http://issadent.generics-pharm.pl/public/")
                .baseUrl(API_URL)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public static LoginService getLoginService() {
        LoginService loginService = getRetrofit().create(LoginService.class);
        return loginService;
    }

    public static ProgramacionService getProgramacionService() {
        ProgramacionService programcionService = getRetrofit().create(ProgramacionService.class);
        return programcionService;
    }

    public static PersonaService getPersonaService() {
        PersonaService personaService = getRetrofit().create(PersonaService.class);
        return personaService;
    }


}
