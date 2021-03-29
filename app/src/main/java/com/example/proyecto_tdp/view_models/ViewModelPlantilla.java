package com.example.proyecto_tdp.view_models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import com.example.proyecto_tdp.base_de_datos.repositorios.RepositorioPlantillas;
import java.util.List;

public class ViewModelPlantilla extends AndroidViewModel {

    private LiveData<List<Plantilla>> plantillas;
    private RepositorioPlantillas repositorioPlantillas;

    public ViewModelPlantilla(@NonNull Application application) {
        super(application);
        repositorioPlantillas = new RepositorioPlantillas(application);
        plantillas = repositorioPlantillas.getPlantillas();
    }

    public LiveData<List<Plantilla>> getAllPlantillas(){
        return plantillas;
    }

    public void removeOberver(Observer<List<Plantilla>> observer){
        plantillas.removeObserver(observer);
    }

    public void insertarPlantilla(Plantilla plantilla){
        repositorioPlantillas.insertarPlantilla(plantilla);
    }

    public void actualizarPlantilla(Plantilla plantilla){
        repositorioPlantillas.actualizarPlantilla(plantilla);
    }

    public void eliminarPlantilla(Plantilla plantilla){
        repositorioPlantillas.eliminarPlantilla(plantilla);
    }
}
