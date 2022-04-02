package com.example.maptest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.maptest.data.RecordsContract;
import com.example.maptest.data.RecordsDBHelper;
import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    private RecordsDBHelper rdbHelper;

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
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<Integer> arrayListID = new ArrayList<>();
        int counter = 1;
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            float distance = cursor.getFloat(1);
            int time = cursor.getInt(2);
            String date = cursor.getString(3);
            String toAl = "Ride №" + counter + ", distance: " + distance + "km, time: " + time + " seconds,  date: " + date;
            arrayList.add(toAl);
            arrayListID.add(id);
            counter ++;
        }
        cursor.close();
        if(!arrayList.isEmpty()){
            ListView listView = findViewById(R.id.listview);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
            arrayAdapter.notifyDataSetChanged();
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(ListViewActivity.this, MapInformationActivity.class);
                    intent.putExtra("number", i + 1);
                    intent.putExtra("idOfRecord", arrayListID.get(i));
                    startActivity(intent);
                }
            });
        }
    }

    public void intentToMA(View view){
        Intent intent = new Intent(ListViewActivity.this, MainActivity.class);
        startActivity(intent);
    }
}