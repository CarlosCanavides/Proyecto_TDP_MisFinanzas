package com.example.proyecto_tdp.base_de_datos.entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.UUID;

@Entity(foreignKeys = @ForeignKey(entity = Categoria.class,
        parentColumns = "id",
        childColumns = "categoriaSuperior",
        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class Categoria {

    @PrimaryKey
    @NonNull
    private String id;

    @NonNull
    @ColumnInfo(name = "nombreCategoria")
    private String nombreCategoria;

    @ColumnInfo(name = "categoriaSuperior")
    private String categoriaSuperior;

    @ColumnInfo(name = "colorCategoria")
    private int colorCategoria;

    @ColumnInfo(name = "tipoCategoria")
    private String tipoCategoria;


    public Categoria(@NonNull String nombreCategoria, String categoriaSuperior, int colorCategoria, String tipoCategoria) {
        id = UUID.randomUUID().toString();
        this.nombreCategoria = nombreCategoria;
        this.categoriaSuperior = categoriaSuperior;
        this.colorCategoria = colorCategoria;
        this.tipoCategoria = tipoCategoria;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(@NonNull String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getCategoriaSuperior() {
        return categoriaSuperior;
    }

    public void setCategoriaSuperior(String categoriaSuperior) {
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
}
