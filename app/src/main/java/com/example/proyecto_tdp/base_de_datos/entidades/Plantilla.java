package com.example.proyecto_tdp.base_de_datos.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Categoria.class,
        parentColumns = "nombreCategoria",
        childColumns = "categoria",
        onDelete = ForeignKey.NO_ACTION))
public class Plantilla {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "etiqueta")
    private String etiqueta;

    @ColumnInfo(name = "precio")
    private float precio;

    @ColumnInfo(name = "categoria", defaultValue = "NULL")
    private String categoria;

    @ColumnInfo(name = "tipoTransaccion")
    private String tipoTransaccion;

    @ColumnInfo(name = "info")
    private String info;

    public Plantilla(String titulo, float precio, String etiqueta, String categoria, String tipoTransaccion, String info) {
        this.titulo = titulo;
        this.etiqueta = etiqueta;
        this.precio = precio;
        if(categoria!=null && categoria!="") {
            this.categoria = categoria;
        }
        this.tipoTransaccion = tipoTransaccion;
        this.info = info;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
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

    public void copy(Transaccion nuevosDatos){
        titulo = nuevosDatos.getTitulo();
        tipoTransaccion = nuevosDatos.getTipoTransaccion();
        precio = nuevosDatos.getPrecio();
        info = nuevosDatos.getInfo();
        categoria = nuevosDatos.getCategoria();
        etiqueta = nuevosDatos.getEtiqueta();
    }
}
