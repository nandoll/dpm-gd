package pe.com.mipredio;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import pe.com.mipredio.utils.Tools;

public class TaskAdd extends AppCompatActivity {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private View viewMainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        viewMainContent = findViewById(R.id.main_content);

        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setTitle("Registrar informe");
        Tools.setSystemBarColor(this, R.color.cyan_50);
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
            Tools.snackBarWithIconSuccess(this,viewMainContent,"La toma de datos ha sido registrado.");
        }
        return super.onOptionsItemSelected(item);
    }

}