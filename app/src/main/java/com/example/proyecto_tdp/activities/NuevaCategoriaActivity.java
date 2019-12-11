package com.example.proyecto_tdp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.proyecto_tdp.R;

public class NuevaCategoriaActivity extends AppCompatActivity {

    private Button btnConfirmar;
    private EditText campoNombre;
    private EditText campoCategoriaSup;
    private EditText campoColor;
    private EditText campoTipoT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_categoria);

        btnConfirmar = findViewById(R.id.btn_confirmar_nueva_categoria);
        campoNombre = findViewById(R.id.campo_nombre_categoria);
        campoCategoriaSup = findViewById(R.id.campo_categoria_superior);
        campoColor = findViewById(R.id.campo_categoria_color);
        campoTipoT = findViewById(R.id.campo_categoria_tipoT);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("nombreCategoria",campoNombre.getText().toString());
                intent.putExtra("categoriaSuperior",campoCategoriaSup.getText().toString());
                intent.putExtra("colorCategoria",campoColor.getText().toString());
                intent.putExtra("tipoC",campoTipoT.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
