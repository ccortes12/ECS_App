package com.example.ecs_app;

import android.app.Application;
import android.os.AsyncTask;

import com.example.ecs_app.Entidades.Area;
import com.example.ecs_app.Entidades.Minera;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class AtiApp extends Application {

    private String fecha;
    private int turno;
    private int rutUsuario;
    private ArrayList<Minera> listaMineras;
    private ArrayList<Area> listaAreas;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getRutUsuario() {
        return rutUsuario;
    }

    public void setRutUsuario(int rutUsuario) {
        this.rutUsuario = rutUsuario;
    }

    public ArrayList<Minera> getListaMineras() {
        return listaMineras;
    }

    public void setListaMineras(ArrayList<Minera> listaMineras) {
        this.listaMineras = listaMineras;
    }

    public ArrayList<Area> getListaAreas() {
        return listaAreas;
    }

    public void setListaAreas(ArrayList<Area> listaAreas) {
        this.listaAreas = listaAreas;
    }

    private class ecs_Despachar extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "ECS_Despachar";
            String SOAP_ACTION = "http://www.atiport.cl/ECS_Despachar";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("Id", strings[0]);
            request.addProperty("codArea",strings[1]);
            request.addProperty("codCelda",strings[2]);
            request.addProperty("patenteCamion","");
            request.addProperty("rut",rutUsuario);
            request.addProperty("fecha",fecha);
            request.addProperty("turno",String.valueOf(turno));



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                transport.call(SOAP_ACTION, envelope);
                SoapPrimitive respuesta = (SoapPrimitive) envelope.getResponse();
                String salida = respuesta.toString();
                return salida;

            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
