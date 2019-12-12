package com.example.proyecto_tdp.base_de_datos.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Categoria.class,
        parentColumns = "nombreCategoria",
        childColumns = "categoriaSuperior",
        onDelete = ForeignKey.SET_DEFAULT))
public class Subcategoria {

    @PrimaryKey
    @NonNull
    private String nombreSubcategoria;

    @ColumnInfo(name = "categoriaSuperior", defaultValue = "NULL")
    private String categoriaSuperior;

    @ColumnInfo(name = "colorSubcategoria")
    private int colorSubcategoria;

    @ColumnInfo(name = "tipoSubcategoria")
    private String tipoSubcategoria;


    public Subcategoria(@NonNull String nombreSubcategoria, String categoriaSuperior, int colorSubcategoria, String tipoSubcategoria) {
        this.nombreSubcategoria = nombreSubcategoria;
        if(categoriaSuperior!=null && !categoriaSuperior.equals("")){
            this.categoriaSuperior = categoriaSuperior;
        }
        this.colorSubcategoria = colorSubcategoria;
        this.tipoSubcategoria = tipoSubcategoria;
    }

    @NonNull
    public String getNombreSubcategoria() {
        return nombreSubcategoria;
    }

    public void setNombreSubcategoria(@NonNull String nombreSubcategoria) {
        this.nombreSubcategoria = nombreSubcategoria;
    }

    public String getCategoriaSuperior() {
        return categoriaSuperior;
    }

    public void setCategoriaSuperior(String categoriaSuperior) {
        this.categoriaSuperior = categoriaSuperior;
    }

    public int getColorSubcategoria() {
        return colorSubcategoria;
    }

    public void setColorSubcategoria(int colorSubcategoria) {
        this.colorSubcategoria = colorSubcategoria;
    }

    public String getTipoSubcategoria() {
        return tipoSubcategoria;
    }

    public void setTipoSubcategoria(String tipoSubcategoria) {
        this.tipoSubcategoria = tipoSubcategoria;
    }

    public void copy(Subcategoria nuevosDatos){
        nombreSubcategoria = nuevosDatos.getNombreSubcategoria();
        categoriaSuperior = nuevosDatos.getCategoriaSuperior();
        colorSubcategoria = nuevosDatos.getColorSubcategoria();
        tipoSubcategoria = nuevosDatos.getTipoSubcategoria();
    }

}
