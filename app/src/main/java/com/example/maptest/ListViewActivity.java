package com.example.maptest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.maptest.data.RecordsContract;
import com.example.maptest.data.RecordsDBHelper;
import com.example.maptest.recycler.RecordAdapter;
import com.example.maptest.recycler.RecordForRecycler;
import com.example.maptest.recycler.RecyclerItemSpace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    private RecordsDBHelper rdbHelper;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
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
        ArrayList<RecordForRecycler> arrayListRecords = new ArrayList<>();
        ArrayList<Integer> arrayListID = new ArrayList<>();
        int counter = 1;
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            float distance = cursor.getFloat(1);
            int time = cursor.getInt(2);
            String date = cursor.getString(3);
            String numberToItem = "№" + counter;
            String distanceToItem = String.format("%.2f", distance / 1000) + " км";
            String timeHours = "" + time / 3600;
            if(timeHours.length() == 1) timeHours = "0" + timeHours;
            String timeMin = "" + time % 3600 / 60;
            if(timeMin.length() == 1) timeMin = "0" + timeMin;
            String timeSec = "" + time % 3600 % 60;
            if(timeSec.length() == 1) timeSec = "0" + timeSec;
            String timeToItem = timeHours + ":" + timeMin + ":" + timeSec;
            arrayListRecords.add(new RecordForRecycler(numberToItem, distanceToItem, timeToItem, date));
            arrayListID.add(id);
            counter ++;
        }
        cursor.close();
        if(!arrayListRecords.isEmpty()){
            recyclerView = findViewById(R.id.list);
            recyclerView.addItemDecoration(new RecyclerItemSpace(10));
            RecordAdapter.OnRecordClickListener recordClickListener = new RecordAdapter.OnRecordClickListener() {
                @Override
                public void onRecordClick(RecordForRecycler record, int position) {
                    Intent intent = new Intent(ListViewActivity.this, MapInformationActivity.class);
                    intent.putExtra("number", position + 1);
                    intent.putExtra("idOfRecord", arrayListID.get(position));
                    startActivity(intent);
                }
            };
            RecordAdapter adapter = new RecordAdapter(this, arrayListRecords, recordClickListener);
            recyclerView.setAdapter(adapter);
        }
    }

    public void intentToMA(View view){
        Intent intent = new Intent(ListViewActivity.this, MainActivity.class);
        startActivity(intent);
    }
}