package pe.com.mipredio.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.auth0.android.jwt.JWT;
import com.google.android.material.navigation.NavigationView;

import pe.com.mipredio.ChartActivity;
import pe.com.mipredio.LoginActivity;
import pe.com.mipredio.R;
import pe.com.mipredio.RouteAssignActivity;
import pe.com.mipredio.TaskListActivity;
import pe.com.mipredio.TechnicalProfessionalListActivity;
import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.SharedPreference;
import pe.com.mipredio.utils.Tools;

public class SidebarClass {

    public static void showHideMenu(Context context, NavigationView nav_view){
        String token = SharedPreference.getDefaultsPreference(Consts.TOKEN , context);
        TokenClass tokenClass = new TokenClass(token);
        Menu m = nav_view.getMenu();
        if(tokenClass.getRol() != null){
            Log.e("ROL_SIDE_BAR",tokenClass.getRol());
            if(!tokenClass.getRol().toLowerCase().equals("jefe")){
                m.findItem(R.id.menuPersonList).setVisible(false);
                m.findItem(R.id.menuImportRoute).setVisible(false);
            }else{
                m.findItem(R.id.menuTaskList).setVisible(false);
            }
        }else{ // modo sin conexion
            m.findItem(R.id.menuPersonList).setVisible(false);
            m.findItem(R.id.menuImportRoute).setVisible(false);
            m.findItem(R.id.menuDashboard).setVisible(false);
        }
    }

    public static boolean actionSidebarMenu(MenuItem item, Activity activity, View view, DrawerLayout drawerLayout){
        switch (item.getItemId()) {
            case R.id.menuPersonList:
                Intent intentPerson = new Intent(activity, TechnicalProfessionalListActivity.class);
                view.getContext().startActivity(intentPerson);
                break;
            case R.id.menuTaskList:
                Intent intent = new Intent(activity, TaskListActivity.class);
                view.getContext().startActivity(intent);
                break;
            case R.id.menuDashboard:
                Intent intentChart = new Intent(activity, ChartActivity.class);
                view.getContext().startActivity(intentChart);
                break;
            case R.id.menuImportRoute:
                Intent intentRoute = new Intent(activity, RouteAssignActivity.class);
                view.getContext().startActivity(intentRoute);
                break;
            case R.id.menuLogout:
                logout(activity, view.getContext() );
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void logout(Activity activity, Context context ){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Aviso");
        alert.setMessage("??Desea salir de la aplicaci??n?");
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreference.deleteSharePreference(activity);
                Intent intent = new Intent(activity, LoginActivity.class);
                context.startActivity(intent);
                activity.finish();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    public static void getInfoSidebarHeader(Context context, NavigationView nav_view){
        // SharedPreference
        String token = SharedPreference.getDefaultsPreference(Consts.TOKEN , context);
        String mode = SharedPreference.getDefaultsPreference(Consts.LOGIN_MODE , context);

        TokenClass tokenClass = new TokenClass(token);

        View headerView = nav_view.getHeaderView(0);
        if (token != null){
            ((TextView)headerView.findViewById(R.id.textName)).setText(tokenClass.getNombre());
            ((TextView)headerView.findViewById(R.id.textSpecialty)).setText(tokenClass.getEspecialidad());
            ((TextView)headerView.findViewById(R.id.textRol)).setText(tokenClass.getRol().toUpperCase());
        }

        if(!mode.equals("anonymous")){
            // String token = SharedPreference.getDefaultsPreference(Consts.TOKEN, context);
            //JWT jwt = new JWT(token);
            if ( !tokenClass.getRol().toLowerCase().equals("jefe") ){

            }

            ((TextView)headerView.findViewById(R.id.textSpecialty)).setVisibility(View.VISIBLE);
            ((TextView)headerView.findViewById(R.id.textRol)).setVisibility(View.VISIBLE);
        }
    }



}
