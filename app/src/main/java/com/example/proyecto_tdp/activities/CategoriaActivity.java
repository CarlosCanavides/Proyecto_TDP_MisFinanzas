package com.example.proyecto_tdp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.agregar_datos.NuevaCategoriaActivity;
import com.example.proyecto_tdp.activities.modificar_datos.SetCategoriaActivity;
import com.example.proyecto_tdp.adapters.AdapterCategorias;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaDeVerificacion;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaSoloCategorias;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriaActivity extends AppCompatActivity {

    protected List<Categoria> categorias;
    protected Map<Categoria,List<Categoria>> mapSubcategorias;
    protected AdapterCategorias adapterCategorias;
    protected ViewModelCategoria viewModelCategoria;
    protected ExpandableListView expandableLV;
    protected FloatingActionButton btnAgregarCategoria;
    protected ExpandableListView.OnChildClickListener childClickListener;

    protected EstrategiaDeVerificacion estrategiaDeVerificacion;

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
        setListenerSeleccionarSubcategoria();
        expandableLV.setOnChildClickListener(childClickListener);
    }

    protected void inicializarViewModels(){
        viewModelCategoria =  ViewModelProviders.of(this).get(ViewModelCategoria.class);
        viewModelCategoria.getAllCategorias().observe(this, new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> c) {
                categorias.clear();
                mapSubcategorias.clear();
                for(Categoria categoria : c){
                    if(categoria.getCategoriaSuperior()==null) {
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
        estrategiaDeVerificacion = new EstrategiaSoloCategorias(viewModelCategoria);
    }

    protected void inicializarListViewCategorias(){
        categorias = new ArrayList<>();
        mapSubcategorias = new HashMap<>();
        adapterCategorias = new AdapterCategorias(categorias, mapSubcategorias);
        expandableLV.setAdapter(adapterCategorias);
    }

    protected void inicializarBotonPrincipal(){
        btnAgregarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriaActivity.this, NuevaCategoriaActivity.class);
                startActivityForResult(intent, Constantes.PEDIDO_NUEVA_CATEGORIA);
            }
        });
    }

    protected void setListenerSeleccionarSubcategoria(){
        childClickListener = new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Categoria categoriaSeleccionada = mapSubcategorias.get(categorias.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(CategoriaActivity.this, SetCategoriaActivity.class);
                intent.putExtra(Constantes.CAMPO_ID, categoriaSeleccionada.getNombreCategoria());
                intent.putExtra(Constantes.CAMPO_TIPO_CATEGORIA, categoriaSeleccionada.getTipoCategoria());
                intent.putExtra(Constantes.CAMPO_NOMBRE_CATEGORIA, categoriaSeleccionada.getNombreCategoria());
                intent.putExtra(Constantes.CAMPO_CATEGORIA_SUPERIOR, categoriaSeleccionada.getCategoriaSuperior());
                intent.putExtra(Constantes.CAMPO_COLOR_CATEGORIA, categoriaSeleccionada.getColorCategoria());
                startActivityForResult(intent, Constantes.PEDIDO_SET_CATEGORIA);
                return true;
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constantes.PEDIDO_NUEVA_CATEGORIA || requestCode==Constantes.PEDIDO_SET_CATEGORIA){
            estrategiaDeVerificacion.verificar(requestCode,resultCode,data);
        }
    }
}
