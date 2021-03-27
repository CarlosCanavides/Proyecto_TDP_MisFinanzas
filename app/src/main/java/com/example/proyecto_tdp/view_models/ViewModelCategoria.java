package com.example.proyecto_tdp.view_models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.repositorios.RepositorioCategorias;
import java.util.List;

public class ViewModelCategoria extends AndroidViewModel {

    private LiveData<List<Categoria>> categorias;
    private RepositorioCategorias repositorioCategorias;

    public ViewModelCategoria(@NonNull Application application) {
        super(application);
        repositorioCategorias = new RepositorioCategorias(application);
        categorias = repositorioCategorias.getCategorias();
    }

    public LiveData<List<Categoria>> getAllCategorias(){
        return categorias;
    }

    public void eliminarObservador(Observer<List<Categoria>> observador){
        categorias.removeObserver(observador);
    }

    public List<Categoria> getSubcategorias(String categoriaSuperior){
        List<Categoria> subcategorias = repositorioCategorias.getSubcategorias(categoriaSuperior);
        return subcategorias;
    }

    public Categoria getCategoriaPorNombre(String categoria){
        return repositorioCategorias.getCategoria(categoria);
    }

    public void insertarCategoria(Categoria categoria){
        repositorioCategorias.insertarCategoria(categoria);
    }

    public void actualizarCategoria(Categoria categoria){
        repositorioCategorias.actualizarCategoria(categoria);
    }

    public void eliminarCategoria(Categoria categoria){
        repositorioCategorias.eliminarCategoria(categoria);
    }
}
