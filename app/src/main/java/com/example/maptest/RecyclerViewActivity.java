package com.example.maptest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.maptest.data.RecordsContract;
import com.example.maptest.data.RecordsDBHelper;
import com.example.maptest.recycler.RecordAdapter;
import com.example.maptest.recycler.RecordForRecycler;
import com.example.maptest.recycler.RecyclerItemSpace;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecordsDBHelper rdbHelper;
    private RecyclerView recyclerView;
    private float totalDistance = 0;
    public TextView tvTotalDist;
    public SwitchCompat switchCompat;
    public RadioGroup radioGroup;
    public ArrayList<RecordForRecycler> arrayListRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        switchCompat = findViewById(R.id.switchcompat);
        radioGroup = findViewById(R.id.rg);
        RadioButton rb = findViewById(R.id.rb1);
        rb.setChecked(true);
        arrayListRecords = new ArrayList<>();
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
        //
        arrayListRecordsNotReversed.add(new RecordForRecycler(1, "10.43 км", "10:32:47", "16.05.1989 04:20"));
        arrayListRecordsNotReversed.add(new RecordForRecycler(2, "30.21 км", "05:04:52", "16.03.2002 14:48"));
        arrayListRecordsNotReversed.add(new RecordForRecycler(3, "20.14 км", "08:09:29", "27.03.2002 19:00"));
        arrayListRecordsNotReversed.add(new RecordForRecycler(4, "40.89 км", "07:11:09", "15.04.2005 13:37"));
        //
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

    public void sortirovka(View view){
        ArrayList<RecordForRecycler> arrayList = arrayListRecords;
        if(!switchCompat.isChecked() && radioGroup.getCheckedRadioButtonId() == R.id.rb1){
            arrayList.sort(RecordForRecycler.compareByDist);
        }
        if(!switchCompat.isChecked() && radioGroup.getCheckedRadioButtonId() == R.id.rb2){
            arrayList.sort(RecordForRecycler.compareByTime);
        }
        if(switchCompat.isChecked() && radioGroup.getCheckedRadioButtonId() == R.id.rb1){
            arrayList.sort(RecordForRecycler.compareByDistReversed);
        }
        if(switchCompat.isChecked() && radioGroup.getCheckedRadioButtonId() == R.id.rb2){
            arrayList.sort(RecordForRecycler.compareByTimeReversed);
        }
        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
            Intent intent = new Intent(RecyclerViewActivity.this, MapInformationActivity.class);
            intent.putExtra("idOfRecord", arrayListRecords.get(position).getId());
            startActivity(intent);
        };
        RecordAdapter adapter = new RecordAdapter(this, arrayList, recordClickListener);
        recyclerView.setAdapter(adapter);
    }
}