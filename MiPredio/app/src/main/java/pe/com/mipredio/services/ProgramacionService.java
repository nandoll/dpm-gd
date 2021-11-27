package pe.com.mipredio.services;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import pe.com.mipredio.request.TaskCompleteRequest;
import pe.com.mipredio.response.ProgramacionCerrarResponse;
import pe.com.mipredio.response.ProgramacionCompletarResponse;
import pe.com.mipredio.response.ProgramacionDetalleResponse;
import pe.com.mipredio.response.ProgramacionListaChartResponse;
import pe.com.mipredio.response.ProgramacionListaMapaResponse;
import pe.com.mipredio.response.ProgramacionListaResponse;
import pe.com.mipredio.response.ProgramacionPhotoUploadResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ProgramacionService {
    @POST("api/programaciones/getByDate")
    Call<List<ProgramacionListaResponse>> programacionLista(@Header("Authorization") String authHeader, @Body Map<String, String> raw);

    @POST("api/programaciones/getByDateAndMap")
    Call<List<ProgramacionListaMapaResponse>> programacionListaMapa(@Header("Authorization") String authHeader, @Body Map<String, String> raw);

    @GET("api/programaciones/getById/{id}")
    Call<ProgramacionDetalleResponse> programacionDetalle(@Header("Authorization") String authHeader, @Path("id") String id);

    @PUT("api/programaciones/update/{id}")
    Call<ProgramacionCompletarResponse> programacionCompletar(@Header("Authorization") String authHeader, @Path("id") String id, @Body TaskCompleteRequest taskCompleteRequest);

    @Multipart
    @POST("api/programaciones/upload/{id}")
    Call<ProgramacionPhotoUploadResponse> programacionPhotoUpload(@Header("Authorization") String authHeader, @Path("id") String id, @Part MultipartBody.Part part);

    @PUT("api/programaciones/dayClose")
    Call<ProgramacionCerrarResponse> programacionCerrar(@Header("Authorization") String authHeader, @Body Map<String, String> raw);

    @POST("api/programaciones/getChartByDate")
    Call<List<ProgramacionListaChartResponse>> programacionListaChart(@Header("Authorization") String authHeader, @Body Map<String, String> raw);

}
