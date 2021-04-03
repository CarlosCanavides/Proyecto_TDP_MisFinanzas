package com.example.proyecto_tdp;

import android.graphics.Color;

public class Constantes {

    public static final String ENERO = "Enero";
    public static final String FEBRERO = "Febrero";
    public static final String MARZO = "Marzo";
    public static final String ABRIL = "Abril";
    public static final String MAYO = "Mayo";
    public static final String JUNIO = "Junio";
    public static final String JULIO = "Julio";
    public static final String AGOSTO = "Agosto";
    public static final String SEPTIEMBRE = "Septiembre";
    public static final String OCTUBRE = "Octubre";
    public static final String NOVIEMBRE = "Noviembre";
    public static final String DICIEMBRE = "Diciembre";
    public static final String HISTORICO = "Historico";

    public static final String FORMATO_FECHA = "yyyy-MM-dd";
    public static final String FORMATO_FECHA_PARA_VISUALIZAR = "dd/MM/yyyy";

    public static final String GASTO = "Gasto";
    public static final String INGRESO = "Ingreso";
    public static final String SIN_TITULO = "Sin titulo";
    public static final String SIN_CATEGORIA = "Sin categoria";
    public static final String SELECCIONAR_CATEGORIA = "Seleccionar Categoria";
    public static final String SELECCIONAR_FECHA_FINAL = "Seleccionar Fecha Final";
    public static final String ID_CATEGORIA_ELEGIDA = "id_categoria_elegida";
    public static final String NOMBRE_CATEGORIA_ELEGIDA = "nombre_categoria_elegida";

    public static final String SELECCIONAR_FRECUENCIA = "Seleccionar Frecuencia";
    public static final String FRECUENCIA_SOLO_UNA_VEZ = "Solo una vez";
    public static final String FRECUENCIA_CADA_DIA = "Cada dia";
    public static final String FRECUENCIA_UNA_VEZ_A_LA_SEMANA = "Una vez a la semana";
    public static final String FRECUENCIA_UNA_VEZ_AL_MES = "Una vez al mes";
    public static final String FRECUENCIA_UNA_VEZ_AL_ANIO = "Una vez al anio";

    public static final String CAMPO_ID = "ID";
    public static final String CAMPO_INFO = "info";
    public static final String CAMPO_TIPO = "tipo";
    public static final String CAMPO_FECHA = "fecha";
    public static final String CAMPO_TITULO = "titulo";
    public static final String CAMPO_PRECIO = "precio";
    public static final String CAMPO_ETIQUETA = "etiqueta";
    public static final String CAMPO_ID_CATEGORIA = "categoria";
    public static final String CAMPO_FRECUENCIA = "frecuencia";
    public static final String CAMPO_FECHA_FINAL = "fecha final";
    public static final String CAMPO_ID_TF_PADRE = "ID transaccion fija padre";
    public static final String BOX_PLANTILLA_SELECCIONADO = "box plantilla seleccionada";
    public static final String BOX_TRANSACCION_FIJA_SELECCIONADO = "box TF seleccionada";
    public static final String CAMPO_EJECUCIONES_RESTANTES = "campo ejecuciones restantes";

    public static final String CAMPO_INFO_ANTERIOR = "info_anterior";
    public static final String CAMPO_TIPO_ANTERIOR = "tipo_anterior";
    public static final String CAMPO_TITULO_ANTERIOR = "titulo_anterior";
    public static final String CAMPO_PRECIO_ANTERIOR = "precio_anterior";
    public static final String CAMPO_ETIQUETA_ANTERIOR = "etiqueta_anterior";
    public static final String CAMPO_ID_CATEGORIA_ANTERIOR = "categoria_anterior";
    public static final String CAMPO_FRECUENCIA_ANTERIOR = "frecuencia_anterior";
    public static final String CAMPO_FECHA_FINAL_ANTERIOR = "fecha_final_anterior";
    public static final String CAMPO_FECHA_INICIO_ANTERIOR = "fecha_inicio_anterior";

    public static final String CAMPO_TIPO_CATEGORIA = "tipo categoria";
    public static final String CAMPO_COLOR_CATEGORIA = "color categoria";
    public static final String CAMPO_NOMBRE_CATEGORIA = "nombre categoria";
    public static final String CAMPO_ID_CATEGORIA_SUPERIOR = "campo_id_categoria_superior";
    public static final String CAMPO_NOMBRE_CATEGORIA_SUPERIOR = "campo_nombre_categoria_superior";
    public static final int COLOR_CATEGORIA_POR_DEFECTO = Color.parseColor("#7373FF");

    public static final int PEDIDO_NUEVA_CATEGORIA = 1;
    public static final int PEDIDO_NUEVA_PLANTILLA = 2;
    public static final int PEDIDO_NUEVA_TRANSACCION = 3;
    public static final int PEDIDO_NUEVA_TRANSACCION_FIJA = 4;

    public static final int PEDIDO_SET_CATEGORIA = 5;
    public static final int PEDIDO_SET_PLANTILLA = 6;
    public static final int PEDIDO_SET_TRANSACCION = 7;
    public static final int PEDIDO_SET_TRANSACCION_FIJA = 8;

    public static final int PEDIDO_SELECCIONAR_CATEGORIA = 9;
    public static final int PEDIDO_SELECCIONAR_PLANTILLA = 10;
}
