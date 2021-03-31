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
        categoriaSuperior = datos.getStringExtra(Constantes.CAMPO_ID_CATEGORIA_SUPERIOR);
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
            nuevaCategoria = new Categoria(nombreCategoria, categoriaSuperior, colorCategoria, tipoCategoria);
            viewModelCategoria.insertarCategoria(nuevaCategoria);
        }
    }

    protected void setCategoria(Intent datos){
        obtenerDatosPrincipales(datos);
        Categoria categoriaModificada = new Categoria(nombreCategoria, categoriaSuperior, colorCategoria, tipoCategoria);
        categoriaModificada.setId(idCategoria);
        viewModelCategoria.actualizarCategoria(categoriaModificada);
    }

    protected void eliminarCategoria(Intent datos){
        idCategoria = datos.getStringExtra(Constantes.CAMPO_ID);
        if(idCategoria!=null) {
            viewModelCategoria.eliminarCategoria(idCategoria);
        }
    }
}
