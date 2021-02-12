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

import com.example.ecs_app.Entidades.Area;
import com.example.ecs_app.Entidades.Celda;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.Entidades.Paquete;

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

public class Remanejo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner minerasSpinner, areasSpinner, celdasSpinner;
    private EditText codigoBarra, lote, paquete;
    private TextView peso, estado, tituloSeccionOrigen, tituloSeccionDestino, celda, area;
    private Button buscar, remanejo;
    private Switch modoManual;
    private LinearLayout seccionOrigen, seccionDestino;
    private ArrayList<Minera> listaMineras;
    private ArrayList<Area> listaAreas;
    private ArrayList<Celda> listaCeldas;
    private ArrayList<String> auxSpinner, auxSpinnerCelda, auxSpinnerArea;
    private ArrayAdapter<String> comboAdapter;
    private String fecha, turno, rutUsuario;
    private Paquete busquedaPaquete;
    private int rutCliente, relacionCliente;

    WS_Torpedo ws = new WS_TorpedoImp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remanejo);

        minerasSpinner = findViewById(R.id.spinner_minera2);
        auxSpinner = new ArrayList<String>();
        areasSpinner = findViewById(R.id.spinner_area2);
        auxSpinnerArea = new ArrayList<String>();
        celdasSpinner = findViewById(R.id.spinner_celda2);
        auxSpinnerCelda = new ArrayList<String>();

        codigoBarra = findViewById(R.id.editText_codigoPaquete);
        lote = findViewById(R.id.ingreso_lote2);
        paquete = findViewById(R.id.ingreso_lote3);
        peso = findViewById(R.id.textView61);
        estado = findViewById(R.id.textView65);
        tituloSeccionOrigen = findViewById(R.id.textView58);
        celda = findViewById(R.id.textView_333);
        area = findViewById(R.id.textView_area22);
        buscar = findViewById(R.id.button3);
        remanejo = findViewById(R.id.button6);
        modoManual = findViewById(R.id.switch1);
        seccionOrigen = findViewById(R.id.linearLayout16);
        seccionDestino = findViewById(R.id.linearLayout18);
        tituloSeccionDestino = findViewById(R.id.textView75);

        cargarSpinner();
        cargarSpinnerAreas();
        estadoCampos(false);

        fecha = ((AtiApp) Remanejo.this.getApplication()).getFecha();
        turno = Integer.toString(((AtiApp) Remanejo.this.getApplication()).getTurno());
        rutUsuario = Integer.toString(((AtiApp) Remanejo.this.getApplication()).getRutUsuario());

        modoManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (modoManual.isChecked()) {
                    estadoCampos(true);
                } else {
                    estadoCampos(false);
                }
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
                        String[] params = {String.valueOf(rutCliente), lote.getText().toString(), paquete.getText().toString()};
                        busquedaPaquete = new Remanejo.ecs_BuscarPaquete().execute(params).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        String[] params = {String.valueOf(rutCliente), codigoBarra.getText().toString()};
                        busquedaPaquete = new Remanejo.ecs_BuscarPaquetesCB().execute(params).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                estado.setText(busquedaPaquete.getDescEstado());

                if(busquedaPaquete.getDescEstado().equalsIgnoreCase("Recepcionado") ||
                        busquedaPaquete.getDescEstado().equalsIgnoreCase("Despachado")) {
                    seccionOrigen.setVisibility(View.INVISIBLE);
                    seccionDestino.setVisibility(View.INVISIBLE);
                    remanejo.setVisibility(View.INVISIBLE);
                    tituloSeccionDestino.setVisibility(View.INVISIBLE);
                    tituloSeccionOrigen.setVisibility(View.INVISIBLE);

                    if(!modoManual.isChecked()){
                        lote.setText(busquedaPaquete.getLote());
                        paquete.setText(busquedaPaquete.getIdPaquete());
                    }
                    peso.setText(String.valueOf(busquedaPaquete.getPeso()));

                }else if(busquedaPaquete.getDescEstado().equalsIgnoreCase("Almacenado")){

                    seccionOrigen.setVisibility(View.VISIBLE);
                    seccionDestino.setVisibility(View.VISIBLE);
                    remanejo.setVisibility(View.VISIBLE);
                    tituloSeccionDestino.setVisibility(View.VISIBLE);
                    tituloSeccionOrigen.setVisibility(View.VISIBLE);

                    if(!modoManual.isChecked()){
                        lote.setText(busquedaPaquete.getLote());
                        paquete.setText(busquedaPaquete.getIdPaquete());
                    }
                    peso.setText(String.valueOf(busquedaPaquete.getPeso()));

                    areasSpinner.setSelection(0);
                    celdasSpinner.setSelection(0);

                    area.setText(busquedaPaquete.getArea());
                    celda.setText(busquedaPaquete.getCelda());

                }else{
                    peso.setText("");
                    seccionOrigen.setVisibility(View.INVISIBLE);
                    seccionDestino.setVisibility(View.INVISIBLE);
                    remanejo.setVisibility(View.INVISIBLE);
                    tituloSeccionDestino.setVisibility(View.INVISIBLE);
                    tituloSeccionOrigen.setVisibility(View.INVISIBLE);
                }

            }
        });

        remanejo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modoManual.isChecked()){

                    new AlertDialog.Builder(Remanejo.this)
                            .setTitle("Confirmar transferencia paquete")
                            .setMessage("Paquete a recepcionar: \n" +
                                    "Lote : " + lote.getText().toString() + "\n" +
                                    "Codigo Paquete : " + busquedaPaquete.getIdPaquete() + "\n" +
                                    "Area origen: " + area.getText().toString() + "\n" +
                                    "Celda origen: " + celda.getText().toString()+ "\n" +
                                    "Area destino: " + areasSpinner.getSelectedItem().toString() + "\n" +
                                    "Celda destino: " + celdasSpinner.getSelectedItem().toString()
                                    )
                            .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String[] params = {busquedaPaquete.getIdPaquete(), busquedaPaquete.getArea(), busquedaPaquete.getCelda(),
                                            areasSpinner.getSelectedItem().toString(), celdasSpinner.getSelectedItem().toString(),
                                            String.valueOf(rutUsuario), fecha, turno};

                                    try {
                                        String resp = new Remanejo.ecs_Remanejar().execute(params).get();
                                        if (resp.equalsIgnoreCase("OK")) {
                                            Toast.makeText(Remanejo.this, "Remanejo con exito", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(Remanejo.this, "ERROR", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                    estadoCampos(true);
                                }
                            })
                            .setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("Mensaje" , "Se cancelo acción");
                                }
                            })
                            .show();

                }else {

                    String[] params = {busquedaPaquete.getIdPaquete(), busquedaPaquete.getArea(), busquedaPaquete.getCelda(),
                            areasSpinner.getSelectedItem().toString(), celdasSpinner.getSelectedItem().toString(),
                            String.valueOf(rutUsuario), fecha, turno};

                    try {
                        String resp = new Remanejo.ecs_Remanejar().execute(params).get();
                        if (resp.equalsIgnoreCase("OK")) {
                            Toast.makeText(Remanejo.this, "Remanejo con exito", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Remanejo.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    estadoCampos(false);
                }
            }
        });

        areasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!parent.getItemAtPosition(position).equals("")){
                    try {
                        String [] codArea = {areasSpinner.getSelectedItem().toString()};
                        listaCeldas = new Remanejo.ecs_listarCeldas().execute(codArea).get();
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

        listaMineras = ((AtiApp) Remanejo.this.getApplication()).getListaMineras();

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

    private void estadoCampos(boolean flag){

        estado.setText("");
        peso.setText("");
        codigoBarra.setText("");
        paquete.setText("");
        lote.setText("");

        remanejo.setVisibility(View.INVISIBLE);
        seccionOrigen.setVisibility(View.INVISIBLE);
        seccionDestino.setVisibility(View.INVISIBLE);
        tituloSeccionDestino.setVisibility(View.INVISIBLE);
        tituloSeccionOrigen.setVisibility(View.INVISIBLE);


        if(flag){
            lote.setFocusable(true);
            lote.setCursorVisible(true);
            lote.setFocusableInTouchMode(true);
            paquete.setFocusable(true);
            paquete.setCursorVisible(true);
            paquete.setFocusableInTouchMode(true);

            codigoBarra.setFocusable(false);
            codigoBarra.setCursorVisible(false);
            codigoBarra.setText("");
        }else{
            lote.setFocusable(false);
            lote.setCursorVisible(false);
            lote.setText("");
            paquete.setFocusable(false);
            paquete.setCursorVisible(false);
            paquete.setText("");

            codigoBarra.setFocusable(true);
            codigoBarra.setCursorVisible(true);
            codigoBarra.setFocusableInTouchMode(true);
        }

    }

    private void cargarSpinnerAreas(){
        listaAreas = ((AtiApp) Remanejo.this.getApplication()).getListaAreas();
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

            return ws.ecs_BuscarPaquete();

        }
    }

    private class ecs_listarCeldas extends AsyncTask<String,Void,ArrayList<Celda>> {

        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<Celda> doInBackground(String... strings) {

            return ws.ecs_ListarCeldas(strings);

        }
    }

    private class ecs_Remanejar extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            return ws.ecs_Remanejar(strings);

        }
    }
}
