package pe.com.mipredio;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import pe.com.mipredio.classes.MapClass;
import pe.com.mipredio.utils.Tools;

public class TaskMapActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private BottomSheetBehavior bottomSheetBehavior;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private View viewMainContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_map);
        viewMainContent = findViewById(R.id.main_content);

        // initMarkers();
        initMapFragment();
        initComponent();
        initToolbar();
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_map_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private void initMarkers(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("taskId");
        String fecha = intent.getStringExtra("taskDate");

        List<MapClass> mapa = getMarkersPredio(id, fecha);
        if(mapa.size() > 0){

            for (int i = 0; i < mapa.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng( mapa.get(i).getLatitude(), mapa.get(i).getLongitude()));
                mMap.addMarker(markerOptions)
                        .setIcon( BitmapDescriptorFactory.fromBitmap( getMarkerBitmapFromView( R.drawable.img_predio ) ) );
                //.setIcon(BitmapDescriptorFactory.fromResource( R.drawable.photo_profile));
                mMap.moveCamera(zoomingLocation(mapa.get(i).getLatitude(), mapa.get(i).getLongitude()));
            }
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    mMap.animateCamera(zoomingLocation(marker.getPosition().latitude, marker.getPosition().longitude));
                    return false;
                }
            });
        }
    }


    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = Tools.configActivityMaps(googleMap);
                UiSettings uiSettings = mMap.getUiSettings();
                uiSettings.setIndoorLevelPickerEnabled(true);
                uiSettings.setMyLocationButtonEnabled(true);
                uiSettings.setMapToolbarEnabled(true);
                uiSettings.setCompassEnabled(true);
                uiSettings.setZoomControlsEnabled(true);
                uiSettings.setZoomGesturesEnabled(true);
                initMarkers();

                /*
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(-12.082396, -77.0073197));
                mMap.addMarker(markerOptions);
                mMap.moveCamera(zoomingLocation());
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        mMap.animateCamera(zoomingLocation());
                        return false;
                    }
                });
                */
            }
        });
    }

    private void initComponent() {
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    private CameraUpdate zoomingLocation(Double latitude, Double longitude) {
        return CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Ubicaciones del predio");
        Tools.setSystemBarColor(this, R.color.cyan_50);
        Tools.setSystemBarLight(this);
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private List<MapClass> getMarkersPredio(String id, String fecha){
        List<MapClass> items = new ArrayList<>();
        if(id != null){
            items.add(new MapClass(-12.08962221353456, -77.04430495519286,"Calle Vida Nueva 4423","Lima - Lima - Lince","987654152","Juan Carlos Peña"));
        }else if (fecha != null){
            items.add(new MapClass(-12.08962221353456, -77.04430495519286,"Calle Vida Nueva 4423","Lima - Lima - Lince","987654152","Juan Carlos Peña"));
            items.add(new MapClass(-12.08872200100299, -77.04443228249477,"Calle Vida Nueva 4423","Lima - Lima - Lince","987989988","Maria Adrea Quinto"));
            items.add(new MapClass(-12.08886887381922, -77.04547968509245,"Calle Vida Nueva 4423","Lima - Lima - Lince","998789888","Luis Santiago Flores"));
            items.add(new MapClass(-12.089145571502245, -77.04572108390096,"Calle Vida Nueva 4423","Lima - Lima - Lince","980890001","Ana Cecilia Torres"));
            items.add(new MapClass(-12.0895783108982, -77.04567217696597,"Calle Vida Nueva 4423","Lima - Lima - Lince","966550447","Santiago Millones"));
            items.add(new MapClass(-12.089634452148799, -77.0443073648624,"Calle Vida Nueva 4423","Lima - Lima - Lince","987722413","Alberto Toledo"));
            items.add(new MapClass(-12.08975921044435, -77.04335045422361,"Calle Vida Nueva 4423","Lima - Lima - Lince","9846654142","Pedro Sotelo"));
            items.add(new MapClass(-12.090593538261201, -77.04554859733639,"Calle Vida Nueva 4423","Lima - Lima - Lince","987897520","Lenin Toledo"));
        }
        return items;
    }
}