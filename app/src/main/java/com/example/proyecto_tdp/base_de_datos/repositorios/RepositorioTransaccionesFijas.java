package com.example.proyecto_tdp.base_de_datos.repositorios;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.example.proyecto_tdp.base_de_datos.AppDataBase;
import com.example.proyecto_tdp.base_de_datos.TransaccionFijaDao;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RepositorioTransaccionesFijas {

    private TransaccionFijaDao transaccionFijaDao;
    private LiveData<List<TransaccionFija>> transaccionesFijas;

    public RepositorioTransaccionesFijas(Application application) {
        AppDataBase dataBase = Room.databaseBuilder(application, AppDataBase.class, "database_app").build();
        transaccionFijaDao = dataBase.transaccionFijaDao();
        transaccionesFijas = transaccionFijaDao.getAllLiveTransaccionesFijas();
    }

    public LiveData<List<TransaccionFija>> getTransaccionesFijas(){
        return transaccionesFijas;
    }

    public List<TransaccionFija> getTransaccionesFijasDesdeHasta(String desde, String hasta){
        try {
            return new RepositorioTransaccionesFijas.ObtenerTransaccionesFijasDesdeHastaAsyncTask(transaccionFijaDao).execute(desde,hasta).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<TransaccionFija>> getLiveTransaccionesDesdeHasta(String desde, String hasta){
        try {
            return new RepositorioTransaccionesFijas.ObtenerLiveTransaccionesFijasDesdeHastaAsyncTask(transaccionFijaDao).execute(desde,hasta).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertarTransaccionFija(TransaccionFija transaccion){
        new RepositorioTransaccionesFijas.InsertTransaccionFijaAsyncTask(transaccionFijaDao).execute(transaccion);
    }

    public void actualizarTransaccionFija(TransaccionFija transaccion){
        new RepositorioTransaccionesFijas.ActualizarTransaccionFijaAsyncTask(transaccionFijaDao).execute(transaccion);
    }

    public void eliminarTransaccionFija(TransaccionFija transaccion){
        new RepositorioTransaccionesFijas.EliminarTransaccionFijaAsyncTask(transaccionFijaDao).execute(transaccion);
    }

    public void eliminarTransaccionFija(int id){
        new RepositorioTransaccionesFijas.EliminarTransaccionFijaIDAsyncTask(transaccionFijaDao).execute(id);
    }

    public static class InsertTransaccionFijaAsyncTask extends AsyncTask<TransaccionFija,Void,Void> {
        private TransaccionFijaDao transaccionFijaDao;

        private InsertTransaccionFijaAsyncTask(TransaccionFijaDao transaccionFijaDao){
            this.transaccionFijaDao = transaccionFijaDao;
        }

        @Override
        protected Void doInBackground(TransaccionFija... transaccions) {
            transaccionFijaDao.insertTransaccionFija(transaccions[0]);
            return null;
        }
    }

    public static class ActualizarTransaccionFijaAsyncTask extends AsyncTask<TransaccionFija,Void,Void> {
        private TransaccionFijaDao transaccionFijaDao;

        private ActualizarTransaccionFijaAsyncTask(TransaccionFijaDao transaccionFijaDao){
            this.transaccionFijaDao = transaccionFijaDao;
        }

        @Override
        protected Void doInBackground(TransaccionFija... transaccions) {
            transaccionFijaDao.upDateTransaccionFija(transaccions[0]);
            return null;
        }
    }

    public static class EliminarTransaccionFijaAsyncTask extends AsyncTask<TransaccionFija,Void,Void> {
        private TransaccionFijaDao transaccionFijaDao;

        private EliminarTransaccionFijaAsyncTask(TransaccionFijaDao transaccionFijaDao){
            this.transaccionFijaDao = transaccionFijaDao;
        }

        @Override
        protected Void doInBackground(TransaccionFija... transaccions) {
            transaccionFijaDao.deleteTransaccionFija(transaccions[0]);
            return null;
        }
    }

    public static class EliminarTransaccionFijaIDAsyncTask extends AsyncTask<Integer,Void,Void> {
        private TransaccionFijaDao transaccionFijaDao;

        private EliminarTransaccionFijaIDAsyncTask(TransaccionFijaDao transaccionFijaDao){
            this.transaccionFijaDao = transaccionFijaDao;
        }

        @Override
        protected Void doInBackground(Integer... id) {
            transaccionFijaDao.eliminarTransaccionFija(id[0]);
            return null;
        }
    }

    public static class ObtenerTransaccionesFijasDesdeHastaAsyncTask extends AsyncTask<String,Void,List<TransaccionFija>> {
        private TransaccionFijaDao transaccionFijaDao;

        private ObtenerTransaccionesFijasDesdeHastaAsyncTask(TransaccionFijaDao transaccionFijaDao){
            this.transaccionFijaDao = transaccionFijaDao;
        }

        @Override
        protected List<TransaccionFija> doInBackground(String... periodo) {
            return transaccionFijaDao.getTransaccionesFijasDesdeHasta(periodo[0],periodo[1]);
        }
    }

    public static class ObtenerLiveTransaccionesFijasDesdeHastaAsyncTask extends AsyncTask<String,Void,LiveData<List<TransaccionFija>>> {
        private TransaccionFijaDao transaccionFijaDao;

        private ObtenerLiveTransaccionesFijasDesdeHastaAsyncTask(TransaccionFijaDao transaccionFijaDao){
            this.transaccionFijaDao = transaccionFijaDao;
        }

        @Override
        protected LiveData<List<TransaccionFija>> doInBackground(String... periodo) {
            return transaccionFijaDao.getLiveTransaccionesFijasDesdeHasta(periodo[0],periodo[1]);
        }
    }
}
