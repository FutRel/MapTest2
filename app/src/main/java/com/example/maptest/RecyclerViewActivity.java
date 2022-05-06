package com.example.maptest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.maptest.data.RecordsContract;
import com.example.maptest.data.RecordsDBHelper;
import com.example.maptest.recycler.RecordAdapter;
import com.example.maptest.recycler.RecordForRecycler;
import com.example.maptest.recycler.RecyclerItemSpace;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecordsDBHelper rdbHelper;
    private RecyclerView recyclerView;
    private float totalDistance = 0;
    public TextView tvTotalDist;
    public RadioGroup radioGroup;
    public ArrayList<RecordForRecycler> arrayListRecords;
    private boolean sortdistFlag = true;
    private boolean sorttimeFlag = true;
    private boolean sortdateFlag = true;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        radioGroup = findViewById(R.id.rg);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb3.setChecked(true);
        rdbHelper = new RecordsDBHelper(this);
        SQLiteDatabase db = rdbHelper.getReadableDatabase();
        String[] columns = {
                RecordsContract.ClassForRecords._id,
                RecordsContract.ClassForRecords.column_distance,
                RecordsContract.ClassForRecords.column_time,
                RecordsContract.ClassForRecords.column_date};
        Cursor cursor = db.query(
                RecordsContract.ClassForRecords.table_name,
                columns, null, null, null, null, null
        );
        ArrayList<RecordForRecycler> arrayListRecordsNotReversed = new ArrayList<>();
        arrayListRecords = new ArrayList<>();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            float distance = cursor.getFloat(1);
            int time = cursor.getInt(2);
            String dateToItem = cursor.getString(3);
            String distanceToItem = String.format("%.2f", distance / 1000) + " км";
            String timeHours = "" + time / 3600;
            if(timeHours.length() == 1) timeHours = "0" + timeHours;
            String timeMin = "" + time % 3600 / 60;
            if(timeMin.length() == 1) timeMin = "0" + timeMin;
            String timeSec = "" + time % 3600 % 60;
            if(timeSec.length() == 1) timeSec = "0" + timeSec;
            String timeToItem = timeHours + ":" + timeMin + ":" + timeSec;
            arrayListRecordsNotReversed.add(new RecordForRecycler(id, distanceToItem, timeToItem, dateToItem));
            totalDistance += distance;
        }
        cursor.close();
        tvTotalDist = findViewById(R.id.arvdist);
        tvTotalDist.setText(String.format("%.2f", totalDistance / 1000) + " км");
        if(!arrayListRecordsNotReversed.isEmpty()){
            for (int i = arrayListRecordsNotReversed.size() - 1; i >= 0; i--) {
                arrayListRecords.add(arrayListRecordsNotReversed.get(i));
            }
            recyclerView = findViewById(R.id.list);
            recyclerView.addItemDecoration(new RecyclerItemSpace(10));
            RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
                Intent intent = new Intent(RecyclerViewActivity.this, MapInformationActivity.class);
                intent.putExtra("idOfRecord", arrayListRecords.get(position).getId());
                startActivity(intent);
            };
            RecordAdapter adapter = new RecordAdapter(this, arrayListRecords, recordClickListener);
            recyclerView.setAdapter(adapter);
        }
    }

    public void intentToMA(View view){
        Intent intent = new Intent(RecyclerViewActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void sortdist (View view){
        ArrayList<RecordForRecycler> arrayList = new ArrayList<>(arrayListRecords);
        if(sortdistFlag) {
            arrayList.sort(RecordForRecycler.compareByDistReversed);
        }
        else {
            arrayList.sort(RecordForRecycler.compareByDist);
        }
        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
            Intent intent = new Intent(RecyclerViewActivity.this, MapInformationActivity.class);
            intent.putExtra("idOfRecord", arrayList.get(position).getId());
            startActivity(intent);
        };
        RecordAdapter adapter = new RecordAdapter(this, arrayList, recordClickListener);
        recyclerView.setAdapter(adapter);
        sortdistFlag = !sortdistFlag;
    }

    public void sorttime (View view){
        ArrayList<RecordForRecycler> arrayList = new ArrayList<>(arrayListRecords);
        if(sorttimeFlag) arrayList.sort(RecordForRecycler.compareByTimeReversed);
        else arrayList.sort(RecordForRecycler.compareByTime);
        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
            Intent intent = new Intent(RecyclerViewActivity.this, MapInformationActivity.class);
            intent.putExtra("idOfRecord", arrayList.get(position).getId());
            startActivity(intent);
        };
        RecordAdapter adapter = new RecordAdapter(this, arrayList, recordClickListener);
        recyclerView.setAdapter(adapter);
        sorttimeFlag = !sorttimeFlag;
    }

    public void sortdate (View view){
        ArrayList<RecordForRecycler> arrayList = new ArrayList<>(arrayListRecords);
        if(!sortdateFlag) Collections.reverse(arrayList);
        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
            Intent intent = new Intent(RecyclerViewActivity.this, MapInformationActivity.class);
            intent.putExtra("idOfRecord", arrayList.get(position).getId());
            startActivity(intent);
        };
        RecordAdapter adapter = new RecordAdapter(this, arrayList, recordClickListener);
        recyclerView.setAdapter(adapter);
        sortdateFlag = !sortdateFlag;

    }
}