package com.example.proyecto_tdp.verificador_estrategia;

import android.content.Intent;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import com.example.proyecto_tdp.view_models.ViewModelPlantilla;
import static android.app.Activity.RESULT_OK;

public class EstrategiaSoloPlantillas extends EstrategiaGeneral {

    protected ViewModelPlantilla viewModelPlantilla;

    public EstrategiaSoloPlantillas(ViewModelPlantilla vmP) {
        viewModelPlantilla = vmP;
    }

    public void verificar(int codigoPedido, int estadoDelResultado, Intent datos){
        if(datos!=null){
            if(codigoPedido == Constantes.PEDIDO_NUEVA_PLANTILLA){
                if(estadoDelResultado==RESULT_OK){
                    insertarNuevaPlantilla(datos);
                }
            }
            else if(codigoPedido ==  Constantes.PEDIDO_SET_PLANTILLA){
                if(estadoDelResultado==RESULT_OK){
                    setPlantilla(datos);
                }
                else {
                    eliminarPlantilla(datos);
                }
            }
        }
    }

    private void insertarNuevaPlantilla(Intent datos){
        obtenerDatosPrincipales(datos);
        Plantilla nuevaPlantilla = new Plantilla(titulo,precio,etiqueta,categoria,tipo,info);
        viewModelPlantilla.insertarPlantilla(nuevaPlantilla);
    }

    private void setPlantilla(Intent datos){
        obtenerDatosPrincipales(datos);
        id = datos.getIntExtra(Constantes.CAMPO_ID,0);
        Plantilla plantillaModificada = new Plantilla(titulo,precio,etiqueta,categoria,tipo,info);
        plantillaModificada.setId(id);
        viewModelPlantilla.actualizarPlantilla(plantillaModificada);
    }

    private void eliminarPlantilla(Intent datos){
        obtenerDatosPrincipales(datos);
        id = datos.getIntExtra(Constantes.CAMPO_ID,0);
        Plantilla plantillaEliminada = new Plantilla(titulo,precio,etiqueta,categoria,tipo,info);
        plantillaEliminada.setId(id);
        viewModelPlantilla.eliminarPlantilla(plantillaEliminada);
    }
}
