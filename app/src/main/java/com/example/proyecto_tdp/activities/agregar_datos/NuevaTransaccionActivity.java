package com.example.proyecto_tdp.activities.agregar_datos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.CategoriaActivity;
import com.example.proyecto_tdp.views.CalculatorInputDialog;
import com.example.proyecto_tdp.views.CalendarioDialog;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NuevaTransaccionActivity extends AppCompatActivity{

    private TextView campoPrecio;
    private TextView campoCategoria;
    private TextView campoFecha;
    private EditText campoTitulo;
    private EditText campoEtiqueta;
    private EditText campoInfo;
    private RadioButton btnGasto;
    private RadioButton btnIngreso;
    private CheckBox btnPlantilla;
    private CheckBox btnTransaccionFija;
    private Spinner listaFrecuencias;
    private TextView campoFechaFinal;
    private LinearLayout panelFechaFinal;
    private Button btnAceptar;
    private Button btnCancelar;

    private static final int PEDIDO_SELECCIONAR_CATEGORIA = 18;
    private CalculatorInputDialog calculatorInputDialog;
    private CalendarioDialog calendarioDialog;
    private CalendarioDialog calendarioFechaFinalDialog;
    private DateFormat formatFecha = new SimpleDateFormat(Constantes.FORMATO_FECHA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_transaccion);
        campoPrecio = findViewById(R.id.campo_transaccion_precio);
        campoCategoria = findViewById(R.id.campo_transaccion_categoria);
        campoTitulo = findViewById(R.id.campo_transaccion_titulo);
        campoEtiqueta = findViewById(R.id.campo_transaccion_etiqueta);
        campoFecha = findViewById(R.id.campo_transaccion_fecha);
        campoFechaFinal = findViewById(R.id.campo_transaccion_fecha_final);
        campoInfo = findViewById(R.id.campo_transaccion_info);
        btnGasto = findViewById(R.id.radiobtn_transaccion_gasto);
        btnIngreso = findViewById(R.id.radiobtn_transaccion_ingreso);
        btnPlantilla = findViewById(R.id.chk_agregar_plantilla);
        btnTransaccionFija = findViewById(R.id.chk_agregar_transaccion_fija);
        listaFrecuencias = findViewById(R.id.lista_desplegable_frecuencia);
        panelFechaFinal = findViewById(R.id.panel_transaccion_fecha_final);
        btnAceptar = findViewById(R.id.btn_transaccion_aceptar);
        btnCancelar = findViewById(R.id.btn_transaccion_cancelar);
        panelFechaFinal.setVisibility(View.GONE);
        listaFrecuencias.setVisibility(View.GONE);
        btnGasto.setChecked(true);

        definirIngresarMonto();
        definirSeleccionarFecha();
        definirSeleccionarCategoria();
        definirOpcionesAvanzadas();
        listenerBotonesPrincipales();
    }

    private void definirIngresarMonto(){
        calculatorInputDialog = new CalculatorInputDialog(this);
        calculatorInputDialog.setPositiveButton(new CalculatorInputDialog.OnInputDoubleListener() {
            @Override
            public boolean onInputDouble(AlertDialog dialog, Double value) {
                campoPrecio.setText(String.format( "%.2f", value));
                return false;
            }
        });
        campoPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorInputDialog.show();
            }
        });
    }

    private void definirSeleccionarFecha(){
        calendarioDialog = new CalendarioDialog();
        calendarioDialog.setListener(new CalendarioDialog.OnSelectDateListener() {
            @Override
            public void onSelectDate(Date date) throws Exception {
                campoFecha.setText(formatFecha.format(date));
            }
        });
        campoFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarioDialog.show(getSupportFragmentManager(), getClass().getSimpleName());
            }
        });
        campoFecha.setText(formatFecha.format(Calendar.getInstance().getTime()));
    }

    private void definirSeleccionarCategoria(){
        campoCategoria.setText("Seleccionar categoria");
        campoCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevaTransaccionActivity.this, CategoriaActivity.class);
                startActivityForResult(intent,PEDIDO_SELECCIONAR_CATEGORIA);
            }
        });
    }

    private void definirOpcionesAvanzadas(){
        ArrayList<String> opcionesFrecuencia = new ArrayList<>();
        opcionesFrecuencia.add(Constantes.SELECCIONAR_FRECUENCIA);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_SOLO_UNA_VEZ);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_MES);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_ANIO);
        ArrayAdapter<CharSequence> adapterFrecuencia = new ArrayAdapter(this, android.R.layout.simple_list_item_1, opcionesFrecuencia);
        listaFrecuencias.setAdapter(adapterFrecuencia);
        btnPlantilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTransaccionFija.setChecked(false);
                if(btnPlantilla.isChecked()) {
                    panelFechaFinal.setVisibility(View.GONE);
                    listaFrecuencias.setVisibility(View.GONE);
                    campoFecha.setText("No necesita fecha");
                    campoFecha.setClickable(false);
                    campoFechaFinal.setText("Seleccionar fecha final");
                }
                else {
                    campoFecha.setText(formatFecha.format(Calendar.getInstance().getTime()));
                    campoFecha.setClickable(true);
                }
            }
        });
        btnTransaccionFija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlantilla.setChecked(false);
                if(btnTransaccionFija.isChecked()){
                    panelFechaFinal.setVisibility(View.VISIBLE);
                    listaFrecuencias.setVisibility(View.VISIBLE);
                    listaFrecuencias.setSelection(0);
                    campoFecha.setText(formatFecha.format(Calendar.getInstance().getTime()));
                    campoFecha.setClickable(true);
                }
                else {
                    panelFechaFinal.setVisibility(View.GONE);
                    listaFrecuencias.setVisibility(View.GONE);
                    campoFechaFinal.setText("Seleccionar fecha final");
                }
            }
        });
        calendarioFechaFinalDialog = new CalendarioDialog();
        calendarioFechaFinalDialog.setListener(new CalendarioDialog.OnSelectDateListener() {
            @Override
            public void onSelectDate(Date date) throws Exception {
                campoFechaFinal.setText(formatFecha.format(date));
            }
        });
        campoFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarioFechaFinalDialog.show(getSupportFragmentManager(), getClass().getSimpleName());
            }
        });
    }

    private void listenerBotonesPrincipales(){
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = campoInfo.getText().toString();
                String fecha = campoFecha.getText().toString();
                String titulo = campoTitulo.getText().toString();
                String precio = campoPrecio.getText().toString();
                String etiqueta = campoEtiqueta.getText().toString();
                String categoria = campoCategoria.getText().toString();
                String tipo;
                if(btnGasto.isChecked()){
                    tipo = btnGasto.getText().toString();
                }
                else {
                    tipo = btnIngreso.getText().toString();
                }
                Intent intent = new Intent();
                intent.putExtra(Constantes.CAMPO_INFO, info);
                intent.putExtra(Constantes.CAMPO_TIPO, tipo);
                intent.putExtra(Constantes.CAMPO_FECHA, fecha);
                intent.putExtra(Constantes.CAMPO_TITULO, titulo);
                intent.putExtra(Constantes.CAMPO_PRECIO, precio);
                intent.putExtra(Constantes.CAMPO_ETIQUETA, etiqueta);
                intent.putExtra(Constantes.CAMPO_CATEGORIA, categoria);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PEDIDO_SELECCIONAR_CATEGORIA) {
            if (resultCode == RESULT_OK) {
                String idCategoriaElegida = data.getStringExtra("id_categoria_elegida");
                campoCategoria.setText(idCategoriaElegida);
            }
        }
    }
}
