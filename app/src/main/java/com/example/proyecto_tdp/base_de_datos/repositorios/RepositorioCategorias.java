package com.example.proyecto_tdp.base_de_datos.repositorios;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.example.proyecto_tdp.base_de_datos.AppDataBase;
import com.example.proyecto_tdp.base_de_datos.CategoriaDao;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public List<Categoria> getSubcategorias(String categoriaSuperior){
        try {
            return new RepositorioCategorias.ObtenerSubcategoriasAsyncTask(categoriaDao).execute(categoriaSuperior).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Categoria getCategoria(String categoria){
        try {
            return new RepositorioCategorias.ObtenerCategoriaAsyncTask(categoriaDao).execute(categoria).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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

    public void eliminarCategoria(String idCategoria){
        new EliminarCategoriaIDAsyncTask(categoriaDao).execute(idCategoria);
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

    public static class EliminarCategoriaIDAsyncTask extends AsyncTask<String,Void,Void> {
        private CategoriaDao categoriaDao;

        private EliminarCategoriaIDAsyncTask(CategoriaDao categoriaDao){
            this.categoriaDao = categoriaDao;
        }

        @Override
        protected Void doInBackground(String... idCategoria) {
            categoriaDao.eliminarCategoria(idCategoria[0]);
            return null;
        }
    }

    public static class ObtenerSubcategoriasAsyncTask extends AsyncTask<String,Void,List<Categoria>> {
        private CategoriaDao categoriaDao;

        private ObtenerSubcategoriasAsyncTask(CategoriaDao categoriaDao){
            this.categoriaDao = categoriaDao;
        }

        @Override
        protected List<Categoria> doInBackground(String... categoriaSuperior) {
            return categoriaDao.getSubcategorias(categoriaSuperior[0]);
        }
    }

    public static class ObtenerCategoriaAsyncTask extends AsyncTask<String,Void,Categoria> {
        private CategoriaDao categoriaDao;

        private ObtenerCategoriaAsyncTask(CategoriaDao categoriaDao){
            this.categoriaDao = categoriaDao;
        }

        @Override
        protected Categoria doInBackground(String... categoria) {
            return categoriaDao.getCategoria(categoria[0]);
        }
    }

}
