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

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import pe.com.mipredio.model.TaskModel;
import pe.com.mipredio.model.TechnicalProfessionalModel;
import pe.com.mipredio.utils.Tools;

public class TechnicalProfessionalListActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , TechnicalProfessionalListAdapter.OnTechProfListener {

    private Toolbar toolbar;
    private ActionBar actionBar;

    private RecyclerView recyclerView;
    private List<TechnicalProfessionalModel> items = new ArrayList<>();
    private NavigationView nav_view;
    private DrawerLayout drawer;
    private TechnicalProfessionalListAdapter mTechnicalProfessionalListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_professional_list);

        initToolbar();
        initNavigationMenu();
        initTechnicalProfessional();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Lista de profesionales");
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

    private void initTechnicalProfessional() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mTechnicalProfessionalListAdapter = new TechnicalProfessionalListAdapter(getDataPrueba(), this);
        recyclerView.setAdapter(mTechnicalProfessionalListAdapter);
    }


    private List<TechnicalProfessionalModel> getDataPrueba() {
        items.add(new TechnicalProfessionalModel(0, "Juan Carlos", "Millones Cuadro", "01255458", "988547199", "Ingeniero","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Luis Torres", "Millones Cuadro", "01245656", "988543459", "Ingeniero Civil","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Gabriel Perez", "Millones Cuadro", "01255671", "988547004", "Ingeniero Civil","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Ana Cecilia", "Millones Cuadro", "01254468", "900040499", "Ingeniero Civil","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Alba", "Millones Cuadro", "01257789", "988547199", "Ingeniero Civil","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Jose Miguel", "Millones Cuadro", "07895458", "95348099", "Ingeniero Civil","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Efrain Torres", "Millones Cuadro", "01220948", "911455036", "Ingeniero Civil","Analista Ruta"));
        items.add(new TechnicalProfessionalModel(0, "Jessica", "Millones Cuadro", "01251123", "988500456", "Ingeniero Civil","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Ericka", "Millones Cuadro", "3455548", "988311447", "Ingeniero Civil","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Hugo", "Millones Cuadro", "04155239", "978933079", "Ingeniero Civil","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Carlos", "Millones Cuadro", "7555458", "981472009", "Ingeniero Civil","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Luis", "Millones Cuadro", "00456345", "900001439", "Ingeniero Civil","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Xavier", "Millones Cuadro", "04565645", "990045699", "Ingeniero Civil","Analista Predio"));
        items.add(new TechnicalProfessionalModel(0, "Martin", "Millones Cuadro", "01209497", "976789784", "Ingeniero Civil","Analista Predio"));
        return items;
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
                Intent intentPerson = new Intent(TechnicalProfessionalListActivity.this, TechnicalProfessionalListActivity.class);
                startActivity(intentPerson);
                break;
            case R.id.menuTaskList:
                Intent intent = new Intent(TechnicalProfessionalListActivity.this, TaskListActivity.class);
                startActivity(intent);
                break;
            case R.id.menuDashboard:
                Intent intentChart = new Intent(TechnicalProfessionalListActivity.this, ChartActivity.class);
                startActivity(intentChart);
                break;
            case R.id.menuLogout:
                Toast.makeText(this, "Salir App", Toast.LENGTH_SHORT).show();
                break;
        }
        // return false;
        return true;
    }


    @Override
    public void onTechnicalClick(int position) {

    }
}