package pe.com.mipredio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.SharedPreference;
import pe.com.mipredio.utils.Tools;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String flag = SharedPreference.getDefaultsPreference(Consts.LOGIN_MODE, this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag == null) {
                    Intent intentDefault = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intentDefault);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, TaskListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);

    }
}