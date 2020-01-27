package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TRF_00 extends AppCompatActivity {

    private Button button_sello;
    private Button button_lotes;
    private Button button_limpiar;

    private EditText cont,codigo,digit,csg,mar,gross,zun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trf_00);

        button_sello = (Button) findViewById(R.id.button_sello);
        button_lotes = (Button) findViewById(R.id.button_lotes);
        button_limpiar = (Button) findViewById(R.id.button_clean);

        cont = (EditText) findViewById(R.id.editText_cont);
        codigo = (EditText) findViewById(R.id.editText_codigo);
        digit = (EditText) findViewById(R.id.editText_digit);
        csg = (EditText) findViewById(R.id.editText_csg);
        mar = (EditText) findViewById(R.id.editText_mar);
        gross = (EditText) findViewById(R.id.editText_gross);
        zun = (EditText) findViewById(R.id.editText_zun);


        button_sello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_sello = new Intent(v.getContext(),TRF_01_sellos.class);
                startActivity(to_sello);
            }
        });

        button_lotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_lotes = new Intent(v.getContext(),TRF_02_lotes.class);
                startActivity(to_lotes);
            }
        });

        button_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarText();
            }
        });
    }

    private void limpiarText(){
        cont.getText().clear();
        codigo.getText().clear();
        digit.getText().clear();
        csg.getText().clear();
        mar.getText().clear();
        gross.getText().clear();
        zun.getText().clear();
    }
}
