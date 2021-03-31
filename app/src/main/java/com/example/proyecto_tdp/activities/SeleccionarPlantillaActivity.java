package com.example.proyecto_tdp.activities;

import android.content.Intent;
import android.os.Bundle;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;

public class SeleccionarPlantillaActivity extends PlantillasActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPlantillaClick(int position) {
        Plantilla plantilla = plantillas.get(position);
        Intent intent = new Intent();
        intent.putExtra(Constantes.CAMPO_ID, plantilla.getId());
        intent.putExtra(Constantes.CAMPO_INFO, plantilla.getInfo());
        intent.putExtra(Constantes.CAMPO_TIPO, plantilla.getTipoTransaccion());
        intent.putExtra(Constantes.CAMPO_TITULO, plantilla.getTitulo());
        intent.putExtra(Constantes.CAMPO_PRECIO, String.format( "%.2f", Math.abs(plantilla.getPrecio())));
        intent.putExtra(Constantes.CAMPO_ETIQUETA, plantilla.getEtiqueta());
        intent.putExtra(Constantes.CAMPO_ID_CATEGORIA, plantilla.getCategoria());
        if(plantilla.getCategoria()!=null){
            Categoria categoria = viewModelCategoria.getCategoriaPorID(plantilla.getId());
            intent.putExtra(Constantes.CAMPO_NOMBRE_CATEGORIA,categoria.getNombreCategoria());
        }
        setResult(RESULT_OK,intent);
        finish();
    }
}
