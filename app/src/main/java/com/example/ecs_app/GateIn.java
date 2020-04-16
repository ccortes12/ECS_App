package com.example.ecs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class GateIn extends AppCompatActivity {

    private EditText patente,contenedor,codigo,digito;
    private Button button_limpiar, button_buscar, button_grabar;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_in);

        getSupportActionBar().setTitle("Gate - in");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        patente = findViewById(R.id.editText_patente);
        contenedor = findViewById(R.id.editText_cont);
        codigo = findViewById(R.id.editText_codigo);
        digito = findViewById(R.id.editText_digito);
        button_limpiar = findViewById(R.id.button_limpiar);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        patente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String st = s.toString();

                if (!st.equals(st.toUpperCase()))
                {
                    st=st.toUpperCase();
                    patente.setText(st);
                }
                patente.setSelection(patente.getText().length());
            }
        });

        button_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                patente.getText().clear();
                contenedor.getText().clear();
                codigo.getText().clear();
                digito.getText().clear();

                patente.requestFocusFromTouch();

                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            }
        });


    }
}
