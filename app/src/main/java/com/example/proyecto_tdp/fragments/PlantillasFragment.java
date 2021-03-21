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
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterHome;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.view_models.ViewModelPlantilla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantillasFragment extends Fragment {

    private View vista;
    private RecyclerView recyclerView;
    private AdapterHome adapterHome;
    private List<Plantilla> plantillas;
    private Map<Plantilla, Integer> mapColorCategoria;
    private ViewModelCategoria viewModelCategoria;
    private ViewModelPlantilla viewModelPlantilla;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_ingresos_gastos_fijos, container, false);
        recyclerView = vista.findViewById(R.id.recycler_ingresos_gastos_fijos);
        inicializarListView();
        inicializarViewModels();
        return vista;
    }

    private void inicializarListView(){
        plantillas = new ArrayList<>();
        mapColorCategoria = new HashMap<>();
        /*adapterHome = new AdapterHome(plantillas,mapColorCategoria);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView.setAdapter(adapterHome);*/
    }

    private void inicializarViewModels(){
        viewModelPlantilla = ViewModelProviders.of(getActivity()).get(ViewModelPlantilla.class);
        viewModelCategoria = ViewModelProviders.of(getActivity()).get(ViewModelCategoria.class);
        recopilarDatos();
    }

    private void recopilarDatos() {
        LiveData<List<Plantilla>> t = viewModelPlantilla.getAllPlantillas();
        if (t != null) {
            t.observe(getActivity(), new Observer<List<Plantilla>>() {
                @Override
                public void onChanged(List<Plantilla> transaccions) {
                    plantillas.clear();
                    adapterHome.notifyDataSetChanged();
                    plantillas.addAll(transaccions);
                    for(Plantilla t : transaccions){
                        Categoria subcategoria = viewModelCategoria.getCategoriaPorNombre(t.getCategoria());
                        mapColorCategoria.put(t,subcategoria.getColorCategoria());
                    }
                    adapterHome.notifyDataSetChanged();
                }
            });
        }
    }
}
