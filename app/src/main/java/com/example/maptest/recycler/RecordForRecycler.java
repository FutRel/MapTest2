package com.example.maptest.recycler;

import java.util.Comparator;

public class RecordForRecycler {

    private int id;
    private String distance;
    private String time;
    private String date;

    public RecordForRecycler(int id, String distance, String time, String date) {
        this.id = id;
        this.distance = distance;
        this.time = time;
        this.date = date;
    }

    public static final Comparator<RecordForRecycler> compareByDist = new Comparator<RecordForRecycler>() {
        @Override
        public int compare(RecordForRecycler rfr1, RecordForRecycler rfr2) {
            int index1 = rfr1.getDistance().indexOf(',');
            int index2 = rfr2.getDistance().indexOf(',');
            int km1 = Integer.parseInt(rfr1.getDistance().substring(0, index1)) * 1000;
            int km2 = Integer.parseInt(rfr2.getDistance().substring(0, index2)) * 1000;
            int m1 = Integer.parseInt(rfr1.getDistance().substring(index1 + 1, index1 + 3)) * 10;
            int m2 = Integer.parseInt(rfr2.getDistance().substring(index2 + 1, index2 + 3)) * 10;
            return km1 + m1 - km2 - m2;
        }
    };

    public static final Comparator<RecordForRecycler> compareByDistReversed = new Comparator<RecordForRecycler>() {
        @Override
        public int compare(RecordForRecycler rfr1, RecordForRecycler rfr2) {
            int index1 = rfr1.getDistance().indexOf(',');
            int index2 = rfr2.getDistance().indexOf(',');
            int km1 = Integer.parseInt(rfr1.getDistance().substring(0, index1)) * 1000;
            int km2 = Integer.parseInt(rfr2.getDistance().substring(0, index2)) * 1000;
            int m1 = Integer.parseInt(rfr1.getDistance().substring(index1 + 1, index1 + 3)) * 10;
            int m2 = Integer.parseInt(rfr2.getDistance().substring(index2 + 1, index2 + 3)) * 10;
            return km2 + m2 - km1 - m1;
        }
    };

    public static final Comparator<RecordForRecycler> compareByTime = new Comparator<RecordForRecycler>() {
        @Override
        public int compare(RecordForRecycler rfr1, RecordForRecycler rfr2) {
            int h1 = Integer.parseInt(rfr1.getTime().substring(0,2)) * 3600;
            int h2 = Integer.parseInt(rfr2.getTime().substring(0,2)) * 3600;
            int m1 = Integer.parseInt(rfr1.getTime().substring(3,5)) * 60;
            int m2 = Integer.parseInt(rfr2.getTime().substring(3,5)) * 60;
            int s1 = Integer.parseInt(rfr1.getTime().substring(6,8));
            int s2 = Integer.parseInt(rfr2.getTime().substring(6,8));
            int time1 = h1 + m1 + s1;
            int time2 = h2 + m2 + s2;
            return time1 - time2;
        }
    };

    public static final Comparator<RecordForRecycler> compareByTimeReversed = new Comparator<RecordForRecycler>() {
        @Override
        public int compare(RecordForRecycler rfr1, RecordForRecycler rfr2) {
            int h1 = Integer.parseInt(rfr1.getTime().substring(0,2)) * 3600;
            int h2 = Integer.parseInt(rfr2.getTime().substring(0,2)) * 3600;
            int m1 = Integer.parseInt(rfr1.getTime().substring(3,5)) * 60;
            int m2 = Integer.parseInt(rfr2.getTime().substring(3,5)) * 60;
            int s1 = Integer.parseInt(rfr1.getTime().substring(6,8));
            int s2 = Integer.parseInt(rfr2.getTime().substring(6,8));
            int time1 = h1 + m1 + s1;
            int time2 = h2 + m2 + s2;
            return time2 - time1;
        }
    };

    public int getId() {
        return id;
    }
    public String getDistance() {
        return distance;
    }
    public String getTime() {
        return time;
    }
    public String getDate() {
        return date;
    }
}
