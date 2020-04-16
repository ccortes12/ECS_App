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

        import org.ksoap2.SoapEnvelope;
        import org.ksoap2.serialization.SoapObject;
        import org.ksoap2.serialization.SoapSerializationEnvelope;
        import org.ksoap2.transport.HttpTransportSE;
        import org.xmlpull.v1.XmlPullParserException;

        import java.io.IOException;
        import java.util.Calendar;

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

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE){

                    Intent next = new Intent(v.getContext(), MenuPrincipal.class); //Proxima actividad
                    if(!turno.getSelectedItem().toString().equalsIgnoreCase("-")) {
                        try {
                            String pass = new validarCredencial().execute().get();

                            String[] partes = pass.split("-");

                            if (partes[0].equalsIgnoreCase("Exito")) {

                                next.putExtra("user",usernameEditText.getText().toString());
                                startActivity(next);
                                finish();


                            } else {
                                if(!partes[1].equalsIgnoreCase("Failed to convert parameter value from a String to a Int32.")){
                                    Toast.makeText(Login.this, "Error, " + partes[1], Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Login.this, "Error, Ingrese un usuario valido", Toast.LENGTH_SHORT).show();
                                }
                                passwordEditText.getText().clear();
                            }

                        } catch (Exception e) {
                            e.getCause();
                        }
                    }else{
                        Toast.makeText(Login.this, "Ingrese turno", Toast.LENGTH_SHORT).show();
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
                        String pass = new validarCredencial().execute().get();

                        String[] partes = pass.split("-");

                        if (partes[0].equalsIgnoreCase("Exito")) {

                            next.putExtra("user",usernameEditText.getText().toString());
                            startActivity(next);
                            finish();

                        } else {
                            if(!partes[1].equalsIgnoreCase("Failed to convert parameter value from a String to a Int32.")){
                                Toast.makeText(Login.this, "Error, " + partes[1], Toast.LENGTH_SHORT).show();
                                vibrar();
                            }else {
                                Toast.makeText(Login.this, "Error, Ingrese un usuario valido", Toast.LENGTH_SHORT).show();
                                vibrar();
                            }
                            passwordEditText.getText().clear();
                        }

                    } catch (Exception e) {
                        e.getCause();
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

            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://www.atiport.cl/saam.ws.movil/movil.asmx";
            String METHOD_NAME = "ValidarCredencial";
            String SOAP_ACTION = "http://tempuri.org/ValidarCredencial";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("usuario", usernameEditText.getText().toString());
            request.addProperty("contrasena", passwordEditText.getText().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, envelope);
                SoapObject resultado_xml = (SoapObject) envelope.getResponse();
                String estado = resultado_xml.getProperty("Estado").toString();
                String mensaje = resultado_xml.getProperty("Mensaje").toString();

                return estado + "-" + mensaje;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}
