package com.example.proyecto_tdp.base_de_datos.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.proyecto_tdp.base_de_datos.DateConverter;
import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = Subcategoria.class,
        parentColumns = "nombreSubcategoria",
        childColumns = "categoria",
        onDelete = ForeignKey.NO_ACTION))
public class Transaccion {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "etiqueta")
    private String etiqueta;

    @ColumnInfo(name = "precio")
    private float precio;

    @ColumnInfo(name = "categoria", defaultValue = "null")
    private String categoria;

    @ColumnInfo(name = "tipoTransaccion")
    private String tipoTransaccion;

    @ColumnInfo(name = "fecha")
    @TypeConverters(DateConverter.class)
    private Date fecha;

    @ColumnInfo(name = "info")
    private String info;

    public Transaccion(String titulo, String etiqueta, float precio, String categoria, String tipoTransaccion, Date fecha, String info) {
        this.titulo = titulo;
        this.etiqueta = etiqueta;
        this.precio = precio;
        if(categoria!=null && categoria!="") {
            this.categoria = categoria;
        }
        this.tipoTransaccion = tipoTransaccion;
        this.fecha = fecha;
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

    public void copy(Transaccion nuevosDatos){
        titulo = nuevosDatos.getTitulo();
        tipoTransaccion = nuevosDatos.getTipoTransaccion();
        precio = nuevosDatos.getPrecio();
        info = nuevosDatos.getInfo();
        fecha = nuevosDatos.getFecha();
        categoria = nuevosDatos.getCategoria();
        etiqueta = nuevosDatos.getEtiqueta();
    }
}
