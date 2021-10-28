package pe.com.mipredio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
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

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerTimeline datePickerTimeline = findViewById(R.id.datePickerTimeline);
        datePickerTimeline.setInitialDate(mYear,mMonth,mDay);
        datePickerTimeline.setPressed(true);
        datePickerTimeline.setActiveDate(c);

        datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                //Tools.snackBarWithIconSuccess(TaskListActivity.this,viewMainContent,"Selected");
            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {
                //Tools.snackBarWithIconSuccess(TaskListActivity.this,viewMainContent,"DisableSelected");
            }
        });

        datePickerTimeline.callOnClick();


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Lista de Tarea");

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
                Intent intentPerson = new Intent(TaskListActivity.this, TechnicalProfessionalListActivity.class);
                startActivity(intentPerson);
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

        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_PENDING));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_PENDING));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_PENDING));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_REGISTER));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_PENDING));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_REGISTER));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_PENDING));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_PENDING));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_SEND));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_PENDING));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_PENDING));
        items.add(new TaskModel(0,"Calle Los Alamos 123","15/02/2021","15:11","Lima - Lima - San Isidro",Tools._STATUS_REGISTER));
        return items;
    }

    @Override
    public void onTaskClick(int position) {

        if(!items.get(position).getEstado().equals(Tools._STATUS_PENDING)){
            Intent intent = new Intent(this, TaskDetailActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, TaskAdd.class);
            startActivity(intent);
        }
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
                Tools.snackBarWithIconSuccess(this,viewMainContent,"El reporte del día ha sido enviado");
                break;
            case R.id.action_map:
                Intent intentMap = new Intent(this,TaskMapActivity.class);
                intentMap.putExtra("taskDate","22/10/2021");
                startActivity(intentMap);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}