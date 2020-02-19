package com.example.ecs_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecs_app.Entidades.Contenedor;
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
import java.util.concurrent.ExecutionException;

public class TRF_02_lotes extends AppCompatActivity {

    private TextView textOperacion, textContenedor;
    private Contenedor contenedor;
    private ArrayList<Paquete> listaPaquetes;
    private TableLayout tblLayout;
    private TextView text_saldo;
    private Button button_grabarLotes;
    private Switch switchEditar;
    private EditText ingresoLote, ingresoID, ingresoPeso;

    private String id_lote,id_paquete;
    private int peso,cor_Lote;


    private double saldo;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trf_02_lotes);

        getSupportActionBar().setTitle("Ingreso Lotes - Manual");

        textOperacion = (TextView) findViewById(R.id.textView24);
        textContenedor = (TextView) findViewById(R.id.textView26);
        text_saldo = (TextView) findViewById(R.id.textView_saldo);
        final TableLayout tblLayout = (TableLayout)findViewById(R.id.tableLayout);
        button_grabarLotes = (Button) findViewById(R.id.button_grabarLote);
        switchEditar = (Switch) findViewById(R.id.switchEditar);

        ingresoLote = (EditText) findViewById(R.id.ingreso_lote);
        ingresoID = (EditText) findViewById(R.id.ingreso_id);
        ingresoPeso = (EditText) findViewById(R.id.ingreso_peso);

        Intent intent = getIntent();
        contenedor = (Contenedor) intent.getSerializableExtra("objContenedor");

        textOperacion.setText(contenedor.getAnnoOperacion() + " - " + contenedor.getCorOperacion());
        textContenedor.setText(contenedor.getSigla() + "  " + contenedor.getNumero() + " - " + contenedor.getDigito());

        //INICIAL Cargar datos
        cargarDatos(tblLayout);

        button_grabarLotes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(validarCargaLotes(tblLayout).equalsIgnoreCase("Campos incompletos")){
                    Toast.makeText(TRF_02_lotes.this,"ERROR, Campos incompletos",Toast.LENGTH_SHORT).show();
                }else if(validarCargaLotes(tblLayout).equalsIgnoreCase("Id no corresponde")){
                    Toast.makeText(TRF_02_lotes.this,"ERROR, Id no corresponde",Toast.LENGTH_SHORT).show();
                }else if(validarCargaLotes(tblLayout).equalsIgnoreCase("Existe un paquete duplicado")){
                    Toast.makeText(TRF_02_lotes.this,"ERROR, Existe un paquete duplicado",Toast.LENGTH_SHORT).show();
                }else if (validarCargaLotes(tblLayout).equalsIgnoreCase("Peso total excede Max gross")) {
                    Toast.makeText(TRF_02_lotes.this, "ERROR, Peso total excede Max gross", Toast.LENGTH_SHORT).show();
                }else{

                    if(switchEditar.isChecked()){
                        //Modificacion de los datos de abajo
                        new AlertDialog.Builder(TRF_02_lotes.this)
                                .setTitle("Confirmación registro paquetes")
                                .setMessage("Se ingresaran todos los paquetes al sistema... ")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Toast.makeText(TRF_02_lotes.this,"EXITO",Toast.LENGTH_SHORT).show();

                                        agregarListaPaquetes(tblLayout);
                                        cargarDatos(tblLayout);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("Mensaje" , "Se cancelo acción");
                                    }
                                }).show();
                    }else{

                        registrarPaqueteCampos(tblLayout);
                        ingresoLote.requestFocus();
                    }

                }
            }
        });

        switchEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (switchEditar.isChecked())
                    estadoCampos(tblLayout,true);
                else
                    estadoCampos(tblLayout,false);
            }
        });

        ingresoPeso.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO){
                    registrarPaqueteCampos(tblLayout);

                }
                return false;
            }
        });
    }

    private class cfs__BuscaPaquetesConsolidados extends AsyncTask<String, Void, ArrayList<Paquete>>{

        @Override
        protected ArrayList<Paquete> doInBackground(String... strings) {

            ArrayList<Paquete> salida = new ArrayList<>();
            SoapObject temp;
            Paquete paquete;

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "CFS_BuscaPaquetesConsolidados";
            String SOAP_ACTION = "http://www.atiport.cl/CFS_BuscaPaquetesConsolidados";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("ano_operacion",contenedor.getAnnoOperacion());
            request.addProperty("cor_operacion",contenedor.getCorOperacion());
            request.addProperty("sigla",contenedor.getSigla());
            request.addProperty("numero",contenedor.getNumero());
            request.addProperty("digito",contenedor.getDigito());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try{

                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                SoapObject listTemp = (SoapObject) resultado_xml.getProperty("lstPaquetes");
                int cantPaquetes = Integer.parseInt(resultado_xml.getProperty("intCantidadPaquetes").toString());

                for(int i = 0; i < cantPaquetes; i++) {

                    temp = (SoapObject) listTemp.getProperty(i);

                    paquete = new Paquete();

                    paquete.setEstado(Integer.parseInt(temp.getProperty("intEstado").toString()));
                    paquete.setLote(temp.getProperty("strLote").toString());
                    paquete.setPeso(Double.parseDouble(temp.getProperty("dblPeso").toString()));
                    paquete.setIdPaquete(temp.getProperty("strIdPaquete").toString());

                    salida.add(paquete);
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

    private void cargarDatos(TableLayout tblLayout){

        saldo = contenedor.getGross() - (int)contenedor.getTara();

        try {

            listaPaquetes = new cfs__BuscaPaquetesConsolidados().execute().get();

            if(listaPaquetes != null){

                for(int i = 0 ; i < listaPaquetes.size(); i++){

                    Paquete p = listaPaquetes.get(i);
                    TableRow row = (TableRow) tblLayout.getChildAt(i+1);

                    EditText celdaActual = (EditText) row.getChildAt(1);celdaActual.setText(p.getLote());
                    celdaActual = (EditText) row.getChildAt(2);celdaActual.setText(p.getIdPaquete());
                    celdaActual = (EditText) row.getChildAt(3);celdaActual.setText(Double.toString(p.getPeso()));

                    //Actualizar saldo
                    saldo -= (p.getPeso() + contenedor.getZuncho());
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        text_saldo.setText(Double.toString(saldo) + " [kg]");

    }

    private String validarCargaLotes(TableLayout tblLayout) {

        double tempSaldo = (double)contenedor.getGross() - contenedor.getTara();
        int cantNoNula = 0;

        ArrayList<String> lista = new ArrayList<>(25);

        for (int i = 1; i < tblLayout.getChildCount(); i++) {

            TableRow row = (TableRow) tblLayout.getChildAt(i);

            TextView cor = (TextView) row.getChildAt(0);
            EditText celdaLote = (EditText) row.getChildAt(1);
            EditText celdaID = (EditText) row.getChildAt(2);
            EditText celdaPeso = (EditText) row.getChildAt(3);

            int camposVacios = 0;
            if (celdaLote.getText().toString().equalsIgnoreCase("")) {
                camposVacios++;
            }
            if (celdaID.getText().toString().equalsIgnoreCase("")) {
                camposVacios++;
            }
            if (celdaPeso.getText().toString().equalsIgnoreCase("")) {
                camposVacios++;
            }

            if (camposVacios > 0 && camposVacios < 3) {
                return "Campos incompletos";
            }else if(camposVacios == 0){
                //TODOS LOS CAMPOS COMPLETOS
                cantNoNula++;

                if(cantNoNula != i){
                    return "Id no corresponde";
                }

                String concate = celdaLote.getText().toString() + "-" + celdaID.getText().toString();
                if(!lista.contains(concate)){
                    lista.add(concate);
                    //Verificar que el la sumatoria de los pesosPaquetes + tara sea menor que el max gross
                    tempSaldo -= (Double.parseDouble(celdaPeso.getText().toString()) + contenedor.getZuncho()); //en validacion agregar el peso zuncho
                    if(tempSaldo < 0){
                        return "Peso total excede Max gross";
                    }

                }else{
                    return "Existe un paquete duplicado";
                }

            }
        }

        return "EXITO";

    }

    private void agregarListaPaquetes(TableLayout tblLayout){

        for(int i = 1; i < tblLayout.getChildCount(); i++){

            String respuesta;
            TableRow row = (TableRow) tblLayout.getChildAt(i);

            TextView cor = (TextView) row.getChildAt(0);
            EditText celdaLote = (EditText) row.getChildAt(1);
            EditText celdaID = (EditText) row.getChildAt(2);
            EditText celdaPeso = (EditText) row.getChildAt(3);

            if(!(celdaLote.getText().toString().equalsIgnoreCase("") || celdaID.getText().toString().equalsIgnoreCase(""))){

                cor_Lote = Integer.parseInt(cor.getText().toString().substring(0, cor.getText().toString().length() -1));
                id_lote = celdaLote.getText().toString();
                id_paquete = celdaID.getText().toString();
                peso = (int)Double.parseDouble(celdaPeso.getText().toString());

                try {
                    respuesta = new cfs_RegistraPaquete().execute().get();
                    if(!respuesta.equalsIgnoreCase("0")){
                        Toast.makeText(TRF_02_lotes.this,"ERROR, " + respuesta,Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private class cfs_RegistraPaquete extends AsyncTask<String,Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {

            String salida = "1";

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "CFS_RegistraPaquete";
            String SOAP_ACTION = "http://www.atiport.cl/CFS_RegistraPaquete";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("cor_cfs", contenedor.getCorCFSEntrega());
            request.addProperty("cod_lugar", "ANF");
            request.addProperty("cor_lote", cor_Lote);
            request.addProperty("cod_origen", "1");
            request.addProperty("id_lote", id_lote);
            request.addProperty("id_paquete", id_paquete);
            request.addProperty("peso", peso);
            request.addProperty("cantidad", 1);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();

                salida = resultado_xml.getValue().toString();

            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return salida;
        }
    }

    private void estadoCampos(TableLayout tblLayout, boolean estado){

        for(int i = 1; i < tblLayout.getChildCount(); i++ ){

            TableRow row = (TableRow)tblLayout.getChildAt(i);

            for(int j = 1; j < 4; j++ ){

                EditText current = (EditText) row.getChildAt(j);

                current.setFocusable(estado);
                current.setCursorVisible(estado);
                if(estado){
                    current.setFocusableInTouchMode(true);
                }

            }
        }

    }

    private boolean camposVacios(){

        return ingresoLote.getText().toString().equalsIgnoreCase("") || ingresoID.getText().toString().equalsIgnoreCase("") || ingresoPeso.getText().toString().equalsIgnoreCase("");
    }

    private void registrarPaqueteCampos(TableLayout tblLayout){

        if(camposVacios()) {
            Toast.makeText(TRF_02_lotes.this, "ERROR, Campos incompletos", Toast.LENGTH_SHORT).show();
        }else{

            Paquete p2 = new Paquete(0,ingresoID.getText().toString(),ingresoLote.getText().toString(),Double.parseDouble(ingresoPeso.getText().toString()),"");

            if(listaPaquetes.contains(p2)){
                Toast.makeText(TRF_02_lotes.this,"ERROR, Existe un paquete duplicado",Toast.LENGTH_SHORT).show();
            }else if (saldo < (p2.getPeso() + contenedor.getZuncho())) {
                Toast.makeText(TRF_02_lotes.this, "ERROR, Peso total excede Max gross", Toast.LENGTH_SHORT).show();

            }else{

                try{
                    cor_Lote = listaPaquetes.size() + 1;
                    id_lote = ingresoLote.getText().toString();
                    id_paquete = ingresoID.getText().toString();
                    peso = (int)Double.parseDouble(ingresoPeso.getText().toString());

                    String respuesta = new cfs_RegistraPaquete().execute().get();
                    if(!respuesta.equalsIgnoreCase("0")){
                        Toast.makeText(TRF_02_lotes.this,"ERROR, " + respuesta,Toast.LENGTH_SHORT).show();
                    }else{

                        Toast.makeText(TRF_02_lotes.this,"EXITO",Toast.LENGTH_SHORT).show();

                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();

                        listaPaquetes.add(p2);
                        cargarDatos(tblLayout);

                        ingresoID.setText("");
                        ingresoLote.setText("");
                        ingresoPeso.setText("");

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
