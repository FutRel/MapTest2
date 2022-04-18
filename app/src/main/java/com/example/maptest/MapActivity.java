package com.example.maptest;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.example.maptest.data.PointsContract;
import com.example.maptest.data.PointsDBHelper;
import com.example.maptest.data.RecordsContract;
import com.example.maptest.data.RecordsDBHelper;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.example.maptest.databinding.ActivityMapsBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import static com.example.maptest.MainActivity.threadBool;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private LocationManager locationManager;
    SensorManager sensorManager;
    Sensor sensorAccelerometer;
    Date data;

    Button follow;
    Button pauseOrResume;
    Button stop;
    TextView dist;
    TextView time;

    private double distance = 0;
    private LatLng lastLatLng = null;
    long timeOfStart = 0;
    int TVTime = 0;

    ArrayList<Double> arrayOfLat = new ArrayList<>();
    ArrayList<Double> arrayOfLng = new ArrayList<>();
    ArrayList<Long> arrOfTime = new ArrayList<>();

    private RecordsDBHelper rdbHelper;
    private PointsDBHelper pdbHelper;

    Thread thread = new Thread(() -> {
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {}
    });
    Thread timerTest = new Thread(() -> {
        while (true){
            if(pauseOrResume.getText().toString().equals("пауза")) {
                try {TimeUnit.SECONDS.sleep(1);
                    time.setText(String.valueOf(TVTime));
                    TVTime++;
                } catch (InterruptedException e) {}
            }
            if (!threadBool) break;
        }
    });

    public boolean isGeoDisabled() {
        LocationManager mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return !mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
    LocationListener locationListenerWDist = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            try {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (lastLatLng == null){
                    lastLatLng = userLocation;
                    arrayOfLat.add(0.0);
                    arrayOfLng.add(0.0);
                }
                else{
                    float[] result = new float[1];
                    Location.distanceBetween(lastLatLng.latitude, lastLatLng.longitude,
                            userLocation.latitude, userLocation.longitude, result);
                    distance += result[0];
                    lastLatLng = userLocation;
                    dist.setText(String.format("%.2f",distance / 1000));
                    arrayOfLat.add(location.getLatitude());
                    arrayOfLng.add(location.getLongitude());
                }

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
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
    LocationListener followListenerWDist = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            try {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (lastLatLng == null){
                    lastLatLng = userLocation;
                    arrayOfLat.add(0.0);
                    arrayOfLng.add(0.0);
                }
                else{
                    float[] result = new float[1];
                    Location.distanceBetween(lastLatLng.latitude, lastLatLng.longitude,
                            userLocation.latitude, userLocation.longitude, result);
                    distance += Math.abs(result[0]);
                    lastLatLng = userLocation;
                    dist.setText(String.format("%.2f",distance / 1000));
                    arrayOfLat.add(location.getLatitude());
                    arrayOfLng.add(location.getLongitude());
                }

                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(userLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, myMap.getCameraPosition().zoom));
            } catch (Exception ex) {
                LatLng userLocation = new LatLng(0, 0);
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(userLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, myMap.getCameraPosition().zoom));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());

        rdbHelper = new RecordsDBHelper(this);
        pdbHelper = new PointsDBHelper(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        follow = findViewById(R.id.follow);
        pauseOrResume = findViewById(R.id.pauseOrResume);
        stop = findViewById(R.id.stop);
        dist = findViewById(R.id.distanse);
        time = findViewById(R.id.time);

        threadBool = true;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        if (isGeoDisabled()) {
            thread.start();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        stop.setOnClickListener(v -> {
            if (stop.getText().toString().equals("назад")) {
                MapActivity.this.startActivity(new Intent(MapActivity.this, MainActivity.class));
                threadBool = false;
            }
            else Toast.makeText(MapActivity.this, "Удерживайте кнопку чтобы остановить запись", Toast.LENGTH_SHORT).show();
        });
        stop.setOnLongClickListener(v -> {
            threadBool = false;
            if (arrayOfLat.size() < 2){
                Toast.makeText(MapActivity.this, "Вы не проехали ни метра\n                      :(", Toast.LENGTH_SHORT).show();
                sensorManager.unregisterListener(sensorEventListener);
                locationManager.removeUpdates(followListenerWDist);
                locationManager.removeUpdates(locationListenerWDist);
                MapActivity.this.startActivity(new Intent(MapActivity.this, MainActivity.class));
                return true;
            }
            else{
                Toast.makeText(MapActivity.this, "Запись окончена", Toast.LENGTH_SHORT).show();
                sensorManager.unregisterListener(sensorEventListener);
                locationManager.removeUpdates(followListenerWDist);
                locationManager.removeUpdates(locationListenerWDist);
                SQLiteDatabase rdb = rdbHelper.getWritableDatabase();
                if (lastLatLng != null) arrOfTime.add(System.currentTimeMillis() - timeOfStart);

                SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
                String date = formatForDateNow.format(data);
                int time = 0;
                for (Long aLong : arrOfTime) time += aLong;
                time /= 1000;
                ContentValues cvrdb = new ContentValues();
                cvrdb.put(RecordsContract.ClassForRecords.column_distance, distance);
                cvrdb.put(RecordsContract.ClassForRecords.column_time, time);
                cvrdb.put(RecordsContract.ClassForRecords.column_date, date);
                rdb.insert(RecordsContract.ClassForRecords.table_name, null, cvrdb);

                SQLiteDatabase rdb2 = rdbHelper.getReadableDatabase();
                String[] columns = {RecordsContract.ClassForRecords._id};
                Cursor cursor = rdb2.query(
                        RecordsContract.ClassForRecords.table_name,
                        columns, null, null, null, null, null
                );
                cursor.moveToLast();
                int recordId = cursor.getInt(0);
                cursor.close();

                SQLiteDatabase pdb = pdbHelper.getWritableDatabase();
                for (int i = 0; i < arrayOfLat.size(); i++) {
                    double latitude = arrayOfLat.get(i);
                    double longitude = arrayOfLng.get(i);
                    ContentValues cvpdb = new ContentValues();
                    cvpdb.put(PointsContract.ClassForPoints.column_latitude, latitude);
                    cvpdb.put(PointsContract.ClassForPoints.column_longitude, longitude);
                    cvpdb.put(PointsContract.ClassForPoints.column_recordId, recordId);
                    pdb.insert(PointsContract.ClassForPoints.table_name, null, cvpdb);
                }
                MapActivity.this.startActivity(new Intent(MapActivity.this, MainActivity.class));
            }
            return true;
        });
        timerTest.start();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
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
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        try {
            LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            myMap.clear();
            myMap.addMarker(new MarkerOptions().position(userLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bmznk_)));
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));
        } catch (Exception ex) {myMap.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(54.759955, 56.020414), 14));}
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

    public void onClickPlus(View view) {
        if (follow.getText().toString().equals("привязать")) myMap.animateCamera(CameraUpdateFactory.zoomIn());
        else myMap.moveCamera(CameraUpdateFactory.zoomIn());
    }

    public void onClickMinus(View view) {
        if (follow.getText().toString().equals("привязать")) myMap.animateCamera(CameraUpdateFactory.zoomOut());
        else myMap.moveCamera(CameraUpdateFactory.zoomOut());
    }

    public void follow(View view) {
        UiSettings uiSettings = myMap.getUiSettings();
        if(pauseOrResume.getText().toString().equals("продолжить")){
            Toast.makeText(this, "Сначала продолжите запись", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pauseOrResume.getText().toString().equals("старт")){
            Toast.makeText(this, "Сначала начните запись", Toast.LENGTH_SHORT).show();
            return;
        }
        if (follow.getText().toString().equals("привязать")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, myMap.getCameraPosition().zoom));

            follow.setText("отвязать");
            uiSettings.setZoomGesturesEnabled(false);
            uiSettings.setScrollGesturesEnabled(false);
            uiSettings.setRotateGesturesEnabled(false);
            uiSettings.setTiltGesturesEnabled(false);
            sensorManager.registerListener(sensorEventListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 4, followListenerWDist);
            locationManager.removeUpdates(locationListenerWDist);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 250, 5, locationListenerWDist);
            locationManager.removeUpdates(followListenerWDist);
            sensorManager.unregisterListener(sensorEventListener);

            follow.setText("привязать");
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

    public void pauseOrResume(View view) {
        UiSettings uiSettings = myMap.getUiSettings();
        if (pauseOrResume.getText().toString().equals("старт")) {
            timeOfStart = System.currentTimeMillis();
            data = new Date();
        }
        if (pauseOrResume.getText().toString().equals("пауза")) {
            arrOfTime.add(System.currentTimeMillis() - timeOfStart);
            sensorManager.unregisterListener(sensorEventListener);
            locationManager.removeUpdates(followListenerWDist);
            locationManager.removeUpdates(locationListenerWDist);
            lastLatLng = null;

            follow.setText("привязать");
            uiSettings.setZoomGesturesEnabled(true);
            uiSettings.setScrollGesturesEnabled(true);
            uiSettings.setRotateGesturesEnabled(true);
            uiSettings.setTiltGesturesEnabled(true);

            myMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder(myMap
                            .getCameraPosition())
                    .bearing(0)
                    .build()));
            pauseOrResume.setText("продолжить");
        }
        else {
            timeOfStart = System.currentTimeMillis();
            pauseOrResume.setText("пауза");
            stop.setText("стоп");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 250, 5, locationListenerWDist);
        }
    }
}