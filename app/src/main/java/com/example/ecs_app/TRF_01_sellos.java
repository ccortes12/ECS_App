package com.example.ecs_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.ecs_app.Entidades.Contenedor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TRF_01_sellos extends AppCompatActivity {

    private TextView textViewCont,textViewOperacion;
    private Button ingresarSello;
    private EditText sello;
    private Contenedor contenedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trf_01_sellos);

        textViewCont = (TextView) findViewById(R.id.Contenedor);
        textViewOperacion = (TextView) findViewById(R.id.Operacion);
        ingresarSello = (Button) findViewById(R.id.button_grabarSello);
        sello = (EditText) findViewById(R.id.editText_sello);

        Intent intent = getIntent();
        contenedor = (Contenedor) intent.getSerializableExtra("objContenedor");

        String cadenaOperacion = contenedor.getAnnoOperacion() + " - " + contenedor.getCorOperacion();
        String cadenaContenedor = contenedor.getSigla() + "  " + contenedor.getNumero() + " - " + contenedor.getDigito();

        textViewCont.setText(cadenaContenedor);
        textViewOperacion.setText(cadenaOperacion);


        if(contenedor.getSello().equalsIgnoreCase("0")){

            sello.setText("");
            sello.setCursorVisible(true);
            sello.setFocusable(true);
            sello.setFocusableInTouchMode(true);
            ingresarSello.setVisibility(View.VISIBLE);

        }else{
            sello.setText(contenedor.getSello());
            sello.setCursorVisible(false);
            sello.setFocusable(false);
            ingresarSello.setVisibility(View.INVISIBLE);
        }

        ingresarSello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insertar el sello en el sistema
                //Validar que el campo no este vacio
                if(!sello.getText().toString().equalsIgnoreCase("")){

                    try {
                        String salida = new cfs_RegistraSello().execute().get();

                        if(salida.equalsIgnoreCase("1")){
                            Toast.makeText(TRF_01_sellos.this,"ERROR, No se pudo realizar la operacion",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(TRF_01_sellos.this,"EXITO",Toast.LENGTH_SHORT).show();
                            sello.setCursorVisible(false);
                            sello.setFocusable(false);
                            ingresarSello.setVisibility(View.INVISIBLE);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    private class cfs_RegistraSello extends AsyncTask<String,Void,String>{
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {

            String salida = "1";

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "CFS_RegistraSello";
            String SOAP_ACTION = "http://www.atiport.cl/CFS_RegistraSello";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);



            request.addProperty("codLugar","ANF");
            request.addProperty("ano_operacion",contenedor.getAnnoOperacion());
            request.addProperty("cor_operacion",contenedor.getCorOperacion());
            request.addProperty("cor_sello",1);
            request.addProperty("cod_sigla",contenedor.getSigla());
            request.addProperty("cod_numero",contenedor.getNumero());
            request.addProperty("cod_digito",contenedor.getDigito());
            request.addProperty("des_sello",sello.getText().toString().toUpperCase());

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
