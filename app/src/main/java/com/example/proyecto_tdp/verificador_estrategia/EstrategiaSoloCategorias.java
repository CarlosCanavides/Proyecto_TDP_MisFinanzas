package com.example.proyecto_tdp.verificador_estrategia;

import android.content.Intent;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import static android.app.Activity.RESULT_OK;

public class EstrategiaSoloCategorias implements EstrategiaDeVerificacion {

    protected int colorCategoria;
    protected String idCategoria;
    protected String tipoCategoria;
    protected String nombreCategoria;
    protected String categoriaSuperior;
    protected ViewModelCategoria viewModelCategoria;

    public EstrategiaSoloCategorias(ViewModelCategoria viewModelCategoria){
        this.viewModelCategoria = viewModelCategoria;
    }

    protected void obtenerDatosPrincipales(Intent datos){
        idCategoria = datos.getStringExtra(Constantes.CAMPO_ID);
        tipoCategoria = datos.getStringExtra(Constantes.CAMPO_TIPO_CATEGORIA);
        nombreCategoria = datos.getStringExtra(Constantes.CAMPO_NOMBRE_CATEGORIA);
        categoriaSuperior = datos.getStringExtra(Constantes.CAMPO_CATEGORIA_SUPERIOR);
        colorCategoria = datos.getIntExtra(Constantes.CAMPO_COLOR_CATEGORIA, Constantes.COLOR_CATEGORIA_POR_DEFECTO);
    }

    @Override
    public void verificar(int codigoPedido, int estadoDelResultado, Intent datos) {
        if(datos!=null){
            if(codigoPedido == Constantes.PEDIDO_NUEVA_CATEGORIA){
                if(estadoDelResultado==RESULT_OK){
                    insertarNuevaCategoria(datos);
                }
            }
            else if(codigoPedido == Constantes.PEDIDO_SET_CATEGORIA){
                if(estadoDelResultado==RESULT_OK){
                    setCategoria(datos);
                }
                else {
                    eliminarCategoria(datos);
                }
            }
        }
    }

    protected void insertarNuevaCategoria(Intent datos){
        obtenerDatosPrincipales(datos);
        if(nombreCategoria!=null) {
            Categoria nuevaCategoria;
            if(categoriaSuperior==null || categoriaSuperior.equals("Seleccionar categoria") || categoriaSuperior.equals("")) {
                nuevaCategoria = new Categoria(nombreCategoria, null, colorCategoria, tipoCategoria);
            }
            else {
                nuevaCategoria = new Categoria(nombreCategoria, categoriaSuperior, colorCategoria, tipoCategoria);
            }
            viewModelCategoria.insertarCategoria(nuevaCategoria);
        }
    }

    protected void setCategoria(Intent datos){
        obtenerDatosPrincipales(datos);
        if(nombreCategoria!=null) {
            Categoria categoriaModificada;
            if(categoriaSuperior==null || categoriaSuperior.equals("Seleccionar categoria") || categoriaSuperior.equals("") || categoriaSuperior.equals("NULL")) {
                categoriaModificada = new Categoria(idCategoria, null, colorCategoria, tipoCategoria);
            }
            else {
                categoriaModificada = new Categoria(idCategoria, categoriaSuperior, colorCategoria, tipoCategoria);
            }

            if(idCategoria.equals(nombreCategoria)) {
                viewModelCategoria.actualizarCategoria(categoriaModificada);
            }
            else {
                viewModelCategoria.actualizarCategoria(categoriaModificada);
                categoriaModificada.setNombreCategoria(nombreCategoria);
                viewModelCategoria.actualizarCategoria(categoriaModificada);
            }
        }
    }

    protected void eliminarCategoria(Intent datos){
        if(idCategoria!=null) {
            viewModelCategoria.eliminarCategoria(idCategoria);
        }
    }
}
