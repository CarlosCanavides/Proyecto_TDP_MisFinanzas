package com.example.proyecto_tdp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterCategorias;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Subcategoria;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.view_models.ViewModelSubcategoria;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriaActivity extends AppCompatActivity {

    private List<Categoria> categorias;
    private Map<Categoria, List<Subcategoria>> mapSubcategorias;
    private AdapterCategorias adapterCategorias;
    private ExpandableListView expandableLV;
    private FloatingActionButton btnAgregarCategoria;

    private ViewModelCategoria viewModelCategoria;
    private ViewModelSubcategoria viewModelSubcategoria;
    private static final int NRO_PEDIDO = 1826;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        setTitle("Categorias");

        btnAgregarCategoria = findViewById(R.id.btnAgregarCategoria);
        btnAgregarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriaActivity.this, NuevaCategoriaActivity.class);
                startActivityForResult(intent,NRO_PEDIDO);
            }
        });

        expandableLV = findViewById(R.id.expLV);
        categorias = new ArrayList<>();
        mapSubcategorias = new HashMap<>();
        adapterCategorias = new AdapterCategorias(categorias, mapSubcategorias);
        expandableLV.setAdapter(adapterCategorias);
        inicializarViewModels();

        expandableLV.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent();
                Subcategoria subcategoria = mapSubcategorias.get(categorias.get(groupPosition)).get(childPosition);
                intent.putExtra("id_categoria_elegida", subcategoria.getNombreSubcategoria());
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NRO_PEDIDO) {
            if (resultCode == RESULT_OK) {
                String nombreCategoria = data.getStringExtra("nombreCategoria");
                String categoriaSuperior = data.getStringExtra("categoriaSuperior");
                String colorCategoria = data.getStringExtra("colorCategoria");
                String tipoC = data.getStringExtra("tipoC");

                int color = Color.parseColor("#7373FF");

                if(nombreCategoria!=null) {
                    if(categoriaSuperior==null || categoriaSuperior.equals("")) {
                        Categoria categoria = new Categoria(nombreCategoria, color, tipoC);
                        viewModelCategoria.insertarCategoria(categoria);
                    }
                    else {
                        Subcategoria subcategoria = new Subcategoria(nombreCategoria,categoriaSuperior,color,tipoC);
                        viewModelSubcategoria.insertarSubcategoria(subcategoria);
                    }
                }
                else {
                    mostrarMensaje("CATEGORIA NO REGISTRADA");
                }
            }
        }
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void inicializarViewModels(){
        viewModelCategoria =  ViewModelProviders.of(this).get(ViewModelCategoria.class);
        viewModelCategoria.getAllCategorias().observe(this, new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> c) {
                categorias.clear();
                categorias.addAll(c);
                adapterCategorias.notifyDataSetChanged();
            }
        });
        viewModelSubcategoria = ViewModelProviders.of(this).get(ViewModelSubcategoria.class);
        viewModelSubcategoria.getAllSubcategorias().observe(this, new Observer<List<Subcategoria>>() {
            @Override
            public void onChanged(List<Subcategoria> subC) {
                for (int i=0; i<subC.size(); i++){
                    Subcategoria nuevaSubcategoria = subC.get(i);
                    Categoria categoriaSuperior = obtenerCategoriaSuperior(nuevaSubcategoria);
                    List<Subcategoria> list = mapSubcategorias.get(categoriaSuperior);
                    if(list!=null){
                        if(!encontre(nuevaSubcategoria,list)) {
                            list.add(nuevaSubcategoria);
                        }
                    }
                    else{
                        list = new ArrayList<>();
                        list.add(nuevaSubcategoria);
                        mapSubcategorias.put(categoriaSuperior, list);
                    }
                }
                adapterCategorias.notifyDataSetChanged();
            }
        });
    }

    private Categoria obtenerCategoriaSuperior(Subcategoria subcategoria){
        Categoria c = null;
        boolean encontre = false;
        for (int i=0; i<categorias.size() && !encontre; i++){
            if(categorias.get(i).getNombreCategoria().equals(subcategoria.getCategoriaSuperior())){
                c = categorias.get(i);
                encontre = true;
            }
        }
        return c;
    }

    private boolean encontre(Subcategoria subcategoria, List<Subcategoria> list){
        boolean e = false;
        for (int i=0; i<list.size() && !e; i++){
            if(list.get(i).getNombreSubcategoria().equals(subcategoria.getNombreSubcategoria())){
                e = true;
            }
        }
        return e;
    }

}
