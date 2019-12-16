package com.example.proyecto_tdp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.proyecto_tdp.activities.SetTransaccionActivity;
import com.example.proyecto_tdp.adapters.AdapterTransacciones;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransaccionesFragment extends Fragment {

    private ExpandableListView expTransacciones;
    private List<Transaccion> transacciones;
    private List<String> fechas;
    private Map<String, List<Transaccion>> mapTransacciones;
    private AdapterTransacciones adapter;

    private View vista;
    private static final int NRO_PEDIDO_SET = 1827;

    private ViewModelTransaccion viewModelTransaccion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_transacciones, container, false);

        expTransacciones = vista.findViewById(R.id.expTransacciones);
        transacciones = new ArrayList<>();
        fechas = new ArrayList<>();
        mapTransacciones = new HashMap<>();
        adapter = new AdapterTransacciones(fechas,mapTransacciones);
        expTransacciones.setAdapter(adapter);

        expTransacciones.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        expTransacciones.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Transaccion transaccion = mapTransacciones.get(fechas.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(getActivity(), SetTransaccionActivity.class);
                intent.putExtra("id", transaccion.getId());
                intent.putExtra("precio", String.format( "%.2f", Math.abs(transaccion.getPrecio())));
                intent.putExtra("categoria", transaccion.getCategoria());
                intent.putExtra("tipoT", transaccion.getTipoTransaccion());
                intent.putExtra("titulo", transaccion.getTitulo());
                intent.putExtra("etiqueta", transaccion.getEtiqueta());
                intent.putExtra("fecha", transaccion.getFecha());
                intent.putExtra("info", transaccion.getInfo());
                startActivityForResult(intent, NRO_PEDIDO_SET);
                return true;
            }
        });

        viewModelTransaccion = ViewModelProviders.of(getActivity()).get(ViewModelTransaccion.class);
        viewModelTransaccion.getAllTransacciones().observe(getActivity(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transaccions) {
                transacciones.clear();
                transacciones.addAll(transaccions);
                fechas.clear();
                mapTransacciones.clear();
                for(Transaccion t : transacciones){
                    Date fechaDate = t.getFecha();
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaNueva = formatter.format(fechaDate);
                    if(!fechas.contains(fechaNueva)){
                        fechas.add(fechaNueva);
                        List<Transaccion> lista = mapTransacciones.get(fechaNueva);
                        if(lista == null){
                            lista = new ArrayList<>();
                            lista.add(t);
                            mapTransacciones.put(fechaNueva,lista);
                        }
                        else {
                            lista.add(t);
                        }
                    }
                    else{
                        mapTransacciones.get(fechaNueva).add(t);
                    }
                }
                adapter.notifyDataSetChanged();
                for (int i=0; i<fechas.size(); i++){
                    expTransacciones.expandGroup(i);
                }
            }
        });
        return vista;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == NRO_PEDIDO_SET) {
            if (resultCode == -1) {
                int id = data.getIntExtra("id",-1);
                String precio = data.getStringExtra("precio");
                String categoria = data.getStringExtra("categoria");
                String tipoTransaccion = data.getStringExtra("tipoT");
                String titulo = data.getStringExtra("titulo");
                String etiqueta = data.getStringExtra("etiqueta");
                String fecha = data.getStringExtra("fecha");
                String info = data.getStringExtra("info");

                float monto = 0;
                try {
                    Locale spanish = new Locale("es", "ES");
                    NumberFormat nf = NumberFormat.getInstance(spanish);
                    monto = nf.parse(precio).floatValue();
                }catch (Exception e) {
                    mostrarMensaje("El monto ingresado debe ser mayor a 0");
                }

                if(id!=-1) {
                    if(tipoTransaccion.equals("Gasto")){
                        monto = monto*(-1);
                    }
                    Transaccion nueva = new Transaccion(titulo, etiqueta, monto, categoria, tipoTransaccion, new Date(), info);
                    nueva.setId(id);
                    viewModelTransaccion.actualizarTransaccion(nueva);
                }
            }
            else if (resultCode == 0) {
                int id = data.getIntExtra("id",-1);
                if(id!=-1) {
                    Transaccion t = new Transaccion("","",0,"","",new Date(),"");
                    t.setId(id);
                    viewModelTransaccion.actualizarTransaccion(t);
                    viewModelTransaccion.eliminarTransaccion(t);
                }
            }
        }
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
