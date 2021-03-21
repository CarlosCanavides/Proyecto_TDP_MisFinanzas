package com.example.proyecto_tdp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterViewPagerTransaccionesFijas;
import com.example.proyecto_tdp.fragments.GastosFijosFragment;
import com.example.proyecto_tdp.fragments.IngresosFijosFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class TransaccionesFijasActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AdapterViewPagerTransaccionesFijas adapterViewPagerTransaccionesFijas;
    private FloatingActionButton btnAgregarNuevaTransaccionFija;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacciones_fijas);
        btnAgregarNuevaTransaccionFija = findViewById(R.id.btn_agregar_transaccion_fija);
        tabLayout = findViewById(R.id.tabLayout_transacciones_fijas);
        viewPager = findViewById(R.id.viewpager_transacciones_fijas);
        inicializarViewPager();
    }

    private void inicializarViewPager(){
        adapterViewPagerTransaccionesFijas = new AdapterViewPagerTransaccionesFijas(getSupportFragmentManager(),2);
        adapterViewPagerTransaccionesFijas.addFragment(new IngresosFijosFragment(),"Ingresos Fijos");
        adapterViewPagerTransaccionesFijas.addFragment(new GastosFijosFragment(),"Gastos fijos");
        viewPager.setAdapter(adapterViewPagerTransaccionesFijas);
        tabLayout.setupWithViewPager(viewPager);
    }
}
