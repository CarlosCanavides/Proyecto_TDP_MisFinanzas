package com.example.proyecto_tdp.base_de_datos.repositorios;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.example.proyecto_tdp.base_de_datos.AppDataBase;
import com.example.proyecto_tdp.base_de_datos.PlantillaDao;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import java.util.List;

public class RepositorioPlantillas {

    private PlantillaDao plantillaDao;
    private LiveData<List<Plantilla>> plantillas;

    public RepositorioPlantillas(Application application) {
        AppDataBase dataBase = Room.databaseBuilder(application, AppDataBase.class, "database_app").build();
        plantillaDao = dataBase.plantillaDao();
        plantillas = plantillaDao.getAllLivePlantillas();
    }

    public LiveData<List<Plantilla>> getPlantillas(){
        return plantillas;
    }

    public void insertarPlantilla(Plantilla plantilla){
        new RepositorioPlantillas.InsertPlantillaAsyncTask(plantillaDao).execute(plantilla);
    }

    public void actualizarPlantilla(Plantilla plantilla){
        new RepositorioPlantillas.ActualizarPlantillaAsyncTask(plantillaDao).execute(plantilla);
    }

    public void eliminarPlantilla(Plantilla plantilla){
        new RepositorioPlantillas.EliminarPlantillaAsyncTask(plantillaDao).execute(plantilla);
    }

    public static class InsertPlantillaAsyncTask extends AsyncTask<Plantilla,Void,Void> {
        private PlantillaDao plantillaDao;

        private InsertPlantillaAsyncTask(PlantillaDao plantillaDaoDao){
            this.plantillaDao = plantillaDaoDao;
        }

        @Override
        protected Void doInBackground(Plantilla... plantillas) {
            plantillaDao.insertPlantilla(plantillas[0]);
            return null;
        }
    }

    public static class ActualizarPlantillaAsyncTask extends AsyncTask<Plantilla,Void,Void> {
        private PlantillaDao plantillaDao;

        private ActualizarPlantillaAsyncTask(PlantillaDao plantillaDao){
            this.plantillaDao = plantillaDao;
        }

        @Override
        protected Void doInBackground(Plantilla... plantillas) {
            plantillaDao.upDatePlantilla(plantillas[0]);
            return null;
        }
    }

    public static class EliminarPlantillaAsyncTask extends AsyncTask<Plantilla,Void,Void> {
        private PlantillaDao plantillaDao;

        private EliminarPlantillaAsyncTask(PlantillaDao plantillaDao){
            this.plantillaDao = plantillaDao;
        }

        @Override
        protected Void doInBackground(Plantilla... plantillas) {
            plantillaDao.deletePlantilla(plantillas[0]);
            return null;
        }
    }
}
