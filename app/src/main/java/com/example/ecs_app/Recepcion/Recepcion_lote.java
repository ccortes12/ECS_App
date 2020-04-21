package com.example.ecs_app.Recepcion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ecs_app.R;

public class Recepcion_lote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepcion_lote);

        getSupportActionBar().setTitle("Recepción lote");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
