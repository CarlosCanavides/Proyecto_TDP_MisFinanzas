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

    public List<Categoria> getSubcategorias(String idCategoriaSuperior){
        try {
            return new RepositorioCategorias.ObtenerSubcategoriasAsyncTask(categoriaDao).execute(idCategoriaSuperior).get();
        }catch(ExecutionException|InterruptedException e){e.printStackTrace();}
        return null;
    }

    public Categoria getCategoriaPorID(String id){
        try {
            return new RepositorioCategorias.ObtenerCategoriaPorIDAsyncTask(categoriaDao).execute(id).get();
        }catch (ExecutionException|InterruptedException e){e.printStackTrace();}
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

    public void eliminarCategoriaPorID(String idCategoria){
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
        protected List<Categoria> doInBackground(String... idCategoriaSuperior) {
            return categoriaDao.getSubcategorias(idCategoriaSuperior[0]);
        }
    }

    public static class ObtenerCategoriaPorIDAsyncTask extends AsyncTask<String,Void,Categoria> {
        private CategoriaDao categoriaDao;

        private ObtenerCategoriaPorIDAsyncTask(CategoriaDao categoriaDao){
            this.categoriaDao = categoriaDao;
        }

        @Override
        protected Categoria doInBackground(String... id) {
            return categoriaDao.getCategoria(id[0]);
        }
    }
}
