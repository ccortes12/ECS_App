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

public class Remanejo extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner minerasSpinner,areasSpinner,celdasSpinner;
    private EditText codigoBarra, lote, paquete;
    private TextView peso, estado,tituloSeccionOrigen,tituloSeccionDestino,celda,area;
    private Button buscar,remanejo;
    private Switch modoManual;
    private LinearLayout seccionOrigen,seccionDestino;
    private ArrayList<Minera> listaMineras;
    private ArrayList<Area> listaAreas;
    private ArrayList<Celda> listaCeldas;
    private ArrayList<String> auxSpinner,auxSpinnerCelda,auxSpinnerArea;
    private ArrayAdapter<String> comboAdapter;
    private String fechaRecepcion,turnoRecepcion,rutUsuario;
    private Paquete busquedaPaquete;
    private int rutCliente,relacionCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remanejo);

        getSupportActionBar().setTitle("Remanejo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        fechaRecepcion = ((AtiApp) Remanejo.this.getApplication()).getFecha();
        turnoRecepcion = Integer.toString(((AtiApp) Remanejo.this.getApplication()).getTurno());
        rutUsuario = Integer.toString(((AtiApp) Remanejo.this.getApplication()).getRutUsuario());

        modoManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modoManual.isChecked()){ //Bloquear campos de cb
                    estadoCampos(true);
                }else{
                    estadoCampos(false);
                }
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Busqueda objeto minera segun la seleccion del spinner
                for (Minera m : listaMineras){
                    if(minerasSpinner.getSelectedItem().toString().equalsIgnoreCase(m.getVchNombreFantasia())){
                        rutCliente = m.getIntRutCliente();
                    }
                }

                if(modoManual.isChecked()){  //Busqueda manual
                    try {
                        busquedaPaquete = new Remanejo.ecs_BuscarPaquete().execute().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{  //Busqueda por CB
                    try {
                        busquedaPaquete = new Remanejo.ecs_BuscarPaquetesCB().execute().get();
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

                    //cargar ubicacion al spinner y bloquear spinner
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

                                    //Primero debo despachar y luego almacenar
                                    String[] params = {busquedaPaquete.getIdPaquete(),busquedaPaquete.getArea(),busquedaPaquete.getCelda(),
                                            areasSpinner.getSelectedItem().toString(),celdasSpinner.getSelectedItem().toString()};

                                    //fechaRecepcion = ((AtiApp) Almacenaje.this.getApplication()).getFecha();
                                    try{
                                        String resp = new Remanejo.ecs_Remanejar().execute(params).get();
                                        if(resp.equalsIgnoreCase("OK")){
                                            Toast.makeText(Remanejo.this,"Remanejo con exito",Toast.LENGTH_SHORT).show();

                                        }else{
                                            Toast.makeText(Remanejo.this,"ERROR",Toast.LENGTH_SHORT).show();
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

                }else{

                    //Primero debo despachar y luego almacenar
                    String[] params = {busquedaPaquete.getIdPaquete(),busquedaPaquete.getArea(),busquedaPaquete.getCelda(),
                            areasSpinner.getSelectedItem().toString(),celdasSpinner.getSelectedItem().toString()};

                    try{
                        String resp = new Remanejo.ecs_Remanejar().execute(params).get();
                        if(resp.equalsIgnoreCase("OK")){
                            Toast.makeText(Remanejo.this,"Remanejo con exito",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(Remanejo.this,"ERROR",Toast.LENGTH_SHORT).show();
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
                //Cargar spinner de celdas dependiendo de la opcion

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

            Paquete auxPaquete;


            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_BuscarPaquetesCB";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_BuscarPaquetesCB";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("rutCliente", rutCliente);
            request.addProperty("codigo_barra", codigoBarra.getText().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                auxPaquete = new Paquete();

                auxPaquete.setPesoBruto(Double.parseDouble(resultado_xml.getProperty("PesoBrutoPaquete").toString()));

                if(auxPaquete.getPesoBruto() != 0){
                    auxPaquete.setMensaje(resultado_xml.getProperty("strDescEstado").toString().trim());
                    auxPaquete.setLote(resultado_xml.getProperty("strLote").toString().trim());
                    auxPaquete.setIdPaquete(resultado_xml.getProperty("strIdPaquete").toString().trim());
                    auxPaquete.setPeso(Integer.parseInt(resultado_xml.getProperty("dblPeso").toString()));

                    auxPaquete.setCodigoPaquete(resultado_xml.getProperty("CodigoPaquete").toString().trim());
                    auxPaquete.setPiezas(Integer.parseInt(resultado_xml.getProperty("Piezas").toString()));
                    auxPaquete.setPesoBruto(Double.parseDouble(resultado_xml.getProperty("PesoBrutoPaquete").toString()));
                    auxPaquete.setPesoNeto(Double.parseDouble(resultado_xml.getProperty("PesoNetoPaquete").toString()));
                    auxPaquete.setChrFlgChequeo(resultado_xml.getProperty("chrFlgChequeo").toString());
                    auxPaquete.setDescEstado(resultado_xml.getProperty("DescEstado").toString());
                    auxPaquete.setArea(resultado_xml.getProperty("Area").toString().trim());
                    auxPaquete.setCelda(resultado_xml.getProperty("Celda").toString().trim());


                }else{
                    auxPaquete.setDescEstado("No se encuentra");
                }
                return auxPaquete;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ecs_BuscarPaquete extends AsyncTask<String,Void, Paquete>{

        @SuppressLint("WrongThread")
        @Override
        protected Paquete doInBackground(String... strings) {
            Paquete auxPaquete;

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_BuscarPaquetes";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_BuscarPaquetes";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("rutCliente", rutCliente);
            request.addProperty("lote",lote.getText().toString());
            request.addProperty("paquete",paquete.getText().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                auxPaquete = new Paquete();

                auxPaquete.setPesoBruto(Double.parseDouble(resultado_xml.getProperty("PesoBrutoPaquete").toString()));

                if(auxPaquete.getPesoBruto() != 0){
                    auxPaquete.setMensaje(resultado_xml.getProperty("strDescEstado").toString());
                    auxPaquete.setLote(resultado_xml.getProperty("strLote").toString().trim());
                    auxPaquete.setIdPaquete(resultado_xml.getProperty("strIdPaquete").toString());
                    auxPaquete.setPeso(Integer.parseInt(resultado_xml.getProperty("dblPeso").toString()));

                    auxPaquete.setCodigoPaquete(resultado_xml.getProperty("CodigoPaquete").toString().trim());
                    auxPaquete.setPiezas(Integer.parseInt(resultado_xml.getProperty("Piezas").toString()));
                    //auxPaquete.setPesoBruto(Double.parseDouble(resultado_xml.getProperty("PesoBrutoPaquete").toString()));
                    auxPaquete.setPesoNeto(Double.parseDouble(resultado_xml.getProperty("PesoNetoPaquete").toString()));
                    auxPaquete.setChrFlgChequeo(resultado_xml.getProperty("chrFlgChequeo").toString());
                    auxPaquete.setDescEstado(resultado_xml.getProperty("DescEstado").toString());
                    auxPaquete.setArea(resultado_xml.getProperty("Area").toString().trim());
                    auxPaquete.setCelda(resultado_xml.getProperty("Celda").toString().trim());

                }else{
                    auxPaquete.setDescEstado("No se encuentra");
                }
                return auxPaquete;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class ecs_listarCeldas extends AsyncTask<String,Void,ArrayList<Celda>> {

        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<Celda> doInBackground(String... strings) {

            ArrayList<Celda> salida = new ArrayList<>();

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_ListarCeldas";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarCeldas";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("codigoArea", strings[0]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                SoapObject listaCeldas = (SoapObject) resultado_xml.getProperty("lstCeldas");

                int numCeldas = listaCeldas.getPropertyCount();

                for(int i = 0; i < numCeldas - 1 ; i++){
                    SoapObject celdaAux = (SoapObject) listaCeldas.getProperty(i);
                    Celda temp = new Celda();
                    temp.setIntEstado(Integer.parseInt(celdaAux.getProperty("intEstado").toString()));
                    temp.setCodCelda(celdaAux.getProperty("codCelda").toString().trim());
                    temp.setDesCelda(celdaAux.getProperty("desCelda").toString().trim());
                    temp.setCodArea(celdaAux.getProperty("codArea").toString().trim());
                    temp.setDesArea(celdaAux.getProperty("desArea").toString().trim());
                    temp.setCapacidadPaquetes(Integer.parseInt(celdaAux.getProperty("capacidadPaquetes").toString()));
                    temp.setPreAcopio(celdaAux.getProperty("preAcopio").toString());
                    temp.setTimeStamp(Integer.parseInt(celdaAux.getProperty("timeStamp").toString()));

                    salida.add(temp);
                }

                return salida;

            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class ecs_Remanejar extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_Remanejar";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_Remanejar";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("Id", strings[0]);
            request.addProperty("codAreaOrigen",strings[1]);
            request.addProperty("codCeldaOrigen",strings[2]);
            request.addProperty("codAreaDestino",strings[3]);
            request.addProperty("codCeldaDestino",strings[4]);
            request.addProperty("rut",rutUsuario);
            request.addProperty("fecha",fechaRecepcion);
            request.addProperty("turno",String.valueOf(turnoRecepcion));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                transport.call(SOAP_ACTION, envelope);
                SoapPrimitive respuesta = (SoapPrimitive) envelope.getResponse();
                String salida = respuesta.toString();
                return salida;

            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
