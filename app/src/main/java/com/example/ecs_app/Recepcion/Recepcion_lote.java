package com.example.ecs_app.Recepcion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecs_app.AtiApp;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.Entidades.Paquete;
import com.example.ecs_app.R;
import com.example.ecs_app.WS_Torpedo;
import com.example.ecs_app.WS_TorpedoImp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class Recepcion_lote extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner listaMineras_spinner;
    private EditText codigoBarra, lote, paquete, editTextLote;
    private TextView peso, estado, textViewLote, pesoBruto, cantPiezas;
    private Button buscar, recepcionar;
    private Switch modoManual;
    private ArrayList<String> auxSpinner;
    private ArrayList<Minera> listaMineras;
    private String fechaRecepcion, turnoRecepcion;
    private int rutCliente, relacionCliente;
    private Paquete busquedaPaquete;
    private Toolbar toolbar;
    ArrayAdapter<String> comboAdapter;
    WS_Torpedo ws = new WS_TorpedoImp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepcion_lote);

        getSupportActionBar().setTitle("Recepción Lote");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listaMineras_spinner = findViewById(R.id.spinner_minera2);
        auxSpinner = new ArrayList<>();

        codigoBarra = findViewById(R.id.editText_codigoPaquete);
        modoManual = findViewById(R.id.switch1);
        buscar = findViewById(R.id.button3);
        lote = findViewById(R.id.ingreso_lote2);
        paquete = findViewById(R.id.ingreso_lote3);
        peso = findViewById(R.id.textView61);
        estado = findViewById(R.id.textView65);
        recepcionar = findViewById(R.id.button_recepcionar2);
        textViewLote = findViewById(R.id.textViewLote);
        editTextLote = findViewById(R.id.editTextLote);
        pesoBruto = findViewById(R.id.textView81);
        cantPiezas = findViewById(R.id.textView181);

        cargarSpinner();
        estadoCampos();

        fechaRecepcion = ((AtiApp) Recepcion_lote.this.getApplication()).getFecha();
        turnoRecepcion = Integer.toString(((AtiApp) Recepcion_lote.this.getApplication()).getTurno());


        modoManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (modoManual.isChecked()) {
                    textViewLote.setVisibility(View.VISIBLE);
                    editTextLote.setVisibility(View.VISIBLE);
                    editTextLote.requestFocus();

                } else {
                    textViewLote.setVisibility(View.GONE);
                    editTextLote.setVisibility(View.GONE);
                }
                estadoCampos();

            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                

                for (Minera m : listaMineras){
                    if(listaMineras_spinner.getSelectedItem().toString().equalsIgnoreCase(m.getVchNombreFantasia())){
                        rutCliente = m.getIntRutCliente();
                    }
                }

                if(modoManual.isChecked()){
                    try {
                        String[] params = {String.valueOf(rutCliente), editTextLote.getText().toString(), codigoBarra.getText().toString()};
                        busquedaPaquete = new ecs_BuscarPaquete().execute(params).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{
                    try {
                        String [] params = {String.valueOf(rutCliente),codigoBarra.getText().toString()};
                        busquedaPaquete = new ecs_BuscarPaquetesCB().execute(params).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(busquedaPaquete.getDescEstado().equalsIgnoreCase("No recepcionado")) {
                    recepcionar.setVisibility(View.VISIBLE);
                    peso.setText(String.valueOf(busquedaPaquete.getPeso()));

                }else if(busquedaPaquete.getDescEstado().equalsIgnoreCase("No se encuentra")){
                    peso.setText("");
                    recepcionar.setVisibility(View.INVISIBLE);
                    estadoCampos();

                }else {
                    lote.setText(busquedaPaquete.getLote());
                    paquete.setText(busquedaPaquete.getCodigoPaquete());
                    peso.setText(String.valueOf(busquedaPaquete.getPesoNeto()));
                    pesoBruto.setText(String.valueOf(busquedaPaquete.getPesoBruto()));
                    cantPiezas.setText(String.valueOf(busquedaPaquete.getPiezas()));
                }

                estado.setText(busquedaPaquete.getDescEstado());

            }
        });

        recepcionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Confirmar recepcion del paquete SOLO en caso de ingresos manuales
                if(modoManual.isChecked()){
                    new AlertDialog.Builder(Recepcion_lote.this)
                            .setTitle("Confirmar recepción paquete")
                            .setMessage("Paquete a recepcionar: \n" +
                                    "Lote : " + editTextLote.getText().toString() + "\n" +
                                    "Codigo Paquete : " + busquedaPaquete.getIdPaquete())
                            .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        String[] params = {busquedaPaquete.getIdPaquete(),String.valueOf(rutCliente),fechaRecepcion,String.valueOf(turnoRecepcion)};
                                        String resp = new ecs_RecepcionPaquete().execute(params).get();

                                        if(resp.equalsIgnoreCase("OK")){
                                            Toast.makeText(Recepcion_lote.this,"Recepcionado con exito",Toast.LENGTH_SHORT).show();

                                        }else{
                                            Toast.makeText(Recepcion_lote.this,"ERROR",Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    estadoCampos();
                                }
                            })
                            .setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("Mensaje" , "Se cancelo acción");
                                }
                            })
                            .show();
                }else{
                    try {
                        String[] params = {busquedaPaquete.getIdPaquete(), String.valueOf(rutCliente), fechaRecepcion, String.valueOf(turnoRecepcion)};
                        String resp = new ecs_RecepcionPaquete().execute(params).get();


                        //Validar respuesta recepcion
                        if (resp.equalsIgnoreCase("OK")) {
                            Toast.makeText(Recepcion_lote.this, "Recepcionado con exito", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Recepcion_lote.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    estadoCampos();
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void estadoCampos() {  //True si esta en modo manual

        editTextLote.setText("");
        codigoBarra.setText("");
        paquete.setText("");
        lote.setText("");
        estado.setText("");
        peso.setText("");
        pesoBruto.setText("");
        cantPiezas.setText("");

        lote.setFocusable(false);
        lote.setCursorVisible(false);
        paquete.setFocusable(false);
        paquete.setCursorVisible(false);

        recepcionar.setVisibility(View.INVISIBLE);
    }

    private void cargarSpinner(){

        listaMineras = ((AtiApp) Recepcion_lote.this.getApplication()).getListaMineras();

        Iterator<Minera> i = listaMineras.iterator();
        auxSpinner.add("");
        while(i.hasNext()){
            Minera item = i.next();
            auxSpinner.add(item.getVchNombreFantasia());
        }
        listaMineras_spinner.setOnItemSelectedListener(this);

        comboAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, auxSpinner);

        listaMineras_spinner.setAdapter(comboAdapter);

    }

    private class ecs_BuscarPaquetesCB extends AsyncTask<String,Void, Paquete>{

        @SuppressLint("WrongThread")
        protected Paquete doInBackground(String... strings) {

            return ws.ecs_BuscarPaquetesCB(strings);

        }
    }

    private class ecs_BuscarPaquete extends AsyncTask<String,Void, Paquete>{

        @SuppressLint("WrongThread")
        @Override
        protected Paquete doInBackground(String... strings) {

            return ws.ecs_BuscarPaquete(strings);

        }
    }

    private class ecs_RecepcionPaquete extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

            return ws.ecs_RecepcionPaquete(strings);

        }
    }
}
