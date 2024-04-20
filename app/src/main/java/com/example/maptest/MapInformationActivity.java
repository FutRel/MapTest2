package com.example.maptest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import com.example.maptest.data.PointsContract;
import com.example.maptest.data.PointsDBHelper;
import com.example.maptest.data.RecordsContract;
import com.example.maptest.data.RecordsDBHelper;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.example.maptest.databinding.ActivityMapInformationBinding;
import java.util.ArrayList;

public class MapInformationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Double> arrayLatitude;
    protected ArrayList<Double> arrayLongitude;
    TextView tvDist;
    TextView tvTime;
    TextView tvDate;
    TextView tvAvSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.maptest.databinding.ActivityMapInformationBinding binding = ActivityMapInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        tvDist = findViewById(R.id.dist_inf);
        tvTime = findViewById(R.id.time_inf);
        tvDate = findViewById(R.id.date_inf);
        tvAvSpeed = findViewById(R.id.avspeed_inf);

        RecordsDBHelper rdbHelper = new RecordsDBHelper(this);
        PointsDBHelper pdbHelper = new PointsDBHelper(this);
        Intent getIntent = getIntent();
        int idOfRecord = getIntent.getIntExtra("idOfRecord", 0);
        SQLiteDatabase rdb = rdbHelper.getReadableDatabase();
        String[] columnsRDB = {
                RecordsContract.ClassForRecords._id,
                RecordsContract.ClassForRecords.column_distance,
                RecordsContract.ClassForRecords.column_time,
                RecordsContract.ClassForRecords.column_date
        };
        Cursor cursorRDB = rdb.query(
                RecordsContract.ClassForRecords.table_name,
                columnsRDB, RecordsContract.ClassForRecords._id + "=?", new String[]{String.valueOf(idOfRecord)}, null, null, null
        );
        cursorRDB.moveToFirst();
        float distance = cursorRDB.getFloat(1);
        int time = cursorRDB.getInt(2);
        String date = cursorRDB.getString(3);
        cursorRDB.close();

        String timeHours = "" + time / 3600;
        if(timeHours.length() == 1) timeHours = "0" + timeHours;
        String timeMin = "" + time % 3600 / 60;
        if(timeMin.length() == 1) timeMin = "0" + timeMin;
        String timeSec = "" + time % 3600 % 60;
        if(timeSec.length() == 1) timeSec = "0" + timeSec;
        String timeStr = timeHours + ":" + timeMin + ":" + timeSec;
        float avspeed = distance / time * 3.6f;
        tvDist.setText(String.format("%.2f", distance / 1000) + " км");
        tvTime.setText(timeStr);
        tvDate.setText(date);
        tvAvSpeed.setText(String.format("%.2f", avspeed) + " км/ч");

        SQLiteDatabase pdb = pdbHelper.getReadableDatabase();
        String[] columnsPDB = {
                PointsContract.ClassForPoints._id,
                PointsContract.ClassForPoints.column_latitude,
                PointsContract.ClassForPoints.column_longitude,
                PointsContract.ClassForPoints.column_recordId
        };
        Cursor cursorPDB = pdb.query(
                PointsContract.ClassForPoints.table_name,
                columnsPDB, PointsContract.ClassForPoints.column_recordId + "=?", new String[]{String.valueOf(idOfRecord)}, null, null, null
        );
        arrayLatitude = new ArrayList<>();
        arrayLongitude = new ArrayList<>();
        while(cursorPDB.moveToNext()){
            double latitude = cursorPDB.getDouble(1);
            double longitude = cursorPDB.getDouble(2);
            arrayLatitude.add(latitude);
            arrayLongitude.add(longitude);
        }
        cursorPDB.close();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings = mMap.getUiSettings();
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.theme));

        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setIndoorLevelPickerEnabled(false);

        if (!arrayLatitude.isEmpty()) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arrayLatitude.get(arrayLatitude.size() - 2),
                    arrayLongitude.get(arrayLongitude.size() - 2)), 16));

            PolylineOptions polylineOptions = new PolylineOptions().color(Color.BLACK);
            PolylineOptions polylineOptionsGrey = new PolylineOptions().color(Color.GRAY);

            for(int i = 0; i < arrayLatitude.size();i++) if (arrayLatitude.get(arrayLatitude.size() - 1) == 0.0){
                arrayLatitude.remove(arrayLatitude.size() - 1);
                arrayLongitude.remove(arrayLongitude.size() - 1);
            }

            mMap.addMarker(new MarkerOptions().position(new LatLng(arrayLatitude.get(arrayLatitude.size() - 1), arrayLongitude.get(arrayLatitude.size() - 1)))
                    .title("Финиш"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(arrayLatitude.get(1), arrayLongitude.get(1)))
                    .title("Старт"));

            for (int i = 1; i < arrayLatitude.size(); i++) {
                if (arrayLatitude.get(i) != 0.0)
                    polylineOptions.add(new LatLng(arrayLatitude.get(i), arrayLongitude.get(i)));
                else {
                    mMap.addPolyline(polylineOptions);
                    polylineOptions = new PolylineOptions().color(Color.BLACK);
                    polylineOptionsGrey.add(new LatLng(arrayLatitude.get(i - 1), arrayLongitude.get(i - 1)), new LatLng(arrayLatitude.get(i + 1), arrayLongitude.get(i + 1)));
                    mMap.addPolyline(polylineOptionsGrey);
                    polylineOptionsGrey = new PolylineOptions().color(Color.GRAY);
                }
            }
            mMap.addPolyline(polylineOptions);
        }
    }

    public void back(View view){startActivity(new Intent(MapInformationActivity.this, RecyclerViewActivity.class));}
    public void plus(View view) {mMap.animateCamera(CameraUpdateFactory.zoomIn());}
    public void minus(View view) {mMap.animateCamera(CameraUpdateFactory.zoomOut());}
}