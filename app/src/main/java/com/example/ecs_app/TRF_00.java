package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

public class TRF_00 extends AppCompatActivity {

    private Button button_sello,button_lotes,button_limpiar, button_buscar, button_grabar;
    private EditText anno,cor,cont,codigo,digit,iso,tara,csg,mar,gross,zun;
    private TextView codShipper,descShipper,codPuerto,descPuerto, descCsg, descMar;
    private Contenedor c;
    private Consignatario cs;
    private Marca ma;

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
        cor = (EditText)  findViewById(R.id.editText_cor);
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
                Intent to_lotes = new Intent(v.getContext(),TRF_02_lotes.class);
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
            }
        });

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

                        //Container consolidado
                        if(c.getChrConsolidado().equalsIgnoreCase("S")) {

                            csg.setText(c.getCodCliente());
                            descCsg.setText(c.getDescCliente());
                            mar.setText(c.getCodMarca());
                            descMar.setText(c.getDescMarca());

                            iso.setText(Integer.toString(c.getIsoCode()));
                            tara.setText(Integer.toString(c.getTara()));

                            gross.setText(Integer.toString(c.getGross()));
                            zun.setText(Double.toString(c.getZuncho()));

                            //mostrar los botones en caso de container consolidado
                            button_grabar.setVisibility(View.INVISIBLE);
                            button_lotes.setVisibility(View.VISIBLE);
                            button_sello.setVisibility(View.VISIBLE);

                        }
                    }else{
                        Toast.makeText(TRF_00.this, c.getDescEstado(), Toast.LENGTH_SHORT).show();
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

    private boolean camposVaciosCONT(){
        return anno.getText().toString().equalsIgnoreCase("") || cor.getText().toString().equalsIgnoreCase("") || cont.getText().toString().equalsIgnoreCase("") || codigo.getText().toString().equalsIgnoreCase("") || digit.getText().toString().equalsIgnoreCase("");
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

                if(Integer.parseInt(resultado_xml.getProperty(0).toString()) != -1){

                    Contenedor contenedor = new Contenedor();
                    contenedor.setDescEstado(resultado_xml.getProperty(1).toString());

                    if(Integer.parseInt(resultado_xml.getProperty(0).toString()) == 0){
                        contenedor.setAnnoOperacion(Integer.parseInt(resultado_xml.getProperty(2).toString()));
                        contenedor.setCorOperacion(Integer.parseInt(resultado_xml.getProperty(3).toString()));
                        contenedor.setSigla(resultado_xml.getProperty(4).toString());
                        contenedor.setNumero(Integer.parseInt(resultado_xml.getProperty(5).toString()));
                        contenedor.setDigito(resultado_xml.getProperty(6).toString());
                        contenedor.setCodShipper(resultado_xml.getProperty(7).toString());
                        contenedor.setDescShipper(resultado_xml.getProperty(8).toString());
                        contenedor.setCodPuerto(resultado_xml.getProperty(9).toString());
                        contenedor.setDescPuerto(resultado_xml.getProperty(10).toString());

                        contenedor.setChrConsolidado(resultado_xml.getProperty("chrConsolidado").toString());

                        if(resultado_xml.getProperty("chrConsolidado").toString().equalsIgnoreCase("S")){
                            //Agregar demas propiedades

                            contenedor.setPesoNeto(Integer.parseInt(resultado_xml.getProperty("intPesoNeto").toString()));
                            contenedor.setZuncho(Double.parseDouble(resultado_xml.getProperty("intPesoZuncho").toString()));
                            contenedor.setCodMarca(resultado_xml.getProperty("strCodMarca").toString());
                            contenedor.setDescMarca(resultado_xml.getProperty("strDescMarca").toString());
                            contenedor.setCodCliente(resultado_xml.getProperty("strCodCliente").toString());
                            contenedor.setDescCliente(resultado_xml.getProperty("strDescCliente").toString());
                            contenedor.setIsoCode(Integer.parseInt(resultado_xml.getProperty("intISOCode").toString()));
                            contenedor.setTara(Integer.parseInt(resultado_xml.getProperty("intTara").toString()));
                            contenedor.setGross(Integer.parseInt(resultado_xml.getProperty("intGross").toString()));
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
}
