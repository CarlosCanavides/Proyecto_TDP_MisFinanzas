package com.example.proyecto_tdp.view_models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import com.example.proyecto_tdp.base_de_datos.repositorios.RepositorioTransaccionesFijas;
import java.util.List;

public class ViewModelTransaccionFija extends AndroidViewModel {

    private LiveData<List<TransaccionFija>> transaccionesFijas;
    private RepositorioTransaccionesFijas repositorioTransaccionesFijas;

    public ViewModelTransaccionFija(@NonNull Application application) {
        super(application);
        repositorioTransaccionesFijas = new RepositorioTransaccionesFijas(application);
        transaccionesFijas = repositorioTransaccionesFijas.getTransaccionesFijas();
    }

    public LiveData<List<TransaccionFija>> getAllTransaccionesFijas(){
        return transaccionesFijas;
    }

    public List<TransaccionFija> getAllTransaccionesFijasPendientes(){
        return repositorioTransaccionesFijas.getAllTransaccionesFijasPendientes();
    }

    public void removeObserver(Observer<List<TransaccionFija>> observer){
        transaccionesFijas.removeObserver(observer);
    }

    public List<TransaccionFija> getTransaccionesFijasDesdeHasta(String desde, String hasta){
        return repositorioTransaccionesFijas.getTransaccionesFijasDesdeHasta(desde,hasta);
    }

    public LiveData<List<TransaccionFija>> getLiveTransaccionesFijasDesdeHasta(String desde, String hasta){
        return repositorioTransaccionesFijas.getLiveTransaccionesDesdeHasta(desde,hasta);
    }

    public void insertarTransaccionFija(TransaccionFija transaccion){
        repositorioTransaccionesFijas.insertarTransaccionFija(transaccion);
    }

    public void actualizarTransaccionFija(TransaccionFija transaccion){
        repositorioTransaccionesFijas.actualizarTransaccionFija(transaccion);
    }

    public void eliminarTransaccionFija(TransaccionFija transaccion){
        repositorioTransaccionesFijas.eliminarTransaccionFija(transaccion);
    }

    public void eliminarTransaccionFija(String id){
        repositorioTransaccionesFijas.eliminarTransaccionFijaPorID(id);
    }
}
