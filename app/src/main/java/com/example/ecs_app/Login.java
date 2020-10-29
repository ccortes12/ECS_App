package com.example.ecs_app;

        import androidx.appcompat.app.AppCompatActivity;

        import android.annotation.SuppressLint;
        import android.app.DatePickerDialog;
        import android.app.DatePickerDialog.OnDateSetListener;
        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.VibrationEffect;
        import android.os.Vibrator;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.inputmethod.EditorInfo;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.ecs_app.Entidades.Area;
        import com.example.ecs_app.Entidades.Minera;
        import com.example.ecs_app.Recepcion.Pre_recepcion;

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
        import java.util.Calendar;
        import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    private EditText usernameEditText,passwordEditText;
    private TextView resultado,fecha;
    private Button ingresoButton;
    private ImageButton pickDate;
    private Spinner turno;
    private boolean pass = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.userId);
        passwordEditText = findViewById(R.id.password);
        fecha = findViewById(R.id.textView_Fecha);
        ingresoButton = findViewById(R.id.button_ingreso);
        pickDate = findViewById(R.id.imageButton);
        turno = findViewById(R.id.spinner);

        Calendar c = Calendar.getInstance();
        final DatePickerDialog datePickerDialog;

        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int mes = c.get(Calendar.MONTH);
        final int anno = c.get(Calendar.YEAR);

        final String fechaString = Integer.toString(dia) + " / " + Integer.toString(mes+1)  + " / " + Integer.toString(anno);
        fecha.setText(fechaString);

        cargarDatos();

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE){

                    Intent next = new Intent(v.getContext(), MenuPrincipal.class);
                    if(!turno.getSelectedItem().toString().equalsIgnoreCase("-")) {

                        try {
                            String respuesta = new validarCredencial().execute().get();

                            if (true){

                                //respuesta.equalsIgnoreCase("OK")) {

                                next.putExtra("user",usernameEditText.getText().toString());

                                int auxTurno = Integer.parseInt(turno.getSelectedItem().toString());
                                int auxRut = Integer.parseInt(usernameEditText.getText().toString());

                                //Grabar rut,fecha y turno en atiApp
                                ((AtiApp) Login.this.getApplication()).setFecha(fechaString);
                                ((AtiApp) Login.this.getApplication()).setTurno(auxTurno);
                                ((AtiApp) Login.this.getApplication()).setRutUsuario(auxRut);

                                startActivity(next);
                                finish();

                            } else {
                                Toast.makeText(Login.this, respuesta, Toast.LENGTH_SHORT).show();
                                vibrar();
                                passwordEditText.getText().clear();
                            }
                        } catch (Exception e) {
                            e.getCause();
                            Toast.makeText(Login.this, "Error, Ingrese valores a los campos", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Login.this, "Ingrese turno", Toast.LENGTH_SHORT).show();
                        vibrar();
                    }
                }
                return false;
            }

        });

        ingresoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next = new Intent(v.getContext(), MenuPrincipal.class);
                if(!turno.getSelectedItem().toString().equalsIgnoreCase("-")) {

                    try {

                        String respuesta = new validarCredencial().execute().get();

                        if (true){

                            //respuesta.equalsIgnoreCase("OK")) {

                            next.putExtra("user",usernameEditText.getText().toString());

                            int auxTurno = Integer.parseInt(turno.getSelectedItem().toString());
                            int auxRut = Integer.parseInt(usernameEditText.getText().toString());

                            //Grabar rut,fecha y turno en atiApp
                            ((AtiApp) Login.this.getApplication()).setFecha(fechaString);
                            ((AtiApp) Login.this.getApplication()).setTurno(auxTurno);
                            ((AtiApp) Login.this.getApplication()).setRutUsuario(auxRut);

                            startActivity(next);
                            finish();

                        } else {

                            Toast.makeText(Login.this, respuesta, Toast.LENGTH_SHORT).show();
                            vibrar();
                            passwordEditText.getText().clear();
                        }

                    } catch (Exception e) {
                        e.getCause();
                        Toast.makeText(Login.this, "Error, Ingrese valores a los campos", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Login.this, "Ingrese turno", Toast.LENGTH_SHORT).show();
                    vibrar();
                }
            }
        });

        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final DatePickerDialog datePickerDialog = new DatePickerDialog(Login.this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        fecha.setText(Integer.toString(dayOfMonth) + " / " + Integer.toString(month + 1) + " / " + Integer.toString(year));
                    }
                },dia, mes , anno);
                datePickerDialog.updateDate(anno,mes,dia);
                datePickerDialog.show();
            }

        });

    }

    protected void onResume(){
        super.onResume();

        usernameEditText.setText("");
        passwordEditText.setText("");

    }

    public void cargarDatos(){
        try {
            ArrayList<Minera> listaMinera = new ecs_listaMineras().execute().get();
            ((AtiApp) Login.this.getApplication()).setListaMineras(listaMinera);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try{
            ArrayList<Area> listaArea = new ecs_listarAreas().execute().get();
            ((AtiApp) Login.this.getApplication()).setListaAreas(listaArea);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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

    private class validarCredencial extends AsyncTask<String,Void,String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {

            boolean resultado = false;

            String NAMESPACE = "http://www.atiport.cl/";
            String URL = "http://www.atiport.cl/ws_services/PRD/Torpedo.asmx";
            String METHOD_NAME = "CFS_ValidaCredencial";
            String SOAP_ACTION = "http://www.atiport.cl/CFS_ValidaCredencial";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("usuario", usernameEditText.getText().toString());
            request.addProperty("password", passwordEditText.getText().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapPrimitive respuesta = (SoapPrimitive) envelope.getResponse();
                String estado = respuesta.toString();

                return estado;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    private class ecs_listaMineras extends AsyncTask<Void,Void, ArrayList<Minera>> {

        @Override
        protected ArrayList<Minera> doInBackground(Void... voids) {

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

    private class ecs_listarAreas extends AsyncTask<Void,Void,ArrayList<Area>>{

        @Override
        protected ArrayList<Area> doInBackground(Void... voids) {

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

                for(int i = 0; i < numAreas - 1; i++){

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
