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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import pe.com.mipredio.api.ApiClient;
import pe.com.mipredio.classes.SidebarClass;
import pe.com.mipredio.model.TaskModel;
import pe.com.mipredio.model.TechnicalProfessionalModel;
import pe.com.mipredio.response.ErrorResponse;
import pe.com.mipredio.response.ErrorUtils;
import pe.com.mipredio.response.PersonaListaResponse;
import pe.com.mipredio.response.ProgramacionListaResponse;
import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.SharedPreference;
import pe.com.mipredio.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TechnicalProfessionalListActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TechnicalProfessionalListAdapter.OnTechProfListener {

    private Toolbar toolbar;
    private ActionBar actionBar;

    private RecyclerView recyclerView;
    private List<TechnicalProfessionalModel> items = new ArrayList<>();
    private NavigationView nav_view;
    private DrawerLayout drawer;
    private TechnicalProfessionalListAdapter mTechnicalProfessionalListAdapter;
    private View viewMainContent;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_professional_list);
        viewMainContent = (View) findViewById(R.id.drawer_layout);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        initToolbar();
        initNavigationMenu();
        listarPersonal();
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
        actionBar.setTitle("Lista de profesionales");
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

    private void listarPersonal() {
        String token = SharedPreference.getDefaultsPreference(Consts.TOKEN, this);
        progressDialog = new ProgressDialog(this);
        Tools.showLoadingProgressDialog(progressDialog);

        Call<List<PersonaListaResponse>> personaLista = ApiClient.getPersonaService().getListaPersonal(token);
        personaLista.enqueue(new Callback<List<PersonaListaResponse>>() {
            @Override
            public void onResponse(Call<List<PersonaListaResponse>> call, Response<List<PersonaListaResponse>> response) {
                if (response.isSuccessful()) {
                    items.clear();
                    List<PersonaListaResponse> lista = response.body();
                    Integer total = lista.size();
                    if (total > 0) {
                        for (int i = 0; i < lista.size(); i++) {
                            items.add(new TechnicalProfessionalModel(lista.get(i).getNombres(), lista.get(i).getEspecialidad(), lista.get(i).getCorreo(), lista.get(i).getFoto()));
                        }
                        mTechnicalProfessionalListAdapter = new TechnicalProfessionalListAdapter(items, TechnicalProfessionalListActivity.this);
                        recyclerView.setAdapter(mTechnicalProfessionalListAdapter);
                    } else {
                        items.clear();
                        mTechnicalProfessionalListAdapter = new TechnicalProfessionalListAdapter(items, TechnicalProfessionalListActivity.this);
                        recyclerView.setAdapter(mTechnicalProfessionalListAdapter);
                    }
                } else {
                    ErrorResponse error = ErrorUtils.parseError(response);
                    Toast.makeText(TechnicalProfessionalListActivity.this, "" + error.getMessages().getError(), Toast.LENGTH_SHORT).show();
                }
                Tools.dismissProgressDialog(progressDialog);
                if (response.code() == Consts.ERROR_UNAUTHORIZED) {
                    Tools.isExpireToken(TechnicalProfessionalListActivity.this, TechnicalProfessionalListActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<PersonaListaResponse>> call, Throwable t) {
                Tools.dismissProgressDialog(progressDialog);
                Toast.makeText(TechnicalProfessionalListActivity.this, "Error al consultar la litsa de personal", Toast.LENGTH_LONG).show();
            }
        });
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


    @Override
    public void onTechnicalClick(int position) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("correo", items.get(position).getCorreo());
        startActivity(intent);
    }
}