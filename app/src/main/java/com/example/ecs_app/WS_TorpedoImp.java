package com.example.ecs_app;

import com.example.ecs_app.Entidades.Area;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.Entidades.Paquete;
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

public class WS_TorpedoImp implements WS_Torpedo{

    public String validarCredencial(String... strings){

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "CFS_ValidaCredencial";
        String SOAP_ACTION = "http://www.atiport.cl/CFS_ValidaCredencial";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("usuario", strings[0]);
        request.addProperty("password", strings[1]);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapPrimitive respuesta = (SoapPrimitive) envelope.getResponse();
            return respuesta.toString();

        }catch (IOException e) {
            e.printStackTrace();
        }catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<Area> ecs_listarAreas() {

        ArrayList<Area> salida = new ArrayList<>();

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_ListarAreas";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarAreas";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            SoapObject listaAreas = (SoapObject) resultado_xml.getProperty("lstAreas");

            int numAreas = listaAreas.getPropertyCount();

            for (int i = 0; i < numAreas - 1; i++) {

                SoapObject areaAux = (SoapObject) listaAreas.getProperty(i);
                Area temp = new Area();
                temp.setEstado(Integer.parseInt(areaAux.getProperty("intEstado").toString()));
                temp.setCodArea(areaAux.getProperty("codArea").toString().trim());
                temp.setDesArea(areaAux.getProperty("desArea").toString().trim());
                temp.setNombre(areaAux.getProperty("Nombre").toString().trim());
                temp.setTimeStamp(Integer.parseInt(areaAux.getProperty("timeStamp").toString()));

                salida.add(temp);
            }
            return salida;

        }catch(HttpResponseException e) {
            e.printStackTrace();
        }catch(SoapFault soapFault) {
            soapFault.printStackTrace();
        }catch(XmlPullParserException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Minera> ecs_listaMineras() {

        ArrayList<Minera> salida = new ArrayList<>();

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_ListarMineras";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarMineras";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("barra", "S");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            SoapObject listTemp = (SoapObject) resultado_xml.getProperty(1);
            SoapObject listaminera = (SoapObject) listTemp.getProperty(0);
            SoapObject minera = (SoapObject) listTemp.getProperty(0);

            int numMineras = listaminera.getPropertyCount();

            for (int i = 0; i < numMineras; i++) {
                SoapObject mineraAux = (SoapObject) minera.getProperty(i);
                Minera temp = new Minera();
                temp.setIntRutCliente(Integer.parseInt(mineraAux.getProperty("intRutCliente").toString()));
                temp.setChrDvCliente(mineraAux.getProperty("chrDvCliente").toString());
                temp.setVchRazonSocial(mineraAux.getProperty("vchRazonSocial").toString());
                temp.setVchNombreFantasia(mineraAux.getProperty("vchNombreFantasia").toString());
                temp.setVchGiro(mineraAux.getProperty("vchGiro").toString());
                temp.setVchDireccion(mineraAux.getProperty("vchDireccion").toString());
                temp.setVchTelefonoFax(mineraAux.getProperty("vchTelefonoFax").toString());
                temp.setChrCodComuna(mineraAux.getProperty("chrCodComuna").toString());
                temp.setBigTimeStamp(Long.parseLong(mineraAux.getProperty("bigTimeStamp").toString()));
                temp.setIntTonMin(Float.parseFloat(mineraAux.getProperty("intTonMin").toString()));
                //temp.setChrTipoEmbarque(mineraAux.getProperty("chrTipoEmbarque").toString());
                temp.setFlgCantidadPiezas(Integer.parseInt(mineraAux.getProperty("flgCantidadPiezas").toString()));
                temp.setDblTarifaAlmacenaje(Float.parseFloat(mineraAux.getProperty("dblTarifaAlmacenaje").toString()));
                temp.setColorPatioGrafico(mineraAux.getProperty("colorPatioGrafico").toString());
                temp.setDblTarTonMinimo(Float.parseFloat(mineraAux.getProperty("dblTarTonMinimo").toString()));
                temp.setIntTonMinTurno2(Float.parseFloat(mineraAux.getProperty("intTonMinTurno2").toString()));

                salida.add(temp);
            }
            return salida;
        }catch(HttpResponseException e) {
            e.printStackTrace();
        }catch(SoapFault soapFault) {
            soapFault.printStackTrace();
        }catch(XmlPullParserException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Paquete ecs_BuscarPaquetesCB(String... strings) {

        Paquete auxPaquete;

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_BuscarPaquetesCB";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_BuscarPaquetesCB";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("rutCliente", strings[0]);
        request.addProperty("codigo_barra", strings[1]);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            auxPaquete = new Paquete();

            auxPaquete.setPesoBruto(Double.parseDouble(resultado_xml.getProperty("PesoBrutoPaquete").toString()));

            if(auxPaquete.getPesoBruto() != 0){
                auxPaquete.setMensaje(resultado_xml.getProperty("strDescEstado").toString());
                auxPaquete.setLote(resultado_xml.getProperty("strLote").toString().trim());
                auxPaquete.setIdPaquete(resultado_xml.getProperty("strIdPaquete").toString());
                auxPaquete.setPeso(Integer.parseInt(resultado_xml.getProperty("dblPeso").toString()));

                auxPaquete.setCodigoPaquete(resultado_xml.getProperty("CodigoPaquete").toString().trim());
                auxPaquete.setPiezas(Integer.parseInt(resultado_xml.getProperty("Piezas").toString()));
                auxPaquete.setPesoBruto(Double.parseDouble(resultado_xml.getProperty("PesoBrutoPaquete").toString()));
                auxPaquete.setPesoNeto(Double.parseDouble(resultado_xml.getProperty("PesoNetoPaquete").toString()));
                auxPaquete.setChrFlgChequeo(resultado_xml.getProperty("chrFlgChequeo").toString());
                auxPaquete.setDescEstado(resultado_xml.getProperty("DescEstado").toString());
                auxPaquete.setArea(resultado_xml.getProperty("Area").toString().trim());
                auxPaquete.setCelda(resultado_xml.getProperty("Celda").toString().trim());

            }else{
                auxPaquete.setDescEstado("No se encuentra");
            }
            return auxPaquete;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Paquete ecs_BuscarPaquete(String... strings) {

        Paquete auxPaquete;

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_BuscarPaquetes";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_BuscarPaquetes";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("rutCliente", strings[0]);
        request.addProperty("lote",strings[1]);
        request.addProperty("paquete",strings[2]);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            auxPaquete = new Paquete();

            auxPaquete.setPesoBruto(Double.parseDouble(resultado_xml.getProperty("PesoBrutoPaquete").toString()));

            if(auxPaquete.getPesoBruto() != 0){
                auxPaquete.setEstado(Integer.parseInt(resultado_xml.getProperty("intEstado").toString()));
                auxPaquete.setMensaje(resultado_xml.getProperty("strDescEstado").toString());
                auxPaquete.setLote(resultado_xml.getProperty("strLote").toString().trim());
                auxPaquete.setIdPaquete(resultado_xml.getProperty("strIdPaquete").toString().trim());
                auxPaquete.setPeso(Integer.parseInt(resultado_xml.getProperty("dblPeso").toString()));

                auxPaquete.setCodigoPaquete(resultado_xml.getProperty("CodigoPaquete").toString().trim());
                auxPaquete.setPiezas(Integer.parseInt(resultado_xml.getProperty("Piezas").toString()));
                //auxPaquete.setPesoBruto(Double.parseDouble(resultado_xml.getProperty("PesoBrutoPaquete").toString()));
                auxPaquete.setPesoNeto(Double.parseDouble(resultado_xml.getProperty("PesoNetoPaquete").toString()));
                auxPaquete.setChrFlgChequeo(resultado_xml.getProperty("chrFlgChequeo").toString());
                auxPaquete.setDescEstado(resultado_xml.getProperty("DescEstado").toString());
                auxPaquete.setArea(resultado_xml.getProperty("Area").toString().trim());
                auxPaquete.setCelda(resultado_xml.getProperty("Celda").toString().trim());

            }else{
                auxPaquete.setDescEstado("No se encuentra");
            }
            return auxPaquete;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public String ecs_RecepcionPaquete(String... strings) {

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_RecepcionPaquete";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_RecepcionPaquete";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("intIdRelacionPaquete", strings[0]);
        request.addProperty("intRutUsuario", strings[1]);
        request.addProperty("fechaRecepcion", strings[2]);
        request.addProperty("intTurnoRecepcion", strings[3]);

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
