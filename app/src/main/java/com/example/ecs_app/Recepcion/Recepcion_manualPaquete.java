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
import com.example.ecs_app.AtiApp;
import com.example.ecs_app.Entidades.PaqueteManual;
import com.example.ecs_app.R;
import com.example.ecs_app.WS_Torpedo;
import com.example.ecs_app.WS_TorpedoImp;
import java.util.ArrayList;
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
    WS_Torpedo ws = new WS_TorpedoImp();

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

        ArrayList<String> salida = new ArrayList<>();

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

            return ws.ingresoPaqueteManual(strings);

        }
    }

    private class ecs_BuscarPaquetesPorCarro extends AsyncTask<String,String,ArrayList<PaqueteManual>>{

        @Override
        protected ArrayList<PaqueteManual> doInBackground(String... strings) {

            return ws.ecs_BuscarPaquetesPorCarro(strings);

        }
    }

    private class ecs_EliminarPaqueteManual extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {

            return ws.ecs_EliminarPaqueteManual(strings);

        }
    }
}