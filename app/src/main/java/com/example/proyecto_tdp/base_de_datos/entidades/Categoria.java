package com.example.proyecto_tdp.base_de_datos.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Categoria {

    @PrimaryKey
    @NonNull
    private String nombreCategoria;

    @ColumnInfo(name = "categoriaSuperior", defaultValue = "NULL")
    private String categoriaSuperior;

    @ColumnInfo(name = "colorCategoria")
    private int colorCategoria;

    @ColumnInfo(name = "tipoCategoria")
    private String tipoCategoria;


    public Categoria(@NonNull String nombreCategoria, String categoriaSuperior, int colorCategoria, String tipoCategoria) {
        this.nombreCategoria = nombreCategoria;
        this.categoriaSuperior = categoriaSuperior;
        this.colorCategoria = colorCategoria;
        this.tipoCategoria = tipoCategoria;
    }

    @NonNull
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public String getCategoriaSuperior() {
        return categoriaSuperior;
    }

    public void setNombreCategoria(@NonNull String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public void setCategoriaSuperior(@NonNull String categoriaSuperior) {
        this.categoriaSuperior = categoriaSuperior;
    }

    public int getColorCategoria() {
        return colorCategoria;
    }

    public void setColorCategoria(int colorCategoria) {
        this.colorCategoria = colorCategoria;
    }

    public String getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(String tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public void copy(Categoria nuevosDatos){
        nombreCategoria = nuevosDatos.getNombreCategoria();
        colorCategoria = nuevosDatos.getColorCategoria();
        tipoCategoria = nuevosDatos.getTipoCategoria();
        categoriaSuperior = nuevosDatos.getCategoriaSuperior();
    }
}
