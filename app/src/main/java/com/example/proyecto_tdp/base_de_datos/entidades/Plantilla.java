package com.example.proyecto_tdp.base_de_datos.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.UUID;

@Entity(foreignKeys = @ForeignKey(entity = Categoria.class,
        parentColumns = "id",
        childColumns = "categoria",
        onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE))
public class Plantilla {

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

    @ColumnInfo(name = "info")
    private String info;

    public Plantilla(String titulo, float precio, String etiqueta, String categoria, String tipoTransaccion, String info) {
        id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.etiqueta = etiqueta;
        this.precio = precio;
        this.categoria = categoria;
        this.tipoTransaccion = tipoTransaccion;
        this.info = info;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
