package com.example.ecs_app.Transferencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ecs_app.AtiApp;
import com.example.ecs_app.Entidades.PaqueteCarro;
import com.example.ecs_app.R;
import com.example.ecs_app.WS_Torpedo;
import com.example.ecs_app.WS_TorpedoImp;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Embarque extends AppCompatActivity {

    private Button buttonBuscar,buttonEmbarque;
    private EditText codigo;
    private String lastCorrelativo,corRecalada,rutCliente,codPuertoDestino,codMarca,rutUsuario,fechaEmbarque,turno,chrBodegaNave,vchCodGrua,vchDesGrua,intRutOperador,vchOperador;
    private ListView listViewPaquetes;
    private ArrayList<PaqueteCarro> listaPaquetes, listaTemp;
    private ArrayList<String> listaStringPaquetes;
    private ArrayAdapter adapter;
    WS_Torpedo ws = new WS_TorpedoImp();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embarque);

        getSupportActionBar().setTitle("Transferencia");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonBuscar = findViewById(R.id.buscarButton);
        codigo = findViewById(R.id.EditTextCodigoB);
        listViewPaquetes = findViewById(R.id.listviewPaquetes);
        buttonEmbarque = findViewById(R.id.buttonAgregarPaquete);

        rutCliente = getIntent().getExtras().getString("rutCliente");
        codPuertoDestino = getIntent().getExtras().getString("codPuertoDestino");
        codMarca = getIntent().getExtras().getString("codMarca");
        corRecalada = ((AtiApp) Embarque.this.getApplication()).getLastCorrelativo();
        rutUsuario = String.valueOf(((AtiApp) Embarque.this.getApplication()).getRutUsuario());
        fechaEmbarque = ((AtiApp) Embarque.this.getApplication()).getFecha();
        turno = String.valueOf(((AtiApp) Embarque.this.getApplication()).getTurno());
        chrBodegaNave = getIntent().getExtras().getString("chrCodBodegaNave");
        vchCodGrua = getIntent().getExtras().getString("vchCodGrua");
        vchDesGrua = getIntent().getExtras().getString("vchDesGrua");
        intRutOperador = getIntent().getExtras().getString("intRutOperador");
        vchOperador = getIntent().getExtras().getString("vchOperador");


        listaPaquetes = new ArrayList<PaqueteCarro>();

        listaStringPaquetes = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(Embarque.this, android.R.layout.simple_list_item_multiple_choice,listaStringPaquetes);
        listViewPaquetes.setAdapter(adapter);



        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!codigo.getText().toString().equalsIgnoreCase("")){
                    lastCorrelativo=codigo.getText().toString();
                    cargarListaPaquetes(lastCorrelativo);
                }else{
                    Toast.makeText(Embarque.this, "ERROR, ingrese un codigo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonEmbarque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i < listaPaquetes.size() ; i++){

                    PaqueteCarro p = listaPaquetes.get(i);

                    if((p.getChrFlgChequeo().equalsIgnoreCase("R") || p.getChrFlgChequeo().equalsIgnoreCase("D")) &&
                            listViewPaquetes.isItemChecked(i)){

                        String[] params = {
                                String.valueOf(p.getIntIdRelacionPaquete()),
                                corRecalada,
                                rutCliente,
                                codPuertoDestino.trim(),
                                codMarca,
                                rutUsuario,
                                fechaEmbarque,
                                turno,
                                "", //chrCodNave
                                chrBodegaNave,
                                vchCodGrua,
                                vchDesGrua,
                                intRutOperador,
                                vchOperador

                        };

                        try {
                            String[] resp = new ecs_registroTransferencia().execute(params).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        seleccionSpinner(false);
                        listaPaquetes.remove(listaPaquetes.get(i));
                        listaStringPaquetes.remove(listaStringPaquetes.get(i));
                        adapter.notifyDataSetChanged();


                    }
                }

            }
        });

        codigo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(!codigo.getText().toString().equalsIgnoreCase("")){
                        cargarListaPaquetes(codigo.getText().toString());
                    }else{
                        Toast.makeText(Embarque.this, "ERROR, ingrese un codigo", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.spinner_row_embarque,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        seleccionSpinner(true);
        return super.onOptionsItemSelected(item);
    }

    private void cargarListaPaquetes(String codigo){

        String[] params = {corRecalada, rutCliente, codPuertoDestino, codMarca, codigo};

        listaPaquetes.clear();
        listaStringPaquetes.clear();
        adapter.notifyDataSetChanged();

        try {
            listaTemp = new ecs_obtenerRelacionPaquete().execute(params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(listaTemp == null){
            Toast.makeText(Embarque.this, "ERROR, en la carga", Toast.LENGTH_SHORT).show();
        }else if(listaTemp.size() == 0){
            Toast.makeText(Embarque.this, "ERROR, no se encontraron paquetes al lote ", Toast.LENGTH_SHORT).show();
        }else{

            for(PaqueteCarro p : listaTemp){

                if(!listaPaquetes.contains(p) && !p.getChrCodigoLote().equalsIgnoreCase("E")){
                    listaPaquetes.add(p);
                    listaStringPaquetes.add("Lote : " + p.getChrCodigoLote().trim() + " - Paquete: " + p.getChrCodigoPaquete().trim() + " - Peso : " + p.getDecPesoNeto() + " [kg]\n " +
                            "Estado : " + p.getChrFlgChequeo());
                }

            }
            adapter.notifyDataSetChanged();
            seleccionSpinner(false);
        }

    }

    private void seleccionSpinner(boolean estado){
        for(int i = 0; i < listaStringPaquetes.size(); i++){

            listViewPaquetes.setItemChecked(i,estado);

        }
    }

    private class ecs_registroTransferencia extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {

            return ws.ecs_RegistroTransferencia(strings);

        }
    }

    private class ecs_obtenerRelacionPaquete extends AsyncTask<String,Void, ArrayList<PaqueteCarro>>{

        @Override
        protected ArrayList<PaqueteCarro> doInBackground(String... strings) {
            return ws.ecs_ObtenerRelacionPaquete(strings);
        }
    }

}