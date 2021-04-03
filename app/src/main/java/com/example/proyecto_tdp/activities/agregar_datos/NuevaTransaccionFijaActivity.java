package com.example.proyecto_tdp.activities.agregar_datos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.SeleccionarCategoriaActivity;
import com.example.proyecto_tdp.activities.SeleccionarPlantillaActivity;
import com.example.proyecto_tdp.views.AvisoDialog;
import com.example.proyecto_tdp.views.CalculatorInputDialog;
import com.example.proyecto_tdp.views.CalendarioDialog;
import com.google.android.material.textfield.TextInputEditText;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NuevaTransaccionFijaActivity extends AppCompatActivity {

    protected TextView campoPrecio;
    protected AutoCompleteTextView campoCategoria;
    protected AutoCompleteTextView campoFecha;
    protected AutoCompleteTextView campoFechaFinal;
    protected TextInputEditText campoTitulo;
    protected TextInputEditText campoEtiqueta;
    protected TextInputEditText campoInfo;
    protected RadioButton btnGasto;
    protected RadioButton btnIngreso;
    protected Button btnAceptar;
    protected Button btnCancelar;
    protected AutoCompleteTextView frecuencia;
    protected DateTimeFormatter formatoFecha;
    protected NumberFormat formatoNumero;
    protected AvisoDialog avisoDialog;
    protected CalendarioDialog calendarioDialog;
    protected CalendarioDialog calendarioDialogFinal;
    protected CalculatorInputDialog calculatorInputDialog;
    protected String idCategoriaElegida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_transaccion_fija);
        campoInfo = findViewById(R.id.set_tf_campo_info);
        campoFecha = findViewById(R.id.set_tf_campo_fecha);
        campoPrecio = findViewById(R.id.set_tf_campo_precio);
        campoTitulo = findViewById(R.id.set_tf_campo_titulo);
        campoEtiqueta = findViewById(R.id.set_tf_campo_etiqueta);
        campoCategoria = findViewById(R.id.set_tf_campo_categoria);
        campoFechaFinal = findViewById(R.id.set_tf_campo_fecha_final);
        btnGasto = findViewById(R.id.set_tf_radiobtn_gasto);
        btnIngreso = findViewById(R.id.set_tf_radiobtn_ingreso);
        btnAceptar = findViewById(R.id.set_tf_btn_aceptar);
        btnCancelar = findViewById(R.id.set_tf_btn_eliminar);
        frecuencia = findViewById(R.id.set_tf_frecuencia);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_seleccionar_plantilla,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SeleccionarPlantillaActivity.class);
        startActivityForResult(intent,Constantes.PEDIDO_SELECCIONAR_PLANTILLA);
        return super.onOptionsItemSelected(item);
    }

    protected void inicializarValoresCampos(){
        campoInfo.setText("");
        campoFecha.setText(formatoFecha.print(LocalDate.now().toDate().getTime()));
        campoTitulo.setText("");
        campoPrecio.setText("0,00");
        campoEtiqueta.setText("");
        campoCategoria.setText(Constantes.SELECCIONAR_CATEGORIA);
        campoFechaFinal.setText(Constantes.SELECCIONAR_FECHA_FINAL);
        btnGasto.setChecked(true);
        btnIngreso.setChecked(false);
        btnCancelar.setText("Cancelar");
        ArrayList<String> opcionesFrecuencia = new ArrayList<>();
        opcionesFrecuencia.add(Constantes.SELECCIONAR_FRECUENCIA);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_SOLO_UNA_VEZ);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_CADA_DIA);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_A_LA_SEMANA);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_MES);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_ANIO);
        ArrayAdapter<String> adapterFrecuencia = new ArrayAdapter<String>(this, R.layout.list_item, opcionesFrecuencia);
        frecuencia.setAdapter(adapterFrecuencia);
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

    protected void definirSeleccionarFecha(){
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
        calendarioDialogFinal = new CalendarioDialog();
        calendarioDialogFinal.setListener(new CalendarioDialog.OnSelectDateListener() {
            @Override
            public void onSelectDate(Date date) throws Exception {
                campoFechaFinal.setText(formatoFecha.print(date.getTime()));
            }
        });
        campoFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarioDialogFinal.show(getSupportFragmentManager(), getClass().getSimpleName());
            }
        });
    }

    protected void definirSeleccionarCategoria(){
        campoCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevaTransaccionFijaActivity.this, SeleccionarCategoriaActivity.class);
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
                    String fechaInicio = campoFecha.getText().toString();
                    String fechaFinal = campoFechaFinal.getText().toString();
                    String frecuenciaSeleccionada = frecuencia.getText().toString();
                    String tipo;
                    if(btnGasto.isChecked()){
                        tipo = btnGasto.getText().toString();
                    }
                    else {
                        tipo = btnIngreso.getText().toString();
                    }

                    Intent intent = new Intent();
                    intent.putExtra(Constantes.CAMPO_INFO, info);
                    intent.putExtra(Constantes.CAMPO_TIPO, tipo);
                    intent.putExtra(Constantes.CAMPO_TITULO, titulo);
                    intent.putExtra(Constantes.CAMPO_ETIQUETA, etiqueta);
                    intent.putExtra(Constantes.CAMPO_FECHA, fechaInicio);
                    intent.putExtra(Constantes.CAMPO_FECHA_FINAL, fechaFinal);
                    intent.putExtra(Constantes.CAMPO_FRECUENCIA, frecuenciaSeleccionada);
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
                    } catch (ParseException e) {
                        e.printStackTrace();
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
        String frecuenciaSeleccionada = frecuencia.getText().toString();
        String fechaI = campoFecha.getText().toString();
        String fechaF = campoFechaFinal.getText().toString();
        Date fechaInicio = null;
        Date fechaFinal = null;
        if(!fechaI.equals("") && !fechaF.equals(Constantes.SELECCIONAR_FECHA_FINAL)) {
            fechaInicio = formatoFecha.parseDateTime(fechaI).toDate();
            fechaFinal = formatoFecha.parseDateTime(fechaF).toDate();
        }
        try {
            precioFinal = formatoNumero.parse(precio).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(precioFinal<=0){
            avisoDialog.setMensaje("Falta dato principal: Para ingresar una nueva transaccion fija debe completar el campo PRECIO");
            avisoDialog.show(getSupportFragmentManager(),"Aviso");
        }
        else if(frecuenciaSeleccionada.equals(Constantes.SELECCIONAR_FRECUENCIA)){
            avisoDialog.setMensaje("Falta dato principal: Para ingresar una nueva transaccion fija debe seleccionar una FRECUENCIA");
            avisoDialog.show(getSupportFragmentManager(),"Aviso");
        }
        else if(frecuenciaSeleccionada.equals(Constantes.FRECUENCIA_SOLO_UNA_VEZ) && fechaInicio!=null){
            verificado=true;
        }
        else if(fechaFinal==null){
            avisoDialog.setMensaje("Error en los datos de fecha: Para ingresar una nueva transaccion fija debe seleccionar las fechas correspondientes FECHA INICIAL - FECHA FINAL");
            avisoDialog.show(getSupportFragmentManager(),"Aviso");
        }
        else if(fechaInicio.after(fechaFinal)){
            avisoDialog.setMensaje("Error en los datos de fecha: Para ingresar una nueva transaccion fija la FECHA-FINAL debe ser posterior a la FECHA-INICIAL");
            avisoDialog.show(getSupportFragmentManager(),"Aviso");
        }
        else {
            verificado=true;
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
                String idCategoria = data.getStringExtra(Constantes.CAMPO_ID);
                String categoria = data.getStringExtra(Constantes.CAMPO_ID_CATEGORIA);
                String precio = data.getStringExtra(Constantes.CAMPO_PRECIO);
                if(idCategoria!=null){
                    idCategoriaElegida = idCategoria;
                    campoCategoria.setText(categoria);
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