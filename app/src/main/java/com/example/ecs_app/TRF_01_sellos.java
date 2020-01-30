package com.example.ecs_app;

import android.content.Intent;
import android.os.Bundle;

import com.example.ecs_app.Entidades.Contenedor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TRF_01_sellos extends AppCompatActivity {

    private TextView textViewCont,textViewOperacion;
    private Button ingresarSello;
    private EditText sello;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trf_01_sellos);

        textViewCont = (TextView) findViewById(R.id.Contenedor);
        textViewOperacion = (TextView) findViewById(R.id.Operacion);
        ingresarSello = (Button) findViewById(R.id.button_grabarSello);
        sello = (EditText) findViewById(R.id.editText_sello);



        Intent intent = getIntent();
        Contenedor contenedor = (Contenedor) intent.getSerializableExtra("objContenedor");

        String cadenaOperacion = contenedor.getAnnoOperacion() + " - " + contenedor.getCorOperacion();
        String cadenaContenedor = contenedor.getSigla() + "  " + contenedor.getNumero() + " - " + contenedor.getDigito();

        textViewCont.setText(cadenaContenedor);
        textViewOperacion.setText(cadenaOperacion);


        if(!contenedor.getSello().equalsIgnoreCase("")){
            sello.setText(contenedor.getSello());
            sello.setCursorVisible(false);
            sello.setFocusable(false);
        }

        ingresarSello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insertar el sello en el sistema
            }
        });



    }

}
