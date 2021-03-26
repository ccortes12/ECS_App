package com.example.ecs_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.Entidades.Paquete;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class Despachar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner minerasSpinner;
    private EditText codigoBarra, lote, paquete,editTextLote;
    private TextView peso, estado, tituloSeccion, celda, area, textViewLote;
    private Button buscar, despachar;
    private Switch modoManual;
    private LinearLayout seccionUbicacion;
    private ArrayList<Minera> listaMineras;
    private ArrayList<String> auxSpinner;
    private ArrayAdapter<String> comboAdapter;
    private String fecha, turno, rutUsuario;
    private Paquete busquedaPaquete;
    private int rutCliente, relacionCliente;
    WS_Torpedo ws = new WS_TorpedoImp();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despacho);

        getSupportActionBar().setTitle("Despachar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        minerasSpinner = findViewById(R.id.spinner_minera2);
        auxSpinner = new ArrayList<String>();

        codigoBarra = findViewById(R.id.editText_codigoPaquete);
        lote = findViewById(R.id.ingreso_lote2);
        paquete = findViewById(R.id.ingreso_lote3);
        peso = findViewById(R.id.textView61);
        estado = findViewById(R.id.textView65);
        tituloSeccion = findViewById(R.id.textView77);
        celda = findViewById(R.id.textView80);
        area = findViewById(R.id.textView78);
        buscar = findViewById(R.id.button9);
        despachar = findViewById(R.id.button11);
        modoManual = findViewById(R.id.switch3);
        seccionUbicacion = findViewById(R.id.linearLayout16);
        editTextLote = findViewById(R.id.editTextLote3);
        textViewLote = findViewById(R.id.textViewLote3);


        cargarSpinner();
        estadoCampos();

        fecha = ((AtiApp) Despachar.this.getApplication()).getFecha();
        turno = Integer.toString(((AtiApp) Despachar.this.getApplication()).getTurno());
        rutUsuario = Integer.toString(((AtiApp) Despachar.this.getApplication()).getRutUsuario());

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
                    if(minerasSpinner.getSelectedItem().toString().equalsIgnoreCase(m.getVchNombreFantasia())){
                        rutCliente = m.getIntRutCliente();
                    }
                }

                if (modoManual.isChecked()) {
                    try {
                        String[] params = {String.valueOf(rutCliente),editTextLote.getText().toString(), codigoBarra.getText().toString()};
                        busquedaPaquete = new Despachar.ecs_BuscarPaquete().execute(params).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        String[] params = {String.valueOf(rutCliente),  codigoBarra.getText().toString()};
                        busquedaPaquete = new Despachar.ecs_BuscarPaquetesCB().execute(params).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                if(busquedaPaquete.getDescEstado().equalsIgnoreCase("Recepcionado")) {
                    seccionUbicacion.setVisibility(View.INVISIBLE);
                    despachar.setVisibility(View.VISIBLE);
                    tituloSeccion.setVisibility(View.INVISIBLE);

                    lote.setText(busquedaPaquete.getLote());
                    paquete.setText(busquedaPaquete.getIdPaquete());
                    peso.setText(String.valueOf(busquedaPaquete.getPeso()));

                }else if(busquedaPaquete.getDescEstado().equalsIgnoreCase("Almacenado")){

                    seccionUbicacion.setVisibility(View.VISIBLE);
                    tituloSeccion.setVisibility(View.VISIBLE);
                    despachar.setVisibility(View.VISIBLE);


                    lote.setText(busquedaPaquete.getLote());
                    paquete.setText(busquedaPaquete.getIdPaquete());
                    peso.setText(String.valueOf(busquedaPaquete.getPeso()));
                    area.setText(busquedaPaquete.getArea());
                    celda.setText(busquedaPaquete.getCelda());

                }else if(busquedaPaquete.getDescEstado().equalsIgnoreCase("Despachado")) {

                    lote.setText(busquedaPaquete.getLote());
                    paquete.setText(busquedaPaquete.getIdPaquete());
                    peso.setText(String.valueOf(busquedaPaquete.getPeso()));

                }else{

                    lote.setText("");
                    paquete.setText("");
                    peso.setText("");
                    seccionUbicacion.setVisibility(View.INVISIBLE);
                    tituloSeccion.setVisibility(View.INVISIBLE);
                    despachar.setVisibility(View.INVISIBLE);
                }

                estado.setText(busquedaPaquete.getDescEstado());
            }
        });

        despachar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(modoManual.isChecked()){

                    new AlertDialog.Builder(Despachar.this)
                            .setTitle("Confirmar despacho paquete")
                            .setMessage("Paquete a despachar \n" +
                                    "Lote : " + editTextLote.getText().toString() + "\n" +
                                    "Codigo Paquete : " + busquedaPaquete.getIdPaquete())
                            .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Primero debo despachar y luego almacenar
                                    String[] params = {busquedaPaquete.getIdPaquete(), busquedaPaquete.getArea(),
                                            busquedaPaquete.getCelda(),
                                            String.valueOf(rutUsuario), fecha, turno};

                                    try {
                                        String respDespacho = new Despachar.ecs_Despachar().execute(params).get();
                                        if (respDespacho.equalsIgnoreCase("OK")) {
                                            Toast.makeText(Despachar.this, "Despachado con exito", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(Despachar.this, "ERROR", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                    estadoCampos();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("Mensaje", "Se cancelo acción");
                                }
                            })
                            .show();
                } else {

                    String[] params = {busquedaPaquete.getIdPaquete(), busquedaPaquete.getArea(), busquedaPaquete.getCelda(),
                            String.valueOf(rutUsuario), fecha, turno};

                    try {
                        String resp = new Despachar.ecs_Despachar().execute(params).get();
                        if (resp.equalsIgnoreCase("OK")) {
                            Toast.makeText(Despachar.this, "Despachado con exito", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Despachar.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
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

    private void cargarSpinner(){

        listaMineras = ((AtiApp) Despachar.this.getApplication()).getListaMineras();

        Iterator<Minera> i = listaMineras.iterator();
        auxSpinner.add("");
        while (i.hasNext()) {
            Minera item = i.next();
            auxSpinner.add(item.getVchNombreFantasia());
        }
        minerasSpinner.setOnItemSelectedListener(this);
        comboAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, auxSpinner);
        minerasSpinner.setAdapter(comboAdapter);

    }

    private void estadoCampos(){

        estado.setText("");
        peso.setText("");
        codigoBarra.setText("");
        paquete.setText("");
        lote.setText("");
        editTextLote.setText("");

        despachar.setVisibility(View.INVISIBLE);
        seccionUbicacion.setVisibility(View.INVISIBLE);
        tituloSeccion.setVisibility(View.INVISIBLE);


        lote.setFocusable(false);
        lote.setCursorVisible(false);
        paquete.setFocusable(false);
        paquete.setCursorVisible(false);


    }

    private class ecs_BuscarPaquetesCB extends AsyncTask<String,Void, Paquete> {

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

    private class ecs_Despachar extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            return ws.ecs_Despachar(strings);
        }
    }
}
