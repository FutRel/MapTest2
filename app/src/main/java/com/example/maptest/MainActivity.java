package com.example.maptest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.maptest.data.PointsContract;
import com.example.maptest.data.PointsDBHelper;
import com.example.maptest.data.RecordsContract;
import com.example.maptest.data.RecordsDBHelper;

public class MainActivity extends AppCompatActivity {

    private RecordsDBHelper rdbHelper;
    private PointsDBHelper pdbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rdbHelper = new RecordsDBHelper(this);
        pdbHelper = new PointsDBHelper(this);
    }

    public void start(View view) {
        SQLiteDatabase rdb = rdbHelper.getWritableDatabase();
        float distance = (float) Math.random() * 100;
        int time = (int) (Math.random() * 10000);
        int day = (int) (Math.random() * 32);
        int month = (int) (Math.random() * 13);
        String date = day + "." + month + ".22";
        ContentValues cvrdb = new ContentValues();
        cvrdb.put(RecordsContract.ClassForRecords.column_distance, distance);
        cvrdb.put(RecordsContract.ClassForRecords.column_time, time);
        cvrdb.put(RecordsContract.ClassForRecords.column_date, date);
        rdb.insert(RecordsContract.ClassForRecords.table_name, null, cvrdb);

        SQLiteDatabase rdb2 = rdbHelper.getReadableDatabase();
        String[] columns = {
                RecordsContract.ClassForRecords._id
        };
        Cursor cursor = rdb2.query(
                RecordsContract.ClassForRecords.table_name,
                columns, null, null, null, null, null
        );
        cursor.moveToLast();
        int recordId = cursor.getInt(0);
        cursor.close();

        SQLiteDatabase pdb = pdbHelper.getWritableDatabase();
        for (int i = 0; i < 7; i++) {
            double latitude = Math.random() * 50;
            double longitude = Math.random() * 50;
            ContentValues cvpdb = new ContentValues();
            cvpdb.put(PointsContract.ClassForPoints.column_latitude, latitude);
            cvpdb.put(PointsContract.ClassForPoints.column_longitude, longitude);
            cvpdb.put(PointsContract.ClassForPoints.column_recordId, recordId);
            pdb.insert(PointsContract.ClassForPoints.table_name, null, cvpdb);
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        }
    }

    public void intentToLV(View view){
        Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
        startActivity(intent);
    }
}