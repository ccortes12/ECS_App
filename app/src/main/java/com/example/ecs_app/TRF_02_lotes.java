package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ecs_app.Entidades.Contenedor;
import com.example.ecs_app.Entidades.Marca;
import com.example.ecs_app.Entidades.Paquete;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
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
    private EditText editText;

    private String[][] matriz = new String[25][4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trf_02_lotes);

        textOperacion = (TextView) findViewById(R.id.textView24);
        textContenedor = (TextView) findViewById(R.id.textView26);
        editText = (EditText) findViewById(R.id.editText);
        TableLayout tblLayout = (TableLayout)findViewById(R.id.tableLayout);

        Intent intent = getIntent();
        contenedor = (Contenedor) intent.getSerializableExtra("objContenedor");

        cargarDatos(tblLayout);

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

                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
