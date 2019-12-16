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
import com.example.proyecto_tdp.base_de_datos.entidades.ResumenMes;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import java.util.ArrayList;
import java.util.List;

public class ResumenFragment extends Fragment {

    private RecyclerView recyclerTransacciones;
    private ArrayList<ResumenMes> listaResumen;
    private AdapterResumen adapter;

    private View vista;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_resumen, container, false);

        recyclerTransacciones = vista.findViewById(R.id.recyclerResumen);
        recyclerTransacciones.setLayoutManager(new GridLayoutManager(getActivity(),1));
        listaResumen = new ArrayList<>();
        adapter = new AdapterResumen(listaResumen);

        ViewModelTransaccion model = ViewModelProviders.of(getActivity()).get(ViewModelTransaccion.class);
        model.getAllTransacciones().observe(getActivity(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transaccions) {

            }
        });

        recyclerTransacciones.setAdapter(adapter);

        return vista;
    }
}
