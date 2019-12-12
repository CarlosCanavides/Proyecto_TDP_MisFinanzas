package com.example.proyecto_tdp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.proyecto_tdp.R;

public class SetTransaccionActivity extends AppCompatActivity {

    private Button btnAceptar;
    private Button btnEliminar;
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
        setContentView(R.layout.activity_set_transaccion);

        campoPrecio = findViewById(R.id.set_campo_precio);
        campoCategoria = findViewById(R.id.set_campo_categoria);
        campoTipoTransaccion = findViewById(R.id.set_campo_tipo_transaccion);
        campoTitulo = findViewById(R.id.set_campo_titulo);
        campoEtiqueta = findViewById(R.id.set_campo_etiqueta);
        campoFecha = findViewById(R.id.set_campo_fecha);
        campoInfo = findViewById(R.id.set_campo_info);

        // Inicialización de los valores de los campos de una transaccion
        Intent intent = getIntent();
        campoPrecio.setText(intent.getStringExtra("precio"));
        campoCategoria.setText(intent.getStringExtra("categoria"));
        campoTipoTransaccion.setText(intent.getStringExtra("tipoT"));
        campoTitulo.setText(intent.getStringExtra("titulo"));
        campoEtiqueta.setText(intent.getStringExtra("etiqueta"));
        campoFecha.setText(intent.getStringExtra("fecha"));
        campoInfo.setText(intent.getStringExtra("info"));

        btnAceptar = findViewById(R.id.btn_aceptar_set);
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

                int id = getIntent().getIntExtra("id",-1);
                if(id!=-1){
                    intent.putExtra("id",id);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnEliminar = findViewById(R.id.btn_eliminar);
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
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
