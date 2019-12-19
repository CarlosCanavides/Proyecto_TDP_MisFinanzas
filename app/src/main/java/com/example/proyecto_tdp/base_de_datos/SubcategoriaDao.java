package com.example.proyecto_tdp.base_de_datos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.proyecto_tdp.base_de_datos.entidades.Subcategoria;
import java.util.List;

@Dao
public interface SubcategoriaDao {

    @Query("SELECT * FROM subcategoria")
    List<Subcategoria> getAllSubcategorias();

    @Query("SELECT * FROM subcategoria")
    LiveData<List<Subcategoria>> getAllLiveSubcategorias();

    @Query("SELECT * FROM subcategoria WHERE categoriaSuperior LIKE (:categoriaPadre)")
    List<Subcategoria> getSubcategoriasHijas(String categoriaPadre);

    @Query("SELECT * FROM subcategoria WHERE nombreSubcategoria LIKE (:subcategoria) LIMIT 1")
    Subcategoria getSubcategoria(String subcategoria);

    @Update
    void upDateSubcategoria(Subcategoria... subcategorias);

    @Insert
    void insertSubcategoria(Subcategoria... subcategorias);

    @Delete
    void deleteSubcategoria(Subcategoria... subcategorias);
}
