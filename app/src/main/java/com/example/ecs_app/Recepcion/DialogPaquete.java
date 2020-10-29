package com.example.ecs_app.Recepcion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaDataSource;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.ecs_app.R;

import org.ksoap2.serialization.SoapObject;

public class DialogPaquete extends AppCompatDialogFragment {

    private EditText lote,codigoPaquete,pesoNeto;
    private PaqueteDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_ingresopaquete,null);

        builder.setView(view)
                .setTitle("Ingreso Paquete")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Grabar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String salidaLote = lote.getText().toString();
                        String salidaCodigo = codigoPaquete.getText().toString();
                        String salidaPesoN = pesoNeto.getText().toString();

                        if(!salidaLote.equalsIgnoreCase("")
                                && !salidaCodigo.equalsIgnoreCase("")
                                && !salidaPesoN.equalsIgnoreCase("")){
                            listener.applyTexts(salidaLote,salidaCodigo,salidaPesoN);
                        }

                    }
        });

        lote = view.findViewById(R.id.lay_editText_lotePaquete);
        codigoPaquete = view.findViewById(R.id.lay_editText_codigoPa);
        pesoNeto = view.findViewById(R.id.lay_editText_pesoNeto);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (PaqueteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "debe implementar el listener");
        }
    }

    public interface PaqueteDialogListener{

        void applyTexts(String lote, String codigoPaquete, String pesoNeto);

    }


}
