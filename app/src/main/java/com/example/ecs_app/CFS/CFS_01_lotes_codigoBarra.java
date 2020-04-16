package com.example.ecs_app.CFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecs_app.Entidades.Contenedor;
import com.example.ecs_app.Entidades.Paquete;
import com.example.ecs_app.R;

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

public class CFS_01_lotes_codigoBarra extends AppCompatActivity {

    private TextView textOperacion, textContenedor,textSaldo;
    private EditText ingresoCodigo;
    private Contenedor contenedor;
    private TableLayout tblLayout;
    private ArrayList<Paquete> listaPaquetes;


    private int saldo;
    private String id_lote,id_paquete;
    private int peso;

    private InputMethodManager imm;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trf_02_lotes_ingreso_codigo_barra);

        getSupportActionBar().setTitle("Ingreso Lotes");



        Intent intent = getIntent();
        contenedor = (Contenedor) intent.getSerializableExtra("objContenedor");

        textOperacion = (TextView) findViewById(R.id.textView24);
        textContenedor = (TextView) findViewById(R.id.textView26);
        ingresoCodigo = (EditText) findViewById(R.id.editText20);
        tblLayout = (TableLayout) findViewById(R.id.tableLayout);
        textSaldo = (TextView) findViewById(R.id.textView32);

        tblLayout.setFocusable(false);

        cargarDatos(tblLayout);

        ingresoCodigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ingresoCodigo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEND || event.getAction() == KeyEvent.ACTION_DOWN){
                    grabarPaqueteCodigo();
                    return true;
                }
                return false;
            }
        });
    }


    private void grabarPaqueteCodigo(){
        try {
            Paquete tempPaquete = new cfs_LeerCodigoBarra().execute().get();

            if(tempPaquete != null) {

                if (tempPaquete.getEstado() == 0) { //Valida lectura

                    id_lote = tempPaquete.getLote();
                    id_paquete = tempPaquete.getIdPaquete();
                    peso = (int) tempPaquete.getPeso();

                    try {

                        if (!listaPaquetes.contains(tempPaquete)) {

                            if ((saldo - peso) >= 0) {

                                //Paso todas las validaciones
                                String respuesta = new cfs_RegistraPaquete().execute().get();

                                if (respuesta.equalsIgnoreCase("0")) { //Registro Exitoso

                                    listaPaquetes.add(tempPaquete);
                                    cargarDatos(tblLayout);

                                } else {
                                    Toast.makeText(CFS_01_lotes_codigoBarra.this, respuesta, Toast.LENGTH_SHORT).show();
                                    vibrar();
                                }
                                ingresoCodigo.setText("");

                            } else {
                                Toast.makeText(CFS_01_lotes_codigoBarra.this, "ERROR, Paquete excede el maximo peso", Toast.LENGTH_SHORT).show();
                                vibrar();
                            }
                            ingresoCodigo.setText("");

                        } else {
                            Toast.makeText(CFS_01_lotes_codigoBarra.this, "ERROR, Paquete ya ingresado", Toast.LENGTH_SHORT).show();
                            vibrar();
                        }
                        ingresoCodigo.setText("");

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        Toast.makeText(CFS_01_lotes_codigoBarra.this, "ERROR, " + e.getCause().toString(), Toast.LENGTH_SHORT).show();
                        vibrar();
                    }
                } else if (tempPaquete.getEstado() == 1) {
                    Toast.makeText(CFS_01_lotes_codigoBarra.this, "ERROR, " + tempPaquete.getMensaje(), Toast.LENGTH_LONG).show();
                    vibrar();
                    ingresoCodigo.setText("");
                }

            }else {
                Toast.makeText(CFS_01_lotes_codigoBarra.this,"ERROR, No es posible leer codigo" ,Toast.LENGTH_LONG).show();
                vibrar();
                ingresoCodigo.setText("");
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos(TableLayout tblLayout){

        String cadenaOperacion = contenedor.getAnnoOperacion() + " - " + contenedor.getCorOperacion();
        String cadenaContenedor = contenedor.getSigla() + "  " + contenedor.getNumero() + " - " + contenedor.getDigito();

        textOperacion.setText(cadenaOperacion);
        textContenedor.setText(cadenaContenedor);

        saldo = contenedor.getGross() - (int)contenedor.getTara();

        try {
            listaPaquetes = new CFS_01_lotes_codigoBarra.cfs__BuscaPaquetesConsolidados().execute().get();

            if(listaPaquetes != null){

                for(int i = 0 ; i < listaPaquetes.size(); i++){

                    Paquete p = listaPaquetes.get(i);
                    TableRow row = (TableRow) tblLayout.getChildAt(i+1);

                    EditText celdaActual = (EditText) row.getChildAt(1);celdaActual.setText(p.getLote());
                    celdaActual = (EditText) row.getChildAt(2);celdaActual.setText(p.getIdPaquete());
                    celdaActual = (EditText) row.getChildAt(3);celdaActual.setText(Double.toString(p.getPeso()));

                    //Actualizar saldo y cor_Lote
                    saldo -= (p.getPeso() + contenedor.getZuncho());

                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        textSaldo.setText(Double.toString(saldo) + " [kg]");

    }

    private class cfs__BuscaPaquetesConsolidados extends AsyncTask<String, Void, ArrayList<Paquete>> {

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

    private class cfs_LeerCodigoBarra extends AsyncTask<String, Void, Paquete> {
        @SuppressLint("WrongThread")
        @Override
        protected Paquete doInBackground(String... strings) {


            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "CFS_LeerCodigoBarra";
            String SOAP_ACTION = "http://www.atiport.cl/CFS_LeerCodigoBarra";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("shipper", contenedor.getCodShipper());
            request.addProperty("lectura", ingresoCodigo.getText().toString());
            request.addProperty("ano_operacion", contenedor.getAnnoOperacion());
            request.addProperty("cor_operacion", contenedor.getCorOperacion());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {

                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                Paquete paquete = new Paquete();

                int estado = Integer.parseInt(resultado_xml.getProperty("intEstado").toString());

                if(estado == 0){

                    paquete.setEstado(Integer.parseInt(resultado_xml.getProperty("intEstado").toString()));
                    paquete.setIdPaquete(resultado_xml.getProperty("strPaquete").toString());
                    paquete.setPeso(Integer.parseInt(resultado_xml.getProperty("intPeso").toString()));
                    paquete.setLote(resultado_xml.getProperty("strLote").toString());
                    paquete.setMensaje(resultado_xml.getProperty("strMensaje").toString());
                    return paquete;

                }else if(estado == 1){
                    paquete.setEstado(1);
                    paquete.setPeso(0);
                    paquete.setLote("");
                    paquete.setIdPaquete("");
                    paquete.setMensaje(resultado_xml.getProperty("strMensaje").toString());
                    return paquete;
                }


            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
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

            int cor_Lote = listaPaquetes.size() + 1;

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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equalsIgnoreCase("0")){
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            }
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
