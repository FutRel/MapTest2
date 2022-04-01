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
            TimeUnit.SECONDS.sleep(1);
            Intent intent2 = new Intent(StartActivity.this, MapActivity.class);
            startActivity(intent2);
        } catch (InterruptedException e) {}
    });
    final MediaPlayer mp = MediaPlayer.create(this, R.raw.sound);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mp.start();
        thread.start();
    }
}