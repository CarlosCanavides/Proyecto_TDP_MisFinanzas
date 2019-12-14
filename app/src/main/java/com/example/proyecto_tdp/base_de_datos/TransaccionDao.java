package com.example.proyecto_tdp.base_de_datos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;

import java.util.Date;
import java.util.List;

@Dao
public interface TransaccionDao {

    @Query("SELECT * FROM transaccion")
    List<Transaccion> getAllTransacciones();

    @Query("SELECT * FROM transaccion")
    LiveData<List<Transaccion>> getAllLiveTransacciones();

    @Query("SELECT * FROM transaccion WHERE id IN (:transacciones)")
    List<Transaccion> loadAllByIds(int[] transacciones);

    @Query("SELECT * FROM transaccion WHERE titulo LIKE :titulo LIMIT 1")
    Transaccion findByName(String titulo);

    @Query("SELECT * FROM transaccion WHERE (fecha>=(:desde) AND fecha<=(:hasta))")
    LiveData<List<Transaccion>> getTransaccionesMes(String desde, String hasta);

    @Update
    void upDateTransaccion(Transaccion... transaccions);

    @Insert
    void insertTransaccion(Transaccion... transacciones);

    @Delete
    void deleteTransaccion(Transaccion transacciones);

}
