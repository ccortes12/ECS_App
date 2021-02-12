package com.example.ecs_app.Transferencia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.ecs_app.R;
import com.example.ecs_app.WS_Torpedo;
import com.example.ecs_app.WS_TorpedoImp;


public class Embarque extends AppCompatActivity {

    WS_Torpedo ws = new WS_TorpedoImp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embarque);

        getSupportActionBar().setTitle("Transferencia");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }


    private class ecs_registroTransferencia extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {

            return ws.ecs_RegistroTransferencia(strings);

        }
    }


}