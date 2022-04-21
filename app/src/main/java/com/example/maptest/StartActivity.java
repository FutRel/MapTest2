package com.example.maptest;

import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {

    Thread thread = new Thread(() -> {
        try {
            TimeUnit.MILLISECONDS.sleep(1500);
            startActivity(new Intent(StartActivity.this, MainActivity.class));
        } catch (InterruptedException ignored) {}
    });
    TextView phrases;
    String[] arrOfPhrases = new String[]{
            "Счастливого пути",
            "Доброго пути",
            "Счастливо добраться",
            "Комфортной езды",
            "Быстрого пути",
            "Без приключений",
            "В добрый путь",
            "Будь внимательней на дороге",
            "Удачи на дорогах",
            "Без бешеных бабулек",
            "Попутного ветра",
            "Да прибудет с тобой Сила",
            "Да благославит тебя Всеотец",
            "SWAAAAG"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_start);

        phrases = findViewById(R.id.phrases);
        phrases.setText(arrOfPhrases[(int)(Math.random()* arrOfPhrases.length)]);

        thread.start();
    }
}