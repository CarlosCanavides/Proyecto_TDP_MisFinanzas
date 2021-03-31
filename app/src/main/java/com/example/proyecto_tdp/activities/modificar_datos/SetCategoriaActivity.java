package com.example.proyecto_tdp.activities.modificar_datos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.activities.agregar_datos.NuevaCategoriaActivity;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import java.util.ArrayList;
import java.util.List;

public class SetCategoriaActivity extends NuevaCategoriaActivity {

    protected String idCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void inicializarValoresCampos() {
        Intent intent = getIntent();
        idCategoria = intent.getStringExtra(Constantes.CAMPO_ID);
        String tipo = intent.getStringExtra(Constantes.CAMPO_TIPO_CATEGORIA);
        String nombre = intent.getStringExtra(Constantes.CAMPO_NOMBRE_CATEGORIA);
        String idSuperior = intent.getStringExtra(Constantes.CAMPO_ID_CATEGORIA_SUPERIOR);
        String nombreCategoriaSuperior = intent.getStringExtra(Constantes.CAMPO_NOMBRE_CATEGORIA_SUPERIOR);
        int color = intent.getIntExtra(Constantes.CAMPO_COLOR_CATEGORIA,Constantes.COLOR_CATEGORIA_POR_DEFECTO);
        if(idSuperior==null) {
            campoCategoriaSup.setText(Constantes.SELECCIONAR_CATEGORIA);
        }
        else {
            idCategoriaSuperior = idSuperior;
            campoCategoriaSup.setText(nombreCategoriaSuperior);
        }
        colorActual = color;
        campoColor.setText(color+"");
        campoNombre.setText(nombre);
        if(tipo!=null) {
            if (tipo.equals(Constantes.GASTO)) {
                btnGasto.setChecked(true);
            } else {
                btnIngreso.setChecked(true);
            }
        }
        if(nombre!=null){
            if(nombre.length()!=0) {
                iconoCategoriaVP.setText(nombre.charAt(0)+"");
            }
            nombreCategoriaVP.setText(nombre);
        }
        btnCancelar.setText("Eliminar");
    }

    @Override
    protected void inicializarViewModel() {
        categoriasSuperiores = new ArrayList<>();
        viewModelCategoria =  new ViewModelProvider(this).get(ViewModelCategoria.class);
        viewModelCategoria.getAllCategorias().observe(this,new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> todasLasCategorias) {
                categoriasSuperiores.clear();
                if(todasLasCategorias!=null){
                    for(Categoria categoria : todasLasCategorias) {
                        if(categoria.getCategoriaSuperior()==null && !categoria.getId().equals(idCategoria)) {
                            categoriasSuperiores.add(categoria);
                        } else if(categoria.getCategoriaSuperior()!=null && categoria.getCategoriaSuperior().equals(idCategoria)){
                            desabilitarSeleccionarCategoriaSuperior();
                        }
                    }
                    seleccionCategoriaDialog.setCategoriasSuperiores(categoriasSuperiores);
                }
            }
        });
    }

    @Override
    protected void listenerBotonesPrincipales() {
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarDatosPrincipales()){
                    Intent intent = new Intent();
                    intent.putExtra(Constantes.CAMPO_COLOR_CATEGORIA,colorActual);
                    intent.putExtra(Constantes.CAMPO_NOMBRE_CATEGORIA,campoNombre.getText().toString());
                    String tipo;
                    if(btnGasto.isChecked()){
                        tipo = Constantes.GASTO;
                    }
                    else {
                        tipo = Constantes.INGRESO;
                    }
                    intent.putExtra(Constantes.CAMPO_TIPO_CATEGORIA,tipo);
                    if(idCategoriaSuperior!=null){
                        intent.putExtra(Constantes.CAMPO_ID_CATEGORIA_SUPERIOR,idCategoriaSuperior);
                    }
                    intent.putExtra(Constantes.CAMPO_ID,idCategoria);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });
    }

    protected void desabilitarSeleccionarCategoriaSuperior(){
        campoCategoriaSup.setEnabled(false);
        campoCategoriaSup.setVisibility(View.INVISIBLE);
    }
}
