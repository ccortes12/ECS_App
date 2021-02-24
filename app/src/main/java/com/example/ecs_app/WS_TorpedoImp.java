package com.example.ecs_app;


import com.example.ecs_app.Entidades.Area;
import com.example.ecs_app.Entidades.Celda;
import com.example.ecs_app.Entidades.Grua;
import com.example.ecs_app.Entidades.Gruero;
import com.example.ecs_app.Entidades.Marca;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.Entidades.Paquete;
import com.example.ecs_app.Entidades.PaqueteCarro;
import com.example.ecs_app.Entidades.PaqueteManual;
import com.example.ecs_app.Entidades.Puerto;

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

    @Override
    public Integer ingresoGuiaManual(String... strings) {

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

    @Override
    public String ingresoPaqueteManual(String... strings) {

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_IngresoPaqueteManual\n";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_IngresoPaqueteManual\n";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("intIdRelacionCarro", strings[0]);
        request.addProperty("chrCodigoLote", strings[1]);
        request.addProperty("chrCodigoPaquete", strings[2]);
        request.addProperty("decPeso", strings[3]);
        request.addProperty("rutUsuarioRecepcion", strings[4]);
        request.addProperty("fechaRecepcion", strings[5]);
        request.addProperty("piezas", 0);
        request.addProperty("zuncho", 0);
        request.addProperty("intTurno", strings[6]);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            String codigo = resultado_xml.getProperty("codigo").toString();
            return codigo;

        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<PaqueteManual> ecs_BuscarPaquetesPorCarro(String... strings) {

        ArrayList<PaqueteManual> salida = new ArrayList<>();

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_BuscarPaquetesPorCarro";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_BuscarPaquetesPorCarro";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("carro", strings[0]);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            SoapObject listTemp = (SoapObject) resultado_xml.getProperty(1);
            SoapObject listaPaquetes = (SoapObject) listTemp.getProperty(0);
            SoapObject paquete = (SoapObject) listTemp.getProperty(0);

            int numPaquetes = listaPaquetes.getPropertyCount();

            //caso de dataTable vacia
            SoapObject auxPaquete = (SoapObject) paquete.getProperty(0);
            if (auxPaquete.getProperty("intIdRelacionPaquete").toString().equalsIgnoreCase("-1")) {
                return salida;
            }

            for (int i = 0; i < numPaquetes; i++) {
                auxPaquete = (SoapObject) paquete.getProperty(i);
                PaqueteManual temp = new PaqueteManual();
                temp.setIntIdRelacionPaquete(Integer.parseInt(auxPaquete.getProperty("intIdRelacionPaquete").toString()));
                String auxLote = auxPaquete.getProperty("chrCodigoLote").toString().replaceAll(" ", "");
                temp.setCodigoLote(Integer.parseInt(auxLote));
                String auxCodPaquete = auxPaquete.getProperty("chrCodigoPaquete").toString().replaceAll(" ", "");
                temp.setCodigoPaquete(Integer.parseInt(auxCodPaquete));
                temp.setPesoNeto(Double.parseDouble(auxPaquete.getProperty("decPesoNeto").toString()));

                salida.add(temp);
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

    @Override
    public String ecs_EliminarPaqueteManual(String... strings) {

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_EliminarPaqueteManual\n";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_EliminarPaqueteManual\n";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("intIdRelacionPaquete", strings[0]);
        request.addProperty("intRutUsuario", strings[1]);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            String codigo = resultado_xml.getProperty("codigo").toString();
            return codigo;

        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public String ecs_AlmacenaPaquete(String... strings) {

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_AlmacenaPaquete";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_AlmacenaPaquete";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


        request.addProperty("area", strings[0]);
        request.addProperty("celda", strings[1]);
        request.addProperty("intIdRelacionPaquete", strings[2]);
        request.addProperty("intRutUsuario", strings[3]);
        request.addProperty("fechaRecepcion", strings[4]);
        request.addProperty("intTurnoRecepcion", strings[5]);

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

    @Override
    public ArrayList<Celda> ecs_ListarCeldas(String... strings) {

        ArrayList<Celda> salida = new ArrayList<>();

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_ListarCeldas";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarCeldas";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("codigoArea", strings[0]);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            SoapObject listaCeldas = (SoapObject) resultado_xml.getProperty("lstCeldas");

            int numCeldas = listaCeldas.getPropertyCount();

            for (int i = 0; i < numCeldas - 1; i++) {
                SoapObject celdaAux = (SoapObject) listaCeldas.getProperty(i);
                Celda temp = new Celda();
                temp.setIntEstado(Integer.parseInt(celdaAux.getProperty("intEstado").toString()));
                temp.setCodCelda(celdaAux.getProperty("codCelda").toString().trim());
                temp.setDesCelda(celdaAux.getProperty("desCelda").toString().trim());
                temp.setCodArea(celdaAux.getProperty("codArea").toString().trim());
                temp.setDesArea(celdaAux.getProperty("desArea").toString().trim());
                temp.setCapacidadPaquetes(Integer.parseInt(celdaAux.getProperty("capacidadPaquetes").toString()));
                temp.setPreAcopio(celdaAux.getProperty("preAcopio").toString());
                temp.setTimeStamp(Integer.parseInt(celdaAux.getProperty("timeStamp").toString()));

                salida.add(temp);
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

    @Override
    public String ecs_Despachar(String... strings) {

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_Despachar";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_Despachar";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("Id", strings[0]);
        request.addProperty("codArea", strings[1]);
        request.addProperty("codCelda", strings[2]);
        request.addProperty("patenteCamion", "");
        request.addProperty("rut", strings[3]);
        request.addProperty("fecha", strings[4]);
        request.addProperty("turno", strings[5]);

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

    @Override
    public String ecs_Remanejar(String... strings) {
        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_Remanejar";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_Remanejar";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("Id", strings[0]);
        request.addProperty("codAreaOrigen", strings[1]);
        request.addProperty("codCeldaOrigen", strings[2]);
        request.addProperty("codAreaDestino", strings[3]);
        request.addProperty("codCeldaDestino", strings[4]);
        request.addProperty("rut", strings[5]);
        request.addProperty("fecha", strings[6]);
        request.addProperty("turno", strings[7]);

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

    @Override
    public ArrayList<Minera> ecs_ListarMinerasRecalada(String... strings) {

        ArrayList<Minera> salida = new ArrayList<>();

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_ListarMinerasRecalada";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarMinerasRecalada";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("corrRecalada", strings[0]);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {

            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            SoapObject auxListaMineras = (SoapObject) resultado_xml.getProperty("lstMineras");

            int numCeldas = auxListaMineras.getPropertyCount();

            for (int i = 0; i < numCeldas - 1; i++) {

                SoapObject celdaAux = (SoapObject) auxListaMineras.getProperty(i);
                Minera temp = new Minera();
                temp.setIntRutCliente(Integer.parseInt(celdaAux.getProperty("rutCliente").toString()));
                temp.setChrDvCliente(celdaAux.getProperty("dvCliente").toString());
                temp.setVchRazonSocial(celdaAux.getProperty("razonSocial").toString());
                temp.setVchNombreFantasia(celdaAux.getProperty("nombreFantasia").toString());
                //temp.setVchGiro(celdaAux.getProperty("vchGiro").toString());
                temp.setVchDireccion(celdaAux.getProperty("direccion").toString());
                //temp.setVchTelefonoFax(celdaAux.getProperty("vchTelefonoFax").toString());
                temp.setChrCodComuna(celdaAux.getProperty("codComuna").toString());
                temp.setBigTimeStamp(Long.parseLong(celdaAux.getProperty("timeStamp").toString()));
                temp.setIntTonMin(Float.parseFloat(celdaAux.getProperty("tonMin").toString()));
                //temp.setChrTipoEmbarque(mineraAux.getProperty("chrTipoEmbarque").toString());
                temp.setFlgCantidadPiezas(Integer.parseInt(celdaAux.getProperty("flgCantidadPiezas").toString()));
                temp.setDblTarifaAlmacenaje(Float.parseFloat(celdaAux.getProperty("dblTarifaAlmacenaje").toString()));
                //temp.setColorPatioGrafico(celdaAux.getProperty("colorPatioGrafico").toString());
                temp.setDblTarTonMinimo(Float.parseFloat(celdaAux.getProperty("tarTonMinimo").toString()));
                temp.setIntTonMinTurno2(Float.parseFloat(celdaAux.getProperty("tonMinTurno2").toString()));

                salida.add(temp);


            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return salida;
    }

    @Override
    public String[] ecs_RegistroTransferencia(String... strings) {

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_RegistroTransferencia";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_RegistroTransferencia";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("intIdRelacionPaquete", strings[0]);
        request.addProperty("intCorRecalada", strings[1]);
        request.addProperty("intRutCliente", strings[2]);
        request.addProperty("vchCodPuertoDestino", strings[3]);
        request.addProperty("chrCodMarca", strings[4]);
        request.addProperty("intRutUsuarioEmbarque", strings[5]);
        request.addProperty("sdtFechaEmbarque", strings[6]);
        request.addProperty("intTurnoEmbarque", strings[7]);
        request.addProperty("chrCodNave", strings[8]);
        request.addProperty("chrCodBodegaNave", strings[9]);
        request.addProperty("vchCodGrua", strings[10]);
        request.addProperty("vchDesGrua", strings[11]);
        request.addProperty("intRutOperador", strings[12]);
        request.addProperty("vchOperador", strings[13]);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);


        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            String codigo = String.valueOf(resultado_xml.getProperty("codigo"));
            String descripcion = String.valueOf(resultado_xml.getProperty("descripcion"));

            return new String[]{codigo, descripcion};
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        return new String[0];

    }

    @Override
    public ArrayList<Puerto> ecs_ListarPuertos(String... strings) {

        ArrayList<Puerto> salida = new ArrayList<>();

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_ListarPuertos";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarPuertos";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("rutCliente", Integer.parseInt(strings[0]));
        request.addProperty("corRecalada", Integer.parseInt(strings[1]));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {

            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            SoapObject auxListaPuertos = (SoapObject) resultado_xml.getProperty("lstPuertos");

            int numCeldas = auxListaPuertos.getPropertyCount();


            for (int i = 0; i < numCeldas - 1; i++) {

                SoapObject celdaAux = (SoapObject) auxListaPuertos.getProperty(i);
                Puerto temp = new Puerto();
                temp.setIntEstado(Integer.parseInt(celdaAux.getProperty("intEstado").toString()));
                temp.setStrCodPuerto(celdaAux.getProperty("strCodPuerto").toString());
                temp.setStrDesPuerto(celdaAux.getProperty("strDesPuerto").toString());

                salida.add(temp);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        return salida;
    }

    @Override
    public ArrayList<Marca> ecs_ListarMarcas(String... strings) {

        ArrayList<Marca> salida = new ArrayList<>();

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_ListarMarcas";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarMarcas";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("rutCliente", Integer.parseInt(strings[0]));
        request.addProperty("corRecalada", Integer.parseInt(strings[1]));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {

            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            SoapObject auxListaMarcas = (SoapObject) resultado_xml.getProperty("lstMarcas");
            int numCeldas = auxListaMarcas.getPropertyCount();

            for (int i = 0; i < numCeldas - 1; i++) {

                SoapObject celdaAux = (SoapObject) auxListaMarcas.getProperty(i);
                Marca temp = new Marca();
                temp.setCodMarca(celdaAux.getProperty("strCodMarca").toString());
                temp.setEstado(Integer.parseInt(celdaAux.getProperty("intEstado").toString()));
                temp.setDescMarca(celdaAux.getProperty("strDescMarca").toString());

                salida.add(temp);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return salida;


    }

    @Override
    public ArrayList<Grua> ecs_ListarGruas() {

        ArrayList<Grua> salida = new ArrayList<>();

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_ListarGruas";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarGruas";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {

            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            SoapObject auxListaGruas = (SoapObject) resultado_xml.getProperty("lstGruas");
            int numCeldas = auxListaGruas.getPropertyCount();

            for (int i = 0; i < numCeldas - 1; i++) {
                SoapObject celdaAux = (SoapObject) auxListaGruas.getProperty(i);
                Grua temp = new Grua();
                temp.setEstado(Integer.parseInt(celdaAux.getProperty("intEstado").toString()));
                temp.setCodGrua(Integer.parseInt(celdaAux.getProperty("strCodGrua").toString()));
                temp.setDescGrua(celdaAux.getProperty("strDesGrua").toString());
                salida.add(temp);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return salida;
    }

    @Override
    public ArrayList<Gruero> ecs_ListarOperadores(String... strings) {

        ArrayList<Gruero> salida = new ArrayList<>();

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_ListarOperadores";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarOperadores";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("corRecalada", Integer.parseInt(strings[0]));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {

            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            SoapObject auxListaGruero = (SoapObject) resultado_xml.getProperty("lstOperador");
            int numCeldas = auxListaGruero.getPropertyCount();

            for (int i = 0; i < numCeldas - 1; i++) {
                SoapObject celdaAux = (SoapObject) auxListaGruero.getProperty(i);
                Gruero temp = new Gruero();
                temp.setEstado(Integer.parseInt(celdaAux.getProperty("intEstado").toString()));
                temp.setCodOperador(Integer.parseInt(celdaAux.getProperty("strCodOperador").toString()));
                temp.setDesOperador(celdaAux.getProperty("strDesOperador").toString());
                salida.add(temp);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return salida;

    }

    @Override
    public ArrayList<String> ecs_ListarParas() {

        ArrayList<String> salida = new ArrayList<>();

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_ListarParas";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_ListarParas";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);


        try {

            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            SoapObject auxListaParas = (SoapObject) resultado_xml.getProperty("lstPara");
            int numCeldas = auxListaParas.getPropertyCount();

            for (int i = 0; i < numCeldas - 1; i++) {
                SoapObject celdaAux = (SoapObject) auxListaParas.getProperty(i);
                String para = celdaAux.getProperty("strCodPara").toString() + celdaAux.getProperty("strDesPara").toString();
                salida.add(para);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return salida;

    }

    @Override
    public String[] ecs_RegistroParas(String... strings) {

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_RegistroParas";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_RegistroParas";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("intCorRecalada", strings[0]);
        request.addProperty("chrCodTipoIncidente", strings[1]);
        request.addProperty("sdtFechaInicio", strings[2]);
        request.addProperty("sdtFechaTermino", strings[3]);
        request.addProperty("intRutCliente", strings[4]);
        request.addProperty("chrCodMarca", strings[5]);
        request.addProperty("chrCodBodegaNave", strings[6]);
        request.addProperty("intRutUsuario", strings[7]);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            String codigo = String.valueOf(resultado_xml.getProperty("codigo"));
            String descripcion = String.valueOf(resultado_xml.getProperty("descripcion"));

            return new String[]{codigo, descripcion};

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return new String[0];
    }

    @Override
    public ArrayList<PaqueteCarro> ecs_ObtenerRelacionPaquete(String... strings) {

        ArrayList<PaqueteCarro> listaPaquetes = new ArrayList<>();

        String NAMESPACE = "http://www.atiport.cl/";
        String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
        String METHOD_NAME = "ECS_ObtenerRelacionPaquete";
        String SOAP_ACTION = "http://www.atiport.cl/ECS_ObtenerRelacionPaquete";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("intCorRecalada", strings[0]);
        request.addProperty("intRutCliente", strings[1]);
        request.addProperty("vchCodPuertoDestino", strings[2]);
        request.addProperty("chrCodMarca", strings[3]);
        request.addProperty("chrCodigoLote", strings[4]);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(URL);

        try {
            transport.call(SOAP_ACTION, envelope);
            SoapObject resultado_xml = (SoapObject) envelope.getResponse();

            SoapObject auxListaPaquetes = (SoapObject) resultado_xml.getProperty("lstRelacionPaquete");
            int numCeldas = auxListaPaquetes.getPropertyCount();

            for (int i = 0; i < numCeldas-1; i++) {

                SoapObject celdaAux = (SoapObject) auxListaPaquetes.getProperty(i);
                PaqueteCarro temp = new PaqueteCarro();

                temp.setIntEstado(Integer.parseInt(celdaAux.getProperty("intEstado").toString()));
                temp.setIntIdRelacionCarro(Integer.parseInt(celdaAux.getProperty("intIdRelacionCarro").toString()));
                temp.setIntIdRelacionPaquete(Integer.parseInt(celdaAux.getProperty("intIdRelacionPaquete").toString()));
                temp.setChrNumeroCarro(celdaAux.getProperty("chrNumeroCarro").toString());
                temp.setSdtFechaRecepcion(celdaAux.getProperty("sdtFechaRecepcion").toString());
                temp.setChrCodigoPaquete(celdaAux.getProperty("chrCodigoPaquete").toString());
                temp.setChrCodigoLote(celdaAux.getProperty("chrCodigoLote").toString());
                temp.setDecPesoNeto(Double.parseDouble(celdaAux.getProperty("decPesoNeto").toString()));
                temp.setDecPesoBruto(Double.parseDouble(celdaAux.getProperty("decPesoBruto").toString()));
                temp.setIntCantidadPiezas(Double.parseDouble(celdaAux.getProperty("intCantidadPiezas").toString()));
                temp.setChrFlgChequeo(celdaAux.getProperty("chrFlgChequeo").toString());

                listaPaquetes.add(temp);
            }

            return listaPaquetes;


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
