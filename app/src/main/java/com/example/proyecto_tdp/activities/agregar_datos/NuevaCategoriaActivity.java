package com.example.proyecto_tdp.activities.agregar_datos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.views.AvisoDialog;
import com.example.proyecto_tdp.views.SeleccionCategoriaDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import yuku.ambilwarna.AmbilWarnaDialog;

public class NuevaCategoriaActivity extends AppCompatActivity {

    protected int colorActual;
    protected Button btnCancelar;
    protected Button btnConfirmar;
    protected AutoCompleteTextView campoCategoriaSup;
    protected RadioButton btnGasto;
    protected RadioButton btnIngreso;
    protected TextInputEditText campoNombre;
    protected TextView campoColor;
    protected TextView iconoCategoriaVP;
    protected TextView nombreCategoriaVP;
    protected AmbilWarnaDialog paletaColores;
    protected SeleccionCategoriaDialog seleccionCategoriaDialog;
    protected AvisoDialog avisoDialog;
    protected List<Categoria> categoriasSuperiores;
    protected ViewModelCategoria viewModelCategoria;
    protected String idCategoriaSuperior;

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
        inicializarViewModel();
        listenerBotonesPrincipales();
        definirSeleccionarColor();
        definirSeleccionarCategoriaSuperior();
        definirMensajeDeAviso();
    }

    protected void inicializarValoresCampos(){
        colorActual = Constantes.COLOR_CATEGORIA_POR_DEFECTO;
        campoColor.setText(colorActual+"");
        campoNombre.setText("");
        campoCategoriaSup.setText(Constantes.SELECCIONAR_CATEGORIA);
        btnGasto.setChecked(true);
        btnIngreso.setChecked(false);
        iconoCategoriaVP.setText("");
        nombreCategoriaVP.setText("");
    }

    protected void inicializarViewModel(){
        categoriasSuperiores = new ArrayList<>();
        viewModelCategoria =  new ViewModelProvider(this).get(ViewModelCategoria.class);
        viewModelCategoria.getAllCategorias().observe(this,new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> todasLasCategorias) {
                categoriasSuperiores.clear();
                if(todasLasCategorias!=null){
                    for(Categoria categoria : todasLasCategorias) {
                        if(categoria.getCategoriaSuperior()==null) {
                            categoriasSuperiores.add(categoria);
                        }
                    }
                    seleccionCategoriaDialog.setCategoriasSuperiores(categoriasSuperiores);
                }
            }
        });
    }

    protected void listenerBotonesPrincipales(){
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarDatosPrincipales()){
                    Intent intent = new Intent();
                    intent.putExtra(Constantes.CAMPO_COLOR_CATEGORIA,colorActual);
                    intent.putExtra(Constantes.CAMPO_NOMBRE_CATEGORIA,campoNombre.getText().toString());
                    String tipo;
                    if(btnGasto.isChecked()){
                        tipo = Constantes.GASTO;
                    }
                    else {
                        tipo = Constantes.INGRESO;
                    }
                    intent.putExtra(Constantes.CAMPO_TIPO_CATEGORIA,tipo);
                    if(idCategoriaSuperior!=null){
                        intent.putExtra(Constantes.CAMPO_ID_CATEGORIA_SUPERIOR,idCategoriaSuperior);
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });
    }

    protected void definirSeleccionarColor(){
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

    protected void definirSeleccionarCategoriaSuperior(){
        seleccionCategoriaDialog = new SeleccionCategoriaDialog(categoriasSuperiores,
                                   new SeleccionCategoriaDialog.SeleccionCategoriaListener(){
                                   @Override
                                   public void onSelectCategoria(String idCategoriaSeleccionada, String nombreCategoriaSeleccionada){
                                       idCategoriaSuperior = idCategoriaSeleccionada;
                                       campoCategoriaSup.setText(nombreCategoriaSeleccionada);
                                   }
        });
        campoCategoriaSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionCategoriaDialog.show(getSupportFragmentManager(),"Seleccionar categoria");
            }
        });
    }

    protected void definirMensajeDeAviso(){
        avisoDialog = new AvisoDialog("Aviso","Faltan completar datos importantes");
    }

    protected boolean verificarDatosPrincipales(){
        boolean verificado = false;
        String nombre = campoNombre.getText().toString();
        if(nombre.equals("")){
            avisoDialog.setMensaje("Falta dato Principal: Para ingresar una nueva categoria debe completar el campo TITULO");
            avisoDialog.show(getSupportFragmentManager(),"Aviso");
        }
        else {
            verificado = true;
        }
        return verificado;
    }
}
