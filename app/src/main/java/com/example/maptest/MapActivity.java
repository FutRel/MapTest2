package com.example.maptest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.example.maptest.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private LocationManager locationManager;
    SensorManager sensorManager;
    Sensor sensorAccelerometer;

    Button follow;
    Button pauseOrResume;

    private long changeStyle = 0;

    ArrayList<Double> arrayOfLat = new ArrayList<>();
    ArrayList<Double> arrayOfLng = new ArrayList<>();

    Thread thread = new Thread(() -> {
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
        }
    });

    public boolean isGeoDisabled() {
        LocationManager mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return !mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        follow = findViewById(R.id.follow);
        pauseOrResume = findViewById(R.id.pauseOrResume);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        if (isGeoDisabled()) {
            thread.start();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        final long[] t = {0};

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float var = ((((int) sensorEvent.values[0] + 1) / 15) * 15);
            if (t[0] % 40 == 0) {
                myMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder(myMap
                                .getCameraPosition())
                        .bearing(var)
                        .build()));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
            t[0]++;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    LocationListener followListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            try {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                arrayOfLat.add(location.getLatitude());
                arrayOfLng.add(location.getLongitude());
                Toast.makeText(MapActivity.this, location.getLatitude() + " " +
                        location.getLongitude(), Toast.LENGTH_SHORT).show();


                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(userLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, myMap.getCameraPosition().zoom));
            } catch (Exception ex) {
                LatLng userLocation = new LatLng(0, 0);
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(userLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, myMap.getCameraPosition().zoom));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            try {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                arrayOfLat.add(location.getLatitude());
                arrayOfLng.add(location.getLongitude());
                Toast.makeText(MapActivity.this, location.getLatitude() + " " +
                        location.getLongitude(), Toast.LENGTH_SHORT).show();

                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(userLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
            } catch (Exception ex) {
                LatLng userLocation = new LatLng(0, 0);
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(userLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        UiSettings uiSettings = myMap.getUiSettings();
        myMap.setBuildingsEnabled(false);
        myMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.bl_wh));

        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setIndoorLevelPickerEnabled(false);

        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) return;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                400, 4, locationListener);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        try {
            LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            myMap.clear();
            myMap.addMarker(new MarkerOptions().position(userLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));
        } catch (Exception ex) {
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(54.759955, 56.020414), 14));
        }
    }

    public void onClick(View view) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));
    }

    public void onClickplus(View view) {
        if (follow.getText().toString().equals("follow")) myMap.animateCamera(CameraUpdateFactory.zoomIn());
        else myMap.moveCamera(CameraUpdateFactory.zoomIn());
    }

    public void onClickminus(View view) {
        if (follow.getText().toString().equals("follow")) myMap.animateCamera(CameraUpdateFactory.zoomOut());
        else myMap.moveCamera(CameraUpdateFactory.zoomOut());
    }

    public void follow(View view) {
        UiSettings uiSettings = myMap.getUiSettings();
        if (follow.getText().toString().equals("follow")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, myMap.getCameraPosition().zoom));

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 4, followListener);
            locationManager.removeUpdates(locationListener);
            sensorManager.registerListener(sensorEventListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI);

            follow.setText("unfollow");
            uiSettings.setZoomGesturesEnabled(false);
            uiSettings.setScrollGesturesEnabled(false);
            uiSettings.setRotateGesturesEnabled(false);
            uiSettings.setTiltGesturesEnabled(false);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 4, locationListener);
            locationManager.removeUpdates(followListener);
            sensorManager.unregisterListener(sensorEventListener);

            follow.setText("follow");
            uiSettings.setZoomGesturesEnabled(true);
            uiSettings.setScrollGesturesEnabled(true);
            uiSettings.setRotateGesturesEnabled(true);
            uiSettings.setTiltGesturesEnabled(true);

            myMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder(myMap
                            .getCameraPosition())
                    .bearing(0)
                    .build()));
        }
    }

    public void change(View view) {
        if (changeStyle % 2 == 1) myMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.bl_wh));
        else myMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.my_style));
        changeStyle++;

    }

    public void pauseOrResume(View view) {
        if (pauseOrResume.getText().toString().equals("pause")) {
            pauseOrResume.setText("resume");
            locationManager.removeUpdates(followListener);
            locationManager.removeUpdates(locationListener);
            sensorManager.unregisterListener(sensorEventListener);
        } else {
            pauseOrResume.setText("pause");
            follow(view);
            follow(view);
        }
    }
}