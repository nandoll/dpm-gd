package pe.com.mipredio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pe.com.mipredio.utils.Tools;

public class LoginActivity extends AppCompatActivity {
    private Button loginFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginFab = (Button) findViewById(R.id.loginIn);

        loginFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        initToolbar();
    }

    private void initToolbar() {
        Tools.setSystemBarColor(this, R.color.cyan_50);
        Tools.setSystemBarLight(this);
    }

}