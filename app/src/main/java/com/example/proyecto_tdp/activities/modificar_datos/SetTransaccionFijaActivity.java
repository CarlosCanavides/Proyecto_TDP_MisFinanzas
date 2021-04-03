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

    protected String precioAnterior;
    protected String idCategoriaAnterior;
    protected String tipoAnterior;
    protected String tituloAnterior;
    protected String etiquetaAnterior;
    protected String infoAnterior;
    protected String fechaInicioAnterior;
    protected String fechaFinalAnterior;
    protected String frecuenciaAnterior;
    protected String idTransaccionFija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void inicializarValoresCampos() {
        Intent intent = getIntent();
        idTransaccionFija = intent.getStringExtra(Constantes.CAMPO_ID);
        idCategoriaAnterior = intent.getStringExtra(Constantes.CAMPO_ID_CATEGORIA);
        idCategoriaElegida = idCategoriaAnterior;
        tipoAnterior = intent.getStringExtra(Constantes.CAMPO_TIPO);
        infoAnterior = intent.getStringExtra(Constantes.CAMPO_INFO);
        precioAnterior = intent.getStringExtra(Constantes.CAMPO_PRECIO);
        tituloAnterior = intent.getStringExtra(Constantes.CAMPO_TITULO);
        etiquetaAnterior = intent.getStringExtra(Constantes.CAMPO_ETIQUETA);
        fechaInicioAnterior = intent.getStringExtra(Constantes.CAMPO_FECHA);
        fechaFinalAnterior = intent.getStringExtra(Constantes.CAMPO_FECHA_FINAL);
        frecuenciaAnterior = intent.getStringExtra(Constantes.CAMPO_FRECUENCIA);
        String nombreCategoriaAnterior = intent.getStringExtra(Constantes.CAMPO_NOMBRE_CATEGORIA);
        campoInfo.setText(infoAnterior);
        campoTitulo.setText(tituloAnterior);
        campoPrecio.setText(precioAnterior);
        campoFecha.setText(fechaInicioAnterior);
        campoEtiqueta.setText(etiquetaAnterior);
        campoFechaFinal.setText(fechaFinalAnterior);
        if(idCategoriaAnterior==null){
            campoCategoria.setText(Constantes.SELECCIONAR_CATEGORIA);
        }
        else {
            campoCategoria.setText(nombreCategoriaAnterior);
        }
        if(tipoAnterior.equals(Constantes.GASTO)){
            btnGasto.setChecked(true);
            btnIngreso.setChecked(false);
        }
        else {
            btnGasto.setChecked(false);
            btnIngreso.setChecked(true);
        }
        btnCancelar.setText("Eliminar");
        ArrayList<String> opcionesFrecuencia = new ArrayList<>();
        opcionesFrecuencia.add(Constantes.SELECCIONAR_FRECUENCIA);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_SOLO_UNA_VEZ);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_A_LA_SEMANA);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_MES);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_ANIO);
        ArrayAdapter<String> adapterFrecuencia = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opcionesFrecuencia);
        frecuencia.setAdapter(adapterFrecuencia);

        if(frecuenciaAnterior.equals(Constantes.FRECUENCIA_SOLO_UNA_VEZ)){
            frecuencia.setSelection(1);
        }else if(frecuenciaAnterior.equals(Constantes.FRECUENCIA_UNA_VEZ_A_LA_SEMANA)){
            frecuencia.setSelection(2);
        }else if(frecuenciaAnterior.equals(Constantes.FRECUENCIA_UNA_VEZ_AL_MES)){
            frecuencia.setSelection(3);
        }else if(frecuenciaAnterior.equals(Constantes.FRECUENCIA_UNA_VEZ_AL_ANIO)){
            frecuencia.setSelection(4);
        }else {
            frecuencia.setSelection(0);
        }
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
                if(verificarDatosPrincipales()){
                    String info = campoInfo.getText().toString();
                    String titulo = campoTitulo.getText().toString();
                    String precio = campoPrecio.getText().toString();
                    String etiqueta = campoEtiqueta.getText().toString();
                    String fechaInicio = campoFecha.getText().toString();
                    String fechaFinal = campoFechaFinal.getText().toString();
                    String frecuenciaSeleccionada = frecuencia.getText().toString();
                    String tipo;
                    if(btnGasto.isChecked()){
                        tipo = btnGasto.getText().toString();
                    }
                    else {
                        tipo = btnIngreso.getText().toString();
                    }

                    Intent intent = new Intent();
                    insertarDatosAnteriores(intent);
                    intent.putExtra(Constantes.CAMPO_INFO, info);
                    intent.putExtra(Constantes.CAMPO_TIPO, tipo);
                    intent.putExtra(Constantes.CAMPO_TITULO, titulo);
                    intent.putExtra(Constantes.CAMPO_ETIQUETA, etiqueta);
                    intent.putExtra(Constantes.CAMPO_FECHA, fechaInicio);
                    intent.putExtra(Constantes.CAMPO_FECHA_FINAL, fechaFinal);
                    intent.putExtra(Constantes.CAMPO_FRECUENCIA, frecuenciaSeleccionada);
                    if(idCategoriaElegida!=null){
                        intent.putExtra(Constantes.CAMPO_ID_CATEGORIA,idCategoriaElegida);
                    }
                    if(idTransaccionFija!=null){
                        intent.putExtra(Constantes.CAMPO_ID,idTransaccionFija);
                    }
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

    private void insertarDatosAnteriores(Intent intent){
        intent.putExtra(Constantes.CAMPO_INFO_ANTERIOR, infoAnterior);
        intent.putExtra(Constantes.CAMPO_TIPO_ANTERIOR, tipoAnterior);
        intent.putExtra(Constantes.CAMPO_TITULO_ANTERIOR, tituloAnterior);
        intent.putExtra(Constantes.CAMPO_ETIQUETA_ANTERIOR, etiquetaAnterior);
        intent.putExtra(Constantes.CAMPO_FECHA_INICIO_ANTERIOR, fechaInicioAnterior);
        intent.putExtra(Constantes.CAMPO_FECHA_FINAL_ANTERIOR, fechaFinalAnterior);
        intent.putExtra(Constantes.CAMPO_FRECUENCIA_ANTERIOR, frecuenciaAnterior);
        if(idCategoriaAnterior!=null){
            intent.putExtra(Constantes.CAMPO_ID_CATEGORIA_ANTERIOR, idCategoriaAnterior);
        }
        try {
            if(tipoAnterior.equals(Constantes.INGRESO)){
                intent.putExtra(Constantes.CAMPO_PRECIO_ANTERIOR, formatoNumero.parse(precioAnterior).floatValue());
            }
            else {
                intent.putExtra(Constantes.CAMPO_PRECIO_ANTERIOR, formatoNumero.parse(precioAnterior).floatValue()*(-1));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
