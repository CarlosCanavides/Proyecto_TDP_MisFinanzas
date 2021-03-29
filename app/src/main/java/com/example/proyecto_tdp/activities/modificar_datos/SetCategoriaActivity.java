package com.example.proyecto_tdp.activities.modificar_datos;

import android.content.Intent;
import android.os.Bundle;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.activities.agregar_datos.NuevaCategoriaActivity;

public class SetCategoriaActivity extends NuevaCategoriaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void inicializarValoresCampos() {
        Intent intent = getIntent();
        String tipo = intent.getStringExtra(Constantes.CAMPO_TIPO_CATEGORIA);
        String nombre = intent.getStringExtra(Constantes.CAMPO_NOMBRE_CATEGORIA);
        String superior = intent.getStringExtra(Constantes.CAMPO_CATEGORIA_SUPERIOR);
        int color = intent.getIntExtra(Constantes.CAMPO_COLOR_CATEGORIA,Constantes.COLOR_CATEGORIA_POR_DEFECTO);
        if(superior==null) {
            superior = "Seleccionar categoria";
        }
        colorActual = color;
        campoColor.setText(color+"");
        campoNombre.setText(nombre);
        campoCategoriaSup.setText(superior);
        if(tipo!=null) {
            if (tipo.equals(Constantes.GASTO)) {
                btnGasto.setChecked(true);
            } else {
                btnIngreso.setChecked(true);
            }
        }
        if(nombre!=null){
            if(nombre.length()!=0) {
                iconoCategoriaVP.setText(nombre.charAt(0)+"");
            }
            nombreCategoriaVP.setText(nombre);
        }
        btnCancelar.setText("Eliminar");
    }
}
