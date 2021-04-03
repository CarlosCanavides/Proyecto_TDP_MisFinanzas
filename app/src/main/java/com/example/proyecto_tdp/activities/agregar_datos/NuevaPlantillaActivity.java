package com.example.proyecto_tdp.activities.agregar_datos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.SeleccionarCategoriaActivity;
import com.example.proyecto_tdp.views.AvisoDialog;
import com.example.proyecto_tdp.views.CalculatorInputDialog;
import com.google.android.material.textfield.TextInputEditText;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NuevaPlantillaActivity extends AppCompatActivity {

    protected TextView campoPrecio;
    protected AutoCompleteTextView campoCategoria;
    protected TextInputEditText campoTitulo;
    protected TextInputEditText campoEtiqueta;
    protected TextInputEditText campoInfo;
    protected RadioButton btnGasto;
    protected RadioButton btnIngreso;
    protected Button btnAceptar;
    protected Button btnCancelar;
    protected NumberFormat formatoNumero;
    protected AvisoDialog avisoDialog;
    protected CalculatorInputDialog calculatorInputDialog;
    protected String idCategoriaElegida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_plantilla);
        campoInfo = findViewById(R.id.set_plantilla_campo_info);
        campoTitulo = findViewById(R.id.set_plantilla_campo_titulo);
        campoPrecio = findViewById(R.id.set_plantilla_campo_precio);
        campoEtiqueta = findViewById(R.id.set_plantilla_campo_etiqueta);
        campoCategoria = findViewById(R.id.set_plantilla_campo_categoria);
        btnGasto = findViewById(R.id.set_plantilla_radiobtn_gasto);
        btnIngreso = findViewById(R.id.set_plantilla_radiobtn_ingreso);
        btnAceptar = findViewById(R.id.set_plantilla_btn_aceptar);
        btnCancelar = findViewById(R.id.set_plantilla_btn_eliminar);
        formatoNumero = NumberFormat.getInstance(new Locale("es", "ES"));
        inicializarValoresCampos();
        definirIngresarMonto();
        definirSeleccionarCategoria();
        listenerBotonesPrincipales();
        definirMensajeDeAviso();
    }

    protected void inicializarValoresCampos(){
        campoInfo.setText("");
        campoTitulo.setText("");
        campoPrecio.setText("0,00");
        campoEtiqueta.setText("");
        campoCategoria.setText(Constantes.SELECCIONAR_CATEGORIA);
        btnGasto.setChecked(true);
        btnIngreso.setChecked(false);
    }

    protected void definirIngresarMonto(){
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

    protected void definirSeleccionarCategoria(){
        campoCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevaPlantillaActivity.this, SeleccionarCategoriaActivity.class);
                startActivityForResult(intent,Constantes.PEDIDO_SELECCIONAR_CATEGORIA);
            }
        });
    }

    protected void listenerBotonesPrincipales(){
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarDatosPrincipales()){
                    String info = campoInfo.getText().toString();
                    String titulo = campoTitulo.getText().toString();
                    String precio = campoPrecio.getText().toString();
                    String etiqueta = campoEtiqueta.getText().toString();
                    String tipo;
                    if(btnGasto.isChecked()){
                        tipo = Constantes.GASTO;
                    }
                    else {
                        tipo = Constantes.INGRESO;
                    }

                    Intent intent = new Intent();
                    intent.putExtra(Constantes.CAMPO_INFO, info);
                    intent.putExtra(Constantes.CAMPO_TIPO, tipo);
                    intent.putExtra(Constantes.CAMPO_TITULO, titulo);
                    intent.putExtra(Constantes.CAMPO_ETIQUETA, etiqueta);
                    if(idCategoriaElegida!=null){
                        intent.putExtra(Constantes.CAMPO_ID_CATEGORIA,idCategoriaElegida);
                    }
                    try {
                        if(tipo.equals(Constantes.INGRESO)){
                            intent.putExtra(Constantes.CAMPO_PRECIO, formatoNumero.parse(precio).floatValue());
                        }
                        else {
                            intent.putExtra(Constantes.CAMPO_PRECIO, formatoNumero.parse(precio).floatValue()*(-1));
                        }
                    }catch(ParseException e){e.printStackTrace();}
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    protected void definirMensajeDeAviso(){
        avisoDialog = new AvisoDialog("Aviso","Faltan completar datos importantes");
    }

    protected boolean verificarDatosPrincipales(){
        boolean verificado = false;
        String precio = campoPrecio.getText().toString();
        float precioFinal = -1f;
        try {
            precioFinal = formatoNumero.parse(precio).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(precioFinal<=0){
            avisoDialog.setMensaje("Falta dato principal: Para ingresar una nueva plantilla debe completar al menos el campo PRECIO");
            avisoDialog.show(getSupportFragmentManager(),"Aviso");
        }
        else {
            verificado = true;
        }
        return verificado;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constantes.PEDIDO_SELECCIONAR_CATEGORIA) {
            if (resultCode == RESULT_OK) {
                idCategoriaElegida = data.getStringExtra(Constantes.ID_CATEGORIA_ELEGIDA);
                String nombreCategoriaElegida = data.getStringExtra(Constantes.NOMBRE_CATEGORIA_ELEGIDA);
                campoCategoria.setText(nombreCategoriaElegida);
            }
        }
    }
}
