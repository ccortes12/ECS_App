package com.example.ecs_app.Transferencia;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ecs_app.AtiApp;
import com.example.ecs_app.R;

import org.joda.time.DateTime;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class ReporteParas extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText observacion;
    private TextView fechaDesde, horaDesde, fechaHasta, horaHasta;
    private ImageButton buttonFechaDesde, buttonHoraDesde, buttonFechaHasta, buttonHoraHasta;
    private Button buttonIngresar;
    private Spinner motivoSpinner;
    private ArrayAdapter<String> comboAdapter;
    private TimePickerDialog timePickerDialog;

    private int dia, mes,anno,hora, minutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_paras);

        getSupportActionBar().setTitle("Reporte paras");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonFechaHasta = findViewById(R.id.imageButton3);
        buttonFechaDesde = findViewById(R.id.imageButton2);
        fechaDesde = findViewById(R.id.textVieFechaDesde);
        fechaHasta = findViewById(R.id.textView103);
        horaDesde = findViewById(R.id.textView101);
        horaHasta = findViewById(R.id.textView105);
        buttonHoraDesde = findViewById(R.id.imageButton);
        buttonHoraHasta = findViewById(R.id.imageButton4);
        motivoSpinner = findViewById(R.id.spinner4);
        buttonIngresar = findViewById(R.id.button10);
        observacion = findViewById(R.id.editTextTextMultiLine2);

        Calendar c = Calendar.getInstance();

        final DatePickerDialog datePickerDialog;

        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int mes = c.get(Calendar.MONTH);
        final int anno = c.get(Calendar.YEAR);

        try {
            cargarSpinnerParas();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        buttonFechaDesde.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                final DatePickerDialog datePickerDialog = new DatePickerDialog(ReporteParas.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fechaDesde.setText(Integer.toString(dayOfMonth) + " / " + Integer.toString(month + 1) + " / " + Integer.toString(year));
                    }
                },dia,mes,anno);
                datePickerDialog.updateDate(anno,mes,dia);
                datePickerDialog.show();

            }
        });

        buttonFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(ReporteParas.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fechaHasta.setText(Integer.toString(dayOfMonth) + " / " + Integer.toString(month + 1) + " / " + Integer.toString(year));
                    }
                },dia,mes,anno);
                datePickerDialog.updateDate(anno,mes,dia);
                datePickerDialog.show();
            }
        });

        buttonHoraDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timePickerDialog = new TimePickerDialog(ReporteParas.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String mensaje = formatoHora(hourOfDay) + " : " + formatoHora(minute);
                        horaDesde.setText(mensaje);
                    }
                },minutos,hora,true);



                timePickerDialog.updateTime(hora,minutos);
                timePickerDialog.show();

            }
        });

        buttonHoraHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(ReporteParas.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String mensaje = formatoHora(hourOfDay) + " : " + formatoHora(minute);
                        horaHasta.setText(mensaje);
                    }
                },minutos,hora,true);

                timePickerDialog.updateTime(hora,minutos);
                timePickerDialog.show();
            }
        });

        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!fechaDesde.getText().toString().equalsIgnoreCase("") && !horaDesde.getText().toString().equalsIgnoreCase("")
                    && !fechaHasta.getText().toString().equalsIgnoreCase("") && !horaHasta.getText().toString().equalsIgnoreCase("")
                    && !motivoSpinner.getSelectedItem().toString().equalsIgnoreCase("")){

                    String[] arrFecha = fechaDesde.getText().toString().replaceAll(" ","").split("/");
                    String[] arrFecha2 = fechaHasta.getText().toString().replaceAll(" ","").split("/");
                    String[] arrHora = horaDesde.getText().toString().replaceAll(" ","").split(":");
                    String[] arrHora2 = horaHasta.getText().toString().replaceAll(" ","").split(":");

                    DateTime dateTimeDesde = new DateTime(Integer.valueOf(arrFecha[2]), Integer.valueOf(arrFecha[1]),Integer.valueOf(arrFecha[0]),Integer.valueOf(arrHora[0]),Integer.valueOf(arrHora[1]),0);
                    DateTime dateTimeHasta = new DateTime(Integer.valueOf(arrFecha2[2]), Integer.valueOf(arrFecha2[1]),Integer.valueOf(arrFecha2[0]),Integer.valueOf(arrHora2[0]),Integer.valueOf(arrHora2[1]),0);

                    if (dateTimeDesde.isBefore(dateTimeHasta)){

                        new AlertDialog.Builder(ReporteParas.this)
                                .setTitle("Confirmar ingreso reporte fallas")
                                .setMessage("Información para: " + "\n" +
                                            "Fecha inicio: " + arrFecha[0] + "/" + arrFecha[1] + "/" + arrFecha[2] +  " -  Hora: " + arrHora[0] + ":" + arrHora[1] + " horas." + "\n" +
                                            "Fecha termino: " + arrFecha2[0] + "/" + arrFecha2[1] + "/" + arrFecha2[2] +  " -  Hora: " + arrHora2[0] + ":" + arrHora2[1] + " horas." + "\n" +
                                            "Motivo: " + motivoSpinner.getSelectedItem().toString() + "\n" +
                                            "Comentario: " + observacion.getText().toString())
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //TODO: Implementar el metodo

                                        String correlativo = ((AtiApp) ReporteParas.this.getApplication()).getLastCorrelativo();



                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("Mensaje" , "Se cancelo acción");
                                    }
                                })
                                .show();

                    }else{
                        Toast.makeText(ReporteParas.this, "ERROR, Las fechas ingresadas no son continuas" , Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(ReporteParas.this, "ERROR, Campos vacios" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cargarSpinnerParas() throws ExecutionException, InterruptedException {

        ArrayList<String> lista = new ecs_listarParas().execute().get();

        lista.add(0,"");

        motivoSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) ReporteParas.this);
        comboAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        motivoSpinner.setAdapter(comboAdapter);

    }

    private String formatoHora(int valor){

        if(valor == 0){
            return "00";
        }else if(valor > 0 && valor < 10){
            return "0" + String.valueOf(valor);
        }else{
            return String.valueOf(valor);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class ecs_listarParas extends AsyncTask<Void,Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

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

                for(int i = 0; i < numCeldas - 1 ; i++) {
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
    }

    private class ecs_registroParas extends AsyncTask<String[],Void,String[]>{

        @Override
        protected String[] doInBackground(String[]... strings) {

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
                String descripcion =  String.valueOf(resultado_xml.getProperty("descripcion"));

                return new String[] {codigo,descripcion};

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return new String[0];
        }
    }
}