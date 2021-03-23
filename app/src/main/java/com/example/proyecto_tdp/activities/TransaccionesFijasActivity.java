package com.example.proyecto_tdp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterViewPagerTransaccionesFijas;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import com.example.proyecto_tdp.fragments.GastosFijosFragment;
import com.example.proyecto_tdp.fragments.IngresosFijosFragment;
import com.example.proyecto_tdp.view_models.ViewModelTransaccionFija;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class TransaccionesFijasActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private AdapterViewPagerTransaccionesFijas adapterViewPagerTransaccionesFijas;
    private ViewModelTransaccionFija viewModelTransaccionFija;
    private FloatingActionButton btnAgregarNuevaTransaccionFija;
    private static final int PEDIDO_AGREGAR_TRANSACCION_FIJA = 12;
    private NumberFormat nf = NumberFormat.getInstance(new Locale("es", "ES"));
    private static final DateTimeFormatter formatFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacciones_fijas);
        btnAgregarNuevaTransaccionFija = findViewById(R.id.btn_agregar_transaccion_fija);
        tabLayout = findViewById(R.id.tabLayout_transacciones_fijas);
        viewPager = findViewById(R.id.viewpager_transacciones_fijas);
        toolbar   = findViewById(R.id.transaccion_fijas_toolbar);
        viewModelTransaccionFija = ViewModelProviders.of(this).get(ViewModelTransaccionFija.class);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarViewPager();
        inicializarBtnAgregarTransaccionFija();
    }

    private void inicializarViewPager(){
        adapterViewPagerTransaccionesFijas = new AdapterViewPagerTransaccionesFijas(getSupportFragmentManager(),2);
        viewPager.setAdapter(adapterViewPagerTransaccionesFijas);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void inicializarBtnAgregarTransaccionFija(){
        btnAgregarNuevaTransaccionFija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaccionesFijasActivity.this, NuevaTransaccionActivity.class);
                startActivityForResult(intent, PEDIDO_AGREGAR_TRANSACCION_FIJA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PEDIDO_AGREGAR_TRANSACCION_FIJA){
            if(resultCode==RESULT_OK){
                String precio = data.getStringExtra("precio");
                String categoria = data.getStringExtra("categoria");
                String tipoTransaccion = data.getStringExtra("tipoT");
                String titulo = data.getStringExtra("titulo");
                String etiqueta = data.getStringExtra("etiqueta");
                String fecha = data.getStringExtra("fecha");
                String info = data.getStringExtra("info");
                String frecuencia = data.getStringExtra("frecuencia");
                String fechaFinal = data.getStringExtra("fechaFinal");

                float monto = 0;
                try {
                    monto = nf.parse(precio).floatValue();
                }catch (Exception e) {
                    e.printStackTrace();
                }
                if(titulo.equals("")){
                    titulo = "Sin título";
                }
                if(categoria.equals("Seleccionar categoria")){
                    categoria = "";
                }
                if(tipoTransaccion.equals(Constantes.GASTO)){
                    monto = monto*(-1);
                }
                Date fechaDate = null;
                if(fecha != null) {
                    fechaDate = formatFecha.parseDateTime(fecha).toDate();
                }
                Date fechaFinalDate = null;
                if(fechaFinal != null) {
                    fechaFinalDate = formatFecha.parseDateTime(fechaFinal).toDate();
                }
                TransaccionFija nueva = new TransaccionFija(titulo, etiqueta, monto, categoria, tipoTransaccion, fechaDate, info, frecuencia, fechaFinalDate);
                viewModelTransaccionFija.insertarTransaccionFija(nueva);
            }
        }
    }
}
