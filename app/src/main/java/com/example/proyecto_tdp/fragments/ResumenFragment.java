package com.example.proyecto_tdp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterResumen;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumenFragment extends Fragment {

    private RecyclerView recyclerTransacciones;
    private AdapterResumen adapter;

    private List<String> meses;
    private Map<String, List<Transaccion>> mapTransacciones;
    private Map<String, Integer> categoriaGastoPredominante;
    private DateFormat formatFecha = new SimpleDateFormat("MM/yyyy");

    private View vista;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_resumen, container, false);
        recyclerTransacciones = vista.findViewById(R.id.recyclerResumen);
        inicializarListResumen();
        inicializarViewModel();
        return vista;
    }

    private void inicializarListResumen(){
        recyclerTransacciones.setLayoutManager(new GridLayoutManager(getActivity(),1));
        meses = new ArrayList<>();
        mapTransacciones = new HashMap<>();
        categoriaGastoPredominante = new HashMap<>();
        adapter = new AdapterResumen(meses,mapTransacciones, categoriaGastoPredominante);
        recyclerTransacciones.setAdapter(adapter);
    }

    private void inicializarViewModel(){
        ViewModelTransaccion model = ViewModelProviders.of(getActivity()).get(ViewModelTransaccion.class);
        model.getAllTransacciones().observe(getActivity(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transaccions) {
                meses.clear();
                mapTransacciones.clear();
                categoriaGastoPredominante.clear();
                adapter.notifyDataSetChanged();
                for(Transaccion t : transaccions){
                    actualizarListaTransaccionesMes(t);
                    actualizarCategoriaGastoPredominante(t);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void actualizarListaTransaccionesMes(Transaccion t){
        String fecha = formatFecha.format(t.getFecha());
        List<Transaccion> listaTransaccionesMes = mapTransacciones.get(fecha);
        if(listaTransaccionesMes==null){
            listaTransaccionesMes = new ArrayList<>();
            listaTransaccionesMes.add(t);
            mapTransacciones.put(fecha,listaTransaccionesMes);
            meses.add(fecha);
        }
        else {
            listaTransaccionesMes.add(t);
        }
    }

    private void actualizarCategoriaGastoPredominante(Transaccion t){
        String categoria = t.getCategoria();
        if(categoria!=null && categoria.equals("Gasto")) {
            Integer aux = categoriaGastoPredominante.get(categoria);
            if (aux == null) {
                categoriaGastoPredominante.put(categoria, 1);
            } else {
                Integer nuevoValor = aux + 1;
                categoriaGastoPredominante.remove(categoria);
                categoriaGastoPredominante.put(categoria, nuevoValor);
            }
        }
    }
}
