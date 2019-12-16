package com.example.proyecto_tdp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.proyecto_tdp.R;

public class SetTransaccionActivity extends AppCompatActivity {

    private TextView campoPrecio;
    private TextView campoCategoria;
    private EditText campoTitulo;
    private EditText campoEtiqueta;
    private EditText campoFecha;
    private EditText campoInfo;
    private RadioButton btnGasto;
    private RadioButton btnIngreso;
    private Button btnAceptar;
    private Button btnEliminar;

    private static final int PEDIDO_SELECCIONAR_CATEGORIA = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_transaccion);

        campoPrecio = findViewById(R.id.set_campo_precio);
        campoCategoria = findViewById(R.id.set_campo_categoria);
        campoTitulo = findViewById(R.id.set_campo_titulo);
        campoEtiqueta = findViewById(R.id.set_campo_etiqueta);
        campoFecha = findViewById(R.id.set_campo_fecha);
        campoInfo = findViewById(R.id.set_campo_info);
        btnGasto = findViewById(R.id.radiobtn_setTransaccion_gasto);
        btnIngreso = findViewById(R.id.radiobtn_setTransaccion_ingreso);
        btnAceptar = findViewById(R.id.btn_aceptar_set);
        btnEliminar = findViewById(R.id.btn_eliminar);

        inicializarValoresCampos();

        campoCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetTransaccionActivity.this, CategoriaActivity.class);
                startActivityForResult(intent,PEDIDO_SELECCIONAR_CATEGORIA);
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String precio = campoPrecio.getText().toString();
                String categoria = campoCategoria.getText().toString();
                String tipoT;
                if(btnGasto.isChecked()){
                    tipoT = btnGasto.getText().toString();
                }
                else {
                    tipoT = btnIngreso.getText().toString();
                }
                String titulo = campoTitulo.getText().toString();
                String etiqueta = campoEtiqueta.getText().toString();
                String fecha = campoFecha.getText().toString();
                String info = campoInfo.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("precio", precio);
                intent.putExtra("categoria", categoria);
                intent.putExtra("tipoT", tipoT);
                intent.putExtra("titulo", titulo);
                intent.putExtra("etiqueta", etiqueta);
                intent.putExtra("fecha", fecha);
                intent.putExtra("info", info);

                int id = getIntent().getIntExtra("id",-1);
                if(id!=-1){
                    intent.putExtra("id",id);
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

    private void inicializarValoresCampos(){
        Intent intent = getIntent();
        campoPrecio.setText(intent.getStringExtra("precio"));
        campoCategoria.setText(intent.getStringExtra("categoria"));
        campoTitulo.setText(intent.getStringExtra("titulo"));
        campoEtiqueta.setText(intent.getStringExtra("etiqueta"));
        campoFecha.setText(intent.getStringExtra("fecha"));
        campoInfo.setText(intent.getStringExtra("info"));
        String tipoT = intent.getStringExtra("tipoT");
        if(btnGasto.getText().toString().equals(tipoT)){
            btnGasto.setChecked(true);
        }
        else {
            btnIngreso.setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
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
