package com.example.proyecto_tdp.base_de_datos.entidades;

import com.example.proyecto_tdp.R;

public class ResumenMes {

    private String fecha;
    private String aviso;
    private int grafico;
    private int cantTransacciones;

    public ResumenMes(String fecha, String aviso, int grafico, int cantTransacciones) {
        this.fecha = fecha;
        this.aviso = aviso;
        this.grafico = grafico;
        this.cantTransacciones = cantTransacciones;
    }

    public  ResumenMes() {
        fecha = "";
        aviso = "";
        grafico = R.drawable.icon_informes;
        cantTransacciones = 0;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAviso() {
        return aviso;
    }

    public void setAviso(String aviso) {
        this.aviso = aviso;
    }

    public int getGrafico() {
        return grafico;
    }

    public void setGrafico(int grafico) {
        this.grafico = grafico;
    }

    public int getCantTransacciones() {
        return cantTransacciones;
    }

    public void setCantTransacciones(int cantTransacciones) {
        this.cantTransacciones = cantTransacciones;
    }
}
