package com.example.proyecto_tdp.verificador_estrategia;

import android.content.Intent;
import android.util.Log;
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
        id = datos.getStringExtra(Constantes.CAMPO_ID);
    }

    protected boolean hayModificacion(Intent datos){
        boolean resultado = true;
        String fechaInicioActual = datos.getStringExtra(Constantes.CAMPO_FECHA);
        String fechaFinalActual = datos.getStringExtra(Constantes.CAMPO_FECHA);
        String tipoAnterior = datos.getStringExtra(Constantes.CAMPO_TIPO_ANTERIOR);
        String infoAnterior = datos.getStringExtra(Constantes.CAMPO_INFO_ANTERIOR);
        Float precioAnterior = datos.getFloatExtra(Constantes.CAMPO_PRECIO_ANTERIOR,-1);
        String tituloAnterior = datos.getStringExtra(Constantes.CAMPO_TITULO_ANTERIOR);
        String etiquetaAnterior = datos.getStringExtra(Constantes.CAMPO_ETIQUETA_ANTERIOR);
        String fechaInicioAnterior = datos.getStringExtra(Constantes.CAMPO_FECHA_INICIO_ANTERIOR);
        String fechaFinalAnterior = datos.getStringExtra(Constantes.CAMPO_FECHA_FINAL_ANTERIOR);
        String categoriaAnterior = datos.getStringExtra(Constantes.CAMPO_ID_CATEGORIA_ANTERIOR);
        String frecuenciaAnterior = datos.getStringExtra(Constantes.CAMPO_FRECUENCIA_ANTERIOR);
        if(tipoAnterior.equals(tipo)&&infoAnterior.equals(info)&&precio==precioAnterior&&tituloAnterior.equals(titulo)
            &&etiquetaAnterior.equals(etiqueta)&&fechaInicioAnterior.equals(fechaInicioActual)
            &&fechaFinalAnterior.equals(fechaFinalActual)&&categoriaAnterior.equals(categoria)
            &&frecuenciaAnterior.equals(frecuencia)){
            resultado = false;
        }
        return resultado;
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
        obtenerDatosPrincipales(datos);
        if(hayModificacion(datos)){
            eliminarTransaccionFija(datos);
            insertarNuevaTransaccionFija(datos);
        }
    }

    private void eliminarTransaccionFija(Intent datos){
        id = datos.getStringExtra(Constantes.CAMPO_ID);
        if(id!=null) {
            //Log.e("AQUII estoy en ETF"," aquiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
            //viewModelTransaccion.eliminarTransaccionesHijas(id);
            viewModelTransaccionFija.eliminarTransaccionFija(id);
        }
    }

    private void insertarSoloUnaVez(){
        TransaccionFija nuevaTransaccionFija;
        Transaccion nuevaTransaccion;
        if(fechaInicio.after(calendario.toDate())){
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaInicio,1,fechaInicio);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
        else {
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaInicio,0,null);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
            Log.e("AQUII en insertar TFUM","IDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"+nuevaTransaccionFija.getId()+"");
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
            calendario = LocalDate.parse(formatoDeFecha.print(fechaInicio.getTime()));
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,0,fechaInicio);
            while ((fechaFinal.after(calendario.toDate())||fechaFinal.compareTo(calendario.toDate())==0) && hoy.after(calendario.toDate())){
                cantidadDeEjecucionesTotales++;
                cantidadDeEjecucionesRealizadas++;
                nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,calendario.toDate(),info,nuevaTransaccionFija.getId());
                viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
                calendario = calendario.plusDays(7);
            }
            Date proximaEjecucion = null;
            if(fechaFinal.after(calendario.toDate())){
                proximaEjecucion = calendario.toDate();
            }
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario = calendario.plusDays(7);
            }
            nuevaTransaccionFija.setCantidadEjecucionesRestantes(cantidadDeEjecucionesTotales-cantidadDeEjecucionesRealizadas);
            nuevaTransaccionFija.setFechaProximaEjecucion(proximaEjecucion);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
        else {
            calendario = LocalDate.parse(formatoDeFecha.print(fechaInicio.getTime()));
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario = calendario.plusDays(7);
            }
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,cantidadDeEjecucionesTotales,fechaInicio);
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
            calendario = LocalDate.parse(formatoDeFecha.print(fechaInicio.getTime()));
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,0,fechaInicio);
            Log.e("AQUII en insertar TFUM","IDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"+nuevaTransaccionFija.getId()+"");
            while ((fechaFinal.after(calendario.toDate())||fechaFinal.compareTo(calendario.toDate())==0) && hoy.after(calendario.toDate())){
                cantidadDeEjecucionesTotales++;
                cantidadDeEjecucionesRealizadas++;
                nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,calendario.toDate(),info,nuevaTransaccionFija.getId());
                viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
                calendario = calendario.plusMonths(1);
            }
            Date proximaEjecucion = null;
            if(fechaFinal.after(calendario.toDate())){
                proximaEjecucion = calendario.toDate();
            }
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario = calendario.plusMonths(1);
            }
            nuevaTransaccionFija.setCantidadEjecucionesRestantes(cantidadDeEjecucionesTotales-cantidadDeEjecucionesRealizadas);
            nuevaTransaccionFija.setFechaProximaEjecucion(proximaEjecucion);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
        else {
            calendario = LocalDate.parse(formatoDeFecha.print(fechaInicio.getTime()));
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario = calendario.plusMonths(1);
            }
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,cantidadDeEjecucionesTotales,fechaInicio);
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
            calendario = LocalDate.parse(formatoDeFecha.print(fechaInicio.getTime()));
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,0,fechaInicio);
            while ((fechaFinal.after(calendario.toDate())||fechaFinal.compareTo(calendario.toDate())==0) && hoy.after(calendario.toDate())){
                cantidadDeEjecucionesTotales++;
                cantidadDeEjecucionesRealizadas++;
                nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,calendario.toDate(),info,nuevaTransaccionFija.getId());
                viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
                calendario = calendario.plusYears(1);
            }
            Date proximaEjecucion = null;
            if(fechaFinal.after(calendario.toDate())){
                proximaEjecucion = calendario.toDate();
            }
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario = calendario.plusYears(1);
            }
            nuevaTransaccionFija.setCantidadEjecucionesRestantes(cantidadDeEjecucionesTotales-cantidadDeEjecucionesRealizadas);
            nuevaTransaccionFija.setFechaProximaEjecucion(proximaEjecucion);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
        else {
            calendario = LocalDate.parse(formatoDeFecha.print(fechaInicio.getTime()));
            while (fechaFinal.after(calendario.toDate()) || fechaFinal.compareTo(calendario.toDate())==0){
                cantidadDeEjecucionesTotales++;
                calendario = calendario.plusYears(1);
            }
            nuevaTransaccionFija = new TransaccionFija(titulo,etiqueta,precio,categoria,tipo,fechaInicio,info,frecuencia,fechaFinal,cantidadDeEjecucionesTotales,fechaInicio);
            viewModelTransaccionFija.insertarTransaccionFija(nuevaTransaccionFija);
        }
    }
}
