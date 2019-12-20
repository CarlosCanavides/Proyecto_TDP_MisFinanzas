package com.example.proyecto_tdp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.views.CalculatorInputDialog;
import com.example.proyecto_tdp.views.CalendarioDialog;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private Button btnAceptar;
    private Button btnCancelar;

    private static final int PEDIDO_SELECCIONAR_CATEGORIA = 18;
    private CalculatorInputDialog calculatorInputDialog;
    private CalendarioDialog calendarioDialog;
    private DateFormat formatFecha =new SimpleDateFormat(Constantes.FORMATO_FECHA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_transaccion);
        campoPrecio = findViewById(R.id.campo_transaccion_precio);
        campoCategoria = findViewById(R.id.campo_transaccion_categoria);
        campoTitulo = findViewById(R.id.campo_transaccion_titulo);
        campoEtiqueta = findViewById(R.id.campo_transaccion_etiqueta);
        campoFecha = findViewById(R.id.campo_transaccion_fecha);
        campoInfo = findViewById(R.id.campo_transaccion_info);
        btnGasto = findViewById(R.id.radiobtn_transaccion_gasto);
        btnIngreso = findViewById(R.id.radiobtn_transaccion_ingreso);
        btnAceptar = findViewById(R.id.btn_transaccion_aceptar);
        btnCancelar = findViewById(R.id.btn_transaccion_cancelar);
        btnGasto.setChecked(true);

        definirIngresarMonto();
        definirSeleccionarFecha();
        definirSeleccionarCategoria();
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
        campoFecha.setText(formatFecha.format(new Date()));
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

    private void listenerBotonesPrincipales(){
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String precio = campoPrecio.getText().toString();
                String categoria = campoCategoria.getText().toString();
                String titulo = campoTitulo.getText().toString();
                String etiqueta = campoEtiqueta.getText().toString();
                String fecha = campoFecha.getText().toString();
                String info = campoInfo.getText().toString();
                String tipoT;
                if(btnGasto.isChecked()){
                    tipoT = btnGasto.getText().toString();
                }
                else {
                    tipoT = btnIngreso.getText().toString();
                }
                Intent intent = new Intent();
                intent.putExtra("precio", precio);
                intent.putExtra("categoria", categoria);
                intent.putExtra("tipoT", tipoT);
                intent.putExtra("titulo", titulo);
                intent.putExtra("etiqueta", etiqueta);
                intent.putExtra("fecha", fecha);
                intent.putExtra("info", info);
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
