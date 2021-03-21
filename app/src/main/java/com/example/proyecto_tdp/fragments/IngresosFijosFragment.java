package com.example.proyecto_tdp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterHome;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.view_models.ViewModelTransaccionFija;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngresosFijosFragment extends Fragment {

    private View vista;
    private RecyclerView ingresos;
    private AdapterHome adapterHome;
    private List<TransaccionFija> transaccionesFijas;
    private Map<TransaccionFija, Integer> mapColorCategoria;
    private ViewModelCategoria viewModelCategoria;
    private ViewModelTransaccionFija viewModelTransaccionFija;
    private Observer<List<TransaccionFija>> observer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_ingresos_gastos_fijos, container, false);
        ingresos = vista.findViewById(R.id.recycler_ingresos_gastos_fijos);
        inicializarListView();
        inicializarViewModels();
        return vista;
    }

    private void inicializarListView(){
        transaccionesFijas = new ArrayList<>();
        mapColorCategoria = new HashMap<>();
        adapterHome = new AdapterHome(transaccionesFijas,mapColorCategoria);
        ingresos.setLayoutManager(new GridLayoutManager(getActivity(),1));
        ingresos.setAdapter(adapterHome);
    }

    private void inicializarViewModels(){
        viewModelTransaccionFija = ViewModelProviders.of(getActivity()).get(ViewModelTransaccionFija.class);
        viewModelCategoria = ViewModelProviders.of(getActivity()).get(ViewModelCategoria.class);
        recopilarDatos();
    }

    private void recopilarDatos() {
        LiveData<List<TransaccionFija>> t = viewModelTransaccionFija.getAllTransaccionesFijas();
        observer = new Observer<List<TransaccionFija>>() {
            @Override
            public void onChanged(List<TransaccionFija> transaccions) {
                transaccionesFijas.clear();
                adapterHome.notifyDataSetChanged();
                transaccionesFijas.addAll(transaccions);
                for(TransaccionFija t : transaccions){
                    Categoria subcategoria = viewModelCategoria.getCategoriaPorNombre(t.getCategoria());
                    mapColorCategoria.put(t,subcategoria.getColorCategoria());
                }
                adapterHome.notifyDataSetChanged();
            }
        };
        if (t != null) {
            t.observe(getActivity(), observer);
        }
    }
}
