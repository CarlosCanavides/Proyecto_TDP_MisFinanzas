package com.example.proyecto_tdp.activities.modificar_datos;

import androidx.annotation.Nullable;
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
import com.example.proyecto_tdp.views.CalendarioDialog;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.Date;

public class SetTransaccionActivity extends AppCompatActivity {

    private TextView campoPrecio;
    private TextView campoCategoria;
    private TextView campoFecha;
    private EditText campoTitulo;
    private EditText campoEtiqueta;
    private EditText campoInfo;
    private RadioButton btnGasto;
    private RadioButton btnIngreso;
    private Button btnAceptar;
    private Button btnEliminar;
    private CalculatorInputDialog calculatorInputDialog;
    private CalendarioDialog calendarioDialog;
    private DateTimeFormatter formatoFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_transaccion);

        campoInfo = findViewById(R.id.set_transaccion_campo_info);
        campoFecha = findViewById(R.id.set_transaccion_campo_fecha);
        campoTitulo = findViewById(R.id.set_transaccion_campo_titulo);
        campoPrecio = findViewById(R.id.set_transaccion_campo_precio);
        campoEtiqueta = findViewById(R.id.set_transaccion_campo_etiqueta);
        campoCategoria = findViewById(R.id.set_transaccion_campo_categoria);
        btnGasto = findViewById(R.id.set_transaccion_radiobtn_gasto);
        btnIngreso = findViewById(R.id.set_transaccion_radiobtn_ingreso);
        btnAceptar = findViewById(R.id.set_transaccion_btn_aceptar);
        btnEliminar = findViewById(R.id.set_transaccion_btn_eliminar);
        formatoFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);

        inicializarValoresCampos();
        definirIngresarMonto();
        definirSeleccionarFecha();
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
        campoFecha.setText(intent.getStringExtra(Constantes.CAMPO_FECHA));
        campoTitulo.setText(intent.getStringExtra(Constantes.CAMPO_TITULO));
        campoPrecio.setText(intent.getStringExtra(Constantes.CAMPO_PRECIO));
        campoEtiqueta.setText(intent.getStringExtra(Constantes.CAMPO_ETIQUETA));
        campoCategoria.setText(intent.getStringExtra(Constantes.CAMPO_CATEGORIA));
        String tipo = intent.getStringExtra(Constantes.CAMPO_TIPO);
        if(btnGasto.getText().toString().equals(tipo)){
            btnGasto.setChecked(true);
        }
        else {
            btnIngreso.setChecked(true);
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

    private void definirSeleccionarFecha(){
        calendarioDialog = new CalendarioDialog();
        calendarioDialog.setListener(new CalendarioDialog.OnSelectDateListener() {
            @Override
            public void onSelectDate(Date date) throws Exception {
                campoFecha.setText(formatoFecha.print(date.getTime()));
            }
        });
        campoFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarioDialog.show(getSupportFragmentManager(), getClass().getSimpleName());
            }
        });
    }

    private void definirSeleccionarCategoria(){
        campoCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetTransaccionActivity.this, CategoriaActivity.class);
                startActivityForResult(intent,Constantes.PEDIDO_SELECCIONAR_CATEGORIA);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constantes.PEDIDO_SELECCIONAR_CATEGORIA) {
            if (resultCode == RESULT_OK) {
                String idCategoriaElegida = data.getStringExtra("id_categoria_elegida");
                campoCategoria.setText(idCategoriaElegida);
            }
        }
    }
}
