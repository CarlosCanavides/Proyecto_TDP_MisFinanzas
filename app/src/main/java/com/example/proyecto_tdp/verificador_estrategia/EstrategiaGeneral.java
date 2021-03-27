package com.example.proyecto_tdp.verificador_estrategia;

import android.content.Intent;
import com.example.proyecto_tdp.Constantes;

public abstract class EstrategiaGeneral implements EstrategiaDeVerificacion{

    protected int id;
    protected float precio;
    protected String tipo;
    protected String info;
    protected String titulo;
    protected String etiqueta;
    protected String categoria;

    public EstrategiaGeneral() {}

    protected void obtenerDatosPrincipales(Intent datos){
        tipo = datos.getStringExtra(Constantes.CAMPO_TIPO);
        info = datos.getStringExtra(Constantes.CAMPO_INFO);
        titulo = datos.getStringExtra(Constantes.CAMPO_TITULO);
        etiqueta = datos.getStringExtra(Constantes.CAMPO_ETIQUETA);
        categoria = datos.getStringExtra(Constantes.CAMPO_CATEGORIA);
        precio = datos.getFloatExtra(Constantes.CAMPO_PRECIO,0);
    }
}
