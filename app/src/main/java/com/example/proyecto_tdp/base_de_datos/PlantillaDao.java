package com.example.proyecto_tdp.base_de_datos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import java.util.List;

@Dao
public interface PlantillaDao {

    @Query("SELECT * FROM plantilla")
    List<Plantilla> getAllPlantillas();

    @Query("SELECT * FROM plantilla ORDER BY titulo ASC")
    LiveData<List<Plantilla>> getAllLivePlantillas();

    @Query("SELECT * FROM plantilla WHERE titulo LIKE :titulo LIMIT 1")
    Plantilla findByName(String titulo);

    @Update
    void upDatePlantilla(Plantilla... plantillas);

    @Insert
    void insertPlantilla(Plantilla... plantillas);

    @Delete
    void deletePlantilla(Plantilla plantilla);
}
