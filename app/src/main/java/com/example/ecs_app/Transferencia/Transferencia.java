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
import com.example.ecs_app.R;

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

    private Spinner  mineraSpinner, puertosSpinnner, marcasSpinner, gruasSpinner, grueroSpinner;
    private Button siguienteButton;

    private ArrayList<String> auxSpinnerMinera, auxSpinnerPuertos,auxSpinnerMarcas,auxSpinnerGrua,auxSpinnerOperadores;
    private ArrayList<Minera> listaMineras, listaminerasSpinner;
    private ArrayList<Puerto> listaPuertos;
    private ArrayList<Grua> listaGruas;
    private ArrayList<Marca> listaMarcas;
    private ArrayList<Gruero> listaOperadores;
    private ArrayAdapter<String> comboAdapter;

    private String correlativo;


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

        final Spinner bodegaSpinner = (Spinner) findViewById(R.id.spinner7);

        Button buttonParas = (Button) findViewById(R.id.buttonParas);
        marcasSpinner = findViewById(R.id.listaMarcas);
        siguienteButton = findViewById(R.id.siguienteButton);

        listaMineras = ((AtiApp) Transferencia.this.getApplication()).getListaMineras();

        correlativo = ((AtiApp) Transferencia.this.getApplication()).getLastCorrelativo();

        //Carga de spinners
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

                    Intent next = new Intent(v.getContext(), Embarque.class);
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

                    //Rescatar el rut cliente
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

            ArrayList<Minera> salida = new ArrayList<>();

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_ListarMinerasRecalada";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarMinerasRecalada";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("corrRecalada", strings[0]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {

                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                SoapObject auxListaMineras = (SoapObject) resultado_xml.getProperty("lstMineras");

                int numCeldas = auxListaMineras.getPropertyCount();

                for(int i = 0; i < numCeldas - 1 ; i++){

                    SoapObject celdaAux = (SoapObject) auxListaMineras.getProperty(i);
                    Minera temp = new Minera();
                    temp.setIntRutCliente(Integer.parseInt(celdaAux.getProperty("rutCliente").toString()));
                    temp.setChrDvCliente(celdaAux.getProperty("dvCliente").toString());
                    temp.setVchRazonSocial(celdaAux.getProperty("razonSocial").toString());
                    temp.setVchNombreFantasia(celdaAux.getProperty("nombreFantasia").toString());
                    //temp.setVchGiro(celdaAux.getProperty("vchGiro").toString());
                    temp.setVchDireccion(celdaAux.getProperty("direccion").toString());
                    //temp.setVchTelefonoFax(celdaAux.getProperty("vchTelefonoFax").toString());
                    temp.setChrCodComuna(celdaAux.getProperty("codComuna").toString());
                    temp.setBigTimeStamp(Long.parseLong(celdaAux.getProperty("timeStamp").toString()));
                    temp.setIntTonMin(Float.parseFloat(celdaAux.getProperty("tonMin").toString()));
                    //temp.setChrTipoEmbarque(mineraAux.getProperty("chrTipoEmbarque").toString());
                    temp.setFlgCantidadPiezas(Integer.parseInt(celdaAux.getProperty("flgCantidadPiezas").toString()));
                    temp.setDblTarifaAlmacenaje(Float.parseFloat(celdaAux.getProperty("dblTarifaAlmacenaje").toString()));
                    //temp.setColorPatioGrafico(celdaAux.getProperty("colorPatioGrafico").toString());
                    temp.setDblTarTonMinimo(Float.parseFloat(celdaAux.getProperty("tarTonMinimo").toString()));
                    temp.setIntTonMinTurno2(Float.parseFloat(celdaAux.getProperty("tonMinTurno2").toString()));

                    salida.add(temp);


                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return salida;
        }
    }

    private class ecs_listarPuertos extends AsyncTask<String,Void,ArrayList<Puerto>>{

        @Override
        protected ArrayList<Puerto> doInBackground(String... strings) {

            ArrayList<Puerto> salida = new ArrayList<>();

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_ListarPuertos";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarPuertos";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("rutCliente", Integer.parseInt(strings[0]));
            request.addProperty("corRecalada", Integer.parseInt(strings[1]));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {

                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                SoapObject auxListaPuertos = (SoapObject) resultado_xml.getProperty("lstPuertos");

                int numCeldas = auxListaPuertos.getPropertyCount();


                for(int i = 0; i < numCeldas - 1 ; i++) {

                    SoapObject celdaAux = (SoapObject) auxListaPuertos.getProperty(i);
                    Puerto temp = new Puerto();
                    temp.setIntEstado(Integer.parseInt(celdaAux.getProperty("intEstado").toString()));
                    temp.setStrCodPuerto(celdaAux.getProperty("strCodPuerto").toString());
                    temp.setStrDesPuerto(celdaAux.getProperty("strDesPuerto").toString());

                    salida.add(temp);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            return salida;
        }
    }

    private class ecs_listarMarcas extends AsyncTask<String,Void,ArrayList<Marca>>{
        @Override
        protected ArrayList<Marca> doInBackground(String... strings) {

            ArrayList<Marca> salida = new ArrayList<>();

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_ListarMarcas";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarMarcas";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("rutCliente", Integer.parseInt(strings[0]));
            request.addProperty("corRecalada", Integer.parseInt(strings[1]));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {

                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                SoapObject auxListaMarcas = (SoapObject) resultado_xml.getProperty("lstMarcas");
                int numCeldas = auxListaMarcas.getPropertyCount();

                for(int i = 0; i < numCeldas - 1 ; i++) {

                    SoapObject celdaAux = (SoapObject) auxListaMarcas.getProperty(i);
                    Marca temp = new Marca();
                    temp.setCodMarca(celdaAux.getProperty("strCodMarca").toString());
                    temp.setEstado(Integer.parseInt(celdaAux.getProperty("intEstado").toString()));
                    temp.setDescMarca(celdaAux.getProperty("strDescMarca").toString());

                    salida.add(temp);

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return salida;
        }
    }

    private class ecs_listarGruas extends AsyncTask<Void,Void,ArrayList<Grua>>{

        @Override
        protected ArrayList<Grua> doInBackground(Void... voids) {

            ArrayList<Grua> salida = new ArrayList<>();

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_ListarGruas";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarGruas";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {

                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                SoapObject auxListaGruas = (SoapObject) resultado_xml.getProperty("lstGruas");
                int numCeldas = auxListaGruas.getPropertyCount();

                for(int i = 0; i < numCeldas - 1 ; i++) {
                    SoapObject celdaAux = (SoapObject) auxListaGruas.getProperty(i);
                    Grua temp = new Grua();
                    temp.setEstado(Integer.parseInt(celdaAux.getProperty("intEstado").toString()));
                    temp.setCodGrua(Integer.parseInt(celdaAux.getProperty("strCodGrua").toString()));
                    temp.setDescGrua(celdaAux.getProperty("strDesGrua").toString());
                    salida.add(temp);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return salida;
        }
    }

    private class ecs_listarOperadores extends AsyncTask<String,Void,ArrayList<Gruero>>{

        @Override
        protected ArrayList<Gruero> doInBackground(String... strings) {
            ArrayList<Gruero> salida = new ArrayList<>();

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_ListarOperadores";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarOperadores";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("corRecalada", Integer.parseInt(strings[0]));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {

                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                SoapObject auxListaGruero = (SoapObject) resultado_xml.getProperty("lstOperador");
                int numCeldas = auxListaGruero.getPropertyCount();

                for(int i = 0; i < numCeldas - 1 ; i++) {
                    SoapObject celdaAux = (SoapObject) auxListaGruero.getProperty(i);
                    Gruero temp = new Gruero();
                    temp.setEstado(Integer.parseInt(celdaAux.getProperty("intEstado").toString()));
                    temp.setCodOperador(Integer.parseInt(celdaAux.getProperty("strCodOperador").toString()));
                    temp.setDesOperador(celdaAux.getProperty("strDesOperador").toString());
                    salida.add(temp);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return salida;
        }
    }
}