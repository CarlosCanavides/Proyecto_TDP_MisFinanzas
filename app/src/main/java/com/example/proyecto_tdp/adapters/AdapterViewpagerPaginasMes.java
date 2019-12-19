package com.example.proyecto_tdp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.proyecto_tdp.fragments.GastoMesFregment;
import java.util.ArrayList;
import java.util.List;

public class AdapterViewpagerPaginasMes extends FragmentStatePagerAdapter {

    private List<GastoMesFregment> fragments;
    private int nroPaginas;
    private int anio;

    public AdapterViewpagerPaginasMes(@NonNull FragmentManager fm, int behavior, int anio) {
        super(fm, behavior);
        nroPaginas = behavior;
        this.anio = anio;
        fragments = new ArrayList<>();
        for(int i=0; i<=12; i++){
            GastoMesFregment mesFregment = GastoMesFregment.newInstance(i,anio);
            fragments.add(mesFregment);
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return nroPaginas;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getMes();
    }

    public void setAnio(int anio){
        this.anio = anio;
        for(GastoMesFregment fregment : fragments){
            fregment.setDatos(anio);
        }
    }
}
