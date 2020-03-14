package com.example.proyecto_tdp.base_de_datos.repositorios;

import android.app.Application;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.example.proyecto_tdp.base_de_datos.AppDataBase;
import com.example.proyecto_tdp.base_de_datos.CategoriaDao;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import java.util.List;

public class RepositorioCategorias {

    private CategoriaDao categoriaDao;
    private LiveData<List<Categoria>> categorias;

    public RepositorioCategorias(Application application) {
        AppDataBase dataBase = Room.databaseBuilder(application, AppDataBase.class, "database_app").build();
        categoriaDao = dataBase.categoriaDao();
        categorias = categoriaDao.getAllLiveCategorias();
    }

    public LiveData<List<Categoria>> getCategorias(){
        return categorias;
    }

    public void insertarCategoria(Categoria categoria){
        new InsertCategoriaAsyncTask(categoriaDao).execute(categoria);
    }

    public void actualizarCategoria(Categoria categoria){
        new ActualizarCategoriaAsyncTask(categoriaDao).execute(categoria);
    }

    public void eliminarCategoria(Categoria categoria){
        new EliminarCategoriaAsyncTask(categoriaDao).execute(categoria);
    }


    public static class InsertCategoriaAsyncTask extends AsyncTask<Categoria,Void,Void> {
        private CategoriaDao categoriaDao;

        private InsertCategoriaAsyncTask(CategoriaDao categoriaDao){
            this.categoriaDao = categoriaDao;
        }

        @Override
        protected Void doInBackground(Categoria... categoria) {
           categoriaDao.insertCategoria(categoria[0]);
            return null;
        }
    }

    public static class ActualizarCategoriaAsyncTask extends AsyncTask<Categoria,Void,Void> {
        private CategoriaDao categoriaDao;

        private ActualizarCategoriaAsyncTask(CategoriaDao categoriaDao){
            this.categoriaDao = categoriaDao;
        }

        @Override
        protected Void doInBackground(Categoria... categoria) {
            categoriaDao.upDateCategoria(categoria[0]);
            return null;
        }
    }

    public static class EliminarCategoriaAsyncTask extends AsyncTask<Categoria,Void,Void> {
        private CategoriaDao categoriaDao;

        private EliminarCategoriaAsyncTask(CategoriaDao categoriaDao){
            this.categoriaDao = categoriaDao;
        }

        @Override
        protected Void doInBackground(Categoria... categoria) {
            categoriaDao.deleteCategoria(categoria[0]);
            return null;
        }
    }

    public static class InsertarCategoriasPorDefectoAsyncTask extends AsyncTask<Void,Void,Void> {
        private CategoriaDao categoriaDao;

        private InsertarCategoriasPorDefectoAsyncTask(CategoriaDao categoriaDao){
            this.categoriaDao = categoriaDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Categoria c1 = new Categoria("Comida",Color.parseColor("#f44336"),"Gasto");
            Categoria c2 = new Categoria("Entretenimiento",Color.parseColor("#f44336"),"Gasto");
            Categoria c3 = new Categoria("Transporte",Color.parseColor("#f44336"),"Gasto");
            Categoria c4 = new Categoria("Ropa",Color.parseColor("#f44336"),"Gasto");
            Categoria c5 = new Categoria("Casa",Color.parseColor("#f44336"),"Gasto");
            Categoria c6 = new Categoria("Salud y belleza",Color.parseColor("#f44336"),"Gasto");
            Categoria c7 = new Categoria("Electrónica",Color.parseColor("#f44336"),"Gasto");
            Categoria c8 = new Categoria("Trabajo",Color.parseColor("#f44336"),"Gasto");
            Categoria c9 = new Categoria("Niños",Color.parseColor("#f44336"),"Gasto");
            Categoria c10 = new Categoria("Servicios",Color.parseColor("#f44336"),"Gasto");
            Categoria c11 = new Categoria("Vacaciones",Color.parseColor("#f44336"),"Gasto");
            categoriaDao.insertCategoria(c1);
            categoriaDao.insertCategoria(c2);
            categoriaDao.insertCategoria(c3);
            categoriaDao.insertCategoria(c4);
            categoriaDao.insertCategoria(c5);
            categoriaDao.insertCategoria(c6);
            categoriaDao.insertCategoria(c7);
            categoriaDao.insertCategoria(c8);
            categoriaDao.insertCategoria(c9);
            categoriaDao.insertCategoria(c10);
            categoriaDao.insertCategoria(c11);
            return null;
        }
    }
}
