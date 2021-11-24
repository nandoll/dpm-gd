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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import pe.com.mipredio.api.ApiClient;
import pe.com.mipredio.classes.SidebarClass;
import pe.com.mipredio.model.TaskModel;
import pe.com.mipredio.response.ErrorResponse;
import pe.com.mipredio.response.ErrorUtils;
import pe.com.mipredio.response.ProgramacionCerrarResponse;
import pe.com.mipredio.response.ProgramacionListaResponse;
import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.SharedPreference;
import pe.com.mipredio.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    private String sharePref;
    private SwipeRefreshLayout swipe_refresh;

    private String fecha;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        viewMainContent = findViewById(R.id.main_content);
        floatingActionButtonAdd = (FloatingActionButton) findViewById(R.id.btnaddTask);
        sharePref = SharedPreference.getDefaultsPreference(Consts.LOGIN_MODE, this);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        initToolbar();
        initNavigationMenu();
        initTaskList();
        initSwipeRefresh();

        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListActivity.this, TaskAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initSwipeRefresh(){
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // pullAndRefresh();
                llenarLista(fecha);
            }
        });
    }
    private void swipeProgress(final boolean show) {
        if (!show) {
            swipe_refresh.setRefreshing(show);
            return;
        }
        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(show);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Tools.isExpireToken(this, this);
    }

    private void initCalendar() {
        if (this._day == null || this._month == null || this._year == null) {
            Calendar cur_calender = Calendar.getInstance();
            this._day = cur_calender.get(Calendar.DAY_OF_MONTH);
            this._month = cur_calender.get(Calendar.MONTH);
            this._year = cur_calender.get(Calendar.YEAR);
        }

        DatePickerDialog recogerFecha = new DatePickerDialog(TaskListActivity.this, R.style.DatePickerThemeLight, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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
                fecha = _year + "-" + String.format("%02d", _month + 1) + "-" + String.format("%02d", _day);
                llenarLista(fecha);
            }
        }, _year, _month, _day);
        recogerFecha.show();
    }
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Lista de Actividad");

        Tools.setSystemBarColor(this, R.color.colorPrimary);
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
        SidebarClass.showHideMenu(sharePref, nav_view);
        SidebarClass.getInfoSidebarHeader(this, nav_view);
    }
    private void initTaskList() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        Calendar cur_calender = Calendar.getInstance();

        String _year = getIntent().getStringExtra("_year");
        String _month = getIntent().getStringExtra("_month");
        String _day = getIntent().getStringExtra("_day");
        long date_ship_millis;

        if (_year != null && _month != null && _day != null) {
            cur_calender.set(Calendar.YEAR, Integer.parseInt(_year));
            cur_calender.set(Calendar.MONTH, Integer.parseInt(_month));
            cur_calender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(_month));
            date_ship_millis = cur_calender.getTimeInMillis();
            this._day = Integer.valueOf(_day);
            this._month = Integer.parseInt(_month) - 1;
            this._year = Integer.parseInt(_year);
        } else {
            this._day = cur_calender.get(Calendar.DAY_OF_MONTH);
            this._month = cur_calender.get(Calendar.MONTH);
            this._year = cur_calender.get(Calendar.YEAR);
            date_ship_millis = cur_calender.getTimeInMillis();
        }
        ((TextView) findViewById(R.id.textDateSelected)).setText(Tools.getFormatDate(date_ship_millis));
        ((TextView) findViewById(R.id.textDateSelectedDay)).setText(Tools.getDateFullDayName(date_ship_millis));

        this.fecha = this._year + "-" + String.format("%02d", this._month + 1) + "-" + String.format("%02d", this._day);
        llenarLista(fecha);
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
        return SidebarClass.actionSidebarMenu(item, this, viewMainContent, drawer);
    }

    private void llenarLista(String fecha) {
        swipeProgress(true);

        String token = SharedPreference.getDefaultsPreference(Consts.TOKEN, this);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("fecha", fecha);
        Call<List<ProgramacionListaResponse>> programacionLista = ApiClient.getProgramacionService().programacionLista(token, requestBody);
        programacionLista.enqueue(new Callback<List<ProgramacionListaResponse>>() {
            @Override
            public void onResponse(Call<List<ProgramacionListaResponse>> call, Response<List<ProgramacionListaResponse>> response) {
                if (response.code() == Consts.ERROR_UNAUTHORIZED) {
                    Tools.isExpireToken(TaskListActivity.this, TaskListActivity.this);
                }
                if (response.isSuccessful()) {
                    items.clear();
                    List<ProgramacionListaResponse> lista = response.body();
                    Integer total = lista.size();
                    if (total > 0) {
                        for (int i = 0; i < lista.size(); i++) {
                            Log.e("EXCEPCION___", lista.get(i).getSituacion());
                            items.add(new TaskModel(lista.get(i).getId(), lista.get(i).getDireccion(), lista.get(i).getFecha(), lista.get(i).getHora(), lista.get(i).getUbigeo(), lista.get(i).getEstado(), lista.get(i).getNroMedidor(), lista.get(i).getSituacion()));
                        }
                        TaskAdapter taskAdapter = new TaskAdapter(items, TaskListActivity.this);
                        recyclerView.setAdapter(taskAdapter);
                        View supportLayout = findViewById(R.id.viewNoResult);
                        supportLayout.setVisibility(View.GONE);
                    } else {
                        // Mostrar el aviso de sin resultado
                        View supportLayout = findViewById(R.id.viewNoResult);
                        supportLayout.setVisibility(View.VISIBLE);

                        items.clear();
                        TaskAdapter taskAdapter = new TaskAdapter(items, TaskListActivity.this);
                        recyclerView.setAdapter(taskAdapter);

                    }
                }
                swipeProgress(false);
            }
            @Override
            public void onFailure(Call<List<ProgramacionListaResponse>> call, Throwable t) {
                swipeProgress(false);
                Toast.makeText(TaskListActivity.this, "Error al consultar la programación", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onTaskClick(int position) {
        if (!items.get(position).getEstado().equals(Consts._STATUS_PENDING)) {
            Intent intent = new Intent(this, TaskDetailActivity.class);
            intent.putExtra("id", items.get(position).getId());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, TaskAddActivity.class);
            intent.putExtra("id", items.get(position).getId());
            intent.putExtra("fecha", this.fecha);
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
                // Tools.snackBarWithIconSuccess(this, viewMainContent, "El reporte del día ha sido enviado");
                cierreDia();
                break;
            case R.id.action_map:
                Intent intentMap = new Intent(this, TaskMapActivity.class);
                intentMap.putExtra("taskDate", this.fecha);
                startActivity(intentMap);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cierreDia() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Aviso");
        alert.setMessage("¿Desea realizar el cierre del día?");
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String token = SharedPreference.getDefaultsPreference(Consts.TOKEN, TaskListActivity.this);
                Map<String, String> requestBody = new HashMap<>();
                requestBody.put("fecha", fecha);
                Call<ProgramacionCerrarResponse> programacionCerrar = ApiClient.getProgramacionService().programacionCerrar(token, requestBody);
                programacionCerrar.enqueue(new Callback<ProgramacionCerrarResponse>() {
                    @Override
                    public void onResponse(Call<ProgramacionCerrarResponse> call, Response<ProgramacionCerrarResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(TaskListActivity.this, "La actividad del día ha sido envida", Toast.LENGTH_LONG).show();
                            llenarLista(response.body().getFecha());
                        } else {
                            ErrorResponse error = ErrorUtils.parseError(response);
                            Toast.makeText(TaskListActivity.this, "" + error.getMessages().getError(), Toast.LENGTH_SHORT).show();
                        }

                        if (response.code() == Consts.ERROR_UNAUTHORIZED) {
                            Tools.isExpireToken(TaskListActivity.this, TaskListActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProgramacionCerrarResponse> call, Throwable t) {
                        String message = t.getLocalizedMessage();
                        Toast.makeText(TaskListActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });

                // Tools.snackBarWithIconSuccess(TaskListActivity.this, viewMainContent, "El reporte del día ha sido enviado");
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}