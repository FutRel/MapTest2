package com.example.maptest;

import android.content.Intent;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {

    Thread thread = new Thread(() -> {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            Intent intent2 = new Intent(StartActivity.this, MapActivity.class);
            startActivity(intent2);
        } catch (InterruptedException e) {}
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sound);
        mp.start();
        thread.start();
    }
}