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
import com.example.proyecto_tdp.activities.agregar_datos.NuevaTransaccionFijaActivity;
import com.example.proyecto_tdp.adapters.AdapterViewPagerTransaccionesFijas;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaDeVerificacion;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaSoloTransaccionesFijas;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccionFija;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class TransaccionesFijasActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private FloatingActionButton btnAgregarNuevaTransaccionFija;
    private EstrategiaDeVerificacion estrategiaDeVerificacion;
    private AdapterViewPagerTransaccionesFijas adapterViewPagerTransaccionesFijas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacciones_fijas);
        btnAgregarNuevaTransaccionFija = findViewById(R.id.btn_agregar_transaccion_fija);
        tabLayout = findViewById(R.id.tabLayout_transacciones_fijas);
        viewPager = findViewById(R.id.viewpager_transacciones_fijas);
        toolbar   = findViewById(R.id.transaccion_fijas_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarViewModels();
        inicializarViewPager();
        inicializarBtnAgregarTransaccionFija();
    }

    private void inicializarViewModels(){
        ViewModelTransaccion viewModelTransaccion = ViewModelProviders.of(this).get(ViewModelTransaccion.class);
        ViewModelTransaccionFija viewModelTransaccionFija = ViewModelProviders.of(this).get(ViewModelTransaccionFija.class);
        estrategiaDeVerificacion = new EstrategiaSoloTransaccionesFijas(viewModelTransaccion,viewModelTransaccionFija);
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
                Intent intent = new Intent(TransaccionesFijasActivity.this, NuevaTransaccionFijaActivity.class);
                startActivityForResult(intent, Constantes.PEDIDO_NUEVA_TRANSACCION_FIJA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constantes.PEDIDO_NUEVA_TRANSACCION_FIJA || requestCode==Constantes.PEDIDO_SET_TRANSACCION_FIJA){
            estrategiaDeVerificacion.verificar(requestCode,resultCode,data);
        }
    }
}
