package com.example.ecs_app.Recepcion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecs_app.Almacenaje;
import com.example.ecs_app.AtiApp;
import com.example.ecs_app.Entidades.Paquete;
import com.example.ecs_app.Entidades.PaqueteManual;
import com.example.ecs_app.R;

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

public class Recepcion_manualPaquete extends AppCompatActivity implements DialogPaquete.PaqueteDialogListener {

    private ListView listView;
    private Button buttonAgregar;
    private TextView carro;
    private int intIDRelacionCarro;
    private String patente;
    private ArrayList<String> listaPaquetesString;
    private ArrayList<PaqueteManual> listaPaquetes;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepcion_manual_paquete);

        getSupportActionBar().setTitle("Agregar paquete");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        intIDRelacionCarro = Integer.parseInt(getIntent().getStringExtra("codigo"));
        patente = getIntent().getStringExtra("patente");

        //Cargar la lista de paquetes segun el carro
        String[] params = {String.valueOf(intIDRelacionCarro)};
        try {
            listaPaquetes = new ecs_BuscarPaquetesPorCarro().execute(params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        listaPaquetesString = cargarPaquetes();

        listView = findViewById(R.id.listview);
        carro = findViewById(R.id.PatenteCarro);
        buttonAgregar = findViewById(R.id.buttonAgregarPaquete);

        adapter = new ArrayAdapter<String>(Recepcion_manualPaquete.this, R.layout.layout_rowlistview,listaPaquetesString);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                PaqueteManual seleccion = listaPaquetes.get(position);
                final String relacionPaquete = String.valueOf(seleccion.getIntIdRelacionPaquete());

                new AlertDialog.Builder(Recepcion_manualPaquete.this)
                        .setTitle("Confirmar eliminar paquete").setMessage("Desea eliminar el paquete : " +  String.valueOf(seleccion.getIntIdRelacionPaquete()) +"\n"
                        + "Lote : " + String.valueOf(seleccion.getCodigoLote()) + "\n"
                        + "Codigo Paquete : " + String.valueOf(seleccion.getCodigoPaquete()) + "\n"
                        + "Peso = " + String.valueOf(seleccion.getPesoNeto()) + " [kg] ")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Eliminar el paquete

                        String usuario = Integer.toString( ((AtiApp) Recepcion_manualPaquete.this.getApplication()).getRutUsuario() );
                        String [] params = {relacionPaquete,usuario};

                        try {
                            String resp = new ecs_EliminarPaqueteManual().execute(params).get();

                            resp = (resp.equalsIgnoreCase("0")) ? "Exito": "Error al eliminar";

                            if(resp.equalsIgnoreCase("Exito")){
                                listaPaquetes.remove(position);
                                listaPaquetesString.remove(position);
                                adapter.notifyDataSetChanged();

                            }

                            Toast.makeText(Recepcion_manualPaquete.this,resp,Toast.LENGTH_SHORT).show();

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
        });

        carro.setText(patente);

        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogPaquete();
            }
        });

    }


    public void openDialogPaquete(){
        DialogPaquete dialogPaquete = new DialogPaquete();
        dialogPaquete.show(getSupportFragmentManager(),"dialogPaquete");

    }

    @Override
    public void applyTexts(String lote, String codigoPaquete, String pesoNeto) {

        // Rescatar usuario, fecha y turno
        String usuario = Integer.toString( ((AtiApp) Recepcion_manualPaquete.this.getApplication()).getRutUsuario() );
        String fechaRecepcion = ((AtiApp) Recepcion_manualPaquete.this.getApplication()).getFecha().replaceAll(" ","");
        String turnoRecepcion = Integer.toString(((AtiApp) Recepcion_manualPaquete.this.getApplication()).getTurno());
        String relacionCarro = String.valueOf(intIDRelacionCarro);
        String[] params = {relacionCarro,lote,codigoPaquete,pesoNeto,usuario,fechaRecepcion,turnoRecepcion};

        try {
            String resp = new ingresoPaqueteManual().execute(params).get();

            if(resp.equalsIgnoreCase("-1")){
                Toast.makeText(Recepcion_manualPaquete.this,"ERROR, no se puede ingresar el paquete", Toast.LENGTH_SHORT).show();

            }else if (resp.equalsIgnoreCase("0")){
                Toast.makeText(Recepcion_manualPaquete.this,"Paquete ya ingresado", Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(Recepcion_manualPaquete.this,"IntRelacionPaquete " + resp , Toast.LENGTH_SHORT).show();
                listaPaquetesString.add("Lote: " + lote + " - Codigo: " + codigoPaquete + "\nPeso neto: " + pesoNeto + " [kg]");
                PaqueteManual paqueteNuevo = new PaqueteManual(Integer.parseInt(resp),Integer.parseInt(lote),
                        Integer.parseInt(codigoPaquete),Double.parseDouble(pesoNeto));
                listaPaquetes.add(paqueteNuevo);
                adapter.notifyDataSetChanged();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<String> cargarPaquetes(){

        ArrayList<String> salida = new ArrayList<String>();

        if(listaPaquetes.size() > 0){

            for(PaqueteManual it: listaPaquetes){
                salida.add("Lote: " + String.valueOf(it.getCodigoLote()) + " - Codigo: " +
                        String.valueOf(it.getCodigoPaquete()) + "\nPeso Neto: " +
                        String.valueOf(it.getPesoNeto()) +  " [kg]");
            }
        }

        return salida;
    }

    private class ingresoPaqueteManual extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

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
    }

    private class ecs_BuscarPaquetesPorCarro extends AsyncTask<String,String,ArrayList<PaqueteManual>>{

        @Override
        protected ArrayList<PaqueteManual> doInBackground(String... strings) {

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
                if (auxPaquete.getProperty("intIdRelacionPaquete").toString().equalsIgnoreCase("-1")){
                    return salida;
                }

                for(int i = 0; i < numPaquetes; i++){
                    auxPaquete = (SoapObject) paquete.getProperty(i);
                    PaqueteManual temp = new PaqueteManual();
                    temp.setIntIdRelacionPaquete(Integer.parseInt(auxPaquete.getProperty("intIdRelacionPaquete").toString()));
                    String auxLote = auxPaquete.getProperty("chrCodigoLote").toString().replaceAll(" ","");
                    temp.setCodigoLote(Integer.parseInt(auxLote));
                    String auxCodPaquete = auxPaquete.getProperty("chrCodigoPaquete").toString().replaceAll(" ","");
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
    }

    private class ecs_EliminarPaqueteManual extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {

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
    }
}