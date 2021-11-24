package pe.com.mipredio;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.com.mipredio.api.ApiClient;
import pe.com.mipredio.classes.MapClass;
import pe.com.mipredio.response.ProgramacionListaMapaResponse;
import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.SharedPreference;
import pe.com.mipredio.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskMapActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private BottomSheetBehavior bottomSheetBehavior;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private View viewMainContent;

    private String id;
    private String fecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_map);
        viewMainContent = findViewById(R.id.main_content);
        id = getIntent().getStringExtra("taskId");
        fecha = getIntent().getStringExtra("taskDate");
        initMap();
        initComponent();
        initToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.isExpireToken(this,this);
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

    private void initMap() {
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
                generateMarkers();
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
        Tools.setSystemBarColor(this, R.color.colorPrimary);
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

    private void getMarkers(List<MapClass> mapa) {
        if (mapa.size() > 0) {
            for (int i = 0; i < mapa.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(mapa.get(i).getLatitude(), mapa.get(i).getLongitude()));
                mMap.addMarker(markerOptions)
                        .setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.img_predio)));
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

    private void generateMarkers() {
        List<MapClass> items = new ArrayList<>();
        String token = SharedPreference.getDefaultsPreference(Consts.TOKEN, this);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("fecha", fecha);
        Call<List<ProgramacionListaMapaResponse>> programacionListaMapa = ApiClient.getProgramacionService().programacionListaMapa(token, requestBody);
        programacionListaMapa.enqueue(new Callback<List<ProgramacionListaMapaResponse>>() {
            @Override
            public void onResponse(Call<List<ProgramacionListaMapaResponse>> call, Response<List<ProgramacionListaMapaResponse>> response) {
                if (response.code() == Consts.ERROR_UNAUTHORIZED) {
                    Tools.isExpireToken(TaskMapActivity.this, TaskMapActivity.this);
                }
                if (response.isSuccessful()) {
                    List<ProgramacionListaMapaResponse> lista = response.body();
                    Integer total = lista.size();

                    if (total > 0) {
                        for (int i = 0; i < lista.size(); i++) {
                            items.add(new MapClass(Double.parseDouble(lista.get(i).getLatitud()), Double.parseDouble(lista.get(i).getLongitud()), "", "", "", ""));
                        }
                    }
                    getMarkers(items);
                    Log.e("TOTAL_MAPA", total.toString());
                }
            }

            @Override
            public void onFailure(Call<List<ProgramacionListaMapaResponse>> call, Throwable t) {
                Toast.makeText(TaskMapActivity.this, "Error al consultar las ubicaciones en mapa", Toast.LENGTH_LONG).show();
            }
        });
    }
}