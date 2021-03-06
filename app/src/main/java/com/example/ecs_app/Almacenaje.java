package com.example.ecs_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecs_app.Entidades.Area;
import com.example.ecs_app.Entidades.Celda;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.Entidades.Paquete;
import com.example.ecs_app.Recepcion.Recepcion_lote;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class Almacenaje extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner minerasSpinner, areasSpinner, celdasSpinner;
    private EditText codigoBarra, lote, paquete, editTextLote;
    private TextView peso, estado, tituloSeccion, areaTextView, celdaTextView, textViewLote;
    private Button buscar, almacenar;
    private Switch modoManual;
    private LinearLayout seccionUbicacion;
    private ArrayList<Minera> listaMineras;
    private ArrayList<Area> listaAreas;
    private ArrayList<Celda> listaCeldas;
    private ArrayList<String> auxSpinner, auxSpinnerCelda, auxSpinnerArea;
    private ArrayAdapter<String> comboAdapter;
    private String fecha, turno, rutUsuario;
    private int rutCliente, relacionCliente;
    private Paquete busquedaPaquete;
    private WS_Torpedo ws = new WS_TorpedoImp();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacenaje);

        getSupportActionBar().setTitle("Almacenaje");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        minerasSpinner = findViewById(R.id.spinner_minera2);
        auxSpinner = new ArrayList<String>();
        areasSpinner = findViewById(R.id.spinner_area);
        auxSpinnerArea = new ArrayList<String>();
        celdasSpinner = findViewById(R.id.spinner_celda);
        auxSpinnerCelda = new ArrayList<String>();

        codigoBarra = findViewById(R.id.editText_codigoPaquete);
        modoManual = findViewById(R.id.switch_manual);
        buscar = findViewById(R.id.button15);
        lote = findViewById(R.id.ingreso_lote2);
        paquete = findViewById(R.id.ingreso_lote3);
        peso = findViewById(R.id.textView61);
        estado = findViewById(R.id.textView65);
        almacenar = findViewById(R.id.button18);
        seccionUbicacion = findViewById(R.id.linearLayout16);
        tituloSeccion = findViewById(R.id.textView95);
        areaTextView = findViewById(R.id.area_textView);
        celdaTextView = findViewById(R.id.celda_textView);
        editTextLote = findViewById(R.id.editText_lote2);
        textViewLote = findViewById(R.id.textViewLote2);

        cargarSpinner();
        cargarSpinnerAreas();
        estadoCampos(false);

        fecha = ((AtiApp) Almacenaje.this.getApplication()).getFecha().replaceAll(" ", "");
        turno = Integer.toString(((AtiApp) Almacenaje.this.getApplication()).getTurno());
        rutUsuario = Integer.toString(((AtiApp) Almacenaje.this.getApplication()).getRutUsuario());

        modoManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (modoManual.isChecked()) {
                    estadoCampos(true);
                    textViewLote.setVisibility(View.VISIBLE);
                    editTextLote.setVisibility(View.VISIBLE);
                    editTextLote.requestFocus();
                } else {
                    estadoCampos(false);
                    textViewLote.setVisibility(View.GONE);
                    editTextLote.setVisibility(View.GONE);
                }
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Busqueda objeto minera segun la seleccion del spinner
                for (Minera m : listaMineras) {
                    if (minerasSpinner.getSelectedItem().toString().equalsIgnoreCase(m.getVchNombreFantasia())) {
                        rutCliente = m.getIntRutCliente();
                    }
                }

                if (modoManual.isChecked()) {  //Busqueda manual
                    try {
                        String[] params = {String.valueOf(rutCliente),editTextLote.getText().toString(),codigoBarra.getText().toString()};
                        busquedaPaquete = new Almacenaje.ecs_BuscarPaquete().execute(params).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {  //Busqueda por CB
                    try {
                        String [] params = {String.valueOf(rutCliente),codigoBarra.getText().toString()};
                        busquedaPaquete = new Almacenaje.ecs_BuscarPaquetesCB().execute(params).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                if(busquedaPaquete.getDescEstado().equalsIgnoreCase("Recepcionado") ||
                        busquedaPaquete.getDescEstado().equalsIgnoreCase("Despachado")) {
                    seccionUbicacion.setVisibility(View.VISIBLE);
                    almacenar.setVisibility(View.VISIBLE);
                    tituloSeccion.setVisibility(View.VISIBLE);

                    areaTextView.setVisibility(View.GONE);
                    celdaTextView.setVisibility(View.GONE);

                    areasSpinner.setVisibility(View.VISIBLE);
                    celdasSpinner.setVisibility(View.VISIBLE);

                    celdasSpinner.setSelection(0);
                    areasSpinner.setSelection(0);

                    peso.setText(String.valueOf(busquedaPaquete.getPeso()));
                    lote.setText(busquedaPaquete.getLote().toString());
                    paquete.setText(busquedaPaquete.getCodigoPaquete().toString());

                }else if(busquedaPaquete.getDescEstado().equalsIgnoreCase("Almacenado")) {

                    seccionUbicacion.setVisibility(View.VISIBLE);
                    tituloSeccion.setVisibility(View.VISIBLE);
                    almacenar.setVisibility(View.INVISIBLE);

                    peso.setText(String.valueOf(busquedaPaquete.getPeso()));
                    lote.setText(busquedaPaquete.getLote().toString());
                    paquete.setText(String.valueOf(busquedaPaquete.getPeso()));

                    //cargar ubicacion al spinner y bloquear spinner
                    cargarSpinnerBlock(busquedaPaquete);

                }else {
                    peso.setText("");
                    lote.setText("");
                    paquete.setText("");
                    seccionUbicacion.setVisibility(View.INVISIBLE);
                    tituloSeccion.setVisibility(View.INVISIBLE);
                    almacenar.setVisibility(View.INVISIBLE);
                }

                estado.setText(busquedaPaquete.getDescEstado());

            }
        });

        almacenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (modoManual.isChecked()) {

                    new AlertDialog.Builder(Almacenaje.this)
                            .setTitle("Confirmar almacenar paquete")
                            .setMessage("Paquete a almacenar: \n" +
                                    "Lote: " + editTextLote.getText().toString() + "\n" +
                                    "Codigo Paquete: " + busquedaPaquete.getIdPaquete() + "\n" +
                                    "Area: " + areasSpinner.getSelectedItem().toString() + "\n" +
                                    "Celda: " + celdasSpinner.getSelectedItem().toString())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Primero debo despachar y luego almacenar
                                    String[] params = {areasSpinner.getSelectedItem().toString(), celdasSpinner.getSelectedItem().toString(),
                                            busquedaPaquete.getIdPaquete(), rutUsuario,fecha, turno};

                                    try {
                                        String resp = new Almacenaje.ecs_AlmacenaPaquete().execute(params).get();

                                        if (resp.equalsIgnoreCase("OK")) {
                                            Toast.makeText(Almacenaje.this, "Almacenado con exito", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(Almacenaje.this, "ERROR", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    estadoCampos(true);
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

                    //Primero debo despachar y luego almacenar
                    String[] params = {areasSpinner.getSelectedItem().toString(), celdasSpinner.getSelectedItem().toString(),
                            busquedaPaquete.getIdPaquete(), rutUsuario, fecha, turno};

                    try {
                        String resp = new Almacenaje.ecs_AlmacenaPaquete().execute(params).get();

                        if (resp.equalsIgnoreCase("OK")) {
                            Toast.makeText(Almacenaje.this, "Almacenado con exito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Almacenaje.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    estadoCampos(false);
                }
            }
        });

        areasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Cargar spinner de celdas dependiendo de la opcion

                if(!parent.getItemAtPosition(position).equals("") && !estado.getText().toString().equalsIgnoreCase("Almacenado")){

                    try {
                        String [] codArea = {areasSpinner.getSelectedItem().toString()};
                        listaCeldas = new ecs_listarCeldas().execute(codArea).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    listaCeldas = new ArrayList();
                }
                cargarCeldas();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Borrar seleccion

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void cargarSpinnerBlock(Paquete paquete){

        areasSpinner.setVisibility(View.GONE);
        areaTextView.setText(paquete.getArea());
        areaTextView.setVisibility(View.VISIBLE);
        celdasSpinner.setVisibility(View.GONE);
        celdaTextView.setText(paquete.getCelda());
        celdaTextView.setVisibility(View.VISIBLE);

    }

    private void cargarSpinnerAreas(){
        listaAreas = ((AtiApp) Almacenaje.this.getApplication()).getListaAreas();
        Iterator<Area> j = listaAreas.iterator();
        auxSpinnerArea.add("");
        while(j.hasNext()){
            Area item = j.next();
            auxSpinnerArea.add(String.valueOf(item.getCodArea()));
        }
        areasSpinner.setOnItemSelectedListener(this);
        comboAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,auxSpinnerArea);
        areasSpinner.setAdapter(comboAdapter);
    }

    private void cargarSpinner(){

        listaMineras = ((AtiApp) Almacenaje.this.getApplication()).getListaMineras();

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

    private void cargarCeldas(){

        Iterator<Celda> j = listaCeldas.iterator();
        auxSpinnerCelda = new ArrayList<String>();
        auxSpinnerCelda.add("");
        while(j.hasNext()){
            Celda item = j.next();
            auxSpinnerCelda.add(item.getCodCelda());
        }
        celdasSpinner.setOnItemSelectedListener(this);
        comboAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,auxSpinnerCelda);
        celdasSpinner.setAdapter(comboAdapter);
    }

    private void estadoCampos(boolean flag) {

        editTextLote.setText("");
        codigoBarra.setText("");
        estado.setText("");
        peso.setText("");
        codigoBarra.setText("");
        paquete.setText("");
        lote.setText("");

        almacenar.setVisibility(View.INVISIBLE);
        seccionUbicacion.setVisibility(View.INVISIBLE);
        tituloSeccion.setVisibility(View.INVISIBLE);

        lote.setFocusable(false);
        lote.setCursorVisible(false);
        paquete.setFocusable(false);
        paquete.setCursorVisible(false);


    }

    private class ecs_listarCeldas extends AsyncTask<String,Void,ArrayList<Celda>> {

        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<Celda> doInBackground(String... strings) {

            return ws.ecs_ListarCeldas(strings);
        }
    }

    private class ecs_BuscarPaquetesCB extends AsyncTask<String,Void, Paquete>{

        @SuppressLint("WrongThread")
        protected Paquete doInBackground(String... strings) {

            return  ws.ecs_BuscarPaquetesCB(strings);

        }
    }

    private class ecs_BuscarPaquete extends AsyncTask<String,Void, Paquete>{

        @SuppressLint("WrongThread")
        @Override
        protected Paquete doInBackground(String... strings) {

            return ws.ecs_BuscarPaquete(strings);

        }
    }

    private class ecs_AlmacenaPaquete extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            return ws.ecs_AlmacenaPaquete(strings);
        }
    }



}
