package com.example.proyecto_tdp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.modificar_datos.SetTransaccionFijaActivity;
import com.example.proyecto_tdp.adapters.AdapterTransaccionesFijas;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaDeVerificacion;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaSoloTransaccionesFijas;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccionFija;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngresosFijosFragment extends Fragment {

    private ExpandableListView ingresos;
    private List<String> frecuencias;
    private Map<String,Categoria> mapCategorias;
    private Map<String, List<TransaccionFija>> mapTransaccionesFijas;
    private AdapterTransaccionesFijas adapterTransaccionesFijas;
    private DateTimeFormatter formatoFecha;
    private ViewModelCategoria viewModelCategoria;
    private ViewModelTransaccionFija viewModelTransaccionFija;
    private Observer<List<TransaccionFija>> observer;
    private EstrategiaDeVerificacion estrategiaDeVerificacion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ingresos_gastos_fijos, container, false);
        ingresos = vista.findViewById(R.id.exp_ingresos_gastos_fijos);
        formatoFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);
        inicializarListView();
        inicializarViewModels();
        return vista;
    }

    private void inicializarListView(){
        frecuencias = new ArrayList<>();
        frecuencias.add(Constantes.FRECUENCIA_SOLO_UNA_VEZ);
        frecuencias.add(Constantes.FRECUENCIA_UNA_VEZ_A_LA_SEMANA);
        frecuencias.add(Constantes.FRECUENCIA_UNA_VEZ_AL_MES);
        frecuencias.add(Constantes.FRECUENCIA_UNA_VEZ_AL_ANIO);
        mapCategorias = new HashMap<>();
        mapTransaccionesFijas = new HashMap<>();
        adapterTransaccionesFijas = new AdapterTransaccionesFijas(frecuencias,mapTransaccionesFijas,mapCategorias);
        ingresos.setAdapter(adapterTransaccionesFijas);

        ingresos.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        ingresos.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                TransaccionFija transaccionFija = mapTransaccionesFijas.get(frecuencias.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(getActivity(), SetTransaccionFijaActivity.class);
                intent.putExtra(Constantes.CAMPO_ID, transaccionFija.getId());
                intent.putExtra(Constantes.CAMPO_INFO, transaccionFija.getInfo());
                intent.putExtra(Constantes.CAMPO_TIPO, transaccionFija.getTipoTransaccion());
                intent.putExtra(Constantes.CAMPO_TITULO, transaccionFija.getTitulo());
                intent.putExtra(Constantes.CAMPO_PRECIO, String.format( "%.2f", Math.abs(transaccionFija.getPrecio())));
                intent.putExtra(Constantes.CAMPO_ETIQUETA, transaccionFija.getEtiqueta());
                intent.putExtra(Constantes.CAMPO_ID_CATEGORIA, transaccionFija.getCategoria());
                intent.putExtra(Constantes.CAMPO_FECHA, formatoFecha.print(transaccionFija.getFecha().getTime()));
                intent.putExtra(Constantes.CAMPO_FECHA_FINAL, formatoFecha.print(transaccionFija.getFechaFinal().getTime()));
                intent.putExtra(Constantes.CAMPO_FRECUENCIA, transaccionFija.getFrecuencia());
                getActivity().startActivityForResult(intent, Constantes.PEDIDO_SET_TRANSACCION_FIJA);
                return true;
            }
        });
    }

    private void inicializarViewModels(){
        ViewModelTransaccion viewModelTransaccion = new ViewModelProvider(this).get(ViewModelTransaccion.class);
        viewModelTransaccionFija = ViewModelProviders.of(getActivity()).get(ViewModelTransaccionFija.class);
        viewModelCategoria = ViewModelProviders.of(getActivity()).get(ViewModelCategoria.class);
        estrategiaDeVerificacion = new EstrategiaSoloTransaccionesFijas(viewModelTransaccion,viewModelTransaccionFija);
        recopilarDatos();
    }

    private void recopilarDatos() {
        LiveData<List<TransaccionFija>> t = viewModelTransaccionFija.getAllTransaccionesFijas();
        observer = new Observer<List<TransaccionFija>>() {
            @Override
            public void onChanged(List<TransaccionFija> transaccions) {
                mapCategorias.clear();
                mapTransaccionesFijas.clear();
                adapterTransaccionesFijas.refresh();
                adapterTransaccionesFijas.notifyDataSetChanged();
                String frecuencia;
                List<TransaccionFija> transaccionFijas;
                for(TransaccionFija t : transaccions) {
                    if (t.getTipoTransaccion().equals(Constantes.INGRESO)) {
                        Categoria categoria;
                        String idCategoria = t.getCategoria();
                        if(idCategoria!=null){
                            categoria = viewModelCategoria.getCategoriaPorID(idCategoria);
                            mapCategorias.put(idCategoria,categoria);
                        }
                        frecuencia = t.getFrecuencia();
                        transaccionFijas = mapTransaccionesFijas.get(frecuencia);
                        if (transaccionFijas == null) {
                            transaccionFijas = new ArrayList<>();
                            transaccionFijas.add(t);
                            mapTransaccionesFijas.put(frecuencia, transaccionFijas);
                        } else {
                            transaccionFijas.add(t);
                        }
                    }
                }
                adapterTransaccionesFijas.notifyDataSetChanged();
                for (int i=0; i<frecuencias.size(); i++){
                    ingresos.expandGroup(i);
                }
            }
        };
        if (t != null) {
            t.observe(getActivity(), observer);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
    }
}
