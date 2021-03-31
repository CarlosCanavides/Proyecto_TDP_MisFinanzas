package com.example.proyecto_tdp.activities.modificar_datos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.activities.CategoriaActivity;
import com.example.proyecto_tdp.activities.agregar_datos.NuevaPlantillaActivity;
import java.text.ParseException;

public class SetPlantillaActivity extends NuevaPlantillaActivity {

    protected String idPlantilla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void inicializarValoresCampos() {
        Intent intent = getIntent();
        idPlantilla = intent.getStringExtra(Constantes.CAMPO_ID);
        campoInfo.setText(intent.getStringExtra(Constantes.CAMPO_INFO));
        campoTitulo.setText(intent.getStringExtra(Constantes.CAMPO_TITULO));
        campoPrecio.setText(intent.getStringExtra(Constantes.CAMPO_PRECIO));
        campoEtiqueta.setText(intent.getStringExtra(Constantes.CAMPO_ETIQUETA));
        idCategoriaElegida = intent.getStringExtra(Constantes.CAMPO_ID_CATEGORIA);
        String nombreCategoriaElegida = intent.getStringExtra(Constantes.CAMPO_NOMBRE_CATEGORIA);
        if(idCategoriaElegida==null){
            campoCategoria.setText(Constantes.SELECCIONAR_CATEGORIA);
        }
        else {
            campoCategoria.setText(nombreCategoriaElegida);
        }
        String tipo = intent.getStringExtra(Constantes.CAMPO_TIPO);
        if(Constantes.INGRESO.equals(tipo)){
            btnIngreso.setChecked(true);
        }
        else {
            btnGasto.setChecked(true);
        }
    }

    @Override
    protected void definirSeleccionarCategoria() {
        campoCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetPlantillaActivity.this, CategoriaActivity.class);
                startActivityForResult(intent,Constantes.PEDIDO_SELECCIONAR_CATEGORIA);
            }
        });
    }

    @Override
    protected void listenerBotonesPrincipales() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarDatosPrincipales()){
                    String info = campoInfo.getText().toString();
                    String titulo = campoTitulo.getText().toString();
                    String precio = campoPrecio.getText().toString();
                    String etiqueta = campoEtiqueta.getText().toString();
                    String tipo;
                    if(btnGasto.isChecked()){
                        tipo = Constantes.GASTO;
                    }
                    else {
                        tipo = Constantes.INGRESO;
                    }

                    Intent intent = new Intent();
                    intent.putExtra(Constantes.CAMPO_INFO, info);
                    intent.putExtra(Constantes.CAMPO_TIPO, tipo);
                    intent.putExtra(Constantes.CAMPO_TITULO, titulo);
                    intent.putExtra(Constantes.CAMPO_ETIQUETA, etiqueta);
                    if(idCategoriaElegida!=null){
                        intent.putExtra(Constantes.CAMPO_ID_CATEGORIA,idCategoriaElegida);
                    }
                    try {
                        if(tipo.equals(Constantes.INGRESO)){
                            intent.putExtra(Constantes.CAMPO_PRECIO, formatoNumero.parse(precio).floatValue());
                        }
                        else {
                            intent.putExtra(Constantes.CAMPO_PRECIO, formatoNumero.parse(precio).floatValue()*(-1));
                        }
                    }catch(ParseException e){e.printStackTrace();}
                    intent.putExtra(Constantes.CAMPO_ID,idPlantilla);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}