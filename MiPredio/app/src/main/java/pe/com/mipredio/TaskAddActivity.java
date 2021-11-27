package pe.com.mipredio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pe.com.mipredio.api.ApiClient;
import pe.com.mipredio.request.TaskCompleteRequest;
import pe.com.mipredio.response.ErrorResponse;
import pe.com.mipredio.response.ErrorUtils;
import pe.com.mipredio.response.ProgramacionCompletarResponse;
import pe.com.mipredio.response.ProgramacionDetalleResponse;
import pe.com.mipredio.response.ProgramacionPhotoUploadResponse;
import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.FileUtil;
import pe.com.mipredio.utils.SharedPreference;
import pe.com.mipredio.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskAddActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private View viewMainContent;
    private FloatingActionButton floatingActionButtonImage;
    private ImageView imageViewPredio;
    private ProgressDialog progressDialog;
    private TextInputEditText editTextAddress,
            editTextNames, editTextLastName,
            editTextDocID, editTextPrevDataCollection,
            editTextCurrentDataCollection, editTextComment;

    private String id;
    FusedLocationProviderClient client;

    private String latitude;
    private String longitude;

    private String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        viewMainContent = findViewById(R.id.main_content);
        floatingActionButtonImage = (FloatingActionButton) findViewById(R.id.floatingImage);
        imageViewPredio = (ImageView) findViewById(R.id.imagePredio);
        client = LocationServices.getFusedLocationProviderClient(this);

        id = getIntent().getStringExtra("id"); // ID de la programacion
        fecha = getIntent().getStringExtra("fecha"); // Fecha de la lista
        init();
    }

    private void init() {
        initToolbar();
        checkPermisionLocate();
        initReferences();
        getInfoTask();
        takeCameraGallery(this);
    }
    private void initReferences() {
        this.editTextAddress = (TextInputEditText) findViewById(R.id.textAddress);
        this.editTextNames = (TextInputEditText) findViewById(R.id.textNames);
        this.editTextLastName = (TextInputEditText) findViewById(R.id.textLastName);
        this.editTextDocID = (TextInputEditText) findViewById(R.id.textDocID);
        this.editTextPrevDataCollection = (TextInputEditText) findViewById(R.id.textPreviousDataCollection);
        this.editTextCurrentDataCollection = (TextInputEditText) findViewById(R.id.textCurrentDataCollection);
        this.editTextComment = (TextInputEditText) findViewById(R.id.textComment);

        this.editTextAddress.setEnabled(false);
        this.editTextNames.setEnabled(false);
        this.editTextLastName.setEnabled(false);
        this.editTextDocID.setEnabled(false);
        this.editTextPrevDataCollection.setEnabled(false);

    }
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Registrar informe");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Tools.setSystemBarColor(this, R.color.colorPrimary);
        Tools.setSystemBarLight(this);
    }
    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Tools.isExpireToken(this, this);
    }

    // Agregar iconos en el toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_toolbar, menu);
        MenuItem itemSend = menu.findItem(R.id.action_send_task);
        itemSend.setVisible(false);

        MenuItem itemMap = menu.findItem(R.id.action_map);
        itemMap.setVisible(false);

        MenuItem itemDate = menu.findItem(R.id.action_calendar);
        itemDate.setVisible(false);
        // return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            if (!validate()) {
                return false;
            }
            TaskCompleteRequest taskCompleteRequest = new TaskCompleteRequest();
            taskCompleteRequest.setComentario(this.editTextComment.getText().toString());
            taskCompleteRequest.setLatitud(latitude);
            taskCompleteRequest.setLongitud(longitude);
            taskCompleteRequest.setMedicion(this.editTextCurrentDataCollection.getText().toString());
            saveTask(taskCompleteRequest);
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTask(TaskCompleteRequest taskCompleteRequest) {
        progressDialog = new ProgressDialog(this);
        Tools.showSaveProgressDialog(progressDialog);

        String token = SharedPreference.getDefaultsPreference(Consts.TOKEN, this);
        Call<ProgramacionCompletarResponse> personaProgCompletarResponse = ApiClient.getProgramacionService().programacionCompletar(token, this.id, taskCompleteRequest);
        personaProgCompletarResponse.enqueue(new Callback<ProgramacionCompletarResponse>() {
            @Override
            public void onResponse(Call<ProgramacionCompletarResponse> call, Response<ProgramacionCompletarResponse> response) {
                if (response.isSuccessful()) {
                    Tools.dismissProgressDialog(progressDialog);
                    Toast.makeText(TaskAddActivity.this, "La información ha sido guardada", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(TaskAddActivity.this, TaskListActivity.class);
                    String dateParts[] = fecha.split("-"); // yyyy-mm-dd
                    intent.putExtra("_year", dateParts[0]);
                    intent.putExtra("_month", dateParts[1]);
                    intent.putExtra("_day", dateParts[2]);
                    startActivity(intent);
                } else {
                    ErrorResponse error = ErrorUtils.parseError(response);
                    Toast.makeText(TaskAddActivity.this, "" + error.getMessages().getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProgramacionCompletarResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(TaskAddActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void takeCameraGallery(Context context) {
        floatingActionButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCameraPermission()) {
                    Log.e("PERMISOS", "0");
                    requestCameraPermission();
                } else {
                    Log.e("PERMISOS", "1");
                    final CharSequence[] options = {"Desde cámara", "Desde galería", "Cancelar"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(false);
                    builder.setTitle("Seleccione una opción");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (options[which].equals("Desde cámara")) {
                                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);

                            } else if (options[which].equals("Desde galería")) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                // Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                            } else if (options[which].equals("Cancelar")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0: // Tomar foto
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap img = (Bitmap) data.getExtras().get("data");
                        String imgPath = FileUtil.getPath(TaskAddActivity.this, getImageUri(TaskAddActivity.this, img));
                        Log.e("RUTA_CAMARA", imgPath);
                        uploadImage(Uri.parse(imgPath));
                    }
                    break;
                case 1:  // Galeria
                    if (resultCode == RESULT_OK && data != null) {
                        Uri img = data.getData();
                        String imgPath = FileUtil.getPath(TaskAddActivity.this, img);

                        Log.e("RUTA_GALERIA", imgPath);
                        uploadImage(Uri.parse(imgPath));
                    }
                    break;
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "intuenty", null);
        Log.d("image uri", path);
        return Uri.parse(path);
    }


    private void uploadImage(Uri uri) {
        File file = new File(uri.getPath());  // Uri.parse(uri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        String token = SharedPreference.getDefaultsPreference(Consts.TOKEN, this);
        Call<ProgramacionPhotoUploadResponse> photoUploadResponse = ApiClient.getProgramacionService().programacionPhotoUpload(token, this.id, part);
        photoUploadResponse.enqueue(new Callback<ProgramacionPhotoUploadResponse>() {
            @Override
            public void onResponse(Call<ProgramacionPhotoUploadResponse> call, Response<ProgramacionPhotoUploadResponse> response) {
                if (response.isSuccessful()) {
                    Picasso.get().load(ApiClient.API_URL_IMAGE + response.body().getFile()).into(imageViewPredio);
                    Tools.snackBarWithIconSuccess(TaskAddActivity.this, viewMainContent, "La imagen ha sido actualizada.");
                } else {
                    Tools.snackBarWithIconError(TaskAddActivity.this, viewMainContent, "No fue posible cargar la imagen. Intente nuevamente");
                }

                if (response.code() == Consts.ERROR_UNAUTHORIZED) {
                    Tools.isExpireToken(TaskAddActivity.this, TaskAddActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ProgramacionPhotoUploadResponse> call, Throwable t) {
                Tools.snackBarWithIconWarning(TaskAddActivity.this, viewMainContent, "Error al intentar subir la image.");
            }
        });
    }

    // Validation
    private boolean validate() {
        if (isEmptyFields()) {
            return false;
        }
        return true;
    }

    private boolean isEmptyFields() {
        String msg = "";
        if (editTextAddress.getText().toString().isEmpty()) {
            editTextAddress.setError("Campo requerido *");
            msg = "El campo Dirección es requerida." + Consts.BREAK_LINE;
        }
        if (editTextNames.getText().toString().isEmpty()) {
            editTextNames.setError("Campo requerido *");
            msg += "El campo Nombre es requerida." + Consts.BREAK_LINE;
        }
        if (editTextLastName.getText().toString().isEmpty()) {
            editTextLastName.setError("Campo requerido *");
            msg += "El campo Apellido es requerida." + Consts.BREAK_LINE;
        }
        if (editTextDocID.getText().toString().isEmpty()) {
            editTextDocID.setError("Campo requerido *");
            msg += "El campo DNI es requerido." + Consts.BREAK_LINE;
        }
        if (editTextCurrentDataCollection.getText().toString().isEmpty()) {
            editTextCurrentDataCollection.setError("Campo requerido *");
            msg += "El campo Medición Actual es requerido." + Consts.BREAK_LINE;
        }
        if (!msg.isEmpty()) {
            Tools.showAlertDialog(this, "Registro de Actividad", msg);
            return true;
        }
        Log.e("VALIDACION", "ok");
        editTextAddress.setError(null);
        editTextNames.setError(null);
        editTextLastName.setError(null);
        editTextDocID.setError(null);
        editTextPrevDataCollection.setError(null);
        editTextCurrentDataCollection.setError(null);
        return false;
    }

    // Obtener información de la actividad
    private void getInfoTask() {
        String token = SharedPreference.getDefaultsPreference(Consts.TOKEN, this);
        Call<ProgramacionDetalleResponse> detalle = ApiClient.getProgramacionService().programacionDetalle(token, id);
        detalle.enqueue(new Callback<ProgramacionDetalleResponse>() {
            @Override
            public void onResponse(Call<ProgramacionDetalleResponse> call, Response<ProgramacionDetalleResponse> response) {
                if (response.isSuccessful()) {
                    editTextAddress.setText(response.body().getDireccion());
                    editTextNames.setText(response.body().getNombres());
                    editTextLastName.setText(response.body().getApellidos());
                    editTextDocID.setText(response.body().getDni());
                    editTextPrevDataCollection.setText(response.body().getMedicion_ant());
                    if (!response.body().getFoto().isEmpty()) {
                        // imageViewPredio
                        Picasso.get().load(ApiClient.API_URL_IMAGE + response.body().getFoto()).into(imageViewPredio);
                    }
                } else {
                    ErrorResponse error = ErrorUtils.parseError(response);
                    Toast.makeText(TaskAddActivity.this, "" + error.getMessages().getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProgramacionDetalleResponse> call, Throwable t) {
                Toast.makeText(TaskAddActivity.this, "No fue posible obtener la actividad. Intente nuevamente", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 44:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkPermisionLocate() {
        if (ActivityCompat.checkSelfPermission(TaskAddActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(TaskAddActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                }
            }
        });
    }

    // Permiso de la camara
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        String[] cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, cameraPermissions, Consts.CAMERA_REQUEST_CODE);
    }

}
