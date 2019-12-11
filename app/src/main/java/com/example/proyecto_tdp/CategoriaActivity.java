package com.example.proyecto_tdp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.codigo.AdapterCategorias;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoriaActivity extends AppCompatActivity {

    private RecyclerView recyclerCategorias;
    private ArrayList<Categoria> categorias;
    private AdapterCategorias adapterCategorias;

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
                Intent intent = new Intent(CategoriaActivity.this,NuevaCategoriaActivity.class);
                startActivityForResult(intent,NRO_PEDIDO);
            }
        });

        categorias = new ArrayList<>();
        adapterCategorias = new AdapterCategorias(categorias);
        recyclerCategorias = findViewById(R.id.recyclerCategorias);
        recyclerCategorias.setLayoutManager(new GridLayoutManager(this,1));
        recyclerCategorias.setHasFixedSize(true);
        recyclerCategorias.setAdapter(adapterCategorias);

        viewModelCategoria = ViewModelProviders.of(this).get(ViewModelCategoria.class);
        viewModelCategoria.getAllCategorias().observe(this, new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> c) {
                categorias.clear();
                categorias.addAll(c);
                adapterCategorias.notifyDataSetChanged();
            }
        });

        adapterCategorias.setOnItemClickListener(new AdapterCategorias.OnItemClickListener() {
            @Override
            public void onItemClik(Categoria categoria) {
                Intent intent = new Intent();
                intent.putExtra("id_categoria_elegida",categoria.getNombreCategoria());
                setResult(RESULT_OK, intent);
                finish();
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

                if(nombreCategoria!="") {
                    Categoria categoria = new Categoria(nombreCategoria, categoriaSuperior, color, tipoC);
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

}
