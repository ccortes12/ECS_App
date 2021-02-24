package com.example.ecs_app.Transferencia;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.ecs_app.AtiApp;
import com.example.ecs_app.Entidades.PaqueteCarro;
import com.example.ecs_app.R;
import com.example.ecs_app.WS_Torpedo;
import com.example.ecs_app.WS_TorpedoImp;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Embarque extends AppCompatActivity {

    private Button button_buscar;
    private EditText codigo;
    private String corRecalada,rutCliente,codPuertoDestino,codMarca;
    private ListView listViewPaquetes;
    private ArrayList<PaqueteCarro> listaPaquetes;
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

        button_buscar = findViewById(R.id.buscarButton);
        codigo = findViewById(R.id.EditTextCodigoB);
        listViewPaquetes = findViewById(R.id.listviewPaquetes);


        rutCliente = "91840000"; //getIntent().getExtras().getString("rutCliente");
        codPuertoDestino = "NOL";  //getIntent().getExtras().getString("codPuertoDestino");
        codMarca = "299";//getIntent().getExtras().getString("codMarca");
        corRecalada = ((AtiApp) Embarque.this.getApplication()).getLastCorrelativo();


        listaStringPaquetes = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(Embarque.this, R.layout.layout_rowlistview,listaStringPaquetes);
        listViewPaquetes.setAdapter(adapter);

        button_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!codigo.getText().toString().equalsIgnoreCase("")){

                    String[] params = {corRecalada, rutCliente, codPuertoDestino, codMarca, codigo.getText().toString()};

                    try {
                        listaPaquetes = new ecs_obtenerRelacionPaquete().execute(params).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(listaPaquetes == null){
                        Toast.makeText(Embarque.this, "ERROR, en la carga", Toast.LENGTH_SHORT).show();
                    }else{
                        for(PaqueteCarro p : listaPaquetes){
                            listaStringPaquetes.add("Lote : " + p.getChrCodigoLote().trim() + " - Paquete: " + p.getChrCodigoPaquete().trim() + " - Peso : " + p.getDecPesoNeto() + " [kg]\n " +
                                    "Estado : " + p.getStrDescEstado());
                        }

                        adapter.notifyDataSetChanged();
                    }

                }else{
                    Toast.makeText(Embarque.this, "ERROR, ingrese un codigo", Toast.LENGTH_SHORT).show();
                }

            }
        });


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