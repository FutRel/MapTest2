package com.example.maptest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordsDBHelper extends SQLiteOpenHelper {

    private static final String database_name = "records.db";
    public static int database_version = 1;

    public RecordsDBHelper(Context context) {
        super(context, database_name, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + RecordsContract.ClassForRecords.table_name + " (" +
                RecordsContract.ClassForRecords._id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecordsContract.ClassForRecords.column_distance + " REAL NOT NULL, " +
                RecordsContract.ClassForRecords.column_time + " INTEGER NOT NULL, " +
                RecordsContract.ClassForRecords.column_date + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}
