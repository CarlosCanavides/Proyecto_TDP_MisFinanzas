package com.example.proyecto_tdp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.agregar_datos.NuevaCategoriaActivity;
import com.example.proyecto_tdp.adapters.AdapterCategorias;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriaActivity extends AppCompatActivity {

    private List<Categoria> categorias;
    private Map<Categoria, List<Categoria>> mapSubcategorias;
    private AdapterCategorias adapterCategorias;
    private ExpandableListView expandableLV;
    private ExpandableListView.OnChildClickListener childClickListener;
    private FloatingActionButton btnAgregarCategoria;
    private ViewModelCategoria viewModelCategoria;
    private static final int PEDIDO_NUEVA_CATEGORIA = 18;
    private static final int PEDIDO_SET_CATEGORIA = 26;
    private static final int COLOR_CATEGORIA_POR_DEFECTO = Color.parseColor("#7373FF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Categorias");
        setContentView(R.layout.activity_categoria);
        expandableLV = findViewById(R.id.expLV);
        btnAgregarCategoria = findViewById(R.id.btnAgregarCategoria);

        inicializarListViewCategorias();
        inicializarViewModels();
        inicializarBotonPrincipal();

        childClickListener = new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Categoria categoriaSeleccionada = mapSubcategorias.get(categorias.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(CategoriaActivity.this, NuevaCategoriaActivity.class);
                intent.putExtra("nombre_categoria", categoriaSeleccionada.getNombreCategoria());
                intent.putExtra("categoria_superior", categoriaSeleccionada.getCategoriaSuperior());
                intent.putExtra("color_categoria", categoriaSeleccionada.getColorCategoria()+"");
                intent.putExtra("tipo_subcategoria", categoriaSeleccionada.getTipoCategoria());
                startActivityForResult(intent, PEDIDO_SET_CATEGORIA);
                return true;
            }
        };
        setListenerSeleccionarSubcategoria();
        expandableLV.setOnChildClickListener(childClickListener);
    }

    private void inicializarViewModels(){
        viewModelCategoria =  ViewModelProviders.of(this).get(ViewModelCategoria.class);
        viewModelCategoria.getAllCategorias().observe(this, new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> c) {
                categorias.clear();
                mapSubcategorias.clear();
                for(Categoria categoria : c){
                    if(categoria.getCategoriaSuperior().equals("NULL")) {
                        categorias.add(categoria);
                        List<Categoria> sc = new ArrayList<>();
                        sc.add(categoria);
                        sc.addAll(viewModelCategoria.getSubcategorias(categoria.getNombreCategoria()));
                        mapSubcategorias.put(categoria, sc);
                    }
                }
                adapterCategorias.notifyDataSetChanged();
            }
        });
    }

    private void inicializarListViewCategorias(){
        categorias = new ArrayList<>();
        mapSubcategorias = new HashMap<>();
        adapterCategorias = new AdapterCategorias(categorias, mapSubcategorias);
        expandableLV.setAdapter(adapterCategorias);
    }

    private void inicializarBotonPrincipal(){
        btnAgregarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriaActivity.this, NuevaCategoriaActivity.class);
                startActivityForResult(intent, PEDIDO_NUEVA_CATEGORIA);
            }
        });
    }

    private void setListenerSeleccionarSubcategoria(){
        Intent intent = getIntent();
        childClickListener = intent.getParcelableExtra("listener_subcategorias");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PEDIDO_NUEVA_CATEGORIA) {
            if (resultCode == RESULT_OK) {
                String nombreCategoria = data.getStringExtra("nombreCategoria");
                String categoriaSuperior = data.getStringExtra("categoriaSuperior");
                int colorCategoria = data.getIntExtra("colorCategoria", COLOR_CATEGORIA_POR_DEFECTO);
                String tipoC = data.getStringExtra("tipoC");

                if(nombreCategoria!=null) {
                    Categoria categoria;
                    if(categoriaSuperior==null || categoriaSuperior.equals("Seleccionar categoria") || categoriaSuperior.equals("")) {
                        categoria = new Categoria(nombreCategoria, "NULL", colorCategoria, tipoC);
                    }
                    else {
                        categoria = new Categoria(nombreCategoria, categoriaSuperior, colorCategoria, tipoC);
                    }
                    viewModelCategoria.insertarCategoria(categoria);
                }
            }
        }
        else if(requestCode == PEDIDO_SET_CATEGORIA){
            if(resultCode == RESULT_OK){
                String nombreCategoria = data.getStringExtra("nombreCategoria");
                String categoriaSuperior = data.getStringExtra("categoriaSuperior");
                int colorCategoria = data.getIntExtra("colorCategoria",COLOR_CATEGORIA_POR_DEFECTO);
                String tipoC = data.getStringExtra("tipoC");
                String idCategoria = data.getStringExtra("idCategoria");

                if(nombreCategoria!=null) {
                    Categoria categoria;
                    if(categoriaSuperior==null || categoriaSuperior.equals("") || categoriaSuperior.equals("Seleccionar categoria")){
                        categoria = new Categoria(idCategoria, "NULL", colorCategoria, tipoC);
                    }
                    else {
                        categoria = new Categoria(idCategoria,categoriaSuperior,colorCategoria,tipoC);
                    }
                    viewModelCategoria.actualizarCategoria(categoria);
                    categoria.setNombreCategoria(nombreCategoria);
                    viewModelCategoria.actualizarCategoria(categoria);
                }
            }
        }
    }
}
