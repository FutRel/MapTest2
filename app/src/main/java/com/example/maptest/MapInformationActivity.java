package com.example.maptest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.example.maptest.data.PointsContract;
import com.example.maptest.data.PointsDBHelper;
import com.example.maptest.data.RecordsContract;
import com.example.maptest.data.RecordsDBHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.maptest.databinding.ActivityMapInformationBinding;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapInformationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapInformationBinding binding;
    private RecordsDBHelper rdbHelper;
    private PointsDBHelper pdbHelper;
    private ArrayList<Double> arrayLatitude;
    private ArrayList<Double> arrayLongitude;
    private long changeStyle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rdbHelper = new RecordsDBHelper(this);
        pdbHelper = new PointsDBHelper(this);
        Intent getIntent = getIntent();
        int number = getIntent.getIntExtra("number", 0);
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
        Toast.makeText(this, number + " " + idOfRecord + " " + distance + " " + time + " " + date, Toast.LENGTH_LONG).show();

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
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.bl_wh));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(arrayLatitude.get(0), arrayLongitude.get(0))));
        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = 0; i < arrayLatitude.size(); i++) polylineOptions.add(new LatLng(arrayLatitude.get(i), arrayLongitude.get(i)));
        mMap.addPolyline(polylineOptions);
    }
    public void back(View view){startActivity(new Intent(MapInformationActivity.this, ListViewActivity.class));}
    public void plus(View view) {mMap.animateCamera(CameraUpdateFactory.zoomIn());}
    public void minus(View view) {mMap.animateCamera(CameraUpdateFactory.zoomOut());}
    public void change(View view){
        if (changeStyle % 2 == 1)mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.bl_wh));
        else mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.my_style));
        changeStyle++;

    }
}