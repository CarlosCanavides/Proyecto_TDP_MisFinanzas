package com.example.proyecto_tdp.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import java.util.List;

public class SeleccionCategoriaDialog extends DialogFragment{

    private String[] categoriasSuperiores;
    private String[] categoriasSuperioresID;
    private String categoriaSeleccionada;
    private String idCategoriaSeleccionada;
    private SeleccionCategoriaListener listener;

    public SeleccionCategoriaDialog(List<Categoria> categorias, SeleccionCategoriaListener listener){
        this.listener = listener;
        categoriasSuperiores = new String[categorias.size()];
        categoriasSuperioresID = new String[categorias.size()];
        for(int i=0; i<categorias.size(); i++){
            Categoria c = categorias.get(i);
            categoriasSuperiores[i] = c.getNombreCategoria();
            categoriasSuperioresID[i] = c.getId();
        }
    }

    public void setCategoriasSuperiores(List<Categoria> categorias){
        categoriasSuperiores = new String[categorias.size()];
        categoriasSuperioresID = new String[categorias.size()];
        for(int i=0; i<categorias.size(); i++){
            Categoria c = categorias.get(i);
            categoriasSuperiores[i] = c.getNombreCategoria();
            categoriasSuperioresID[i] = c.getId();
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Categorias");
        builder.setSingleChoiceItems(categoriasSuperiores, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoriaSeleccionada = categoriasSuperiores[which];
                idCategoriaSeleccionada = categoriasSuperioresID[which];
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onSelectCategoria(idCategoriaSeleccionada,categoriaSeleccionada);
            }
        });
        return builder.create();
    }

    public interface SeleccionCategoriaListener{
        void onSelectCategoria(String idCategoriaSeleccionada, String nombreCategoriaSeleccionada);
    }
}
