package com.example.ecs_app.Transferencia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.ecs_app.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Embarque extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embarque);

        getSupportActionBar().setTitle("Transferencia");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



    }

    private class ecs_registroTransferencia extends AsyncTask<String[],Void,String[]>{

        @Override
        protected String[] doInBackground(String[]... strings) {

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_RegistroTransferencia";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_RegistroTransferencia";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("intIdRelacionPaquete", strings[0]);
            request.addProperty("intCorRecalada", strings[1]);
            request.addProperty("intRutCliente", strings[2]);
            request.addProperty("vchCodPuertoDestino", strings[3]);
            request.addProperty("chrCodMarca", strings[4]);
            request.addProperty("intRutUsuarioEmbarque", strings[5]);
            request.addProperty("sdtFechaEmbarque", strings[6]);
            request.addProperty("intTurnoEmbarque", strings[7]);
            request.addProperty("chrCodNave", strings[8]);  //Aclarar porfavor
            request.addProperty("chrCodBodegaNave", strings[9]);
            request.addProperty("vchCodGrua", strings[10]);
            request.addProperty("vchDesGrua", strings[11]); //De donde nace esto?
            request.addProperty("intRutOperador", strings[12]);
            request.addProperty("vchOperador", strings[13]);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);


            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                String codigo = String.valueOf(resultado_xml.getProperty("codigo"));
                String descripcion =  String.valueOf(resultado_xml.getProperty("descripcion"));

                return new String[] {codigo,descripcion};
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


                return new String[0];
        }
    }


}