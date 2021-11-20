package pe.com.mipredio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


import org.json.JSONException;

import pe.com.mipredio.api.ApiClient;
import pe.com.mipredio.classes.TokenClass;
import pe.com.mipredio.request.LoginRequest;
import pe.com.mipredio.response.ErrorResponse;
import pe.com.mipredio.response.ErrorUtils;
import pe.com.mipredio.response.LoginResponse;
import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.SharedPreference;
import pe.com.mipredio.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private MaterialButton loginBtn;
    private MaterialButton materialButtonEnterOff;
    private ProgressBar progress_bar;

    private TextInputEditText editTextCorreo;
    private TextInputEditText editTextPassword;

    private View viewMainContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewMainContent = findViewById(R.id.main_content);

        loginBtn = (MaterialButton) findViewById(R.id.loginIn);
        materialButtonEnterOff = (MaterialButton) findViewById(R.id.enterOff);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        editTextCorreo = (TextInputEditText) findViewById(R.id.textMail);
        editTextPassword = (TextInputEditText) findViewById(R.id.textPassword);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });

        materialButtonEnterOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference.setDefaultsPreference(Consts.LOGIN_MODE, "anonymous", LoginActivity.this);
                startActivity(new Intent(LoginActivity.this, TaskListActivity.class));
                finish();
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
        loginRequest.setUsername(editTextCorreo.getText().toString());
        loginRequest.setPassword(editTextPassword.getText().toString());

        inicioSesion(loginRequest);
    }

    public void inicioSesion(LoginRequest loginRequest) {
        loginBtn.setAlpha(0f);
        progress_bar.setVisibility(View.VISIBLE);

        Call<LoginResponse> loginResponseCall = ApiClient.getLoginService().loginPersona(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loginBtn.setAlpha(1f);
                progress_bar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // TokenClass tokenClass = null;
                    // tokenClass = new TokenClass(response.body().getToken());
                    SharedPreference.setDefaultsPreference(Consts.LOGIN_MODE,"account", LoginActivity.this);
                    SharedPreference.setDefaultsPreference(Consts.TOKEN, response.body().getToken() , LoginActivity.this );
                    startActivity(new Intent(LoginActivity.this, TaskListActivity.class));
                    finish();
                } else {
                    ErrorResponse error = ErrorUtils.parseError(response);
                    Tools.snackBarWithIconError(LoginActivity.this, viewMainContent, error.getMessages().getError());
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