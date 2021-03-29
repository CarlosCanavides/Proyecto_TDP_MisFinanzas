package com.example.proyecto_tdp.base_de_datos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import java.util.List;

@Dao
public interface CategoriaDao {

    @Query("SELECT * FROM categoria")
    List<Categoria> getAllCategorias();

    @Query("SELECT * FROM categoria")
    LiveData<List<Categoria>> getAllLiveCategorias();

    @Query("SELECT * FROM categoria WHERE nombreCategoria IN (:categorias)")
    List<Categoria> loadAllByIds(String[] categorias);

    @Query("SELECT * FROM categoria WHERE nombreCategoria LIKE :nombreCategoria LIMIT 1")
    Categoria findByName(String nombreCategoria);

    @Query("SELECT * FROM categoria WHERE categoriaSuperior LIKE NULL")
    List<Categoria> getCategoriasSuperiores();

    @Query("SELECT * FROM categoria WHERE categoriaSuperior LIKE :categoriaSuperior")
    List<Categoria> getSubcategorias(String categoriaSuperior);

    @Query("SELECT * FROM categoria WHERE nombreCategoria LIKE :nombreCategoria LIMIT 1")
    Categoria getCategoria(String nombreCategoria);

    @Query("DELETE FROM categoria WHERE nombreCategoria LIKE :id")
    void eliminarCategoria(String id);

    @Update
    void upDateCategoria(Categoria... categorias);

    @Insert
    void insertCategoria(Categoria... categorias);

    @Delete
    void deleteCategoria(Categoria... categorias);
}
