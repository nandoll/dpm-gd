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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import pe.com.mipredio.api.ApiClient;
import pe.com.mipredio.classes.DateCalendarClass;
import pe.com.mipredio.classes.SidebarClass;
import pe.com.mipredio.classes.TokenClass;
import pe.com.mipredio.model.TaskModel;
import pe.com.mipredio.response.ErrorResponse;
import pe.com.mipredio.response.ErrorUtils;
import pe.com.mipredio.response.ProgramacionCerrarResponse;
import pe.com.mipredio.response.ProgramacionListaResponse;
import pe.com.mipredio.services.MyFirebaseMessageService;
import pe.com.mipredio.sqlite.TaskDBHelper;
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
    private String token;
    TokenClass tokenClass;
    private String mailPersona;
    private String entryMode;

    private String sharePref;
    private SwipeRefreshLayout swipe_refresh;
    private DateCalendarClass dateCalendarClass;

    // SQLite
    TaskDBHelper taskDBHelper;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        viewMainContent = findViewById(R.id.main_content);
        floatingActionButtonAdd = (FloatingActionButton) findViewById(R.id.btnaddTask);
        sharePref = SharedPreference.getDefaultsPreference(Consts.LOGIN_MODE, this);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        dateCalendarClass = new DateCalendarClass(this);
        token = SharedPreference.getDefaultsPreference(Consts.TOKEN, this);
        tokenClass = new TokenClass(token);
        mailPersona = getIntent().getStringExtra("correo"); // Correo del tecnico seleccionado
        entryMode = SharedPreference.getDefaultsPreference(Consts.LOGIN_MODE, this);

        if(entryMode.equals("account")){
            floatingActionButtonAdd.setVisibility(View.GONE);
        }

        taskDBHelper = new TaskDBHelper(this);

        init();

        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListActivity.this, TaskAddActivity.class);
                intent.putExtra("fecha", dateCalendarClass.get_fecha() );
                startActivity(intent);
            }
        });

        MyFirebaseMessageService.messageCloseDay();
    }


    private void init() {
        ((TextView) findViewById(R.id.textDateSelected)).setText(dateCalendarClass.getTextMonthYear());
        ((TextView) findViewById(R.id.textDateSelectedDay)).setText(dateCalendarClass.getTextDay());

        initToolbar();
        initNavigationMenu();

        llenarLista();
        initSwipeRefresh();
    }

    private void initSwipeRefresh() {
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                llenarLista();
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

        SidebarClass.showHideMenu(this, nav_view);
        SidebarClass.getInfoSidebarHeader(this, nav_view);
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

    private void llenarLista() {
        ((TextView) findViewById(R.id.textDateSelected)).setText(dateCalendarClass.getTextMonthYear());
        ((TextView) findViewById(R.id.textDateSelectedDay)).setText(dateCalendarClass.getTextDay());
        View supportLayout = findViewById(R.id.viewNoResult);
        supportLayout.setVisibility(View.GONE);
        if (entryMode.equals("account")) {
            swipeProgress(true);
            getListaByService(supportLayout);
        } else {
            swipeProgress(false);
            getListaBySQLite(supportLayout);
        }
    }

    private void getListaByService(View supportLayout) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("fecha", dateCalendarClass.get_fecha());
        if (this.token != null) {
            TokenClass tokenClass = new TokenClass(this.token);
            if (tokenClass.getRol().equals("jefe")) {
                requestBody.put("tecnico", mailPersona);
            }
        }
        Call<List<ProgramacionListaResponse>> programacionLista = ApiClient.getProgramacionService().programacionLista(token, requestBody);
        programacionLista.enqueue(new Callback<List<ProgramacionListaResponse>>() {
            @Override
            public void onResponse(Call<List<ProgramacionListaResponse>> call, Response<List<ProgramacionListaResponse>> response) {
                if (response.isSuccessful()) {
                    items.clear();
                    List<ProgramacionListaResponse> lista = response.body();
                    Integer total = lista.size();
                    if (total > 0) {
                        for (int i = 0; i < lista.size(); i++) {
                            items.add(new TaskModel(lista.get(i).getId(), lista.get(i).getDireccion(), lista.get(i).getFecha(), lista.get(i).getHora(), lista.get(i).getUbigeo(), lista.get(i).getEstado(), lista.get(i).getNroMedidor(), lista.get(i).getSituacion()));
                        }
                        TaskAdapter taskAdapter = new TaskAdapter(items, TaskListActivity.this);
                        recyclerView.setAdapter(taskAdapter);
                    } else {
                        supportLayout.setVisibility(View.VISIBLE);
                        items.clear();
                        TaskAdapter taskAdapter = new TaskAdapter(items, TaskListActivity.this);
                        recyclerView.setAdapter(taskAdapter);
                    }
                }
                swipeProgress(false);

                if (response.code() == Consts.ERROR_UNAUTHORIZED) {
                    Tools.isExpireToken(TaskListActivity.this, TaskListActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<ProgramacionListaResponse>> call, Throwable t) {
                swipeProgress(false);
                Toast.makeText(TaskListActivity.this, "Error al consultar la programación", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getListaBySQLite(View supportLayout) {
        Log.e("FECHA" , dateCalendarClass.get_fecha());
        items = taskDBHelper.getListTaskSQLite(dateCalendarClass.get_fecha());
        Log.e("ITEMS_SQLITE", items.size() + "");
        TaskAdapter taskAdapter = new TaskAdapter(items, TaskListActivity.this);
        recyclerView.setAdapter(taskAdapter);
        if (items.size() <= 0) {
            supportLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onTaskClick(int position) {
        if (
                !items.get(position).getEstado().equals(Consts._STATUS_PENDING) ||
                        items.get(position).getSituacion().equals(Consts._STATUS_SEND)
        ) {
            Intent intent = new Intent(this, TaskDetailActivity.class);
            intent.putExtra("id", items.get(position).getId());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, TaskAddActivity.class);
            intent.putExtra("id", items.get(position).getId());
            intent.putExtra("fecha", dateCalendarClass.get_fecha());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_toolbar, menu);

        if(sharePref.equals("account")){
            if (this.token != null) {
                if (tokenClass.getRol().equals("jefe")) {
                    MenuItem itemSave = menu.findItem(R.id.action_save);
                    itemSave.setVisible(false);
                    MenuItem itemSend = menu.findItem(R.id.action_send_task);
                    itemSend.setVisible(false);
                } else {
                    MenuItem item = menu.findItem(R.id.action_save);
                    item.setVisible(false);
                }
            }
        }else{
            MenuItem item = menu.findItem(R.id.action_save);
            item.setVisible(false);
            MenuItem itemMap = menu.findItem(R.id.action_map);
            itemMap.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar:
                // initCalendar();
                dateCalendarClass.calendarPicker();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dateCalendarClass.getPicker().setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            dateCalendarClass.set_year(year);
                            dateCalendarClass.set_month(month + 1);
                            dateCalendarClass.set_day(dayOfMonth);
                            dateCalendarClass.setFormat();
                            llenarLista();
                        }
                    });
                }
                break;
            case R.id.action_send_task:
                cierreDia();
                break;
            case R.id.action_map:
                Intent intentMap = new Intent(this, TaskMapActivity.class);
                intentMap.putExtra("taskDate", dateCalendarClass.get_fecha());
                intentMap.putExtra("taskMail", this.mailPersona);
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
                // String token = SharedPreference.getDefaultsPreference(Consts.TOKEN, TaskListActivity.this);
                Map<String, String> requestBody = new HashMap<>();
                requestBody.put("fecha", dateCalendarClass.get_fecha());
                Call<ProgramacionCerrarResponse> programacionCerrar = ApiClient.getProgramacionService().programacionCerrar(token, requestBody);
                programacionCerrar.enqueue(new Callback<ProgramacionCerrarResponse>() {
                    @Override
                    public void onResponse(Call<ProgramacionCerrarResponse> call, Response<ProgramacionCerrarResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(TaskListActivity.this, "La actividad del día ha sido envida", Toast.LENGTH_LONG).show();
                            // llenarLista(response.body().getFecha());
                            llenarLista();
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