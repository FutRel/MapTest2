package com.example.maptest.data;

import android.provider.BaseColumns;

public final class RecordsContract {

    private RecordsContract(){}

    public static final class ClassForRecords implements BaseColumns {
        public static String table_name = "records";
        public static String _id = BaseColumns._ID;
        public static String column_distance = "distance";
        public static String column_time = "time";
        public static String column_date = "date";
    }
}
