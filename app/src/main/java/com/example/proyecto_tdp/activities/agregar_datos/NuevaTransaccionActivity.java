package com.example.proyecto_tdp.activities.agregar_datos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.SeleccionarCategoriaActivity;
import com.example.proyecto_tdp.activities.SeleccionarPlantillaActivity;
import com.example.proyecto_tdp.views.AvisoDialog;
import com.example.proyecto_tdp.views.CalculatorInputDialog;
import com.example.proyecto_tdp.views.CalendarioDialog;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NuevaTransaccionActivity extends AppCompatActivity{

    protected TextView campoFecha;
    protected TextView campoPrecio;
    protected TextView campoCategoria;
    protected TextView campoFechaFinal;
    protected EditText campoTitulo;
    protected EditText campoEtiqueta;
    protected EditText campoInfo;
    protected Button btnAceptar;
    protected Button btnCancelar;
    protected RadioButton btnGasto;
    protected RadioButton btnIngreso;
    protected CheckBox btnPlantilla;
    protected CheckBox btnTransaccionFija;
    protected Spinner listaFrecuencias;
    protected ArrayAdapter<CharSequence> adapterFrecuencia;
    protected LinearLayout panelFechaFinal;
    protected AvisoDialog avisoDialog;
    protected CalendarioDialog calendarioDialog;
    protected CalendarioDialog calendarioFechaFinalDialog;
    protected CalculatorInputDialog calculatorInputDialog;
    protected DateTimeFormatter formatoFecha;
    protected NumberFormat formatoNumero;
    protected String idCategoriaElegida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_transaccion);
        campoPrecio = findViewById(R.id.campo_transaccion_precio);
        campoCategoria = findViewById(R.id.campo_transaccion_categoria);
        campoTitulo = findViewById(R.id.campo_transaccion_titulo);
        campoEtiqueta = findViewById(R.id.campo_transaccion_etiqueta);
        campoFecha = findViewById(R.id.campo_transaccion_fecha);
        campoFechaFinal = findViewById(R.id.campo_transaccion_fecha_final);
        campoInfo = findViewById(R.id.campo_transaccion_info);
        btnGasto = findViewById(R.id.radiobtn_transaccion_gasto);
        btnIngreso = findViewById(R.id.radiobtn_transaccion_ingreso);
        btnPlantilla = findViewById(R.id.chk_agregar_plantilla);
        btnTransaccionFija = findViewById(R.id.chk_agregar_transaccion_fija);
        listaFrecuencias = findViewById(R.id.lista_desplegable_frecuencia);
        panelFechaFinal = findViewById(R.id.panel_transaccion_fecha_final);
        btnAceptar = findViewById(R.id.btn_transaccion_aceptar);
        btnCancelar = findViewById(R.id.btn_transaccion_cancelar);
        panelFechaFinal.setVisibility(View.GONE);
        listaFrecuencias.setVisibility(View.GONE);
        btnGasto.setChecked(true);
        formatoFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);
        formatoNumero = NumberFormat.getInstance(new Locale("es", "ES"));
        definirIngresarMonto();
        definirSeleccionarFecha();
        definirSeleccionarCategoria();
        definirOpcionesAvanzadas();
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
        campoFecha.setText(formatoFecha.print(LocalDate.now()));
    }

    protected void definirSeleccionarCategoria(){
        campoCategoria.setText(Constantes.SELECCIONAR_CATEGORIA);
        campoCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevaTransaccionActivity.this, SeleccionarCategoriaActivity.class);
                startActivityForResult(intent,Constantes.PEDIDO_SELECCIONAR_CATEGORIA);
            }
        });
    }

    protected void definirOpcionesAvanzadas(){
        ArrayList<String> opcionesFrecuencia = new ArrayList<>();
        opcionesFrecuencia.add(Constantes.SELECCIONAR_FRECUENCIA);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_SOLO_UNA_VEZ);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_A_LA_SEMANA);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_MES);
        opcionesFrecuencia.add(Constantes.FRECUENCIA_UNA_VEZ_AL_ANIO);
        adapterFrecuencia = new ArrayAdapter(this, android.R.layout.simple_list_item_1, opcionesFrecuencia);
        listaFrecuencias.setAdapter(adapterFrecuencia);
        btnPlantilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTransaccionFija.setChecked(false);
                if(btnPlantilla.isChecked()) {
                    panelFechaFinal.setVisibility(View.GONE);
                    listaFrecuencias.setVisibility(View.GONE);
                    campoFecha.setText("No necesita fecha");
                    campoFecha.setClickable(false);
                    campoFechaFinal.setText(Constantes.SELECCIONAR_FECHA_FINAL);
                }
                else {
                    campoFecha.setText(formatoFecha.print(LocalDate.now()));
                    campoFecha.setClickable(true);
                }
            }
        });
        btnTransaccionFija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlantilla.setChecked(false);
                if(btnTransaccionFija.isChecked()){
                    panelFechaFinal.setVisibility(View.VISIBLE);
                    listaFrecuencias.setVisibility(View.VISIBLE);
                    listaFrecuencias.setSelection(0);
                    campoFecha.setText(formatoFecha.print(LocalDate.now()));
                    campoFecha.setClickable(true);
                }
                else {
                    panelFechaFinal.setVisibility(View.GONE);
                    listaFrecuencias.setVisibility(View.GONE);
                    campoFechaFinal.setText(Constantes.SELECCIONAR_FECHA_FINAL);
                }
            }
        });
        calendarioFechaFinalDialog = new CalendarioDialog();
        calendarioFechaFinalDialog.setListener(new CalendarioDialog.OnSelectDateListener() {
            @Override
            public void onSelectDate(Date date) throws Exception {
                campoFechaFinal.setText(formatoFecha.print(date.getTime()));
            }
        });
        campoFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarioFechaFinalDialog.show(getSupportFragmentManager(), getClass().getSimpleName());
            }
        });
    }

    protected void listenerBotonesPrincipales(){
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarDatosPrincipales()){
                    String info = campoInfo.getText().toString();
                    String fecha = campoFecha.getText().toString();
                    String titulo = campoTitulo.getText().toString();
                    String precio = campoPrecio.getText().toString();
                    String etiqueta = campoEtiqueta.getText().toString();
                    String fechaFinal = campoFechaFinal.getText().toString();
                    String frecuencia = (String) adapterFrecuencia.getItem(listaFrecuencias.getSelectedItemPosition());
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
                    intent.putExtra(Constantes.CAMPO_FECHA, fecha);
                    intent.putExtra(Constantes.CAMPO_TITULO, titulo);
                    intent.putExtra(Constantes.CAMPO_ETIQUETA, etiqueta);
                    intent.putExtra(Constantes.BOX_PLANTILLA_SELECCIONADO, btnPlantilla.isChecked());
                    intent.putExtra(Constantes.BOX_TRANSACCION_FIJA_SELECCIONADO, btnTransaccionFija.isChecked());
                    if(!fechaFinal.equals(Constantes.SELECCIONAR_FECHA_FINAL)){
                        intent.putExtra(Constantes.CAMPO_FECHA_FINAL, fechaFinal);
                    }
                    if(frecuencia!=null && !frecuencia.equals(Constantes.SELECCIONAR_FRECUENCIA)){
                        intent.putExtra(Constantes.CAMPO_FRECUENCIA,frecuencia);
                    }
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
        String frecuenciaSeleccionada = listaFrecuencias.getSelectedItem().toString();
        float precioFinal = -1f;
        try {
            precioFinal = formatoNumero.parse(precio).floatValue();
        }catch(ParseException e){e.printStackTrace();}
        if(precioFinal<=0){
            avisoDialog.setMensaje("Falta dato principal: Para ingresar una nueva transaccion debe completar al menos el campo PRECIO");
            avisoDialog.show(getSupportFragmentManager(),"Aviso");
        }
        else {
            if(btnTransaccionFija.isChecked()){
                String fechaI = campoFecha.getText().toString();
                String fechaF = campoFechaFinal.getText().toString();
                Date fechaInicio = null;
                Date fechaFinal = null;
                if(!fechaI.equals("")) {
                    fechaInicio = formatoFecha.parseDateTime(fechaI).toDate();
                }
                if(!fechaF.equals(Constantes.SELECCIONAR_FECHA_FINAL)){
                    fechaFinal = formatoFecha.parseDateTime(fechaF).toDate();
                }
                if(frecuenciaSeleccionada.equals(Constantes.SELECCIONAR_FRECUENCIA)){
                    avisoDialog.setMensaje("Falta dato principal: Para ingresar una nueva transaccion fija debe seleccionar una FRECUENCIA");
                    avisoDialog.show(getSupportFragmentManager(),"Aviso");
                }
                else if(frecuenciaSeleccionada.equals(Constantes.FRECUENCIA_SOLO_UNA_VEZ) && fechaInicio!=null){
                    verificado=true;
                }
                else if(fechaFinal==null){
                    avisoDialog.setMensaje("Error en los datos de fecha: Para ingresar una nueva transaccion fija debe seleccionar las fechas correspondientes");
                    avisoDialog.show(getSupportFragmentManager(),"Aviso");
                }
                else if(fechaInicio.after(fechaFinal)){
                    avisoDialog.setMensaje("Error en los datos de fecha: Para ingresar una nueva transaccion fija la FECHA-FINAL debe ser posterior a la FECHA-INICIO");
                    avisoDialog.show(getSupportFragmentManager(),"Aviso");
                }
                else {
                    verificado=true;
                }
            }
            else {
                verificado = true;
            }
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
