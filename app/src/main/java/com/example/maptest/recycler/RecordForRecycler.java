package com.example.maptest.recycler;

public class RecordForRecycler {

    private String number;
    private String distance;
    private String time;
    private String date;

    public RecordForRecycler(String number, String distance, String time, String date) {
        this.number = number;
        this.distance = distance;
        this.time = time;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
