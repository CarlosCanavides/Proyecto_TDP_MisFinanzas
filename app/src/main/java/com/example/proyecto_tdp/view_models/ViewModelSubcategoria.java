package com.example.proyecto_tdp.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyecto_tdp.base_de_datos.entidades.Subcategoria;
import com.example.proyecto_tdp.base_de_datos.repositorios.RepositorioSubcategorias;

import java.util.List;

public class ViewModelSubcategoria extends AndroidViewModel {

    private LiveData<List<Subcategoria>> subcategorias;
    private RepositorioSubcategorias repositorioCategorias;

    public ViewModelSubcategoria(@NonNull Application application) {
        super(application);
        repositorioCategorias = new RepositorioSubcategorias(application);
        subcategorias = repositorioCategorias.getCategorias();
    }

    public LiveData<List<Subcategoria>> getAllSubcategorias(){
        return subcategorias;
    }

    public void insertarSubcategoria(Subcategoria subcategoria){
        repositorioCategorias.insertarCategoria(subcategoria);
    }

    public void actualizarSubcategoria(Subcategoria subcategoria){
        repositorioCategorias.actualizarCategoria(subcategoria);
    }

    public void eliminarSubcategoria(Subcategoria subcategoria){
        repositorioCategorias.eliminarCategoria(subcategoria);
    }

}
