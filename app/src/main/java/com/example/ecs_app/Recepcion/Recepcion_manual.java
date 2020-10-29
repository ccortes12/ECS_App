package com.example.ecs_app.Recepcion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ecs_app.AtiApp;
import com.example.ecs_app.Despachar;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.R;

import org.kobjects.util.Strings;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class Recepcion_manual extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerMineras;
    private EditText patente,guiaDigital;
    private Button grabar;
    private ArrayList<Minera> listaMineras;
    private ArrayList<String> auxSpinner;
    private ArrayAdapter<String> comboAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepcion_manual);

        getSupportActionBar().setTitle("Recepción manual");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spinnerMineras = findViewById(R.id.spinner2);
        patente = findViewById(R.id.editText80);
        guiaDigital = findViewById(R.id.editText81);
        auxSpinner = new ArrayList<String>();
        grabar = findViewById(R.id.button4);

        cargarSpinner();

        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!patente.getText().toString().equalsIgnoreCase("") && !guiaDigital.getText().toString().equalsIgnoreCase("")
                 && !spinnerMineras.getSelectedItem().toString().equalsIgnoreCase("")){

                    int rutCliente = buscarRutMinera(spinnerMineras.getSelectedItem().toString());
                    String[] params = {String.valueOf(rutCliente),guiaDigital.getText().toString(),patente.getText().toString()};

                    int intIDRelacionCarro;

                    try {
                        intIDRelacionCarro = new ingresoGuiaManual().execute(params).get();

                        if (intIDRelacionCarro == -1){
                            Toast.makeText(Recepcion_manual.this,"ERROR",Toast.LENGTH_SHORT).show();
                            patente.setText("");
                            guiaDigital.setText("");
                        }else{
                            Intent intentManual = new Intent(v.getContext(), Recepcion_manualPaquete.class);
                            intentManual.putExtra("codigo",String.valueOf(intIDRelacionCarro));
                            startActivity(intentManual);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(Recepcion_manual.this,"Complete los campos", Toast.LENGTH_SHORT).show();
                    vibrar();
                }

            }
        });


    }

    private void cargarSpinner(){

        listaMineras = ((AtiApp) Recepcion_manual.this.getApplication()).getListaMineras();

        Iterator<Minera> i = listaMineras.iterator();
        auxSpinner.add("");
        while(i.hasNext()){
            Minera item = i.next();
            auxSpinner.add(item.getVchNombreFantasia());
        }
        spinnerMineras.setOnItemSelectedListener(this);

        comboAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, auxSpinner);

        spinnerMineras.setAdapter(comboAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int buscarRutMinera(String nombreFantasia) {

        listaMineras = ((AtiApp) Recepcion_manual.this.getApplication()).getListaMineras();
        Iterator<Minera> i = listaMineras.iterator();
        while (i.hasNext()) {
            Minera item = i.next();

            if (item.getVchNombreFantasia().equalsIgnoreCase(nombreFantasia)) {
                return item.getIntRutCliente();
            }
        }
        return -1;
    }


    private class ingresoGuiaManual extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_IngresoGuiaManual";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_IngresoGuiaManual";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("rutMinera", strings[0]);
            request.addProperty("numGuia", strings[1]);
            request.addProperty("patente", strings[2]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                int codigo = Integer.parseInt(resultado_xml.getProperty("codigo").toString());

                return codigo;
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

    private void vibrar(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }


}
