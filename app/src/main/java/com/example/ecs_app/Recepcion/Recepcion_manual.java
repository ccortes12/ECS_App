package com.example.ecs_app.Recepcion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ecs_app.R;

public class Recepcion_manual extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepcion_manual);

        getSupportActionBar().setTitle("Recepción manual");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}
