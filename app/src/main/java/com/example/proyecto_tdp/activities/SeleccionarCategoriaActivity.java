package com.example.proyecto_tdp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;

public class SeleccionarCategoriaActivity extends CategoriaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListenerSeleccionarSubcategoria() {
        childClickListener = new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent();
                Categoria categoria = mapSubcategorias.get(categorias.get(groupPosition)).get(childPosition);
                intent.putExtra("id_categoria_elegida", categoria.getNombreCategoria());
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
        };
    }
}
