package com.example.proyecto_tdp.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AvisoDialog extends AppCompatDialogFragment {

    protected String titulo;
    protected String mensaje;

    public AvisoDialog(String titulo, String mensaje){
        this.titulo = titulo;
        this.mensaje = mensaje;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
    }
}
