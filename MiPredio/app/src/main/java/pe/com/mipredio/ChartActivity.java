package pe.com.mipredio;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.com.mipredio.api.ApiClient;
import pe.com.mipredio.classes.DateCalendarClass;
import pe.com.mipredio.classes.SidebarClass;
import pe.com.mipredio.classes.TokenClass;
import pe.com.mipredio.response.ErrorResponse;
import pe.com.mipredio.response.ErrorUtils;
import pe.com.mipredio.response.ProgramacionListaChartResponse;
import pe.com.mipredio.response.ProgramacionListaResponse;
import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.SharedPreference;
import pe.com.mipredio.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawer;
    private NavigationView nav_view;
    private View viewMainContent;
    private String token;
    private ProgressDialog progressDialog;

    BarChart mChart;
    private float groupSpace = 0.4f;
    private float barSpace = 0f;
    private float barWidth = 0.3f;

    private DateCalendarClass dateCalendarClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        viewMainContent = findViewById(R.id.main_content);
        this.token = SharedPreference.getDefaultsPreference(Consts.TOKEN, this);
        mChart = (BarChart) findViewById(R.id.bar_chart);
        initToolbar();
        initNavigationMenu();
        dateCalendarClass = new DateCalendarClass(this);
        cargarChart();

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
        actionBar.setTitle("Reporte General");

        Tools.setSystemBarColor(this, R.color.colorPrimary);
        Tools.setSystemBarLight(this);
    }

    // Agregar iconos en el toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_toolbar, menu);
        MenuItem itemSave = menu.findItem(R.id.action_save);
        itemSave.setVisible(false);

        MenuItem itemMap = menu.findItem(R.id.action_map);
        itemMap.setVisible(false);

        MenuItem itemTask = menu.findItem(R.id.action_send_task);
        itemTask.setVisible(false);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar:
                dateCalendarClass.calendarPicker();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dateCalendarClass.getPicker().setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            dateCalendarClass.set_year(year);
                            dateCalendarClass.set_month(month + 1);
                            dateCalendarClass.set_day(dayOfMonth);
                            dateCalendarClass.setFormat();
                            cargarChart();
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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

    public void cargarChart() {
        progressDialog = new ProgressDialog(this);
        Tools.showLoadingProgressDialog(progressDialog);

        TokenClass tokenClass = new TokenClass(this.token);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("fecha", dateCalendarClass.get_fecha());
        requestBody.put("usuario", tokenClass.getUsername());
        Call<List<ProgramacionListaChartResponse>> chart = ApiClient.getProgramacionService().programacionListaChart(token, requestBody);
        chart.enqueue(new Callback<List<ProgramacionListaChartResponse>>() {
            @Override
            public void onResponse(Call<List<ProgramacionListaChartResponse>> call, Response<List<ProgramacionListaChartResponse>> response) {
                if (response.isSuccessful()) {
                    agregarItems(response.body());
                } else {
                    agregarItems(null);
                    ErrorResponse error = ErrorUtils.parseError(response);
                    Toast.makeText(ChartActivity.this, "" + error.getMessages().getError(), Toast.LENGTH_SHORT).show();
                }
                Tools.dismissProgressDialog(progressDialog);
            }

            @Override
            public void onFailure(Call<List<ProgramacionListaChartResponse>> call, Throwable t) {
                Tools.dismissProgressDialog(progressDialog);
                Toast.makeText(ChartActivity.this, "Error al consultar la programación", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void agregarItems(List<ProgramacionListaChartResponse> lista) {
        ArrayList<BarEntry> barRegister = new ArrayList<>();
        ArrayList<BarEntry> barPending = new ArrayList<>();
        String[] labels = new String[lista.size() + 2];
        float[] colRegister = new float[lista.size()];
        float[] colPending = new float[lista.size()];

        if (lista.size() > 0) {
            int row = 0;
            labels[0] = "";
            for (ProgramacionListaChartResponse item : lista) {
                labels[row + 1] = item.getNombres(); // .get(row).getNombres();
                colRegister[row] = Float.parseFloat(item.getRegistrado());
                colPending[row] = Float.parseFloat(item.getPendiente());
                row++;
            }
            labels[row + 1] = "";
        }

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();

        XAxis xAxis = mChart.getXAxis();
        mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(true);

        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(11f);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelRotationAngle(-90);
        xAxis.setAxisMaximum(labels.length - 1.1f);

        for (int i = 0; i < colRegister.length; i++) {
            barRegister.add(new BarEntry(i, colRegister[i]));
            barPending.add(new BarEntry(i, colPending[i]));
        }

        BarDataSet setColRegister = new BarDataSet(barRegister, "Registrados");
        setColRegister.setColor(Color.rgb(105, 240, 174));
        setColRegister.setValueTextColor(Color.BLACK);
        setColRegister.setValueTextSize(11f);
        setColRegister.setHighlightEnabled(true);
        setColRegister.setDrawValues(true);
        setColRegister.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat mFormat = new DecimalFormat("###,##0");
                return mFormat.format(value);
            }
        });

        BarDataSet setColPending = new BarDataSet(barPending, "Pendientes");
        setColPending.setColor(Color.rgb(153, 153, 153));
        setColPending.setValueTextSize(11f);
        setColPending.setHighlightEnabled(true);
        setColPending.setDrawValues(true);
        setColPending.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat mFormat = new DecimalFormat("###,##0");
                return mFormat.format(value);
            }
        });

        dataSets.add(setColRegister);
        dataSets.add(setColPending);
        BarData data = new BarData(dataSets);
        data.setBarWidth(barWidth);

        mChart.setData(data);
        mChart.animateY(2000);
        mChart.setScaleEnabled(false);
        mChart.setVisibleXRangeMaximum(4f); // Nro de bloques a visualizar al cargar
        mChart.groupBars(1f, groupSpace, barSpace);
        mChart.setFitBars(true);
        mChart.invalidate();
    }


}