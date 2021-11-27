package pe.com.mipredio;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import pe.com.mipredio.api.ApiClient;
import pe.com.mipredio.response.ErrorResponse;
import pe.com.mipredio.response.ErrorUtils;
import pe.com.mipredio.response.ProgramacionDetalleResponse;
import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.SharedPreference;
import pe.com.mipredio.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDetailActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private View viewMainContent;
    private ImageButton imageButtonMap;
    private ProgressDialog progressDialog;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        viewMainContent = findViewById(R.id.main_content);
        imageButtonMap = findViewById(R.id.imageButtonMap);
        id = getIntent().getStringExtra("id"); // ID de la actividad/tarea seleccionada
        initToolbar();
        // mapButton();
        cargarDatos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.isExpireToken(this, this);
    }

    private void mapButton(String latitude, String longitude) {
        imageButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskDetailActivity.this, TaskMapActivity.class);
                // intent.putExtra("taskId", "999"); // Pasar el ID a la activity para mostrar solo 1 marcador de la Tarea
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setTitle("Detalle del informe");
        Tools.setSystemBarColor(this, R.color.colorPrimary);
        Tools.setSystemBarLight(this);
    }

    private void cargarDatos() {
        progressDialog = new ProgressDialog(this);
        Tools.showLoadingProgressDialog(progressDialog);
        String token = SharedPreference.getDefaultsPreference(Consts.TOKEN, this);
        Call<ProgramacionDetalleResponse> detalle = ApiClient.getProgramacionService().programacionDetalle(token, id);
        detalle.enqueue(new Callback<ProgramacionDetalleResponse>() {
            @Override
            public void onResponse(Call<ProgramacionDetalleResponse> call, Response<ProgramacionDetalleResponse> response) {
                if (response.isSuccessful()) {

                    if(!response.body().getFoto().isEmpty()){
                        Picasso.get().load( ApiClient.API_URL_IMAGE + response.body().getFoto() ).into(  (ImageView) findViewById(R.id.imageTask) );
                    }

                    ((TextInputEditText) findViewById(R.id.textNroMedidor)).setText(response.body().getMedidor());
                    ((TextInputEditText) findViewById(R.id.textDNI)).setText(response.body().getDni());
                    ((TextInputEditText) findViewById(R.id.textDireccion)).setText(response.body().getDireccion());
                    ((TextInputEditText) findViewById(R.id.textNombresApellidos)).setText(response.body().getNombres().concat(" ").concat(response.body().getApellidos()));
                    ((TextInputEditText) findViewById(R.id.textLatitudLongitud)).setText(response.body().getLatitud().concat(" ").concat(response.body().getLongitud()));
                    ((TextInputEditText) findViewById(R.id.textMedicionAnterior)).setText(response.body().getMedicion_ant());
                    ((TextInputEditText) findViewById(R.id.textMedicion)).setText(response.body().getMedicion());
                    ((TextInputEditText) findViewById(R.id.textComentario)).setText(response.body().getComentario());
                    Tools.dismissProgressDialog(progressDialog);
                    mapButton(response.body().getLatitud(), response.body().getLongitud());
                } else {
                    ErrorResponse error = ErrorUtils.parseError(response);
                    Toast.makeText(TaskDetailActivity.this, "" + error.getMessages().getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProgramacionDetalleResponse> call, Throwable t) {
                Toast.makeText(TaskDetailActivity.this, "No fue posible obtener la actividad. Intente nuevamente", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}