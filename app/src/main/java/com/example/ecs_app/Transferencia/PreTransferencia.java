package com.example.ecs_app.Transferencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecs_app.AtiApp;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.R;
import com.example.ecs_app.WS_Torpedo;
import com.example.ecs_app.WS_TorpedoImp;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PreTransferencia extends AppCompatActivity {

    private EditText correlativo;
    private Button siguienteButton;
    private ArrayList<Minera> lista;
    WS_Torpedo ws = new WS_TorpedoImp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_transferencia);

        getSupportActionBar().setTitle("Transferencia");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        correlativo = findViewById(R.id.EditTextCodigoB);
        siguienteButton = findViewById(R.id.siguienteButton);

        siguienteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] params = {correlativo.getText().toString()};

                try {
                    lista = new ecs_listarMinerasRecalada().execute(params).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(lista.size()>0){

                    Intent next = new Intent(v.getContext(), Transferencia.class);

                    ((AtiApp) PreTransferencia.this.getApplication()).setLastCorrelativo(correlativo.getText().toString());
                    startActivity(next);

                }else{
                    Toast.makeText(PreTransferencia.this, "Correlativo ingresado invalido" , Toast.LENGTH_SHORT).show();
                    correlativo.setText("");
                }


            }
        });

        correlativo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE){
                    String[] params = {correlativo.getText().toString()};

                    try {
                        lista = new ecs_listarMinerasRecalada().execute(params).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(lista.size()>0){

                        Intent next = new Intent(v.getContext(), Transferencia.class);

                        ((AtiApp) PreTransferencia.this.getApplication()).setLastCorrelativo(correlativo.getText().toString());
                        startActivity(next);
                        //finish();

                    }else{
                        Toast.makeText(PreTransferencia.this, "Correlativo ingresado invalido" , Toast.LENGTH_SHORT).show();
                        correlativo.setText("");
                    }

                }
                return false;
            }
        });

    }


    private class ecs_listarMinerasRecalada extends AsyncTask<String,Void, ArrayList<Minera>> {

        @Override
        protected ArrayList<Minera> doInBackground(String... strings) {

            return ws.ecs_ListarMinerasRecalada(strings);

        }
    }
}