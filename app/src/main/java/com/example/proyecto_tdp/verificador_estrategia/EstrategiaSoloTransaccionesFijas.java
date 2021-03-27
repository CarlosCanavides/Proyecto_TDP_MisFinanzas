package com.example.proyecto_tdp.verificador_estrategia;

import android.content.Intent;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccionFija;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.Date;
import static android.app.Activity.RESULT_OK;

public class EstrategiaSoloTransaccionesFijas extends EstrategiaGeneral{

    protected ViewModelTransaccion viewModelTransaccion;
    protected ViewModelTransaccionFija viewModelTransaccionFija;
    protected DateTimeFormatter formatoDeFecha;
    protected LocalDate calendario;

    protected Date fechaInicio;
    protected Date fechaFinal;
    protected String frecuencia;
    protected int cantidadEjecucionesRestantes;

    public EstrategiaSoloTransaccionesFijas(ViewModelTransaccion vmT, ViewModelTransaccionFija vmTF){
        viewModelTransaccion = vmT;
        viewModelTransaccionFija = vmTF;
        formatoDeFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);
        calendario = LocalDate.now();
    }

    @Override
    public void verificar(int codigoPedido, int estadoDelResultado, Intent datos) {
        if(datos!=null){
            if(codigoPedido == Constantes.PEDIDO_NUEVA_TRANSACCION_FIJA){
                if(estadoDelResultado==RESULT_OK){
                    insertarNuevaTransaccionFija(datos);
                }
            }
            else if(codigoPedido ==  Constantes.PEDIDO_SET_TRANSACCION_FIJA){
                if(estadoDelResultado==RESULT_OK){
                    setTransaccionFija(datos);
                }
                else {
                    eliminarTransaccionFija(datos);
                }
            }
        }
    }

    @Override
    protected void obtenerDatosPrincipales(Intent datos) {
        super.obtenerDatosPrincipales(datos);
        String fechaI = datos.getStringExtra(Constantes.CAMPO_FECHA);
        fechaInicio = formatoDeFecha.parseDateTime(fechaI).toDate();
        String fechaF = datos.getStringExtra(Constantes.CAMPO_FECHA_FINAL);
        fechaFinal = formatoDeFecha.parseDateTime(fechaF).toDate();
        frecuencia = datos.getStringExtra(Constantes.CAMPO_FRECUENCIA);
    }

    private void insertarNuevaTransaccionFija(Intent datos){
        obtenerDatosPrincipales(datos);
        calendario = LocalDate.now();
        if(frecuencia.equals(Constantes.FRECUENCIA_SOLO_UNA_VEZ)){
            insertarSoloUnaVez();
        }
        else {
            if (frecuencia.equals(Constantes.FRECUENCIA_UNA_VEZ_A_LA_SEMANA)) {
                insertarUnaVezAlaSemana();
            }
            else {
                if(frecuencia.equals(Constantes.FRECUENCIA_UNA_VEZ_AL_MES)){
                    insertarUnaVezAlMes();
                }
                else if(frecuencia.equals(Constantes.FRECUENCIA_UNA_VEZ_AL_ANIO)){
                    insertarUnaVezAlAnio();
                }
            }
        }
    }

    private void setTransaccionFija(Intent datos){
        TransaccionFija transaccionFijaModificada;
        Transaccion transaccionModificada;
        obtenerDatosPrincipales(datos);
        calendario = LocalDate.now();
        String fechaInicioActual = datos.getStringExtra(Constantes.CAMPO_FECHA);
        String fechaFinalActual = datos.getStringExtra(Constantes.CAMPO_FECHA);
        String fechaInicioAnterior = datos.getStringExtra(Constantes.CAMPO_FECHA_INICIO_ANTERIOR);
        String fechaFinalAnterior = datos.getStringExtra(Constantes.CAMPO_FECHA_FINAL_ANTERIOR);
        String frecuenciaAnterior = datos.getStringExtra(Constantes.CAMPO_FRECUENCIA_ANTERIOR);
        cantidadEjecucionesRestantes = datos.getIntExtra(Constantes.CAMPO_EJECUCIONES_RESTANTES, 0);
        if(fechaInicioActual.equals(fechaInicioAnterior) && fechaFinalActual.equals(fechaFinalAnterior) && frecuencia.equals(frecuenciaAnterior)){
            transaccionFijaModificada = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,cantidadEjecucionesRestantes);
            transaccionFijaModificada.setId(id);
            viewModelTransaccionFija.actualizarTransaccionFija(transaccionFijaModificada);
        }
        else {
            eliminarTransaccionFija(datos);
            insertarNuevaTransaccionFija(datos);
        }
    }

    private void eliminarTransaccionFija(Intent datos){
        viewModelTransaccion.eliminarTransaccionesHijas(id);
        viewModelTransaccionFija.eliminarTransaccionFija(id);
    }

    private void insertarSoloUnaVez(){
        TransaccionFija nuevaTransaccionFija;
        Transaccion nuevaTransaccion;
        if(fechaInicio.after(calendario.toDate())){
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaInicio,1);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
        else {
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaInicio,0);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
            nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,nuevaTransaccionFija.getId());
            viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
        }
    }

    private void insertarUnaVezAlaSemana(){
        Transaccion nuevaTransaccion;
        TransaccionFija nuevaTransaccionFija;
        int cantidadDeEjecucionesTotales = 0;
        int cantidadDeEjecucionesRealizadas = 0;
        Date hoy = LocalDate.now().toDate();
        if(fechaInicio.before(hoy)){
            calendario = new LocalDate(fechaInicio);
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,0);
            while ((fechaFinal.after(calendario.toDate())||fechaFinal.compareTo(calendario.toDate())==0) && hoy.after(calendario.toDate())){
                cantidadDeEjecucionesTotales++;
                cantidadDeEjecucionesRealizadas++;
                nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,calendario.toDate(),info,nuevaTransaccionFija.getId());
                viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
                calendario.plusDays(7);
            }
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario.plusDays(7);
            }
            nuevaTransaccionFija.setCantidadEjecucionesRestantes(cantidadDeEjecucionesTotales-cantidadDeEjecucionesRealizadas);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
        else {
            calendario = new LocalDate(fechaInicio);
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario.plusDays(7);
            }
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,cantidadDeEjecucionesTotales);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
    }

    private void insertarUnaVezAlMes(){
        Transaccion nuevaTransaccion;
        TransaccionFija nuevaTransaccionFija;
        int cantidadDeEjecucionesTotales = 0;
        int cantidadDeEjecucionesRealizadas = 0;
        Date hoy = LocalDate.now().toDate();
        if(fechaInicio.before(hoy)){
            calendario = new LocalDate(fechaInicio);
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,0);
            while ((fechaFinal.after(calendario.toDate())||fechaFinal.compareTo(calendario.toDate())==0) && hoy.after(calendario.toDate())){
                cantidadDeEjecucionesTotales++;
                cantidadDeEjecucionesRealizadas++;
                nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,calendario.toDate(),info,nuevaTransaccionFija.getId());
                viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
                calendario.plusMonths(1);
            }
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario.plusMonths(1);
            }
            nuevaTransaccionFija.setCantidadEjecucionesRestantes(cantidadDeEjecucionesTotales-cantidadDeEjecucionesRealizadas);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
        else {
            calendario = new LocalDate(fechaInicio);
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario.plusMonths(1);
            }
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,cantidadDeEjecucionesTotales);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
    }

    private void insertarUnaVezAlAnio(){
        Transaccion nuevaTransaccion;
        TransaccionFija nuevaTransaccionFija;
        int cantidadDeEjecucionesTotales = 0;
        int cantidadDeEjecucionesRealizadas = 0;
        Date hoy = LocalDate.now().toDate();
        if(fechaInicio.before(hoy)){
            calendario = new LocalDate(fechaInicio);
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,0);
            while ((fechaFinal.after(calendario.toDate())||fechaFinal.compareTo(calendario.toDate())==0) && hoy.after(calendario.toDate())){
                cantidadDeEjecucionesTotales++;
                cantidadDeEjecucionesRealizadas++;
                nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,calendario.toDate(),info,nuevaTransaccionFija.getId());
                viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
                calendario.plusYears(1);
            }
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario.plusYears(1);
            }
            nuevaTransaccionFija.setCantidadEjecucionesRestantes(cantidadDeEjecucionesTotales-cantidadDeEjecucionesRealizadas);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
        else {
            calendario = new LocalDate(fechaInicio);
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario.plusYears(1);
            }
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,cantidadDeEjecucionesTotales);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
    }
}
