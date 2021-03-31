package com.example.proyecto_tdp.verificador_estrategia;

import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccionFija;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class EstrategiaTFPendientes {

    protected ViewModelTransaccion viewModelTransaccion;
    protected ViewModelTransaccionFija viewModelTransaccionFija;
    protected LocalDate calendario;
    protected DateTimeFormatter formatoDeFecha;
    protected float precio;
    protected String tipo;
    protected String info;
    protected String titulo;
    protected String etiqueta;
    protected String categoria;
    protected String idTransaccionFijaPadre;
    protected Date fecha;
    protected Date fechaFinal;

    public EstrategiaTFPendientes(ViewModelTransaccion viewModelTransaccion, ViewModelTransaccionFija viewModelTransaccionFija){
        this.viewModelTransaccion = viewModelTransaccion;
        this.viewModelTransaccionFija = viewModelTransaccionFija;
        formatoDeFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);
        calendario = LocalDate.now();
    }

    public void insertarTFPendientes(){
        LocalDate hoy = LocalDate.now();
        List<TransaccionFija> transaccionFijasPendientes = viewModelTransaccionFija.getAllTransaccionesFijasPendientes();
        for(TransaccionFija transaccionFija : transaccionFijasPendientes){
            if(transaccionFija.getFechaProximaEjecucion()!=null) {
                if (transaccionFija.getFechaProximaEjecucion().before(hoy.toDate())) {
                    insertarPendientes(transaccionFija);
                }
            }
        }
    }

    protected void insertarPendientes(TransaccionFija transaccionFija){
        String frecuencia = transaccionFija.getFrecuencia();
        if(frecuencia.equals(Constantes.FRECUENCIA_SOLO_UNA_VEZ)){
            insertarSoloUnaVez(transaccionFija);
        }
        else {
            if (frecuencia.equals(Constantes.FRECUENCIA_UNA_VEZ_A_LA_SEMANA)) {
                insertarUnaVezAlaSemana(transaccionFija);
            }
            else {
                if(frecuencia.equals(Constantes.FRECUENCIA_UNA_VEZ_AL_MES)){
                    insertarUnaVezAlMes(transaccionFija);
                }
                else if(frecuencia.equals(Constantes.FRECUENCIA_UNA_VEZ_AL_ANIO)){
                    insertarUnaVezAlAnio(transaccionFija);
                }
            }
        }
    }

    protected void obtenerDatos(TransaccionFija transaccionFija){
        precio = transaccionFija.getPrecio();
        tipo = transaccionFija.getTipoTransaccion();
        info = transaccionFija.getInfo();
        titulo = transaccionFija.getTitulo();
        categoria = transaccionFija.getCategoria();
        etiqueta = transaccionFija.getEtiqueta();
        fecha = transaccionFija.getFechaProximaEjecucion();
        fechaFinal = transaccionFija.getFechaFinal();
        idTransaccionFijaPadre = transaccionFija.getId();
    }

    protected void insertarSoloUnaVez(TransaccionFija transaccionFija){
        obtenerDatos(transaccionFija);
        Transaccion nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,fecha,info,idTransaccionFijaPadre);
        viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
        transaccionFija.setFechaProximaEjecucion(null);
        transaccionFija.setCantidadEjecucionesRestantes(0);
        viewModelTransaccionFija.actualizarTransaccionFija(transaccionFija);
    }

    protected void insertarUnaVezAlaSemana(TransaccionFija transaccionFija){
        obtenerDatos(transaccionFija);
        Transaccion transaccion;
        int cantidadDeEjecucionesRealizadas = 0;
        Date hoy = LocalDate.now().toDate();
        calendario = LocalDate.parse(formatoDeFecha.print(fecha.getTime()));
        while (fechaFinal.after(calendario.toDate()) && hoy.after(calendario.toDate())){
            cantidadDeEjecucionesRealizadas++;
            transaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,calendario.toDate(),info,idTransaccionFijaPadre);
            viewModelTransaccion.insertarTransaccion(transaccion);
            calendario = calendario.plusDays(7);
        }
        Date proximaEjecucion = null;
        if(fechaFinal.after(calendario.toDate())){
            proximaEjecucion = calendario.toDate();
        }
        transaccionFija.setCantidadEjecucionesRestantes(transaccionFija.getCantidadEjecucionesRestantes()-cantidadDeEjecucionesRealizadas);
        transaccionFija.setFechaProximaEjecucion(proximaEjecucion);
        viewModelTransaccionFija.insertarTransaccionFija(transaccionFija);
    }

    protected void insertarUnaVezAlMes(TransaccionFija transaccionFija){
        obtenerDatos(transaccionFija);
        Transaccion transaccion;
        int cantidadDeEjecucionesRealizadas = 0;
        Date hoy = LocalDate.now().toDate();
        calendario = LocalDate.parse(formatoDeFecha.print(fecha.getTime()));
        while (fechaFinal.after(calendario.toDate()) && hoy.after(calendario.toDate())){
            cantidadDeEjecucionesRealizadas++;
            transaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,calendario.toDate(),info,idTransaccionFijaPadre);
            viewModelTransaccion.insertarTransaccion(transaccion);
            calendario = calendario.plusMonths(1);
        }
        Date proximaEjecucion = null;
        if(fechaFinal.after(calendario.toDate())){
            proximaEjecucion = calendario.toDate();
        }
        transaccionFija.setCantidadEjecucionesRestantes(transaccionFija.getCantidadEjecucionesRestantes()-cantidadDeEjecucionesRealizadas);
        transaccionFija.setFechaProximaEjecucion(proximaEjecucion);
        viewModelTransaccionFija.insertarTransaccionFija(transaccionFija);
    }

    protected void insertarUnaVezAlAnio(TransaccionFija transaccionFija){
        obtenerDatos(transaccionFija);
        Transaccion transaccion;
        int cantidadDeEjecucionesRealizadas = 0;
        Date hoy = LocalDate.now().toDate();
        calendario = LocalDate.parse(formatoDeFecha.print(fecha.getTime()));
        while (fechaFinal.after(calendario.toDate()) && hoy.after(calendario.toDate())){
            cantidadDeEjecucionesRealizadas++;
            transaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,calendario.toDate(),info,idTransaccionFijaPadre);
            viewModelTransaccion.insertarTransaccion(transaccion);
            calendario = calendario.plusYears(1);
        }
        Date proximaEjecucion = null;
        if(fechaFinal.after(calendario.toDate())){
            proximaEjecucion = calendario.toDate();
        }
        transaccionFija.setCantidadEjecucionesRestantes(transaccionFija.getCantidadEjecucionesRestantes()-cantidadDeEjecucionesRealizadas);
        transaccionFija.setFechaProximaEjecucion(proximaEjecucion);
        viewModelTransaccionFija.insertarTransaccionFija(transaccionFija);
    }
}
