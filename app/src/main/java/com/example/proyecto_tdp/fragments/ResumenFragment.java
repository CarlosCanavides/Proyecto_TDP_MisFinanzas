package com.example.proyecto_tdp.fragments;

import android.os.Bundle;
import android.util.Log;
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

import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterResumen;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumenFragment extends Fragment {

    private RecyclerView recyclerTransacciones;
    private List<String> meses;
    private Map<String, List<Transaccion>> mapTransacciones;
    private Map<String,HashMap<String, Float>> categoriaGastoPredominante;
    private Map<String, Integer> mapColorCategoria;
    private AdapterResumen adapter;
    private DateTimeFormatter formatoFecha = DateTimeFormat.forPattern("MM/yyyy");
    private ViewModelCategoria viewModelCategoria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_resumen, container, false);
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
        mapColorCategoria = new HashMap<>();
        adapter = new AdapterResumen(meses,mapTransacciones,categoriaGastoPredominante,mapColorCategoria);
        recyclerTransacciones.setAdapter(adapter);
    }

    private void inicializarViewModel(){
        ViewModelTransaccion model = ViewModelProviders.of(getActivity()).get(ViewModelTransaccion.class);
        viewModelCategoria = ViewModelProviders.of(getActivity()).get(ViewModelCategoria.class);
        model.getAllTransacciones().observe(getActivity(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transaccions) {
                meses.clear();
                mapTransacciones.clear();
                categoriaGastoPredominante.clear();
                mapColorCategoria.clear();
                adapter.refresh();
                for(Transaccion t : transaccions) {
                    actualizarListaTransaccionesMes(t);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void actualizarListaTransaccionesMes(Transaccion t){
        String fecha = formatoFecha.print(t.getFecha().getTime());
        List<Transaccion> listaTransaccionesMes = mapTransacciones.get(fecha);
        if(listaTransaccionesMes==null){
            listaTransaccionesMes = new ArrayList<>();
            listaTransaccionesMes.add(t);
            mapTransacciones.put(fecha,listaTransaccionesMes);
            meses.add(fecha);
            HashMap<String,Float> mapGastoPredominanteMes = new HashMap<>();
            categoriaGastoPredominante.put(fecha,mapGastoPredominanteMes);
            actualizarCategoriaGastoPredominante(t,mapGastoPredominanteMes);
        }
        else {
            listaTransaccionesMes.add(t);
            HashMap<String,Float> mapGastoPredominanteMes = categoriaGastoPredominante.get(fecha);
            actualizarCategoriaGastoPredominante(t,mapGastoPredominanteMes);
        }
    }

    private void actualizarCategoriaGastoPredominante(Transaccion t, HashMap<String,Float> mapGastoPredominanteMes){
        String categoria = t.getCategoria();
        Categoria categoriaObjeto = null;
        if(categoria!=null){
            categoriaObjeto = viewModelCategoria.getCategoriaPorNombre(t.getCategoria());
        }
        if(categoria==null && t.getTipoTransaccion().equals(Constantes.GASTO)){
            categoria = "Sin categoria";
        }
        if(categoria!=null && t.getTipoTransaccion().equals(Constantes.GASTO)) {
            Float aux = mapGastoPredominanteMes.get(categoria);
            if (aux == null) {
                mapGastoPredominanteMes.put(categoria, Math.abs(t.getPrecio()));
                if(categoriaObjeto!=null) {
                    mapColorCategoria.put(categoria, categoriaObjeto.getColorCategoria());
                }
            } else {
                Float nuevoValor = aux + t.getPrecio();
                mapGastoPredominanteMes.remove(categoria);
                mapGastoPredominanteMes.put(categoria, nuevoValor);
            }
        }
    }
}
