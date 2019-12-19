package com.example.proyecto_tdp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.proyecto_tdp.R;
import yuku.ambilwarna.AmbilWarnaDialog;

public class NuevaCategoriaActivity extends AppCompatActivity {

    private Button btnConfirmar;
    private EditText campoNombre;
    private EditText campoCategoriaSup;
    private TextView campoColor;
    private EditText campoTipoT;
    private AmbilWarnaDialog paletaColores;
    private int colorActual = Color.parseColor("#7373FF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_categoria);

        btnConfirmar = findViewById(R.id.btn_confirmar_nueva_categoria);
        campoNombre = findViewById(R.id.campo_nombre_categoria);
        campoCategoriaSup = findViewById(R.id.campo_categoria_superior);
        campoColor = findViewById(R.id.campo_categoria_color);
        campoTipoT = findViewById(R.id.campo_categoria_tipoT);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre_subcategoria");
        String superior = intent.getStringExtra("categoria_superior");
        String color = intent.getStringExtra("color_subcategoria");
        String tipo = intent.getStringExtra("tipo_subcategoria");
        campoNombre.setText(nombre);
        campoCategoriaSup.setText(superior);
        campoColor.setText(color);
        campoTipoT.setText(tipo);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("nombreCategoria",campoNombre.getText().toString());
                intent.putExtra("categoriaSuperior",campoCategoriaSup.getText().toString());
                intent.putExtra("colorCategoria",colorActual);
                intent.putExtra("tipoC",campoTipoT.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        definirSeleccionarColor();
    }

    private void definirSeleccionarColor(){
        final Context context = this;
        campoColor.setText(colorActual+"");
        campoColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paletaColores = new AmbilWarnaDialog(context, colorActual, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        colorActual = color;
                        campoColor.setText(""+color);
                    }
                });
                paletaColores.show();
            }
        });
    }
}
