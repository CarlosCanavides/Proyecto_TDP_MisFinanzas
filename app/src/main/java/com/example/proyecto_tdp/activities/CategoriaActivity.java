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
import com.example.proyecto_tdp.adapters.AdapterListCategorias;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriaActivity extends AppCompatActivity {

    private ExpandableListView expLV;
    private AdapterListCategorias adapterCategorias;
    private List<String> categorias;
    private Map<String, List<String>> mapSubcategorias;

    private FloatingActionButton btnAgregarCategoria;
    private static final int NRO_PEDIDO = 1826;

    private ViewModelCategoria viewModelCategoria;

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

        cargar();
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

                if(nombreCategoria!="") {
                    Categoria categoria = new Categoria(nombreCategoria, color, tipoC);
                    viewModelCategoria.insertarCategoria(categoria);
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

    private void cargar() {
        expLV = findViewById(R.id.expLV);
        categorias = new ArrayList<>();
        mapSubcategorias = new HashMap<>();

        viewModelCategoria = ViewModelProviders.of(this).get(ViewModelCategoria.class);
        viewModelCategoria.getAllCategorias().observe(this, new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> c) {
                categorias.clear();
                /*listaCategorias.addAll(c);
                adapterCategorias.notifyDataSetChanged();*/
            }
        });
    }

}
