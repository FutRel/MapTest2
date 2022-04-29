package com.example.maptest.recycler;

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
