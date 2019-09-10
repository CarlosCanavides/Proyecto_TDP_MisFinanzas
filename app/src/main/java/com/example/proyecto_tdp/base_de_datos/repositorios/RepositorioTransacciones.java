package com.example.proyecto_tdp.base_de_datos.repositorios;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.proyecto_tdp.base_de_datos.AppDataBase;
import com.example.proyecto_tdp.base_de_datos.TransaccionDao;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import java.util.List;

public class RepositorioTransacciones {

    private TransaccionDao transaccionDao;
    private LiveData<List<Transaccion>> transacciones;

    public RepositorioTransacciones(Application application) {
        AppDataBase dataBase = Room.databaseBuilder(application, AppDataBase.class, "database_app").build();
        transaccionDao = dataBase.transaccionDao();
        transacciones = transaccionDao.getAllLiveTransacciones();
    }

    public LiveData<List<Transaccion>> getTransacciones(){
        return transacciones;
    }

    public void insertarTransaccion(Transaccion transaccion){
        new InsertTransaccionAsyncTask(transaccionDao).execute(transaccion);
    }

    public void actualizarTransaccion(Transaccion transaccion){
        new ActualizarTransaccionAsyncTask(transaccionDao).execute(transaccion);
    }

    public void eliminarTransaccion(Transaccion transaccion){
        new EliminarTransaccionAsyncTask(transaccionDao).execute(transaccion);
    }

    public static class InsertTransaccionAsyncTask extends AsyncTask<Transaccion,Void,Void> {
        private TransaccionDao transaccionDao;

        private InsertTransaccionAsyncTask(TransaccionDao transaccionDao){
            this.transaccionDao = transaccionDao;
        }

        @Override
        protected Void doInBackground(Transaccion... transaccions) {
            transaccionDao.insertTransaccion(transaccions[0]);
            return null;
        }

    }

    public static class ActualizarTransaccionAsyncTask extends AsyncTask<Transaccion,Void,Void> {
        private TransaccionDao transaccionDao;

        private ActualizarTransaccionAsyncTask(TransaccionDao transaccionDao){
            this.transaccionDao = transaccionDao;
        }

        @Override
        protected Void doInBackground(Transaccion... transaccions) {
            transaccionDao.upDateTransaccion(transaccions[0]);
            return null;
        }

    }

    public static class EliminarTransaccionAsyncTask extends AsyncTask<Transaccion,Void,Void> {
        private TransaccionDao transaccionDao;

        private EliminarTransaccionAsyncTask(TransaccionDao transaccionDao){
            this.transaccionDao = transaccionDao;
        }

        @Override
        protected Void doInBackground(Transaccion... transaccions) {
            transaccionDao.deleteTransaccion(transaccions[0]);
            return null;
        }

    }

}
