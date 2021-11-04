package pe.com.mipredio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import pe.com.mipredio.utils.Tools;

public class RouteAssignActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_assign);

        initToolbar();
        initNavigationMenu();


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Importar Excel");
        Tools.setSystemBarColor(this, R.color.cyan_100);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menuPersonList:
                Intent intentPerson = new Intent(RouteAssignActivity.this, TechnicalProfessionalListActivity.class);
                startActivity(intentPerson);
                break;
            case R.id.menuTaskList:
                Intent intent = new Intent(RouteAssignActivity.this, TaskListActivity.class);
                startActivity(intent);
                break;
            case R.id.menuDashboard:
                Intent intentChart = new Intent(RouteAssignActivity.this, ChartActivity.class);
                startActivity(intentChart);
                break;
            case R.id.menuImportRoute:
                Intent intentRoute = new Intent(RouteAssignActivity.this, RouteAssignActivity.class);
                startActivity(intentRoute);
                break;
            case R.id.menuLogout:
                Toast.makeText(this, "Salir App", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}