package com.example.proyecto_tdp.base_de_datos;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;

@Database(entities = {Transaccion.class, Categoria.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract TransaccionDao transaccionDao();
    public abstract CategoriaDao categoriaDao();
    public abstract SubcategoriaDao subcategoriaDao();
}
