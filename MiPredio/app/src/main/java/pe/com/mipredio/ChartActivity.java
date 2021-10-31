package pe.com.mipredio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import pe.com.mipredio.utils.Tools;

public class ChartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawer;
    private NavigationView nav_view;
    private View viewMainContent;

    BarChart mChart;
    private float groupSpace = 0.4f;
    private float barSpace = 0f;
    private float barWidth = 0.3f;

    private Integer _day;
    private Integer _month;
    private Integer _year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        viewMainContent = findViewById(R.id.main_content);
        mChart = (BarChart) findViewById(R.id.bar_chart);
        initToolbar();
        initNavigationMenu();
        initChartBar();

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Reporte General");

        Tools.setSystemBarColor(this, R.color.cyan_50);
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
                initCalendar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initCalendar() {
        if (_day == null || _month == null || _year == null) {
            Calendar cur_calender = Calendar.getInstance();
            _day = cur_calender.get(Calendar.DAY_OF_MONTH);
            _month = cur_calender.get(Calendar.MONTH);
            _year = cur_calender.get(Calendar.YEAR);
        }

        Tools.snackBarWithIconSuccess(ChartActivity.this, viewMainContent, "Año:" + _year + "Mes: " + _month + " Dia: " + _day);
        DatePickerDialog recogerFecha = new DatePickerDialog(ChartActivity.this, R.style.DatePickerThemeLight, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                _year = year;
                _month = month;
                _day = dayOfMonth;
                Tools.snackBarWithIconSuccess(ChartActivity.this, viewMainContent, "Año:" + _year + "Mes: " + _month + " Dia: " + _day);
                // Toast.makeText(TaskListActivity.this, "_year ...." + _year + " ... _month " + _month + " ... _day " + _day ,Toast.LENGTH_LONG).show();

            }
        }, _year, _month, _day);
        recogerFecha.show();
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
                Intent intentPerson = new Intent(ChartActivity.this, TechnicalProfessionalListActivity.class);
                startActivity(intentPerson);
                break;
            case R.id.menuTaskList:
                Intent intentTask = new Intent(ChartActivity.this, TaskListActivity.class);
                startActivity(intentTask);
                break;
            case R.id.menuDashboard:
                // Toast.makeText(this,"Dashabord",Toast.LENGTH_SHORT).show();
                Intent intentChart = new Intent(ChartActivity.this, ChartActivity.class);
                startActivity(intentChart);
                break;
            case R.id.menuLogout:
                Toast.makeText(this, "Salir App", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void initChartBar() {
        ArrayList<BarEntry> barRegister = new ArrayList<>();
        ArrayList<BarEntry> barPending = new ArrayList<>();
        String[] labels = {"", "Luis Antonio", "Carlos Alberto", "Luisa Miranda", "James Perez", "Airthon Mendoza", ""};
        float[] colRegister = {45, 38, 45, 30, 33}; // Registrados
        float[] colPending = {2, 3, 1, 3, 4}; // Pendientes
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