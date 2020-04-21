package com.example.ecs_app.Recepcion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ecs_app.R;

public class Pre_recepcion extends AppCompatActivity {

    private Button button_recepcionLote, button_recepcionManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_recepcion);

        getSupportActionBar().setTitle("Menú Recepción");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        button_recepcionLote = findViewById(R.id.button_recepcion1);
        button_recepcionManual = findViewById(R.id.button_recepcionManual);

        button_recepcionManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentManual = new Intent(v.getContext(), Recepcion_manual.class);
                startActivity(intentManual);
            }
        });

        button_recepcionLote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLote = new Intent(v.getContext(),Recepcion_lote.class);
                startActivity(intentLote);
            }
        });
    }
}
