package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private String id_lote,id_paquete;
    private int peso,cor_Lote;

    private double saldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trf_02_lotes);

        textOperacion = (TextView) findViewById(R.id.textView24);
        textContenedor = (TextView) findViewById(R.id.textView26);
        text_saldo = (TextView) findViewById(R.id.textView_saldo);
        final TableLayout tblLayout = (TableLayout)findViewById(R.id.tableLayout);
        button_grabarLotes = (Button) findViewById(R.id.button_grabarLote);

        Intent intent = getIntent();
        contenedor = (Contenedor) intent.getSerializableExtra("objContenedor");

        saldo = contenedor.getGross() - contenedor.getTara();

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
                }else{
                    Toast.makeText(TRF_02_lotes.this,"EXITO",Toast.LENGTH_SHORT).show();
                    agregarListaPaquetes(tblLayout);
                    cargarDatos(tblLayout);
                }
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

        String cadenaOperacion = contenedor.getAnnoOperacion() + " - " + contenedor.getCorOperacion();
        String cadenaContenedor = contenedor.getSigla() + "  " + contenedor.getNumero() + " - " + contenedor.getDigito();

        textOperacion.setText(cadenaOperacion);
        textContenedor.setText(cadenaContenedor);

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
                    saldo -= p.getPeso();
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        text_saldo.setText(Double.toString(saldo));
    }

    private String validarCargaLotes(TableLayout tblLayout) {

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
}
