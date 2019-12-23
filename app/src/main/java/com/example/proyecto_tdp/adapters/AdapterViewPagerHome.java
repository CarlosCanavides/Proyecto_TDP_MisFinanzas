package com.example.proyecto_tdp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.proyecto_tdp.fragments.GastosFijosFragment;
import com.example.proyecto_tdp.fragments.IngresosFijosFragment;
import com.example.proyecto_tdp.fragments.PlantillasFragment;

import java.util.ArrayList;
import java.util.List;

public class AdapterViewPagerHome extends FragmentStatePagerAdapter {

    private int numeroPaginas;
    private List<Fragment> fragments;

    public AdapterViewPagerHome(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        numeroPaginas = behavior;
        fragments = new ArrayList<>();
        fragments.add(new IngresosFijosFragment());
        fragments.add(new GastosFijosFragment());
        fragments.add(new PlantillasFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return numeroPaginas;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String titulo;
        switch (position){
            case 0 : titulo = "Ingresos fijos"; break;
            case 1 : titulo = "Gastos fijos"; break;
            case 2 : titulo = "Plantillas"; break;
            default: titulo = "";
        }
        return titulo;
    }
}
