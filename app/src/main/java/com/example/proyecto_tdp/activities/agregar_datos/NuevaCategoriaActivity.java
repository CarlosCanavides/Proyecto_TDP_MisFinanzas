package com.example.proyecto_tdp.activities.agregar_datos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.views.SeleccionCategoriaDialog;
import java.util.ArrayList;
import java.util.List;
import yuku.ambilwarna.AmbilWarnaDialog;

public class NuevaCategoriaActivity extends AppCompatActivity {

    private int colorActual;
    private Button btnCancelar;
    private Button btnConfirmar;
    private Button campoCategoriaSup;
    private RadioButton btnGasto;
    private RadioButton btnIngreso;
    private EditText campoNombre;
    private TextView campoColor;
    private TextView iconoCategoriaVP;
    private TextView nombreCategoriaVP;
    private AmbilWarnaDialog paletaColores;
    private SeleccionCategoriaDialog seleccionCategoriaDialog;
    private List<Categoria> categoriasSuperiores;
    private ViewModelCategoria viewModelCategoria;
    private Observer<List<Categoria>> observador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_categoria);

        btnGasto = findViewById(R.id.radiobtn_categoria_tipo_gasto);
        btnIngreso = findViewById(R.id.radiobtn_categoria_tipo_ingreso);
        btnCancelar = findViewById(R.id.btn_cancelar_nueva_categoria);
        btnConfirmar = findViewById(R.id.btn_confirmar_nueva_categoria);
        campoColor = findViewById(R.id.campo_categoria_color);
        campoNombre = findViewById(R.id.campo_nombre_categoria);
        campoCategoriaSup = findViewById(R.id.campo_categoria_superior);
        iconoCategoriaVP = findViewById(R.id.vista_previa_icono_categoria);
        nombreCategoriaVP = findViewById(R.id.vista_previa_categoria_nombre);

        inicializarValoresCampos();
        listenerBotonesPrincipales();
        definirSeleccionarColor();
        definirSeleccionarCategoriaSuperior();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModelCategoria.eliminarObservador(observador);
    }

    private void inicializarValoresCampos(){
        Intent intent = getIntent();
        String tipo = intent.getStringExtra(Constantes.CAMPO_TIPO_CATEGORIA);
        String nombre = intent.getStringExtra(Constantes.CAMPO_NOMBRE_CATEGORIA);
        String superior = intent.getStringExtra(Constantes.CAMPO_CATEGORIA_SUPERIOR);
        int color = intent.getIntExtra(Constantes.CAMPO_COLOR_CATEGORIA,Constantes.COLOR_CATEGORIA_POR_DEFECTO);
        if(superior==null) {
            superior = "Seleccionar categoria";
        }
        colorActual = color;
        campoColor.setText(color+"");
        campoNombre.setText(nombre);
        campoCategoriaSup.setText(superior);
        if(tipo!=null) {
            if (tipo.equals(Constantes.GASTO)) {
                btnGasto.setChecked(true);
            } else {
                btnIngreso.setChecked(true);
            }
        }
        if(nombre!=null){
            if(nombre.length()!=0) {
                iconoCategoriaVP.setText(nombre.charAt(0)+"");
            }
            nombreCategoriaVP.setText(nombre);
        }
        categoriasSuperiores = new ArrayList<>();
        viewModelCategoria =  ViewModelProviders.of(this).get(ViewModelCategoria.class);
        LiveData<List<Categoria>> liveData = viewModelCategoria.getAllCategorias();
        if(liveData!=null){
            observador = new Observer<List<Categoria>>() {
                @Override
                public void onChanged(List<Categoria> categorias) {
                    for(Categoria categoria : categorias) {
                        if(categoria.getCategoriaSuperior().equals("NULL")) {
                            categoriasSuperiores.add(categoria);
                        }
                    }
                    seleccionCategoriaDialog.setCategoriasSuperiores(categorias);
                }
            };
            liveData.observe(this,observador);
        }
    }

    private void listenerBotonesPrincipales(){
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra(Constantes.CAMPO_COLOR_CATEGORIA,colorActual);
                intent.putExtra(Constantes.CAMPO_NOMBRE_CATEGORIA,campoNombre.getText().toString());
                intent.putExtra(Constantes.CAMPO_CATEGORIA_SUPERIOR,campoCategoriaSup.getText().toString());
                String tipo;
                if(btnGasto.isChecked()){
                    tipo = Constantes.GASTO;
                }
                else {
                    tipo = Constantes.INGRESO;
                }
                intent.putExtra(Constantes.CAMPO_TIPO_CATEGORIA,tipo);
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
                    public void onCancel(AmbilWarnaDialog dialog) {}

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
                seleccionCategoriaDialog.show(getSupportFragmentManager(),"Seleccionar categoria");
            }
        });
    }
}
