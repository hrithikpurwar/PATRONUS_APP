package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.TaskStackBuilder;

import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseAnalytics;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,userInterface.class);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        TaskStackBuilder.create(this).addNextIntentWithParentStack(intent).startActivities();
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }
}