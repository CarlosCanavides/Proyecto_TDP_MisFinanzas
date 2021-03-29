package com.example.proyecto_tdp.verificador_estrategia;

import android.content.Intent;
import android.util.Log;

import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import com.example.proyecto_tdp.view_models.ViewModelPlantilla;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccionFija;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import static android.app.Activity.RESULT_OK;

public class EstrategiaIngresoDeDatos implements EstrategiaDeVerificacion{

    protected ViewModelPlantilla viewModelPlantilla;
    protected ViewModelTransaccion viewModelTransaccion;
    protected ViewModelTransaccionFija viewModelTransaccionFija;
    protected DateTimeFormatter formatoDeFecha;
    protected NumberFormat formatoDeNumero;
    protected LocalDate calendario;

    public EstrategiaIngresoDeDatos(ViewModelPlantilla vmP, ViewModelTransaccion vmT, ViewModelTransaccionFija vmTF){
        viewModelPlantilla = vmP;
        viewModelTransaccion = vmT;
        viewModelTransaccionFija = vmTF;
        calendario = LocalDate.now();
        formatoDeFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);
        formatoDeNumero = NumberFormat.getInstance(new Locale("es", "ES"));
    }

    public void verificar(int codigoPedido, int estadoDelResultado, Intent datos) {
        if(datos!=null){
            if(codigoPedido==Constantes.PEDIDO_NUEVA_PLANTILLA || codigoPedido==Constantes.PEDIDO_NUEVA_TRANSACCION || codigoPedido==Constantes.PEDIDO_NUEVA_TRANSACCION_FIJA){
                ejecutarInserccion(estadoDelResultado,datos);
            }
        }
    }

    private void ejecutarInserccion(int estadoDelResultado, Intent datos){
        if(estadoDelResultado==RESULT_OK){
            String titulo = datos.getStringExtra(Constantes.CAMPO_TITULO);
            String etiqueta = datos.getStringExtra(Constantes.CAMPO_ETIQUETA);
            String categoria = datos.getStringExtra(Constantes.CAMPO_CATEGORIA);
            String tipo = datos.getStringExtra(Constantes.CAMPO_TIPO);
            String info = datos.getStringExtra(Constantes.CAMPO_INFO);
            float precio = datos.getFloatExtra(Constantes.CAMPO_PRECIO,0);
            if(esPlantilla(datos)){
                Plantilla nuevaPlantilla = new Plantilla(titulo,precio,etiqueta,categoria,tipo,info);
                viewModelPlantilla.insertarPlantilla(nuevaPlantilla);
            }
            else {
                String fecha = datos.getStringExtra(Constantes.CAMPO_FECHA);
                Date fechaDate = formatoDeFecha.parseDateTime(fecha).toDate();
                calendario = LocalDate.now();
                if(!esTransaccionFija(datos) && !fechaDate.after(calendario.toDate())) {
                    Transaccion nuevaTransaccion = new Transaccion(titulo, etiqueta, precio, categoria, tipo, fechaDate, info, null);
                    viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
                }
                else {
                    TransaccionFija nuevaTransaccionFija;
                    String fechaFinal = datos.getStringExtra(Constantes.CAMPO_FECHA_FINAL);
                    String frecuencia = datos.getStringExtra(Constantes.CAMPO_FRECUENCIA);
                    Date fechaFinalDate;
                    if(fechaFinal!=null){
                        fechaFinalDate= formatoDeFecha.parseDateTime(fechaFinal).toDate();
                        insertarTFPorFrecuencia(fechaDate,fechaFinalDate,frecuencia,titulo,etiqueta,precio,categoria,tipo,info);
                    }
                    else {
                        fechaFinalDate = fechaDate;
                        frecuencia = Constantes.FRECUENCIA_SOLO_UNA_VEZ;
                        nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaDate,info,frecuencia,fechaFinalDate,1);
                        viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
                    }
                }
            }
        }
    }

    protected boolean esPlantilla(Intent datos){
        return datos.getBooleanExtra(Constantes.BOX_PLANTILLA_SELECCIONADO, false);
    }

    protected boolean esTransaccionFija(Intent datos){
        return datos.getBooleanExtra(Constantes.BOX_TRANSACCION_FIJA_SELECCIONADO, false);
    }

    protected void insertarTFPorFrecuencia(Date fechaInicio, Date fechaFinal, String frecuencia, String titulo,
                                           String etiqueta, float precio, String categoria, String tipo, String info){
        TransaccionFija nuevaTransaccionFija;
        Transaccion nuevaTransaccion;
        if(frecuencia.equals(Constantes.FRECUENCIA_SOLO_UNA_VEZ)){
            calendario = LocalDate.now();
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
        else {
            int cantidadDeEjecucionesTotales = 0;
            int cantidadDeEjecucionesRealizadas = 0;
            Date hoy = LocalDate.now().toDate();
            if(frecuencia.equals(Constantes.FRECUENCIA_UNA_VEZ_A_LA_SEMANA)){
                if(fechaInicio.before(calendario.toDate())){
                    calendario = new LocalDate(fechaInicio);
                    nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,0);
                    while ((fechaFinal.after(calendario.toDate())||fechaFinal.compareTo(calendario.toDate())==0) && hoy.after(calendario.toDate())){
                        cantidadDeEjecucionesTotales++;
                        cantidadDeEjecucionesRealizadas++;
                        nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,nuevaTransaccionFija.getId());
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
            else if(frecuencia.equals(Constantes.FRECUENCIA_UNA_VEZ_AL_MES)){
                if(fechaInicio.before(calendario.toDate())){
                    calendario = new LocalDate(fechaInicio);
                    nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,0);
                    viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
                    while ((fechaFinal.after(calendario.toDate())||fechaFinal.compareTo(calendario.toDate())==0) && hoy.after(calendario.toDate())){
                        cantidadDeEjecucionesTotales++;
                        cantidadDeEjecucionesRealizadas++;
                        nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,nuevaTransaccionFija.getId());
                        viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
                        calendario.plusMonths(1);
                    }
                    while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                        cantidadDeEjecucionesTotales++;
                        calendario.plusMonths(1);
                    }
                    nuevaTransaccionFija.setCantidadEjecucionesRestantes(cantidadDeEjecucionesTotales-cantidadDeEjecucionesRealizadas);
                    viewModelTransaccionFija.actualizarTransaccionFija(nuevaTransaccionFija);
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
            else {
                if(fechaInicio.before(calendario.toDate())){
                    calendario = new LocalDate(fechaInicio);
                    nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,0);
                    viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
                    while ((fechaFinal.after(calendario.toDate())||fechaFinal.compareTo(calendario.toDate())==0) && hoy.after(calendario.toDate())){
                        cantidadDeEjecucionesTotales++;
                        cantidadDeEjecucionesRealizadas++;
                        nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,nuevaTransaccionFija.getId());
                        viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
                        calendario.plusYears(1);
                    }
                    while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                        cantidadDeEjecucionesTotales++;
                        calendario.plusYears(1);
                    }
                    nuevaTransaccionFija.setCantidadEjecucionesRestantes(cantidadDeEjecucionesTotales-cantidadDeEjecucionesRealizadas);
                    viewModelTransaccionFija.actualizarTransaccionFija(nuevaTransaccionFija);
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
    }
}
