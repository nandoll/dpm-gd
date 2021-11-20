package pe.com.mipredio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import pe.com.mipredio.api.ApiClient;
import pe.com.mipredio.request.TaskCompleteRequest;
import pe.com.mipredio.response.ErrorResponse;
import pe.com.mipredio.response.ErrorUtils;
import pe.com.mipredio.response.ProgramacionCompletarResponse;
import pe.com.mipredio.response.ProgramacionDetalleResponse;
import pe.com.mipredio.utils.Consts;
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
        takeCameraGallery();
        initReferences();
        getInfoTask();
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

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setTitle("Registrar informe");
        Tools.setSystemBarColor(this, R.color.colorPrimary);
        Tools.setSystemBarLight(this);
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
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

        return true;
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
                    // Tools.snackBarWithIconSuccess(TaskAddActivity.this, viewMainContent, "La información ha sido guardada");
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

    private void takeCameraGallery() {
        floatingActionButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TaskAddActivity.this, "Hola Mundo", Toast.LENGTH_SHORT).show();
                ImagePicker.Companion.with(TaskAddActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ((LinearLayout) findViewById(R.id.layoutProfile)).setBackground(getResources().getDrawable(R.drawable.bg_no_item_city));
        Uri uri = data.getData();
        imageViewPredio.setImageURI(uri);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
}
