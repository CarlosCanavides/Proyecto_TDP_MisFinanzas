package com.example.proyecto_tdp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class AdapterViewPagerTransaccionesFijas extends FragmentPagerAdapter {

    private int numeroPaginas;
    private List<Fragment> fragments;
    private List<String> titulos;

    public AdapterViewPagerTransaccionesFijas(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        numeroPaginas = behavior;
        fragments = new ArrayList<>(behavior);
        titulos = new ArrayList<>(behavior);
    }

    public void addFragment(Fragment fragment, String label){
        fragments.add(fragment);
        titulos.add(label);
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
        return titulos.get(position);
    }
}
