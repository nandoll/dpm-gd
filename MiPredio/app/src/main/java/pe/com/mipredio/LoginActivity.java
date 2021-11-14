package pe.com.mipredio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


import pe.com.mipredio.api.ApiClient;
import pe.com.mipredio.request.LoginRequest;
import pe.com.mipredio.response.ErrorResponse;
import pe.com.mipredio.response.ErrorUtils;
import pe.com.mipredio.response.LoginResponse;
import pe.com.mipredio.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private MaterialButton loginBtn;
    private TextView textViewEnterOff;
    private ProgressBar progress_bar;

    private TextInputEditText editTextCorreo;
    private TextInputEditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (MaterialButton) findViewById(R.id.loginIn);
        textViewEnterOff = (TextView) findViewById(R.id.enterOff);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        editTextCorreo = (TextInputEditText) findViewById(R.id.textMail);
        editTextPassword = (TextInputEditText) findViewById(R.id.textPassword);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });

        textViewEnterOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        initToolbar();
    }

    private void loginAccount() {
        if (
                ((TextInputEditText) findViewById(R.id.textMail)).getText().toString().isEmpty() ||
                        ((TextInputEditText) findViewById(R.id.textPassword)).getText().toString().isEmpty()
        ) {
            Tools.showAlertDialog(this, "Aviso", "Ingrese su correo electrónico y contraseña válido");
            return;
        }
        if (!Tools.isEmail(((TextInputEditText) findViewById(R.id.textMail)).getText().toString())) {
            Tools.showAlertDialog(this, "Aviso", "El correo electrónico ingresado no tiene un formato válido");
            return;
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCorreo(editTextCorreo.getText().toString());
        loginRequest.setContrasena(editTextPassword.getText().toString());

        inicioSesion(loginRequest);
        /*
        progress_bar.setVisibility(View.VISIBLE);
        loginBtn.setAlpha(0f);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress_bar.setVisibility(View.GONE);
                loginBtn.setAlpha(1f);
            }
        }, 1000);

        Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
        startActivity(intent);
        */
    }

    public void inicioSesion(LoginRequest loginRequest) {
        loginBtn.setAlpha(0f);

        Call<LoginResponse> loginResponseCall = ApiClient.getLoginService().loginPersona(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                progress_bar.setVisibility(View.GONE);
                loginBtn.setAlpha(1f);

                if (response.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    ErrorResponse error = ErrorUtils.parseError(response);
                    Toast.makeText(LoginActivity.this, "" + error.getMessages().getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                loginBtn.setAlpha(1f);
                String message = t.getLocalizedMessage();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }



    private void initToolbar() {
        Tools.setSystemBarColor(this, R.color.colorWhite);
        Tools.setSystemBarLight(this);
    }

}