package com.example.proyecto_tdp.activities.modificar_datos;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.CategoriaActivity;
import com.example.proyecto_tdp.views.CalculatorInputDialog;

public class SetPlantillaActivity extends AppCompatActivity {

    private TextView campoPrecio;
    private TextView campoCategoria;
    private EditText campoTitulo;
    private EditText campoEtiqueta;
    private EditText campoInfo;
    private RadioButton btnGasto;
    private RadioButton btnIngreso;
    private Button btnAceptar;
    private Button btnEliminar;
    private CalculatorInputDialog calculatorInputDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_plantilla);

        campoPrecio = findViewById(R.id.set_plantilla_campo_precio);
        campoCategoria = findViewById(R.id.set_plantilla_campo_categoria);
        campoTitulo = findViewById(R.id.set_plantilla_campo_titulo);
        campoEtiqueta = findViewById(R.id.set_plantilla_campo_etiqueta);
        campoInfo = findViewById(R.id.set_plantilla_campo_info);
        btnGasto = findViewById(R.id.set_plantilla_radiobtn_gasto);
        btnIngreso = findViewById(R.id.set_plantilla_radiobtn_ingreso);
        btnAceptar = findViewById(R.id.set_plantilla_btn_aceptar);
        btnEliminar = findViewById(R.id.set_plantilla_btn_eliminar);

        inicializarValoresCampos();
        definirIngresarMonto();
        definirSeleccionarCategoria();
        listenerBotonesPrincipales();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void inicializarValoresCampos(){
        Intent intent = getIntent();
        campoInfo.setText(intent.getStringExtra(Constantes.CAMPO_INFO));
        campoTitulo.setText(intent.getStringExtra(Constantes.CAMPO_TITULO));
        campoPrecio.setText(intent.getStringExtra(Constantes.CAMPO_PRECIO));
        campoEtiqueta.setText(intent.getStringExtra(Constantes.CAMPO_ETIQUETA));
        campoCategoria.setText(intent.getStringExtra(Constantes.CAMPO_CATEGORIA));
        String tipo = intent.getStringExtra(Constantes.CAMPO_TIPO);
        if(Constantes.INGRESO.equals(tipo)){
            btnIngreso.setChecked(true);
        }
        else {
            btnGasto.setChecked(true);
        }
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

    private void definirSeleccionarCategoria(){
        campoCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetPlantillaActivity.this, CategoriaActivity.class);
                startActivityForResult(intent,Constantes.PEDIDO_SELECCIONAR_CATEGORIA);
            }
        });
    }

    private void listenerBotonesPrincipales(){
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = campoInfo.getText().toString();
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
                intent.putExtra(Constantes.CAMPO_TITULO, titulo);
                intent.putExtra(Constantes.CAMPO_PRECIO, precio);
                intent.putExtra(Constantes.CAMPO_ETIQUETA, etiqueta);
                intent.putExtra(Constantes.CAMPO_CATEGORIA, categoria);
                int id = getIntent().getIntExtra(Constantes.CAMPO_ID,-1);
                if(id!=-1){
                    intent.putExtra(Constantes.CAMPO_ID,id);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}