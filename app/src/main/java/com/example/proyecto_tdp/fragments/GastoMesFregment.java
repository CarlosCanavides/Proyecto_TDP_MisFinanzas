package com.example.proyecto_tdp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterInformeMes;
import com.example.proyecto_tdp.base_de_datos.entidades.Subcategoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.view_models.ViewModelSubcategoria;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GastoMesFregment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ENERO = "Enero";
    private static final String FEBRERO = "Febrero";
    private static final String MARZO = "Marzo";
    private static final String ABRIL = "Abril";
    private static final String MAYO = "Mayo";
    private static final String JUNIO = "Junio";
    private static final String JULIO = "Julio";
    private static final String AGOSTO = "Agosto";
    private static final String SEPTIEMBRE = "Septiembre";
    private static final String OCTUBRE = "Octubre";
    private static final String NOVIEMBRE = "Noviembre";
    private static final String DICIEMBRE = "Diciembre";
    private static final String HISTORICO = "Historico";

    private int sectionNumber;
    private String mes;
    private int anio;
    private View vista;
    private List<Transaccion> transaccionesMes;
    private List<Subcategoria> subcategoriasMes;
    private Map<Subcategoria,Float> mapSubcategoriasGasto;
    private AdapterInformeMes adapterInforme;

    private RecyclerView recyclerSubcategorias;
    private ViewModelTransaccion viewModelTransaccion;
    private ViewModelSubcategoria viewModelSubcategoria;

    private GastoMesFregment(int nroMes, int nroAnio){
        switch (nroMes){
            case 0 : mes = ENERO; break;
            case 1 : mes = FEBRERO; break;
            case 2 : mes = MARZO; break;
            case 3 : mes = ABRIL; break;
            case 4 : mes = MAYO; break;
            case 5 : mes = JUNIO; break;
            case 6 : mes = JULIO; break;
            case 7 : mes = AGOSTO; break;
            case 8 : mes = SEPTIEMBRE; break;
            case 9 : mes = OCTUBRE; break;
            case 10 : mes = NOVIEMBRE; break;
            case 11 : mes = DICIEMBRE; break;
            case 12 : mes = HISTORICO;
        }
        anio = nroAnio;
    }

    public static GastoMesFregment newInstance(int sectionNumber, int anio){
        GastoMesFregment fragment = new GastoMesFregment(sectionNumber, anio);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        vista = inflater.inflate(R.layout.fragment_gasto_mes, container, false);
        recyclerSubcategorias = vista.findViewById(R.id.recycler_informe_categorias);
        inicializarLisViewCategorias();
        recopilarDatos();
        sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        return vista;
    }

    private void inicializarLisViewCategorias(){
        subcategoriasMes = new ArrayList<>();
        mapSubcategoriasGasto = new HashMap<>();
        transaccionesMes = new ArrayList<>();
        adapterInforme = new AdapterInformeMes(subcategoriasMes, mapSubcategoriasGasto);
        recyclerSubcategorias.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerSubcategorias.setAdapter(adapterInforme);
    }

    private void recopilarDatos(){
        viewModelTransaccion = ViewModelProviders.of(getActivity()).get(ViewModelTransaccion.class);
        viewModelSubcategoria = ViewModelProviders.of(getActivity()).get(ViewModelSubcategoria.class);
        viewModelTransaccion.getAllTransacciones().observe(getActivity(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transaccions) {
                for (Transaccion t : transaccions) {
                    Subcategoria subcategoria = viewModelSubcategoria.getSubcategoriaPorNombre(t.getCategoria());
                    Float gastoCategoria = mapSubcategoriasGasto.get(subcategoria);
                    if (gastoCategoria == null) {
                        mostrarMensaje("holaaaaaa");
                        subcategoriasMes.add(subcategoria);
                        mapSubcategoriasGasto.put(subcategoria, Math.abs(t.getPrecio()));
                    } else {
                        mapSubcategoriasGasto.remove(subcategoria);
                        mapSubcategoriasGasto.put(subcategoria, gastoCategoria + Math.abs(t.getPrecio()));
                    }
                }
                adapterInforme.notifyDataSetChanged();
            }
        });
    }

    public String getMes(){
        return mes;
    }

    public void setDatos(int anio){
        subcategoriasMes.clear();
        mapSubcategoriasGasto.clear();
        transaccionesMes = viewModelTransaccion.getAllTransacciones().getValue();
        if(transaccionesMes!=null) {
            for (Transaccion t : transaccionesMes) {
                Subcategoria subcategoria = viewModelSubcategoria.getSubcategoriaPorNombre(t.getCategoria());
                Float gastoCategoria = mapSubcategoriasGasto.get(subcategoria);
                if (gastoCategoria == null) {
                    mostrarMensaje("holaaaaaa");
                    subcategoriasMes.add(subcategoria);
                    mapSubcategoriasGasto.put(subcategoria, Math.abs(t.getPrecio()));
                } else {
                    mapSubcategoriasGasto.remove(subcategoria);
                    mapSubcategoriasGasto.put(subcategoria, gastoCategoria + Math.abs(t.getPrecio()));
                }
            }
            adapterInforme.notifyDataSetChanged();
        }
        else {
            mostrarMensaje("holaaaaaa");
        }
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
