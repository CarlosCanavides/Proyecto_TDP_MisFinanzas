package com.example.proyecto_tdp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NuevaTransaccionActivity extends AppCompatActivity {

    private Button btnAceptar;
    private Button btnCancelar;
    private EditText campoPrecio;
    private EditText campoCategoria;
    private EditText campoTipoTransaccion;
    private EditText campoTitulo;
    private EditText campoEtiqueta;
    private EditText campoFecha;
    private EditText campoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_transaccion);

        campoPrecio = findViewById(R.id.campo_transaccion_precio);
        campoCategoria = findViewById(R.id.campo_transaccion_categoria);
        campoTipoTransaccion = findViewById(R.id.campo_tipo_transaccion);
        campoTitulo = findViewById(R.id.campo_transaccion_titulo);
        campoEtiqueta = findViewById(R.id.campo_transaccion_etiqueta);
        campoFecha = findViewById(R.id.campo_transaccion_fecha);
        campoInfo = findViewById(R.id.campo_transaccion_info);

        btnAceptar = findViewById(R.id.btn_transaccion_aceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String precio = campoPrecio.getText().toString();
                String categoria = campoCategoria.getText().toString();
                String tipoT = campoTipoTransaccion.getText().toString();
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
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        btnCancelar = findViewById(R.id.btn_transaccion_cancelar);
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
