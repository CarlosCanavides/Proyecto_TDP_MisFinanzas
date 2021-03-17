package com.example.proyecto_tdp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.views.SeleccionCategoriaDialog;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class NuevaCategoriaActivity extends AppCompatActivity {

    private Button btnConfirmar;
    private Button btnCancelar;
    private RadioButton btnGasto;
    private RadioButton btnIngreso;
    private EditText campoNombre;
    private Button campoCategoriaSup;
    private SeleccionCategoriaDialog seleccionCategoriaDialog;
    private TextView campoColor;
    private AmbilWarnaDialog paletaColores;
    private TextView iconoCategoriaVP;
    private TextView nombreCategoriaVP;
    private int colorActual;
    private List<Categoria> categoriasSuperiores;
    private static final int COLOR_CATEGORIA_POR_DEFECTO = Color.parseColor("#7373FF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_categoria);

        btnConfirmar = findViewById(R.id.btn_confirmar_nueva_categoria);
        btnCancelar = findViewById(R.id.btn_cancelar_nueva_categoria);
        btnGasto = findViewById(R.id.radiobtn_categoria_tipo_gasto);
        btnIngreso = findViewById(R.id.radiobtn_categoria_tipo_ingreso);
        campoNombre = findViewById(R.id.campo_nombre_categoria);
        campoCategoriaSup = findViewById(R.id.campo_categoria_superior);
        campoColor = findViewById(R.id.campo_categoria_color);
        iconoCategoriaVP = findViewById(R.id.vista_previa_icono_categoria);
        nombreCategoriaVP = findViewById(R.id.vista_previa_categoria_nombre);

        inicializarValoresCampos();
        listenerBotonesPrincipales();
        definirSeleccionarColor();
        definirSeleccionarCategoriaSuperior();
    }

    private void inicializarValoresCampos(){
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre_subcategoria");
        String superior = intent.getStringExtra("categoria_superior");
        int color = intent.getIntExtra("color_subcategoria",COLOR_CATEGORIA_POR_DEFECTO);
        String tipo = intent.getStringExtra("tipo_subcategoria");
        if(superior==null) {
            superior = "Seleccionar categoria superior";
        }
        campoNombre.setText(nombre);
        campoCategoriaSup.setText(superior);
        campoColor.setText(color+"");
        colorActual = color;
        if(tipo!=null) {
            if (tipo.equals("gasto")) {
                btnGasto.setChecked(true);
            } else {
                btnIngreso.setChecked(true);
            }
        }
        if(nombre!=null){
            if(nombre.length()!=0) {
                iconoCategoriaVP.setText(nombre.charAt(0) + "");
            }
            nombreCategoriaVP.setText(nombre);
        }
        categoriasSuperiores = new ArrayList<>();
        ViewModelCategoria viewModelCategoria =  ViewModelProviders.of(this).get(ViewModelCategoria.class);
        viewModelCategoria.getAllCategorias().observe(this, new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> categorias) {
                categoriasSuperiores.addAll(categorias);
                seleccionCategoriaDialog.setCategoriasSuperiores(categorias);
            }
        });
    }

    private void listenerBotonesPrincipales(){
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("nombreCategoria",campoNombre.getText().toString());
                intent.putExtra("categoriaSuperior",campoCategoriaSup.getText().toString());
                intent.putExtra("colorCategoria",colorActual);
                String tipoCategoria;
                if(btnGasto.isChecked()){
                    tipoCategoria = "gasto";
                }
                else {
                    tipoCategoria = "ingreso";
                }
                intent.putExtra("tipoC",tipoCategoria);
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
                        Drawable bg = iconoCategoriaVP.getBackground();
                        bg.setColorFilter(color, PorterDuff.Mode.SRC);
                    }
                });
                paletaColores.show();
            }
        });
    }

    private void definirSeleccionarCategoriaSuperior(){
        seleccionCategoriaDialog = new SeleccionCategoriaDialog(categoriasSuperiores,
                                   new SeleccionCategoriaDialog.SeleccionCategoriaListener(){
                                   @Override
                                   public void onSelectCategoria(String idCategoriaSeleccionada){
                                       campoCategoriaSup.setText(idCategoriaSeleccionada);
                                   }
        });
        campoCategoriaSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionCategoriaDialog.show(getSupportFragmentManager(),"Seleccionar categoria superior");
            }
        });
    }
}
