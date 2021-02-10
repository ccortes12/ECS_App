package com.example.ecs_app.Transferencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecs_app.AtiApp;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PreTransferencia extends AppCompatActivity {

    private EditText correlativo;
    private Button siguienteButton;
    private ArrayList<Minera> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_transferencia);

        getSupportActionBar().setTitle("Transferencia");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        correlativo = findViewById(R.id.EditTextCodigoB);
        siguienteButton = findViewById(R.id.siguienteButton);

        siguienteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] params = {correlativo.getText().toString()};

                try {
                    lista = new ecs_listarMinerasRecalada().execute(params).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(lista.size()>0){

                    Intent next = new Intent(v.getContext(), Transferencia.class);

                    //next.putExtra("correlativo",correlativo.getText().toString());

                    ((AtiApp) PreTransferencia.this.getApplication()).setLastCorrelativo(correlativo.getText().toString());
                    startActivity(next);

                }else{
                    Toast.makeText(PreTransferencia.this, "Correlativo ingresado invalido" , Toast.LENGTH_SHORT).show();
                    correlativo.setText("");
                }


            }
        });

    }


    private class ecs_listarMinerasRecalada extends AsyncTask<String,Void, ArrayList<Minera>> {

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
}