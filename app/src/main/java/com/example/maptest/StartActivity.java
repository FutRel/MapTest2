package com.example.maptest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {

    Thread thread = new Thread(() -> {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            Intent intent2 = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent2);
        } catch (InterruptedException e) {}
    });
    TextView phrases;
    String[] arrOfPhrases = new String[]{
            "Счастливого пути!",
            "Доброго пути!",
            "Счастливо добраться!",
            "Комфортной езды!",
            "Быстрого пути!",
            "Без приключений!",
            "В добрый путь!",
            "Будь внимательней на дороге!",
            "Удачи на дорогах",
            "Пусть удовольствие от дороги будет сильнее усталости",
            "Без бешеных бабулек!",
            "Скатертью дорога!",
            "Попутного ветра!",
            "Не гоняй и дураку дорогу дай",
            "Да прибудет с тобой Сила!",
            "Да благославит тебя Всеотец",
            ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sound);
        mp.start();
        phrases = findViewById(R.id.phrases);
        phrases.setText(arrOfPhrases[(int)(Math.random()* arrOfPhrases.length)]);
        thread.start();
    }
}