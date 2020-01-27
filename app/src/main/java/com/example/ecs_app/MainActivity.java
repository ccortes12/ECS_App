package com.example.ecs_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText,passwordEditText;
    private TextView resultado,fecha;
    private Button ingresoButton;
    private ImageButton pickDate;
    private boolean pass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.userId);
        passwordEditText = findViewById(R.id.password);
        resultado = findViewById(R.id.resultado);
        fecha = findViewById(R.id.textView_Fecha);
        ingresoButton = findViewById(R.id.button_ingreso);
        pickDate = findViewById(R.id.imageButton);

        Calendar c = Calendar.getInstance();
        final DatePickerDialog datePickerDialog;

        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int mes = c.get(Calendar.MONTH);
        final int anno = c.get(Calendar.YEAR);

        final String fechaString = Integer.toString(dia) + " / " + Integer.toString(mes+1)  + " / " + Integer.toString(anno);
        fecha.setText(fechaString);

        ingresoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next = new Intent(v.getContext(),TRF_00.class);

                validarCredencial validacion = new validarCredencial();
                validacion.execute();



                if(pass){
                    startActivity(next);
                }else{
                    Toast.makeText(MainActivity.this, "Error, Ingrese nuevamente", Toast.LENGTH_SHORT).show();
                    passwordEditText.getText().clear(); usernameEditText.getText().clear();
                }

            }
        });

        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        fecha.setText(Integer.toString(dayOfMonth) + " / " + Integer.toString(month + 1) + " / " + Integer.toString(year));
                    }
                },dia, mes , anno);
                datePickerDialog.updateDate(anno,mes,dia);
                datePickerDialog.show();
            }
            // datePickerDialog.show();  Por que chucha no?

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
                pass = true;
            } else {pass = false;}
        }
    }




}
