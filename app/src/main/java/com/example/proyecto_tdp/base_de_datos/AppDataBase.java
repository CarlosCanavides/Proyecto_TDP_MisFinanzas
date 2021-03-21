package com.example.proyecto_tdp.base_de_datos;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;

@Database(entities = {Transaccion.class, Categoria.class, TransaccionFija.class, Plantilla.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDataBase extends RoomDatabase {
    public abstract TransaccionDao transaccionDao();
    public abstract CategoriaDao categoriaDao();
    public abstract TransaccionFijaDao transaccionFijaDao();
    public abstract PlantillaDao plantillaDao();
}
