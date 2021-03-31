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
import com.example.proyecto_tdp.activities.SeleccionarCategoriaActivity;
import com.example.proyecto_tdp.views.AvisoDialog;
import com.example.proyecto_tdp.views.CalculatorInputDialog;
import com.example.proyecto_tdp.views.CalendarioDialog;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class SetTransaccionActivity extends AppCompatActivity {

    protected TextView campoPrecio;
    protected TextView campoCategoria;
    protected TextView campoFecha;
    protected EditText campoTitulo;
    protected EditText campoEtiqueta;
    protected EditText campoInfo;
    protected RadioButton btnGasto;
    protected RadioButton btnIngreso;
    protected Button btnAceptar;
    protected Button btnEliminar;
    protected CalculatorInputDialog calculatorInputDialog;
    protected CalendarioDialog calendarioDialog;
    protected AvisoDialog avisoDialog;
    protected DateTimeFormatter formatoFecha;
    protected NumberFormat formatoNumero;
    protected String idTransaccion;
    protected String idTransaccionFijaPadre;
    protected String idCategoriaElegida;

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
        formatoNumero = NumberFormat.getInstance(new Locale("es", "ES"));
        inicializarValoresCampos();
        definirIngresarMonto();
        definirSeleccionarFecha();
        definirSeleccionarCategoria();
        listenerBotonesPrincipales();
        definirMensajeDeAviso();
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
        idTransaccion = intent.getStringExtra(Constantes.CAMPO_ID);
        idTransaccionFijaPadre = intent.getStringExtra(Constantes.CAMPO_ID_TF_PADRE);
        campoInfo.setText(intent.getStringExtra(Constantes.CAMPO_INFO));
        campoTitulo.setText(intent.getStringExtra(Constantes.CAMPO_TITULO));
        campoPrecio.setText(intent.getStringExtra(Constantes.CAMPO_PRECIO));
        campoEtiqueta.setText(intent.getStringExtra(Constantes.CAMPO_ETIQUETA));
        idCategoriaElegida = intent.getStringExtra(Constantes.CAMPO_ID_CATEGORIA);
        String nombreCategoriaElegida = intent.getStringExtra(Constantes.CAMPO_NOMBRE_CATEGORIA);
        if(idCategoriaElegida==null){
            campoCategoria.setText(Constantes.SELECCIONAR_CATEGORIA);
        }
        else {
            campoCategoria.setText(nombreCategoriaElegida);
        }
        String fecha = intent.getStringExtra(Constantes.CAMPO_FECHA);
        campoFecha.setText(fecha);
        String tipo = intent.getStringExtra(Constantes.CAMPO_TIPO);
        if(Constantes.INGRESO.equals(tipo)){
            btnIngreso.setChecked(true);
            btnGasto.setChecked(false);
        }
        else {
            btnGasto.setChecked(true);
            btnIngreso.setChecked(false);
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
                Intent intent = new Intent(SetTransaccionActivity.this, SeleccionarCategoriaActivity.class);
                startActivityForResult(intent,Constantes.PEDIDO_SELECCIONAR_CATEGORIA);
            }
        });
    }

    private void listenerBotonesPrincipales(){
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarDatosPrincipales()) {
                    String info = campoInfo.getText().toString();
                    String fecha = campoFecha.getText().toString();
                    String titulo = campoTitulo.getText().toString();
                    String precio = campoPrecio.getText().toString();
                    String etiqueta = campoEtiqueta.getText().toString();
                    String tipo;
                    if (btnGasto.isChecked()) {
                        tipo = btnGasto.getText().toString();
                    } else {
                        tipo = btnIngreso.getText().toString();
                    }
                    Intent intent = new Intent();
                    intent.putExtra(Constantes.CAMPO_INFO, info);
                    intent.putExtra(Constantes.CAMPO_TIPO, tipo);
                    intent.putExtra(Constantes.CAMPO_FECHA, fecha);
                    intent.putExtra(Constantes.CAMPO_TITULO, titulo);
                    intent.putExtra(Constantes.CAMPO_ETIQUETA, etiqueta);
                    try {
                        if (tipo.equals(Constantes.INGRESO)) {
                            intent.putExtra(Constantes.CAMPO_PRECIO, formatoNumero.parse(precio).floatValue());
                        } else {
                            intent.putExtra(Constantes.CAMPO_PRECIO, formatoNumero.parse(precio).floatValue() * (-1));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (idCategoriaElegida != null) {
                        intent.putExtra(Constantes.CAMPO_ID_CATEGORIA, idCategoriaElegida);
                    }
                    if (idTransaccion != null) {
                        intent.putExtra(Constantes.CAMPO_ID, idTransaccion);
                    }
                    if (idTransaccionFijaPadre != null) {
                        intent.putExtra(Constantes.CAMPO_ID_TF_PADRE, idTransaccionFijaPadre);
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }
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

    protected void definirMensajeDeAviso(){
        avisoDialog = new AvisoDialog("Aviso","Faltan completar datos importantes");
    }

    protected boolean verificarDatosPrincipales(){
        boolean verificado = false;
        String precio = campoPrecio.getText().toString();
        float precioFinal = -1f;
        try {
            precioFinal = formatoNumero.parse(precio).floatValue();
        }catch(ParseException e){e.printStackTrace();}
        String fecha = campoFecha.getText().toString();
        Date fechaDate = formatoFecha.parseDateTime(fecha).toDate();
        if(precioFinal<=0){
            avisoDialog.setMensaje("Falta dato principal: Para ingresar una nueva transaccion debe completar al menos el campo PRECIO");
            avisoDialog.show(getSupportFragmentManager(),"Aviso");
        }
        else if(fechaDate.before(LocalDate.now().toDate())){
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
        else if(requestCode==Constantes.PEDIDO_SELECCIONAR_PLANTILLA){
            if(resultCode==RESULT_OK && data!=null){
                String info = data.getStringExtra(Constantes.CAMPO_INFO);
                String tipo = data.getStringExtra(Constantes.CAMPO_TIPO);
                String titulo = data.getStringExtra(Constantes.CAMPO_TITULO);
                String etiqueta = data.getStringExtra(Constantes.CAMPO_ETIQUETA);
                String idCategoria = data.getStringExtra(Constantes.CAMPO_ID_CATEGORIA);
                String nombreCategoria = data.getStringExtra(Constantes.CAMPO_NOMBRE_CATEGORIA);
                String precio = data.getStringExtra(Constantes.CAMPO_PRECIO);
                if(idCategoria!=null){
                    campoCategoria.setText(nombreCategoria);
                    idCategoriaElegida = idCategoria;
                }
                campoTitulo.setText(titulo);
                campoInfo.setText(info);
                campoEtiqueta.setText(etiqueta);
                campoPrecio.setText(precio);
                if(tipo.equals(Constantes.GASTO)){
                    btnGasto.setChecked(true);
                    btnIngreso.setChecked(false);
                }
                else {
                    btnGasto.setChecked(false);
                    btnIngreso.setChecked(true);
                }
            }
        }
    }
}
