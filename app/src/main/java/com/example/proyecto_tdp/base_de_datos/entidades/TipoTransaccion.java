package com.example.proyecto_tdp.base_de_datos.entidades;

class TipoTransaccion {

    private String nombreTransaccion;

    public TipoTransaccion(String nombre) {
        nombreTransaccion = nombre;
    }

    public String getNombreTransaccion() {
        return nombreTransaccion;
    }

}
