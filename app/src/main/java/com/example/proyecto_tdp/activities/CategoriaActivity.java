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
    private ExpandableListView.OnChildClickListener childClickListener;
    private FloatingActionButton btnAgregarCategoria;
    private ViewModelCategoria viewModelCategoria;
    private ViewModelSubcategoria viewModelSubcategoria;
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
                Subcategoria subcategoria = mapSubcategorias.get(categorias.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(CategoriaActivity.this, NuevaCategoriaActivity.class);
                intent.putExtra("nombre_subcategoria", subcategoria.getNombreSubcategoria());
                intent.putExtra("categoria_superior", subcategoria.getCategoriaSuperior());
                intent.putExtra("color_subcategoria", subcategoria.getColorSubcategoria()+"");
                intent.putExtra("tipo_subcategoria", subcategoria.getTipoSubcategoria());
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
                categorias.addAll(c);
                mapSubcategorias.clear();
                for(Categoria categoria : categorias){
                    List<Subcategoria> sc = new ArrayList<>();
                    sc.add(new Subcategoria(categoria.getNombreCategoria()+" General","",categoria.getColorCategoria(),categoria.getTipoCategoria()));
                    sc.addAll(viewModelSubcategoria.getSubcategoriasHijas(categoria.getNombreCategoria()));
                    mapSubcategorias.put(categoria,sc);
                }
                adapterCategorias.notifyDataSetChanged();
            }
        });
        viewModelSubcategoria = ViewModelProviders.of(this).get(ViewModelSubcategoria.class);
        viewModelSubcategoria.getAllSubcategorias().observe(this, new Observer<List<Subcategoria>>() {
            @Override
            public void onChanged(List<Subcategoria> subC) {
                mapSubcategorias.clear();
                for(Categoria categoria : categorias){
                    List<Subcategoria> sc = new ArrayList<>();
                    sc.add(new Subcategoria(categoria.getNombreCategoria()+" General","",categoria.getColorCategoria(),categoria.getTipoCategoria()));
                    sc.addAll(viewModelSubcategoria.getSubcategoriasHijas(categoria.getNombreCategoria()));
                    mapSubcategorias.put(categoria,sc);
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
                Subcategoria subcategoria = mapSubcategorias.get(categorias.get(groupPosition)).get(childPosition);
                intent.putExtra("id_categoria_elegida", subcategoria.getNombreSubcategoria());
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
                    if(categoriaSuperior==null || categoriaSuperior.equals("Seleccionar categoria superior")) {
                        Categoria categoria = new Categoria(nombreCategoria, colorCategoria, tipoC);
                        viewModelCategoria.insertarCategoria(categoria);
                    }
                    else {
                        Subcategoria subcategoria = new Subcategoria(nombreCategoria,categoriaSuperior,colorCategoria,tipoC);
                        viewModelSubcategoria.insertarSubcategoria(subcategoria);
                    }
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
                    if(categoriaSuperior==null || categoriaSuperior.equals("") || categoriaSuperior.equals("Categoría superior")){
                        Categoria categoria = new Categoria(idCategoria, colorCategoria, tipoC);
                        viewModelCategoria.actualizarCategoria(categoria);
                        categoria.setNombreCategoria(nombreCategoria);
                        viewModelCategoria.actualizarCategoria(categoria);
                    }
                    else {
                        Subcategoria subcategoria = new Subcategoria(idCategoria,categoriaSuperior,colorCategoria,tipoC);
                        viewModelSubcategoria.actualizarSubcategoria(subcategoria);
                        subcategoria.setNombreSubcategoria(nombreCategoria);
                        viewModelSubcategoria.actualizarSubcategoria(subcategoria);
                    }
                }
            }
        }
    }
}
