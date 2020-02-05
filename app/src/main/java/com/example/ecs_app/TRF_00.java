package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecs_app.Entidades.Consignatario;
import com.example.ecs_app.Entidades.Contenedor;
import com.example.ecs_app.Entidades.Marca;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

public class TRF_00 extends AppCompatActivity {


    private Contenedor c = new Contenedor();

    private Button button_sello,button_lotes,button_limpiar, button_buscar, button_grabar;
    private EditText anno,cor,cont,codigo,digit,iso,tara,csg,mar,gross,zun;
    private TextView codShipper,descShipper,codPuerto,descPuerto, descCsg, descMar;
    private Consignatario cs;
    private Marca ma;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trf_00);

        button_sello = (Button) findViewById(R.id.button_sello);
        button_lotes = (Button) findViewById(R.id.button_lotes);
        button_limpiar = (Button) findViewById(R.id.button_clean);
        button_buscar = (Button) findViewById(R.id.button_buscar);
        button_grabar = (Button) findViewById(R.id.button_grabar);

        anno = (EditText) findViewById(R.id.editText_anno);
        cor = (EditText) findViewById(R.id.editText_cor);
        cont = (EditText) findViewById(R.id.editText_cont);
        codigo = (EditText) findViewById(R.id.editText_codigo);
        digit = (EditText) findViewById(R.id.editText_digit);
        iso = (EditText) findViewById(R.id.inputISO);
        tara = (EditText) findViewById(R.id.inputTara);
        csg = (EditText) findViewById(R.id.editText_csg);
        mar = (EditText) findViewById(R.id.editText_mar);
        gross = (EditText) findViewById(R.id.editText_gross);
        zun = (EditText) findViewById(R.id.editText_zun);

        codShipper = (TextView) findViewById(R.id.codShipper);
        descShipper = (TextView) findViewById(R.id.descShipper);
        codPuerto = (TextView) findViewById(R.id.codPuerto);
        descPuerto = (TextView) findViewById(R.id.descPuerto);
        descCsg = (TextView) findViewById(R.id.textView20);
        descMar = (TextView) findViewById(R.id.textView19);


        user = getIntent().getStringExtra("user");

        estadoIngresosConsolidado(false);

        digit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!digit.getText().toString().equalsIgnoreCase("")){
                    buscarContenedor();
                }
            }
        });

        csg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!csg.getText().toString().equalsIgnoreCase("")){

                    try{

                        cs = new cfs_BuscaConsignatario().execute().get();
                        if(cs != null){

                            if(cs.getEstado() == 0){
                                descCsg.setText(cs.getDescCliente());
                            }else if(cs.getEstado() == 1){
                                descCsg.setText("");
                            }
                        }else{
                            Toast.makeText(TRF_00.this, cs.getDescEstado(), Toast.LENGTH_SHORT).show();
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    descCsg.setText("");
                }
            }
        });

        mar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                descMar.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!mar.getText().toString().equalsIgnoreCase("")){

                    try{

                        ma = new cfs_BuscaMarca().execute().get();
                        if(ma != null){
                            if(ma.getEstado() == 0){
                                descMar.setText(ma.getDescMarca());
                            }else if(ma.getEstado() == 1){
                                descMar.setText("");
                            }
                        }else{
                            Toast.makeText(TRF_00.this, ma.getDescEstado(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    descMar.setText("");
                }
            }
        });


        button_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarInfo2();
                button_lotes.setVisibility(View.INVISIBLE);
                button_sello.setVisibility(View.INVISIBLE);
                buscarContenedor();
            }
        });

        button_sello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_sello = new Intent(v.getContext(), TRF_01_sellos.class);
                to_sello.putExtra("objContenedor", (Serializable) c);
                startActivity(to_sello);
            }
        });

        button_lotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //COMPROBAR EL METODO SI ES MANUAL O POR CODIGO DE BARRA
                Intent to_lotes = new Intent(v.getContext(),TRF_02_lotes.class);
                to_lotes.putExtra("objContenedor",(Serializable) c);
                startActivity(to_lotes);
            }
        });

        button_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarText();
            }
        });

        button_grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Grabar nuevo contenedor en el sistema

                //Validar todos los campos no vacios
                if(!allCamposVacios()){

                    // VALIDAR CAMPOS MARCA & CSG
                    if(!(descPuerto.getText().toString().equalsIgnoreCase("") || descMar.getText().toString().equalsIgnoreCase(""))){

                        //CONFIRMAR CON EL USUARIO LA CONSOLIDACION
                        confimarIngresoContainer();

                        // TRANSACCION AL WEB SERVICE
                    }else{
                        Toast.makeText(TRF_00.this, "Error, CSG o MAR invalidos", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(TRF_00.this, "Error, Complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void confimarIngresoContainer(){

        new AlertDialog.Builder(TRF_00.this)
                .setMessage("¿Confirma registro Container?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // TRANSACCION AL WEB SERVICE
                        try {
                            String respuestaTransaccion = new cfs_RegistraConsolidado().execute().get();

                            if(isNumeric(respuestaTransaccion)){

                                buscarContenedor();

                            }else{
                                Toast.makeText(TRF_00.this,"Error, ingreso Container",Toast.LENGTH_SHORT).show();
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
                        Log.d("Mensaje" , "Se cancelo acción");
                    }
                })
                .show();

    }

    private void buscarContenedor(){

        if(!camposVaciosCONT()){

            try{
                c = new cfs_BuscaContenedor().execute().get();

                if (c != null){

                    if(c.getDescEstado().equalsIgnoreCase("OK")){
                        codShipper.setText(c.getCodShipper());
                        descShipper.setText(c.getDescShipper());
                        codPuerto.setText(c.getCodPuerto());
                        descPuerto.setText(c.getDescPuerto());

                        button_grabar.setVisibility(View.VISIBLE);
                        estadoIngresosConsolidado(true);

                        //Container consolidado
                        if(c.getChrConsolidado().equalsIgnoreCase("S")) {

                            csg.setText(c.getCodCliente());
                            descCsg.setText(c.getDescCliente());
                            mar.setText(c.getCodMarca());
                            descMar.setText(c.getDescMarca());

                            iso.setText(Double.toString(c.getIsoCode()));
                            tara.setText(Double.toString(c.getTara()));

                            gross.setText(Double.toString(c.getGross()));
                            zun.setText(Double.toString(c.getZuncho()));

                            //mostrar los botones en caso de container consolidado
                            button_grabar.setVisibility(View.INVISIBLE);
                            button_lotes.setVisibility(View.VISIBLE);
                            button_sello.setVisibility(View.VISIBLE);

                            estadoIngresosConsolidado(false);

                        }
                    }else{
                        Toast.makeText(TRF_00.this, c.getDescEstado(), Toast.LENGTH_SHORT).show();
                        estadoIngresosConsolidado(false);
                        limpiarInfoCont();
                        limpiarInfo2();
                        button_lotes.setVisibility(View.INVISIBLE);
                        button_sello.setVisibility(View.INVISIBLE);
                    }
                }
            }catch(Exception e){
                Toast.makeText(TRF_00.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(TRF_00.this, "Error, complete los campos", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNumeric(String s){

        if (s == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private boolean camposVaciosCONT(){
        return anno.getText().toString().equalsIgnoreCase("") || cor.getText().toString().equalsIgnoreCase("") || cont.getText().toString().equalsIgnoreCase("") || codigo.getText().toString().equalsIgnoreCase("") || digit.getText().toString().equalsIgnoreCase("");
    }

    private boolean allCamposVacios(){

        return iso.getText().toString().equalsIgnoreCase("") || tara.getText().toString().equalsIgnoreCase("") || csg.getText().toString().equalsIgnoreCase("")
                || mar.getText().toString().equalsIgnoreCase("") || gross.getText().toString().equalsIgnoreCase("") || zun.getText().toString().equalsIgnoreCase("") || camposVaciosCONT();
    }

    private void limpiarText(){
        cont.getText().clear();
        codigo.getText().clear();
        digit.getText().clear();
        csg.getText().clear();
        mar.getText().clear();
        gross.getText().clear();
        zun.getText().clear();

        limpiarInfoCont();

        limpiarInfo2();

        button_grabar.setVisibility(View.INVISIBLE);
        button_sello.setVisibility(View.INVISIBLE);
        button_lotes.setVisibility(View.INVISIBLE);

        estadoIngresosConsolidado(false);

        c = new Contenedor();

    }

    private void limpiarInfoCont(){
        codShipper.setText("");
        descPuerto.setText("");
        descShipper.setText("");
        codPuerto.setText("");
    }

    private void limpiarInfo2(){
        iso.setText("");
        tara.setText("");
        csg.setText("");
        mar.setText("");
        descCsg.setText("");
        descMar.setText("");
        gross.setText("");
        zun.setText("");
    }

    private void estadoIngresosConsolidado(boolean estado){

        iso.setFocusable(estado);
        iso.setCursorVisible(estado);

        tara.setFocusable(estado);
        tara.setCursorVisible(estado);

        csg.setFocusable(estado);
        csg.setCursorVisible(estado);

        mar.setFocusable(estado);
        mar.setCursorVisible(estado);

        gross.setFocusable(estado);
        gross.setCursorVisible(estado);

        zun.setFocusable(estado);
        zun.setCursorVisible(estado);

        if(estado){
            iso.setFocusableInTouchMode(true);
            tara.setFocusableInTouchMode(true);
            csg.setFocusableInTouchMode(true);
            mar.setFocusableInTouchMode(true);
            gross.setFocusableInTouchMode(true);
            zun.setFocusableInTouchMode(true);
        }

    }

    private class cfs_BuscaContenedor extends AsyncTask<String, Void, Contenedor>{

        @SuppressLint("WrongThread")
        @Override
        protected Contenedor doInBackground(String... strings) {

            boolean resultado = false;

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "CFS_BuscaContenedor";
            String SOAP_ACTION = "http://www.atiport.cl/CFS_BuscaContenedor";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            int anno_int = Integer.parseInt(anno.getText().toString());
            int cor_int = Integer.parseInt(cor.getText().toString());
            int numero_int = Integer.parseInt(codigo.getText().toString());

            request.addProperty("ano_operacion",anno_int);
            request.addProperty("cor_operacion",cor_int);
            request.addProperty("sigla",cont.getText().toString());
            request.addProperty("numero",numero_int);
            request.addProperty("digito",digit.getText().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try{

                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                if(Integer.parseInt(resultado_xml.getProperty("intEstado").toString()) != -1){

                    Contenedor contenedor = new Contenedor();
                    contenedor.setDescEstado(resultado_xml.getProperty("strDescEstado").toString());

                    if(Integer.parseInt(resultado_xml.getProperty("intEstado").toString()) == 0){
                        contenedor.setAnnoOperacion(Integer.parseInt(resultado_xml.getProperty("intAnoOperacion").toString()));
                        contenedor.setCorOperacion(Integer.parseInt(resultado_xml.getProperty("intCorOperacion").toString()));
                        contenedor.setSigla(resultado_xml.getProperty("strSigla_cnt").toString());
                        contenedor.setNumero(Integer.parseInt(resultado_xml.getProperty("strNumero_cnt").toString()));
                        contenedor.setDigito(resultado_xml.getProperty("strDigito_cnt").toString());
                        contenedor.setCodShipper(resultado_xml.getProperty("strCodShipper").toString());
                        contenedor.setDescShipper(resultado_xml.getProperty("strDescShipper").toString());
                        contenedor.setCodPuerto(resultado_xml.getProperty("strCodPuerto").toString());
                        contenedor.setDescPuerto(resultado_xml.getProperty("strDescPuerto").toString());

                        contenedor.setChrConsolidado(resultado_xml.getProperty("chrConsolidado").toString());

                        if(resultado_xml.getProperty("chrConsolidado").toString().equalsIgnoreCase("S")){
                            //Agregar demas propiedades

                            contenedor.setPesoNeto(Integer.parseInt(resultado_xml.getProperty("intPesoNeto").toString()));
                            contenedor.setZuncho(Integer.parseInt(resultado_xml.getProperty("intPesoZuncho").toString()));
                            contenedor.setCodMarca(resultado_xml.getProperty("strCodMarca").toString());
                            contenedor.setDescMarca(resultado_xml.getProperty("strDescMarca").toString());
                            contenedor.setCodCliente(resultado_xml.getProperty("strCodCliente").toString());
                            contenedor.setDescCliente(resultado_xml.getProperty("strDescCliente").toString());
                            contenedor.setIsoCode(Integer.parseInt(resultado_xml.getProperty("intISOCode").toString()));
                            contenedor.setTara(Integer.parseInt(resultado_xml.getProperty("intTara").toString()));
                            contenedor.setGross(Integer.parseInt(resultado_xml.getProperty("intGross").toString()));

                            contenedor.setCorCFSEntrega(Integer.parseInt(resultado_xml.getProperty("intCorCFS").toString()));

                            //Si no existe la informacion retorna cero
                            contenedor.setSello(resultado_xml.getProperty("strSello").toString());
                        }
                    }
                    return contenedor;
                }
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

    private class cfs_BuscaConsignatario extends AsyncTask<String, Void, Consignatario>{

        @SuppressLint("WrongThread")
        @Override
        protected Consignatario doInBackground(String... strings) {

            boolean resultado = false;

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "CFS_BuscaConsignatario";
            String SOAP_ACTION = "http://www.atiport.cl/CFS_BuscaConsignatario";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("codigoConsignatario",csg.getText().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try{

                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                if(Integer.parseInt(resultado_xml.getProperty("intEstado").toString()) != -1){

                    Consignatario consignatario = new Consignatario();
                    consignatario.setEstado(Integer.parseInt(resultado_xml.getProperty("intEstado").toString()));
                    consignatario.setDescEstado(resultado_xml.getProperty("strDescEstado").toString());

                    if(Integer.parseInt(resultado_xml.getProperty("intEstado").toString()) == 0){

                        consignatario.setCodCliente(resultado_xml.getProperty("strCodCliente").toString());
                        consignatario.setDescCliente(resultado_xml.getProperty("strDescCliente").toString());

                    }
                    return consignatario;
                }
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

    private class cfs_BuscaMarca extends AsyncTask<String, Void, Marca>{

        @SuppressLint("WrongThread")
        @Override
        protected Marca doInBackground(String... strings) {

            boolean resultado = false;

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "CFS_BuscaMarca";
            String SOAP_ACTION = "http://www.atiport.cl/CFS_BuscaMarca";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("codigoMarca",mar.getText().toString());
            request.addProperty("codigoShipper",codShipper.getText().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try{

                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();

                if(Integer.parseInt(resultado_xml.getProperty("intEstado").toString()) != -1){

                    Marca marca = new Marca();

                    marca.setEstado(Integer.parseInt(resultado_xml.getProperty("intEstado").toString()));
                    marca.setDescEstado(resultado_xml.getProperty("strDescEstado").toString());

                    if(Integer.parseInt(resultado_xml.getProperty("intEstado").toString()) == 0){

                        marca.setCodMarca(resultado_xml.getProperty("strCodMarca").toString());
                        marca.setDescMarca(resultado_xml.getProperty("strDescMarca").toString());
                    }
                    return marca;
                }
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

    private class cfs_RegistraConsolidado extends  AsyncTask<String, Void, String>{

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {

            boolean resultado = false;

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "CFS_RegistraConsolidado";
            String SOAP_ACTION = "http://www.atiport.cl/CFS_RegistraConsolidado";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            int anno_operacion = Integer.parseInt(anno.getText().toString());
            int cor_operacion = Integer.parseInt(cor.getText().toString());
            int cor_numero = Integer.parseInt(codigo.getText().toString());
            int isocode = Integer.parseInt(iso.getText().toString());
            int tara2 = Integer.parseInt(tara.getText().toString());
            int maxGross = Integer.parseInt(gross.getText().toString());
            int zuncho = Integer.parseInt(zun.getText().toString());

            request.addProperty("codLugar","ANF");
            request.addProperty("ano_operacion",anno_operacion);
            request.addProperty("cor_operacion",cor_operacion);
            request.addProperty("cor_secuencia",1);
            request.addProperty("cod_sigla",cont.getText().toString());
            request.addProperty("cod_numero",cor_numero);
            request.addProperty("cod_digito",digit.getText().toString());
            request.addProperty("cod_shipper",codShipper.getText().toString());
            request.addProperty("cod_cliente",csg.getText().toString());
            request.addProperty("marca",mar.getText().toString());
            request.addProperty("isocode",isocode);
            request.addProperty("tara",tara2);
            request.addProperty("gross",maxGross);
            request.addProperty("zuncho",zuncho);
            request.addProperty("login",user);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try{

                transport.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();

                return resultado_xml.toString();

            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "N";

        }
    }

}
