package com.example.maptest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PointsDBHelper extends SQLiteOpenHelper {

    private static final String database_name = "points.db";
    public static int database_version = 1;

    public PointsDBHelper(Context context){
        super(context, database_name, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + PointsContract.ClassForPoints.table_name + " (" +
                PointsContract.ClassForPoints._id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PointsContract.ClassForPoints.column_latitude + " REAL, " +
                PointsContract.ClassForPoints.column_longitude + " REAL, " +
                PointsContract.ClassForPoints.column_recordId + " INTEGER NOT NULL);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
