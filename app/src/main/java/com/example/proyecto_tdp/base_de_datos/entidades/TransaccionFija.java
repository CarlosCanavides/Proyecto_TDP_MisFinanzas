package com.example.proyecto_tdp.base_de_datos.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.proyecto_tdp.base_de_datos.DateConverter;
import java.util.Date;
import java.util.UUID;

@Entity(foreignKeys = @ForeignKey(entity = Categoria.class,
        parentColumns = "id",
        childColumns = "categoria",
        onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE))
public class TransaccionFija {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "etiqueta")
    private String etiqueta;

    @ColumnInfo(name = "precio")
    private float precio;

    @ColumnInfo(name = "categoria")
    private String categoria;

    @ColumnInfo(name = "tipoTransaccion")
    private String tipoTransaccion;

    @ColumnInfo(name = "fecha")
    @TypeConverters(DateConverter.class)
    private Date fecha;

    @ColumnInfo(name = "info")
    private String info;

    @ColumnInfo(name = "frecuencia")
    private String frecuencia;

    @ColumnInfo(name = "fechaFinal")
    @TypeConverters(DateConverter.class)
    private Date fechaFinal;

    @ColumnInfo(name = "cantidadEjecucionesRestantes")
    private int cantidadEjecucionesRestantes;

    @ColumnInfo(name = "fechaProximaEjecucion")
    private Date fechaProximaEjecucion;

    public TransaccionFija(String titulo, String etiqueta, float precio, String categoria, String tipoTransaccion, Date fecha, String info, String frecuencia, Date fechaFinal, int cantidadEjecucionesRestantes, Date fechaProximaEjecucion) {
        id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.etiqueta = etiqueta;
        this.precio = precio;
        this.categoria = categoria;
        this.tipoTransaccion = tipoTransaccion;
        this.fecha = fecha;
        this.info = info;
        this.frecuencia = frecuencia;
        this.fechaFinal = fechaFinal;
        this.cantidadEjecucionesRestantes = cantidadEjecucionesRestantes;
        this.fechaProximaEjecucion = fechaProximaEjecucion;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFrecuencia(){
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia){
        this.frecuencia = frecuencia;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fecha) {
        this.fechaFinal = fecha;
    }

    public int getCantidadEjecucionesRestantes(){
        return cantidadEjecucionesRestantes;
    }

    public void setCantidadEjecucionesRestantes(int cantidadEjecucionesRestantes){
        this.cantidadEjecucionesRestantes = cantidadEjecucionesRestantes;
    }

    public Date getFechaProximaEjecucion(){
        return fechaProximaEjecucion;
    }

    public void setFechaProximaEjecucion(Date fechaProximaEjecucion){
        this.fechaProximaEjecucion = fechaProximaEjecucion;
    }
}
