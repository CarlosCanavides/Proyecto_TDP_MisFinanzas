package com.example.proyecto_tdp.verificador_estrategia;

import android.content.Intent;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.Date;
import static android.app.Activity.RESULT_OK;

public class EstrategiaSoloTransacciones extends EstrategiaGeneral{

    protected ViewModelTransaccion viewModelTransaccion;
    protected DateTimeFormatter formatoDeFecha;
    protected String idTransaccionFijaPadre;
    protected Date fecha;

    public EstrategiaSoloTransacciones(ViewModelTransaccion viewModelTransaccion){
        this.viewModelTransaccion = viewModelTransaccion;
        formatoDeFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);
    }

    @Override
    public void verificar(int codigoPedido, int estadoDelResultado, Intent datos) {
        if(datos!=null){
            if(codigoPedido== Constantes.PEDIDO_NUEVA_TRANSACCION){
                if(estadoDelResultado==RESULT_OK){
                    insertarNuevaTransaccion(datos);
                }
            }
            else if(codigoPedido==Constantes.PEDIDO_SET_TRANSACCION){
                if(estadoDelResultado==RESULT_OK){
                    setTransaccion(datos);
                }
                else {
                    eliminarTransaccion(datos);
                }
            }
        }
    }

    @Override
    protected void obtenerDatosPrincipales(Intent datos) {
        super.obtenerDatosPrincipales(datos);
        id = datos.getStringExtra(Constantes.CAMPO_ID);
        idTransaccionFijaPadre = datos.getStringExtra(Constantes.CAMPO_ID_TF_PADRE);
        String fechaTexto = datos.getStringExtra(Constantes.CAMPO_FECHA);
        fecha = formatoDeFecha.parseDateTime(fechaTexto).toDate();
    }

    protected void insertarNuevaTransaccion(Intent datos){
        obtenerDatosPrincipales(datos);
        Transaccion nuevaTransaccion = new Transaccion(titulo,etiqueta,precio,categoria,tipo,fecha,info,null);
        viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
    }

    protected void setTransaccion(Intent datos){
        obtenerDatosPrincipales(datos);
        Transaccion transaccionModificada;
        if(idTransaccionFijaPadre!=null){
            transaccionModificada = new Transaccion(titulo,etiqueta,precio,categoria,tipo,fecha,info,idTransaccionFijaPadre);
        }
        else {
            transaccionModificada = new Transaccion(titulo,etiqueta,precio,categoria,tipo,fecha,info,null);
        }
        transaccionModificada.setId(id);
        viewModelTransaccion.actualizarTransaccion(transaccionModificada);
    }

    protected void eliminarTransaccion(Intent datos){
        id = datos.getStringExtra(Constantes.CAMPO_ID);
        if(id!=null){
            viewModelTransaccion.eliminarTransaccion(id);
        }
    }
}
