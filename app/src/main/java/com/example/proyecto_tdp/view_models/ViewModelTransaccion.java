package com.example.proyecto_tdp.view_models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.base_de_datos.repositorios.RepositorioTransacciones;
import java.util.List;

public class ViewModelTransaccion extends AndroidViewModel {

    private LiveData<List<Transaccion>> transacciones;
    private RepositorioTransacciones repositorioTransacciones;

    public ViewModelTransaccion(@NonNull Application application) {
        super(application);
        repositorioTransacciones = new RepositorioTransacciones(application);
        transacciones = repositorioTransacciones.getTransacciones();
    }

    public LiveData<List<Transaccion>> getAllTransacciones(){
        return transacciones;
    }

    public void insertarTransaccion(Transaccion transaccion){
        repositorioTransacciones.insertarTransaccion(transaccion);
    }

    public void actualizarTransaccion(Transaccion transaccion){
        repositorioTransacciones.actualizarTransaccion(transaccion);
    }

    public void eliminarTransaccion(Transaccion transaccion){
        repositorioTransacciones.eliminarTransaccion(transaccion);
    }
}
