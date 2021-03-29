package com.example.proyecto_tdp.base_de_datos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import java.util.List;

@Dao
public interface TransaccionFijaDao {

    @Query("SELECT * FROM transaccionFija")
    List<TransaccionFija> getAllTransaccionesFijas();

    @Query("SELECT * FROM transaccionFija")
    LiveData<List<TransaccionFija>> getAllLiveTransaccionesFijas();

    @Query("SELECT * FROM transaccionFija WHERE fechaProximaEjecucion NOT LIKE null")
    List<TransaccionFija> getAllTransaccionesFijasPendientes();

    @Query("SELECT * FROM transaccionFija WHERE fecha BETWEEN date(:desde) AND date(:hasta)")
    LiveData<List<TransaccionFija>> getLiveTransaccionesFijasDesdeHasta(String desde, String hasta);

    @Query("SELECT * FROM transaccionFija WHERE fecha BETWEEN date(:desde) AND date(:hasta)")
    List<TransaccionFija> getTransaccionesFijasDesdeHasta(String desde, String hasta);

    @Query("SELECT * FROM transaccionFija WHERE titulo LIKE :titulo LIMIT 1")
    TransaccionFija findByName(String titulo);

    @Query("SELECT * FROM transaccionFija WHERE (fecha>=(:desde) AND fecha<=(:hasta))")
    LiveData<List<TransaccionFija>> getTransaccionesFijasMes(String desde, String hasta);

    @Query("DELETE FROM transaccionFija WHERE id LIKE :id")
    void eliminarTransaccionFija(int id);

    @Update
    void upDateTransaccionFija(TransaccionFija... transaccions);

    @Insert
    void insertTransaccionFija(TransaccionFija... transacciones);

    @Delete
    void deleteTransaccionFija(TransaccionFija transacciones);
}
