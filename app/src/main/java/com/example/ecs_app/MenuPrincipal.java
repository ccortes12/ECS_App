package com.example.ecs_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.example.ecs_app.CFS.CFS_principal;
import com.example.ecs_app.Recepcion.Pre_recepcion;
import com.example.ecs_app.Transferencia.PreTransferencia;

public class MenuPrincipal extends AppCompatActivity {

    private Button cfs_button, gate_button,recepcion_button,despacho_button,almacenaje_button,
            remanejo_button,transferencia_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cfs_button = findViewById(R.id.button_recepcionManual);
        gate_button = findViewById(R.id.button_recepcion1);
        recepcion_button = findViewById(R.id.button_recepcion);
        despacho_button = findViewById(R.id.button_acopio);
        almacenaje_button = findViewById(R.id.button_almacenaje);
        remanejo_button = findViewById(R.id.button_remanejo);
        transferencia_button = findViewById(R.id.button_transferencia);

        cfs_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCFS = new Intent(v.getContext(), CFS_principal.class);
                startActivity(intentCFS);
            }
        });

        gate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentGate = new Intent(v.getContext(), GateIn.class);
                startActivity(intentGate);

            }
        });

        recepcion_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentRecepcion = new Intent(v.getContext(), Pre_recepcion.class);
                startActivity(intentRecepcion);
            }
        });

        despacho_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAcopio = new Intent(v.getContext(), Despachar.class);
                startActivity(intentAcopio);
            }
        });

        almacenaje_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAlmacenaje = new Intent(v.getContext(), Almacenaje.class);
                startActivity(intentAlmacenaje);
            }
        });

        remanejo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRemanejo = new Intent(v.getContext(), Remanejo.class);
                startActivity(intentRemanejo);
            }
        });

        transferencia_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTransferencia = new Intent(v.getContext(), PreTransferencia.class);
                startActivity(intentTransferencia);
            }
        });

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                new AlertDialog.Builder(MenuPrincipal.this)
                        .setTitle("Fin de sesión")
                        .setMessage("¿Desea salir de la sesión?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(MenuPrincipal.this, Login.class);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Mensaje" , "Se cancelo acción");
                            }
                        })
                        .show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
