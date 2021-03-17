package com.example.proyecto_tdp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterViewPagerHome;
import com.example.proyecto_tdp.fragments.GastosFijosFragment;
import com.example.proyecto_tdp.fragments.IngresosFijosFragment;
import com.example.proyecto_tdp.fragments.PlantillasFragment;
import com.google.android.material.tabs.TabLayout;

public class TransaccionesFijasActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AdapterViewPagerHome adapterViewPagerHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacciones_fijas);

        tabLayout = findViewById(R.id.principal_tabLayout);
        viewPager = findViewById(R.id.principal_viewpager);
        inicializarViewPager();
    }

    private void inicializarViewPager(){
        adapterViewPagerHome = new AdapterViewPagerHome(getSupportFragmentManager(),3);
        adapterViewPagerHome.addFragment(new IngresosFijosFragment(),"Ingresos Fijos");
        adapterViewPagerHome.addFragment(new GastosFijosFragment(),"Gastos fijos");
        adapterViewPagerHome.addFragment(new PlantillasFragment(),"Plantillas");
        viewPager.setAdapter(adapterViewPagerHome);
        tabLayout.setupWithViewPager(viewPager);
    }
}
