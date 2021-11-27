package pe.com.mipredio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import pe.com.mipredio.classes.TokenClass;
import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.SharedPreference;
import pe.com.mipredio.utils.Tools;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String flag = SharedPreference.getDefaultsPreference(Consts.LOGIN_MODE, this);
        boolean expired = Tools.expiredToken(this, this);
        String token = SharedPreference.getDefaultsPreference(Consts.TOKEN, this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag == null || expired) {
                    Intent intentDefault = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intentDefault);
                    finish();
                } else {
                    TokenClass tokenClass = new TokenClass(token);
                    if (tokenClass.getRol().toLowerCase().equals("jefe")) {
                        Intent intent = new Intent(SplashActivity.this, TechnicalProfessionalListActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashActivity.this, TaskListActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }
        }, 1000);
        initToolbar();
    }

    private void initToolbar() {
        Tools.setSystemBarColor(this, R.color.colorPrimary);
        Tools.setSystemBarLight(this);
    }
}