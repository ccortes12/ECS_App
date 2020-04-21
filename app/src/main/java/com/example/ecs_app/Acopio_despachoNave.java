package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Acopio_despachoNave extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acopio_despacho_nave);

        getSupportActionBar().setTitle("Acopio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
