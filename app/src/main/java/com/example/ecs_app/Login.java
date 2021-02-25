package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ecs_app.Entidades.Area;
import com.example.ecs_app.Entidades.Minera;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private TextView resultado, fecha;
    private Button ingresoButton;
    private ImageButton pickDate;
    private Spinner turno;

    WS_Torpedo ws = new WS_TorpedoImp();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.userId);
        passwordEditText = findViewById(R.id.password);
        fecha = findViewById(R.id.textView_Fecha);
        ingresoButton = findViewById(R.id.button_ingreso);
        pickDate = findViewById(R.id.imageButton);
        turno = findViewById(R.id.spinnerTurno);

        //Carga spinner turnos
        ArrayAdapter<String> comboSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ((AtiApp) Login.this.getApplication()).getListaTurnos());
        turno.setAdapter(comboSpinner);


        Calendar c = Calendar.getInstance();
        final DatePickerDialog datePickerDialog;

        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int mes = c.get(Calendar.MONTH);
        final int anno = c.get(Calendar.YEAR);

        final String fechaString = Integer.toString(dia) + " / " + Integer.toString(mes + 1) + " / " + Integer.toString(anno);
        fecha.setText(fechaString);

        cargarDatos();

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Intent next = new Intent(v.getContext(), MenuPrincipal.class);
                    if (!turno.getSelectedItem().toString().equalsIgnoreCase("-")) {

                        try {

                            String[] params = {usernameEditText.getText().toString(), passwordEditText.getText().toString()};
                            String respuesta = new validarCredencial().execute(params).get();

                            if (respuesta.equalsIgnoreCase("OK")) {

                                next.putExtra("user", usernameEditText.getText().toString());

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
                    } else {
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
                if (!turno.getSelectedItem().toString().equalsIgnoreCase("-")) {

                    try {

                        String[] params = {usernameEditText.getText().toString(), passwordEditText.getText().toString()};
                        String respuesta = new validarCredencial().execute(params).get();

                        if (respuesta.equalsIgnoreCase("OK")) {

                            next.putExtra("user", usernameEditText.getText().toString());

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

                } else {
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
                }, dia, mes, anno);
                datePickerDialog.updateDate(anno, mes, dia);
                datePickerDialog.show();
            }

        });

    }

    protected void onResume() {
        super.onResume();
        usernameEditText.setText("");
        passwordEditText.setText("");

    }

    public void cargarDatos() {
        try {
            ArrayList<Minera> listaMinera = new ecs_listaMineras().execute().get();
            ((AtiApp) Login.this.getApplication()).setListaMineras(listaMinera);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ArrayList<Area> listaArea = new ecs_listarAreas().execute().get();
            ((AtiApp) Login.this.getApplication()).setListaAreas(listaArea);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void vibrar() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private class validarCredencial extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            return ws.validarCredencial(strings);

        }

    }

    private class ecs_listaMineras extends AsyncTask<Void, Void, ArrayList<Minera>> {
        @Override
        protected ArrayList<Minera> doInBackground(Void... voids) {

            return ws.ecs_listaMineras();

        }
    }

    private class ecs_listarAreas extends AsyncTask<Void, Void, ArrayList<Area>> {
        @Override
        protected ArrayList<Area> doInBackground(Void... voids) {

            return ws.ecs_listarAreas();

        }
    }
}
