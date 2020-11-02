package com.example.geolocalizacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng Home, Japon, Alemania, Italia, Francia;
    private float Zoom;

    private static final String TAG = "Estilo de Mapa";
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private LocationManager locationManager;
    private Location location;

    private double latitudeStart = 0, longitudeStart = 0;
    private double latitudeEnd = 0, longitudeEnd = 0;
    double distance;
    DecimalFormat Kilometer = new DecimalFormat("#.00");
    private int seleccion = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        mapFragment.getMapAsync(this);
        Home = new LatLng(-18.028350, -70.240839);
        Zoom = 16;

        ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latitudeStart = location.getLatitude();
        longitudeEnd = location.getLongitude();
    }

    private void seleccionMapa(int seleccion) {
        Japon = new LatLng(35.41222,139.413016);
        Alemania = new LatLng(51.165691,10.451526);
        Italia = new LatLng(41.87194,12.56738);
        Francia = new LatLng(48.511224,2.205496);

        Location locationA = new Location("Lugar A");
        location.setLatitude(latitudeStart);
        location.setLongitude(longitudeStart);
        Location locationB = new Location("Lugar B");

        switch (seleccion) {
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.addMarker(new MarkerOptions().position(Japon).title("Japon"));
                latitudeEnd = 35.680513;
                longitudeEnd = 139.769051;
                locationB.setLatitude(latitudeEnd);
                locationB.setLongitude(longitudeEnd);
                distance = locationA.distanceTo(locationB)/1000;
                Toast.makeText(getApplicationContext(),"Japon esta a "+String.valueOf(Kilometer.format(distance))+" KM",Toast.LENGTH_LONG).show();
                agregarIcono(Japon, R.drawable.globe);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.addMarker(new MarkerOptions().position(Italia).title("Italia"));
                latitudeEnd = 41.902609;
                longitudeEnd = 12.494847;
                locationB.setLatitude(latitudeEnd);
                locationB.setLongitude(longitudeEnd);
                distance = locationA.distanceTo(locationB)/1000;
                Toast.makeText(getApplicationContext(),"Italia esta a "+String.valueOf(Kilometer.format(distance))+" KM",Toast.LENGTH_LONG).show();
                agregarIcono(Italia, R.drawable.mountain);
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mMap.addMarker(new MarkerOptions().position(Alemania).title("Alemania"));
                latitudeEnd = 52.516934;
                longitudeEnd = 13.403190;
                locationB.setLatitude(latitudeEnd);
                locationB.setLongitude(longitudeEnd);
                distance = locationA.distanceTo(locationB)/1000;
                Toast.makeText(getApplicationContext(),"Alemania esta a "+String.valueOf(Kilometer.format(distance))+" KM",Toast.LENGTH_LONG).show();
                agregarIcono(Alemania, R.drawable.satellite);
                break;
            case 4:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mMap.addMarker(new MarkerOptions().position(Francia).title("Francia"));
                latitudeEnd = 48.843489;
                longitudeEnd = 2.355331;
                locationB.setLatitude(latitudeEnd);
                locationB.setLongitude(longitudeEnd);
                distance = locationA.distanceTo(locationB)/1000;
                Toast.makeText(getApplicationContext(),"Francia esta a "+String.valueOf(Kilometer.format(distance))+" KM",Toast.LENGTH_LONG).show();
                agregarIcono(Francia, R.drawable.location);
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style Parsing Failed.");
            }
        }catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't Find Style. Error: ", e);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Home,Zoom));
        GroundOverlayOptions homeOverlay = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.earth)).position(Home, 100);
        mMap.addGroundOverlay(homeOverlay);

        setMapLongClick(mMap);
        setPoiClick(mMap);
        enableMyLocation();

        Spinner spinner = (Spinner)findViewById(R.id.spnMapType);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccion = position;
                seleccionMapa(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(), "Lat: %1$.5f, Long: %2$.5f", latLng.latitude, latLng.longitude);
                map.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.app_name)).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
        });
    }

    private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {
                Marker poiMarker = mMap.addMarker(new MarkerOptions().position(poi.latLng).title(poi.name));
                poiMarker.showInfoWindow();
                poiMarker.setTag("poi");
            }
        });
    }

    private void agregarIcono(LatLng location, int imagenNueva) {
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(location, 16);
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(imagenNueva)).anchor(0.5f,0.8f).position(location));
        mMap.animateCamera(ubicacion);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }
}