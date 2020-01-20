package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView resultado;
    private Button ingresoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.userId);
        passwordEditText = findViewById(R.id.password);
        resultado = findViewById(R.id.resultado);
        ingresoButton = findViewById(R.id.button_ingreso);

        ingresoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next = new Intent(v.getContext(),TRF_00.class);

                validarCredencial validacion = new validarCredencial();
                validacion.execute();

                if(resultado.getText().toString().equalsIgnoreCase("OK")){
                    startActivity(next);
                }
                /**String welcome = "BOYON" ;
                // TODO : initiate successful logged in experience
                Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();*/


            }
        });

    }

    private class validarCredencial extends AsyncTask<String,Void,Boolean> {
        @SuppressLint("WrongThread")
        @Override
        protected Boolean doInBackground(String... strings) {

            boolean resultado = false;

            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://www.atiport.cl/saam.ws.movil/movil.asmx";
            String METHOD_NAME = "ValidarCredencial";
            String SOAP_ACTION = "http://tempuri.org/ValidarCredencial";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            //Añadir aqui parametros si fueran necesarios
            //request.addProperty(nombre,valor);

            request.addProperty("usuario", usernameEditText.getText().toString());
            request.addProperty("contrasena", passwordEditText.getText().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();
                String estado = resultado_xml.getProperty(0).toString();

                if (estado.equalsIgnoreCase("Exito")) {
                    resultado = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                resultado.setText("OK");
            } else {
                resultado.setText("ERROR");
            }
        }
    }




}
