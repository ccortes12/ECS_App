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
import com.example.ecs_app.WS_Torpedo;
import com.example.ecs_app.WS_TorpedoImp;

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

public class  ReporteParas extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText observacion;
    private TextView fechaDesde, horaDesde, fechaHasta, horaHasta;
    private ImageButton buttonFechaDesde, buttonHoraDesde, buttonFechaHasta, buttonHoraHasta;
    private Button buttonIngresar;
    private Spinner motivoSpinner;
    private ArrayAdapter<String> comboAdapter;
    private TimePickerDialog timePickerDialog;
    private String rutCliente,correlativo,codMarca,chrCodBodegaNave,rutUsuario;
    private int dia, mes, anno, hora, minutos;

    WS_Torpedo ws = new WS_TorpedoImp();

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
        buttonIngresar = findViewById(R.id.buttonIngresarPara);


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

        rutCliente = getIntent().getExtras().getString("rutCliente");
        correlativo = ((AtiApp) ReporteParas.this.getApplication()).getLastCorrelativo();
        codMarca = getIntent().getExtras().getString("codMarca");
        chrCodBodegaNave = getIntent().getExtras().getString("chrCodBodegaNave");
        rutUsuario = String.valueOf(((AtiApp) ReporteParas.this.getApplication()).getRutUsuario());



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

                if(!fechaDesde.getText().toString().equalsIgnoreCase("Ingrese fecha") && !horaDesde.getText().toString().equalsIgnoreCase("Ingrese hora")
                    && !fechaHasta.getText().toString().equalsIgnoreCase("Ingrese fecha") && !horaHasta.getText().toString().equalsIgnoreCase("Ingrese hora")
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
                                            "Motivo: " + motivoSpinner.getSelectedItem().toString())
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String[] codMotivo = motivoSpinner.getSelectedItem().toString().split(" ");

                                        String[] params = {
                                                correlativo,
                                                codMotivo[0],
                                                fechaDesde.getText().toString().replace(" ","") + " " + horaDesde.getText().toString().replace(" ",""),
                                                fechaHasta.getText().toString().replace(" ","") + " " + horaHasta.getText().toString().replace(" ",""),
                                                rutCliente,
                                                codMarca,
                                                chrCodBodegaNave,
                                                rutUsuario
                                        };

                                        String resp = "";

                                        try {
                                            resp = new ecs_registroParas().execute(params).get();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        if(resp.equalsIgnoreCase("1")){
                                            limpiarCampos();
                                            Toast.makeText(ReporteParas.this, "Para registrada con exito" , Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(ReporteParas.this, "ERROR, no se registro para" , Toast.LENGTH_SHORT).show();
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
        comboAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_simple_row, lista);
        comboAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_simple_row);
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

    private void limpiarCampos(){

        fechaDesde.setText("");
        horaDesde.setText("");
        fechaHasta.setText("");
        horaHasta.setText("");
        motivoSpinner.setSelection(0);

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

            return ws.ecs_ListarParas();

        }
    }

    private class ecs_registroParas extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return ws.ecs_RegistroParas(strings);
        }
    }
}