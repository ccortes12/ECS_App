package com.example.ecs_app.Transferencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ecs_app.AtiApp;
import com.example.ecs_app.Entidades.Grua;
import com.example.ecs_app.Entidades.Gruero;
import com.example.ecs_app.Entidades.Marca;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.Entidades.Puerto;
import com.example.ecs_app.Login;
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
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class Transferencia extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner  mineraSpinner, puertosSpinnner, marcasSpinner, gruasSpinner, grueroSpinner,bodegaSpinner;
    private Button siguienteButton;
    private ArrayList<String> auxSpinnerMinera, auxSpinnerPuertos, auxSpinnerMarcas, auxSpinnerGrua, auxSpinnerOperadores;
    private ArrayList<Minera> listaMineras, listaminerasSpinner;
    private ArrayList<Puerto> listaPuertos;
    private ArrayList<Grua> listaGruas;
    private ArrayList<Marca> listaMarcas;
    private ArrayList<Gruero> listaOperadores;
    private ArrayAdapter<String> comboAdapter;
    private String correlativo;

    WS_Torpedo ws = new WS_TorpedoImp();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencia);

        getSupportActionBar().setTitle("Transferencia");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mineraSpinner = findViewById(R.id.listaMineras);
        auxSpinnerMinera = new ArrayList<String>();
        puertosSpinnner = findViewById(R.id.spinner3);
        auxSpinnerPuertos = new ArrayList<String>();
        marcasSpinner = findViewById(R.id.listaMarcas);
        auxSpinnerMarcas = new ArrayList<String>();
        gruasSpinner = findViewById(R.id.spinner5);
        auxSpinnerGrua = new ArrayList<String>();
        grueroSpinner = findViewById(R.id.spinner6);
        auxSpinnerOperadores = new ArrayList<String>();
        bodegaSpinner = (Spinner) findViewById(R.id.spinner7);
        Button buttonParas = (Button) findViewById(R.id.buttonParas);
        marcasSpinner = findViewById(R.id.listaMarcas);
        siguienteButton = findViewById(R.id.siguienteButton);

        listaMineras = ((AtiApp) Transferencia.this.getApplication()).getListaMineras();
        correlativo = ((AtiApp) Transferencia.this.getApplication()).getLastCorrelativo();

        //Carga de spinners
        ArrayAdapter<String> comboSpinner2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ((AtiApp) Transferencia.this.getApplication()).getBodegasEmbarque());
        bodegaSpinner.setAdapter(comboSpinner2);

        String[] params = {correlativo};

        try {
            listaminerasSpinner = new ecs_listarMinerasRecalada().execute(params).get();
            cargarSpinnerMineras();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            listaOperadores = new ecs_listarOperadores().execute(params).get();
            cargarOperadores();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            cargarGruas();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        siguienteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(mineraSpinner.getSelectedItem().toString().equalsIgnoreCase("")
                        || puertosSpinnner.getSelectedItem().toString().equals("") ||
                marcasSpinner.getSelectedItem().toString().equals("") || gruasSpinner.getSelectedItem().toString().equals("")
                        || grueroSpinner.getSelectedItem().toString().equals("") || bodegaSpinner.getSelectedItem().toString().equals(""))){

                    //Entrega parametros a Embarque activity
                    Intent next = new Intent(v.getContext(), Embarque.class);

                    for(Minera m : listaMineras){
                        if(mineraSpinner.getSelectedItem().toString().equalsIgnoreCase(m.getVchNombreFantasia())){
                            String rutSeleccionado = String.valueOf(m.getIntRutCliente());
                            next.putExtra("rutCliente",rutSeleccionado);
                            break;
                        }
                    }

                    String[] auxMarca = marcasSpinner.getSelectedItem().toString().split("-");
                    String valorMarca = auxMarca[0].trim();

                    for(Marca marca: listaMarcas){

                        if(valorMarca.equalsIgnoreCase(marca.getCodMarca().trim())){
                            String codMarca = marca.getCodMarca();
                            next.putExtra("codMarca",codMarca.trim());
                            break;
                        }
                    }

                    String[] auxPuerto = puertosSpinnner.getSelectedItem().toString().split("-");
                    next.putExtra("codPuertoDestino",auxPuerto[1].trim());

                    startActivity(next);
                    //finish();

                }else{
                    Toast.makeText(Transferencia.this, "Seleccione todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });



        mineraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!parent.getItemAtPosition(position).equals("")){

                    int rutSeleccionado = 0;

                    for(Minera m : listaMineras){
                        if(mineraSpinner.getSelectedItem().toString().equalsIgnoreCase(m.getVchNombreFantasia())){
                            rutSeleccionado = m.getIntRutCliente();
                        }
                    }

                    //Cargar lista puertos y marcas
                    String[] params2 = {String.valueOf(rutSeleccionado),correlativo};
                    try {
                        listaPuertos = new ecs_listarPuertos().execute(params2).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        listaMarcas = new ecs_listarMarcas().execute(params2).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }else{
                    listaPuertos = new ArrayList();
                    listaMarcas = new ArrayList();
                }
                cargarPuertosMinera();
                cargarMarcas();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonParas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!(mineraSpinner.getSelectedItem().toString().equalsIgnoreCase("")
                        || puertosSpinnner.getSelectedItem().toString().equals("") ||
                        marcasSpinner.getSelectedItem().toString().equals("") || gruasSpinner.getSelectedItem().toString().equals("")
                        || grueroSpinner.getSelectedItem().toString().equals("") || bodegaSpinner.getSelectedItem().toString().equals(""))){

                    Intent next = new Intent(v.getContext(), ReporteParas.class);
                    startActivity(next);
                    //finish();

                }else{
                    Toast.makeText(Transferencia.this, "Seleccione todos los campos", Toast.LENGTH_SHORT).show();
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

    private void cargarSpinnerMineras(){

        Iterator<Minera> i = listaminerasSpinner.iterator();

        auxSpinnerMinera = new ArrayList<String>();
        auxSpinnerMinera.add("");

        while(i.hasNext()) {
            Minera item = i.next();
            auxSpinnerMinera.add(item.getVchNombreFantasia());
        }


        comboAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, auxSpinnerMinera);
        comboAdapter.notifyDataSetChanged();
        mineraSpinner.setAdapter(comboAdapter);
        mineraSpinner.setOnItemSelectedListener(this);
    }

    private void cargarPuertosMinera(){

        Iterator<Puerto> i = listaPuertos.iterator();

        auxSpinnerPuertos = new ArrayList<String>();
        auxSpinnerPuertos.add("");
        while(i.hasNext()){
            Puerto item = i.next();
            auxSpinnerPuertos.add(item.getStrDesPuerto() + " - " + item.getStrCodPuerto());
        }

        puertosSpinnner.setOnItemSelectedListener(this);
        comboAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, auxSpinnerPuertos);
        puertosSpinnner.setAdapter(comboAdapter);
    }

    private void cargarGruas() throws ExecutionException, InterruptedException {

        listaGruas = new ecs_listarGruas().execute().get();

        Iterator<Grua> i = listaGruas.iterator();

        auxSpinnerGrua.add("");
        while(i.hasNext()){
            Grua item = i.next();
            auxSpinnerGrua.add(item.getDescGrua());
        }

        gruasSpinner.setOnItemSelectedListener(this);
        comboAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,auxSpinnerGrua);
        gruasSpinner.setAdapter(comboAdapter);

    }

    private void cargarOperadores(){

        Iterator<Gruero> i = listaOperadores.iterator();

        auxSpinnerOperadores = new ArrayList<String>();
        auxSpinnerOperadores.add("");

        while(i.hasNext()) {
            Gruero item = i.next();
            auxSpinnerOperadores.add(item.getDesOperador());
        }

        comboAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, auxSpinnerOperadores);
        comboAdapter.notifyDataSetChanged();
        grueroSpinner.setAdapter(comboAdapter);
        grueroSpinner.setOnItemSelectedListener(this);
    }

    private void cargarMarcas(){

        Iterator<Marca> i = listaMarcas.iterator();

        auxSpinnerMarcas = new ArrayList<String>();
        auxSpinnerMarcas.add("");
        while(i.hasNext()){
            Marca item = i.next();
            auxSpinnerMarcas.add(item.getCodMarca() + " - " + item.getDescMarca());
        }

        marcasSpinner.setOnItemSelectedListener(this);
        comboAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, auxSpinnerMarcas);
        marcasSpinner.setAdapter(comboAdapter);
    }


    private class ecs_listarMinerasRecalada extends AsyncTask<String,Void,ArrayList<Minera>>{

        @Override
        protected ArrayList<Minera> doInBackground(String... strings) {

            return ws.ecs_ListarMinerasRecalada(strings);

        }
    }

    private class ecs_listarPuertos extends AsyncTask<String,Void,ArrayList<Puerto>>{

        @Override
        protected ArrayList<Puerto> doInBackground(String... strings) {

            return ws.ecs_ListarPuertos(strings);

        }
    }

    private class ecs_listarMarcas extends AsyncTask<String,Void,ArrayList<Marca>>{
        @Override
        protected ArrayList<Marca> doInBackground(String... strings) {

            return ws.ecs_ListarMarcas(strings);

        }
    }

    private class ecs_listarGruas extends AsyncTask<Void,Void,ArrayList<Grua>>{

        @Override
        protected ArrayList<Grua> doInBackground(Void... voids) {

            return ws.ecs_ListarGruas();

        }
    }

    private class ecs_listarOperadores extends AsyncTask<String,Void,ArrayList<Gruero>>{

        @Override
        protected ArrayList<Gruero> doInBackground(String... strings) {

            return ws.ecs_ListarOperadores(strings);

        }
    }
}