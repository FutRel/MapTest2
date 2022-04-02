package com.example.maptest.data;

import android.provider.BaseColumns;

public final class PointsContract {

    private PointsContract(){}

    public static final class ClassForPoints implements BaseColumns {
        public static String table_name = "points";
        public static String _id = BaseColumns._ID;
        public static String column_latitude = "latitude";
        public static String column_longitude = "longitude";
        public static String column_recordId = "recordId";
    }
}
