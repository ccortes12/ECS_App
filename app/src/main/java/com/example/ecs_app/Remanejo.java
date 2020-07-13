package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Remanejo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remanejo);

        getSupportActionBar().setTitle("Remanejo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
