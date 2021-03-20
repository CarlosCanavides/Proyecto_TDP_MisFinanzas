package com.example.proyecto_tdp.base_de_datos;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;

@Database(entities = {Transaccion.class, Categoria.class }, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDataBase extends RoomDatabase {
    public abstract TransaccionDao transaccionDao();
    public abstract CategoriaDao categoriaDao();
}
