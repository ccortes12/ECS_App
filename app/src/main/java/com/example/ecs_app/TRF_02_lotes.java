package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ecs_app.Entidades.Contenedor;

public class TRF_02_lotes extends AppCompatActivity {

    private TextView textOperacion, textContenedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trf_02_lotes);

        textOperacion = (TextView) findViewById(R.id.textView24);
        textContenedor = (TextView) findViewById(R.id.textView26);

        Intent intent = getIntent();
        Contenedor contenedor = (Contenedor) intent.getSerializableExtra("objContenedor");

        String cadenaOperacion = contenedor.getAnnoOperacion() + " - " + contenedor.getCorOperacion();
        String cadenaContenedor = contenedor.getSigla() + "  " + contenedor.getNumero() + " - " + contenedor.getDigito();

        textOperacion.setText(cadenaOperacion);
        textContenedor.setText(cadenaContenedor);

    }

}
