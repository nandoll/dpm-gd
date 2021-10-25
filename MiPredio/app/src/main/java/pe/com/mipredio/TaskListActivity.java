package pe.com.mipredio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.vivekkaushik.datepicker.DatePickerTimeline;

import java.util.ArrayList;
import java.util.List;

import pe.com.mipredio.model.TaskModel;
import pe.com.mipredio.utils.Tools;

public class TaskListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TaskAdapter.OnTaskListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawer;
    private NavigationView nav_view;


    private RecyclerView recyclerView;
    private TaskAdapter mAdapter;

    private List<TaskModel> items = new ArrayList<>();

    private View viewMainContent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        viewMainContent = findViewById(R.id.main_content);

        initToolbar();
        initNavigationMenu();
        initCalendar();
        initTaskList();
    }

    private void initCalendar() {
        DatePickerTimeline datePickerTimeline = findViewById(R.id.datePickerTimeline);
        // datePickerTimeline.setInitialDate(2019, 3, 21);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Lista de Tarea");

        //Tools.setSystemBarColor(this, R.color.cyan_400);
        // Tools.setSystemBarLight(this);

        Tools.setSystemBarColor(this, R.color.cyan_50);
        Tools.setSystemBarLight(this);
    }

    private void initNavigationMenu() {
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.bringToFront();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);
    }

    private  void initTaskList(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new TaskAdapter( getDataPrueba(), this);
        recyclerView.setAdapter(mAdapter);

        /*
        mAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TaskModel obj, int position) {
                Toast.makeText(TaskListActivity.this,"Hola Mundo",Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuProfile:
                Toast.makeText(this, "Perfil de usuario", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuPersonList:
                Toast.makeText(this, "Lista de personal", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuTaskList:
                // Toast.makeText(this, "Lista de tareas", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TaskListActivity.this, TaskListActivity.class);
                startActivity(intent);
                break;
            case R.id.menuDashboard:
                Toast.makeText(this, "Dashabord", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuLogout:
                Toast.makeText(this, "Salir App", Toast.LENGTH_SHORT).show();
                break;
        }
        // return false;
        return true;
    }


    private List<TaskModel> getDataPrueba(){
        // List<TaskModel> items = new ArrayList<>();

        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro"));
        return items;
    }

    @Override
    public void onTaskClick(int position) {
        Intent intent = new Intent(this, TaskAdd.class);
        startActivity(intent);
    }

    // Agregar iconos en el toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_toolbar, menu);
        MenuItem item = menu.findItem(R.id.action_save);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_send_task:
                // Toast.makeText(this,"El reporte del día ha sido enviado",Toast.LENGTH_SHORT).show();
                // Snackbar.make(viewMainContent,"El reporte del día ha sido enviado",Snackbar.LENGTH_LONG).show();
                Tools.snackBarWithIconSuccess(this,viewMainContent,"El reporte del día ha sido enviado");
                break;
        }

        /*
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        */
        return super.onOptionsItemSelected(item);
    }
}