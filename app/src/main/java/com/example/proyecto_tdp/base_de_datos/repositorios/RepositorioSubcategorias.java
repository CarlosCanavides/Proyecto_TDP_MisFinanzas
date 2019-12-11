package com.example.proyecto_tdp.base_de_datos.repositorios;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.proyecto_tdp.base_de_datos.AppDataBase;
import com.example.proyecto_tdp.base_de_datos.SubcategoriaDao;
import com.example.proyecto_tdp.base_de_datos.entidades.Subcategoria;

import java.util.List;

public class RepositorioSubcategorias {

    private SubcategoriaDao subcategoriaDao;
    private LiveData<List<Subcategoria>> subcategorias;

    public RepositorioSubcategorias(Application application) {
        AppDataBase dataBase = Room.databaseBuilder(application, AppDataBase.class, "database_app").build();
        subcategoriaDao = dataBase.subcategoriaDao();
        subcategorias = subcategoriaDao.getAllLiveSubcategorias();
    }

    public LiveData<List<Subcategoria>> getCategorias(){
        return subcategorias;
    }

    public void insertarCategoria(Subcategoria subcategoria){
        new RepositorioSubcategorias.InsertarSubcategoriaAsyncTask(subcategoriaDao).execute(subcategoria);
    }

    public void actualizarCategoria(Subcategoria subcategoria){
        new RepositorioSubcategorias.ActualizarSubcategoriaAsyncTask(subcategoriaDao).execute(subcategoria);
    }

    public void eliminarCategoria(Subcategoria subcategoria){
        new RepositorioSubcategorias.EliminarSubcategoriaAsyncTask(subcategoriaDao).execute(subcategoria);
    }

    public static class InsertarSubcategoriaAsyncTask extends AsyncTask<Subcategoria,Void,Void> {
        private SubcategoriaDao subcategoriaDao;

        private InsertarSubcategoriaAsyncTask(SubcategoriaDao subcategoriaDao){
            this.subcategoriaDao = subcategoriaDao;
        }

        @Override
        protected Void doInBackground(Subcategoria... subcategoria) {
            subcategoriaDao.insertSubcategoria(subcategoria[0]);
            return null;
        }

    }

    public static class ActualizarSubcategoriaAsyncTask extends AsyncTask<Subcategoria,Void,Void> {
        private SubcategoriaDao subcategoriaDao;

        private ActualizarSubcategoriaAsyncTask(SubcategoriaDao subcategoriaDao){
            this.subcategoriaDao = subcategoriaDao;
        }

        @Override
        protected Void doInBackground(Subcategoria... subcategoria) {
            subcategoriaDao.upDateSubcategoria(subcategoria[0]);
            return null;
        }

    }

    public static class EliminarSubcategoriaAsyncTask extends AsyncTask<Subcategoria,Void,Void> {
        private SubcategoriaDao subcategoriaDao;

        private EliminarSubcategoriaAsyncTask(SubcategoriaDao subcategoriaDao){
            this.subcategoriaDao = subcategoriaDao;
        }

        @Override
        protected Void doInBackground(Subcategoria... subcategoria) {
            subcategoriaDao.deleteSubcategoria(subcategoria[0]);
            return null;
        }

    }

}
