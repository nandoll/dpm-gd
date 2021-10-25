package pe.com.mipredio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import pe.com.mipredio.utils.Tools;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawer;
    private NavigationView nav_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initNavigationMenu();

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Información General");

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
        switch(item.getItemId()){
            case R.id.menuProfile:
                Toast.makeText(this,"Perfil de usuario",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuPersonList:
                Toast.makeText(this,"Lista de personal",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuTaskList:
                Toast.makeText(this,"Lista de tareas",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,TaskListActivity.class);
                startActivity(intent);
                break;
            case R.id.menuDashboard:
                Toast.makeText(this,"Dashabord",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuLogout:
                Toast.makeText(this,"Salir App",Toast.LENGTH_SHORT).show();
                break;
        }
        // return false;
        return true;
    }
}