package pe.com.mipredio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


import pe.com.mipredio.utils.Tools;

public class LoginActivity extends AppCompatActivity {
    private MaterialButton loginBtn;
    private TextView textViewEnterOff;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (MaterialButton) findViewById(R.id.loginIn);
        textViewEnterOff = (TextView) findViewById(R.id.enterOff);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

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
    }

    private void initToolbar() {
        Tools.setSystemBarColor(this, R.color.colorWhite);
        Tools.setSystemBarLight(this);
    }

}