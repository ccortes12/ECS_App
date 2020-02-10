package com.example.ecs_app;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.ecs_app.Entidades.Contenedor;
import com.example.ecs_app.Entidades.Sello;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

        final String cadenaOperacion = contenedor.getAnnoOperacion() + " - " + contenedor.getCorOperacion();
        final String cadenaContenedor = contenedor.getSigla() + "  " + contenedor.getNumero() + " - " + contenedor.getDigito();

        textViewCont.setText(cadenaContenedor);
        textViewOperacion.setText(cadenaOperacion);


        cargarSello();

        ingresarSello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insertar el sello en el sistema
                //Validar que el campo no este vacio

                if (!sello.getText().toString().equalsIgnoreCase("")) {
                    new AlertDialog.Builder(TRF_01_sellos.this)
                            .setTitle("Confirmación registro sello")
                            .setMessage("OPERACIÓN : " + cadenaOperacion + "\n" +
                                    "CONTENEDOR : " + cadenaContenedor + "\n" +
                                    "SELLO : " + sello.getText().toString().toUpperCase())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        String salida = new cfs_RegistraSello().execute().get();

                                        if (salida.equalsIgnoreCase("0")) {
                                            Toast.makeText(TRF_01_sellos.this, "EXITO", Toast.LENGTH_SHORT).show();
                                            cargarSello();
                                        } else {
                                            Toast.makeText(TRF_01_sellos.this, "ERROR, " + salida, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("Mensaje", "Acción cancelada ");
                                }
                            })
                            .show();
                }else{
                    Toast.makeText(TRF_01_sellos.this,"ERROR, Ingrese un sello",Toast.LENGTH_SHORT).show();
                }
            }

        });

        sello.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String st = s.toString();
                if (!st.equals(st.toUpperCase()))
                {
                    st=st.toUpperCase();
                    sello.setText(st);
                }
                sello.setSelection(sello.getText().length());
            }
        });
    }

    private void cargarSello(){

        //Ya viene con el objeto contenedor que puede presentar un "0" si no tiene el sello
        Sello tempSello = null;
        try {

            tempSello = new cfs_BuscaSello().execute().get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if(tempSello.getEstado() == 0){ //Tiene sello

            contenedor.setSello(tempSello.getSello());
            sello.setText(contenedor.getSello());
            sello.setCursorVisible(false);
            sello.setFocusable(false);
            ingresarSello.setVisibility(View.INVISIBLE);

        }else if(tempSello.getEstado() == 1){ //NO tiene sello


            sello.setCursorVisible(true);
            sello.setFocusable(true);
            sello.setFocusableInTouchMode(true);
            ingresarSello.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(TRF_01_sellos.this,"ERROR, " + tempSello.getMensaje(),Toast.LENGTH_SHORT).show();
        }

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

    private class cfs_BuscaSello extends AsyncTask<String, Void, Sello> {
        @Override
        protected Sello doInBackground(String... strings) {

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "CFS_BuscaSello";
            String SOAP_ACTION = "http://www.atiport.cl/CFS_BuscaSello";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("ano_operacion", contenedor.getAnnoOperacion());
            request.addProperty("cor_operacion", contenedor.getCorOperacion());
            request.addProperty("sigla", contenedor.getSigla());
            request.addProperty("numero", contenedor.getNumero());
            request.addProperty("digito", contenedor.getDigito());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                Sello sello = new Sello();

                sello.setEstado(Integer.parseInt(resultado_xml.getProperty("intEstado").toString()));
                sello.setMensaje(resultado_xml.getProperty("strMensaje").toString());
                sello.setSello(resultado_xml.getProperty("strDesSello").toString());

                return sello;


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
}
