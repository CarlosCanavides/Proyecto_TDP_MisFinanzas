package com.example.proyecto_tdp.activities.modificar_datos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.activities.CategoriaActivity;
import com.example.proyecto_tdp.activities.agregar_datos.NuevaTransaccionFijaActivity;

import java.text.ParseException;
import java.util.ArrayList;

public class SetTransaccionFijaActivity extends NuevaTransaccionFijaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void inicializarValoresCampos() {
        Intent intent = getIntent();
        campoInfo.setText(intent.getStringExtra(Constantes.CAMPO_INFO));
        campoFecha.setText(intent.getStringExtra(Constantes.CAMPO_FECHA));
        campoTitulo.setText(intent.getStringExtra(Constantes.CAMPO_TITULO));
        campoPrecio.setText(intent.getStringExtra(Constantes.CAMPO_PRECIO));
        campoEtiqueta.setText(intent.getStringExtra(Constantes.CAMPO_ETIQUETA));
        campoCategoria.setText(intent.getStringExtra(Constantes.CAMPO_CATEGORIA));
        campoFechaFinal.setText(intent.getStringExtra(Constantes.CAMPO_FECHA_FINAL));
        String tipo = intent.getStringExtra(Constantes.CAMPO_TIPO);
        if(btnGasto.getText().toString().equals(tipo)){
            btnGasto.setChecked(true);
        }
        else {
            btnIngreso.setChecked(true);
        }
        btnCancelar.setText("Eliminar");
        ArrayList<String> opcionesFrecuencia = new ArrayList<>();
        opcionesFrecuencia.add(Constantes.SELECCIONAR_FRECUENCIA);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_SOLO_UNA_VEZ);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_MES);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_ANIO);
        ArrayAdapter<String> adapterFrecuencia = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opcionesFrecuencia);
        frecuencia.setAdapter(adapterFrecuencia);
    }

    @Override
    protected void definirSeleccionarCategoria() {
        campoCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetTransaccionFijaActivity.this, CategoriaActivity.class);
                startActivityForResult(intent,Constantes.PEDIDO_SELECCIONAR_CATEGORIA);
            }
        });
    }

    @Override
    protected void listenerBotonesPrincipales() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = campoInfo.getText().toString();
                String titulo = campoTitulo.getText().toString();
                String precio = campoPrecio.getText().toString();
                String etiqueta = campoEtiqueta.getText().toString();
                String categoria = campoCategoria.getText().toString();
                String fechaInicio = campoFecha.getText().toString();
                String fechaFinal = campoFechaFinal.getText().toString();
                String frecuenciaSeleccionada = frecuencia.getSelectedItem().toString();
                String tipo;
                if(btnGasto.isChecked()){
                    tipo = btnGasto.getText().toString();
                }
                else {
                    tipo = btnIngreso.getText().toString();
                }
                int id = getIntent().getIntExtra(Constantes.CAMPO_ID,-1);
                Intent intent = new Intent();
                intent.putExtra(Constantes.CAMPO_INFO, info);
                intent.putExtra(Constantes.CAMPO_TIPO, tipo);
                intent.putExtra(Constantes.CAMPO_TITULO, titulo);
                intent.putExtra(Constantes.CAMPO_ETIQUETA, etiqueta);
                intent.putExtra(Constantes.CAMPO_CATEGORIA, categoria);
                intent.putExtra(Constantes.CAMPO_FECHA, fechaInicio);
                intent.putExtra(Constantes.CAMPO_FECHA_FINAL, fechaFinal);
                intent.putExtra(Constantes.CAMPO_FRECUENCIA, frecuenciaSeleccionada);
                try {
                    if(tipo.equals(Constantes.INGRESO)){
                        intent.putExtra(Constantes.CAMPO_PRECIO, formatoNumero.parse(precio).floatValue());
                    }
                    else {
                        intent.putExtra(Constantes.CAMPO_PRECIO, formatoNumero.parse(precio).floatValue()*(-1));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(id!=-1){
                    intent.putExtra(Constantes.CAMPO_ID,id);
                }
                setResult(RESULT_OK, intent);
                finish();
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