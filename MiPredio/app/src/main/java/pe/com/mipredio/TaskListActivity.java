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

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

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
    private FloatingActionButton floatingActionButtonAdd;
    private Integer _day;
    private Integer _month;
    private Integer _year;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        viewMainContent = findViewById(R.id.main_content);
        floatingActionButtonAdd = (FloatingActionButton) findViewById(R.id.btnaddTask);

        initToolbar();
        initNavigationMenu();
        initTaskList();

        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListActivity.this, TaskAdd.class);
                startActivity(intent);
            }
        });
    }


    private void initCalendar() {
        if (_day == null || _month == null || _year == null) {
            Calendar cur_calender = Calendar.getInstance();
            _day = cur_calender.get(Calendar.DAY_OF_MONTH);
            _month = cur_calender.get(Calendar.MONTH);
            _year = cur_calender.get(Calendar.YEAR);
        }

        Tools.snackBarWithIconSuccess(TaskListActivity.this, viewMainContent, "Año:" + _year + "Mes: " + _month + " Dia: " + _day);
        DatePickerDialog recogerFecha = new DatePickerDialog(TaskListActivity.this, R.style.DatePickerThemeLight, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                /*
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                txtFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                */

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                long date_ship_millis = calendar.getTimeInMillis();
                ((TextView) findViewById(R.id.textDateSelected)).setText(Tools.getFormatDate(date_ship_millis));
                ((TextView) findViewById(R.id.textDateSelectedDay)).setText(Tools.getDateFullDayName(date_ship_millis));

                _year = year;
                _month = month;
                _day = dayOfMonth;
                Toast.makeText(TaskListActivity.this, "_year ...." + _year + " ... _month " + _month + " ... _day " + _day, Toast.LENGTH_LONG).show();
            }
        }, _year, _month, _day);
        recogerFecha.show();

        /*com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePicker = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        ((TextView) findViewById(R.id.textDateSelected)).setText( Tools.getFormatDate(date_ship_millis) );
                        ((TextView) findViewById(R.id.textDateSelectedDay)).setText( Tools.getDateFullDayName(date_ship_millis) );

                        _year = year;
                        _month = monthOfYear;
                        _day = dayOfMonth;

                        Tools.snackBarWithIconSuccess(TaskListActivity.this, viewMainContent, "Año:" + _year + "Mes: " + _month + " Dia: " + _day );

                    }
                },
                cur_calender.get(_year),
                cur_calender.get(_month),
                cur_calender.get(_day)
        );
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Datepickerdialog");*/
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

    private void initTaskList() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new TaskAdapter(getDataPrueba(), this);
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
                // Toast.makeText(this, "Dashabord", Toast.LENGTH_SHORT).show();
                Intent intentChart = new Intent(TaskListActivity.this, ChartActivity.class);
                startActivity(intentChart);
                break;
            case R.id.menuLogout:
                Toast.makeText(this, "Salir App", Toast.LENGTH_SHORT).show();
                break;
        }
        // return false;
        return true;
    }


    private List<TaskModel> getDataPrueba() {
        // List<TaskModel> items = new ArrayList<>();

        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_PENDING));
        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_PENDING));
        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_PENDING));
        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_REGISTER));
        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_PENDING));
        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_REGISTER));
        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_PENDING));
        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_PENDING));
        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_SEND));
        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_PENDING));
        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_PENDING));
        items.add(new TaskModel(0, "Calle Los Alamos 123", "15/02/2021", "15:11", "Lima - Lima - San Isidro", Tools._STATUS_REGISTER));
        return items;
    }

    @Override
    public void onTaskClick(int position) {

        if (!items.get(position).getEstado().equals(Tools._STATUS_PENDING)) {
            Intent intent = new Intent(this, TaskDetailActivity.class);
            startActivity(intent);
        } else {
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
        switch (item.getItemId()) {
            case R.id.action_calendar:
                initCalendar();
                break;
            case R.id.action_send_task:
                Tools.snackBarWithIconSuccess(this, viewMainContent, "El reporte del día ha sido enviado");
                break;
            case R.id.action_map:
                Intent intentMap = new Intent(this, TaskMapActivity.class);
                intentMap.putExtra("taskDate", "22/10/2021");
                startActivity(intentMap);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}