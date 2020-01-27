package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TRF_00 extends AppCompatActivity {

    private Button button_sello;
    private Button button_lotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trf_00);

        button_sello = (Button) findViewById(R.id.button_sello);
        button_lotes = (Button) findViewById(R.id.button_lotes);


        button_sello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_sello = new Intent(v.getContext(),TRF_01_sellos.class);
                startActivity(to_sello);
            }
        });

    }
}
