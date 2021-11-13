package pe.com.mipredio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import pe.com.mipredio.utils.Tools;

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
            editTextCurrentDataCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        viewMainContent = findViewById(R.id.main_content);
        floatingActionButtonImage = (FloatingActionButton) findViewById(R.id.floatingImage);
        imageViewPredio = (ImageView) findViewById(R.id.imagePredio);
        init();
    }

    private void init() {
        initToolbar();
        takeCameraGallery();
        initReferences();
    }

    private void initReferences() {
        this.editTextAddress = (TextInputEditText) findViewById(R.id.textAddress);
        this.editTextNames = (TextInputEditText) findViewById(R.id.textNames);
        this.editTextLastName = (TextInputEditText) findViewById(R.id.textLastName);
        this.editTextDocID = (TextInputEditText) findViewById(R.id.textDocID);
        this.editTextPrevDataCollection = (TextInputEditText) findViewById(R.id.textPreviousDataCollection);
        this.editTextCurrentDataCollection = (TextInputEditText) findViewById(R.id.textCurrentDataCollection);
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
            // Tools.snackBarWithIconSuccess(this, viewMainContent, "La toma de datos ha sido registrado.");
            //if (!validate()) {
                // ...
                // ...
                progressDialog = new ProgressDialog( this );
                Tools.showSaveProgressDialog(progressDialog);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Tools.dismissProgressDialog(progressDialog);
                        Toast.makeText(TaskAddActivity.this, "Cerrando", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            //}
        }
        return super.onOptionsItemSelected(item);
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
            msg = "El campo Dirección es requerida." + Tools.BREAK_LINE;
        }
        if (editTextNames.getText().toString().isEmpty()) {
            editTextNames.setError("Campo requerido *");
            msg += "El campo Nombre es requerida." + Tools.BREAK_LINE;
        }
        if (editTextLastName.getText().toString().isEmpty()) {
            editTextLastName.setError("Campo requerido *");
            msg += "El campo Apellido es requerida." + Tools.BREAK_LINE;
        }
        if (editTextDocID.getText().toString().isEmpty()) {
            editTextDocID.setError("Campo requerido *");
            msg += "El campo DNI es requerido." + Tools.BREAK_LINE;
        }
        if (editTextPrevDataCollection.getText().toString().isEmpty()) {
            editTextPrevDataCollection.setError("Campo requerido *");
            msg += "El campo Medición Anterior es requerido." + Tools.BREAK_LINE;
        }
        if (editTextCurrentDataCollection.getText().toString().isEmpty()) {
            editTextCurrentDataCollection.setError("Campo requerido *");
            msg += "El campo Medición Actual es requerido." + Tools.BREAK_LINE;
        }
        if (!msg.isEmpty()) {
            Tools.showAlertDialog(this, "Registro de Actividad", msg);
            return false;
        }
        editTextAddress.setError(null);
        editTextNames.setError(null);
        editTextLastName.setError(null);
        editTextDocID.setError(null);
        editTextPrevDataCollection.setError(null);
        editTextCurrentDataCollection.setError(null);
        return true;
    }

}
