package pe.com.mipredio;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import pe.com.mipredio.utils.Tools;

public class TaskDetailActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private View viewMainContent;
    private ImageButton imageButtonMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        viewMainContent = findViewById(R.id.main_content);
        imageButtonMap = findViewById(R.id.imageButtonMap);
        initToolbar();

        mapButton();
    }

    private void mapButton() {
        imageButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TaskMapActivity.class);
                intent.putExtra("taskId","999"); // Pasar el ID a la activity para mostrar solo 1 marcador de la Tarea
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
        Tools.setSystemBarColor(this, R.color.cyan_50);
        Tools.setSystemBarLight(this);
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