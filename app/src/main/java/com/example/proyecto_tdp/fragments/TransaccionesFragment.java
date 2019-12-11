package com.example.proyecto_tdp.fragments;

import android.content.Intent;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_tdp.activities.SetTransaccionActivity;
import com.example.proyecto_tdp.codigo.AdapterTransacciones;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;

import java.util.ArrayList;
import java.util.List;

public class TransaccionesFragment extends Fragment {

    private RecyclerView recyclerTransacciones;
    private ArrayList<Transaccion> transacciones;
    private AdapterTransacciones adapter;

    private View vista;
    private static final int NRO_PEDIDO_SET = 1827;

    private ViewModelTransaccion viewModelTransaccion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_transacciones, container, false);

        transacciones = new ArrayList<>();
        adapter = new AdapterTransacciones(transacciones);
        recyclerTransacciones = vista.findViewById(R.id.recyclerViewId);
        recyclerTransacciones.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerTransacciones.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterTransacciones.OnItemClickListener() {
            @Override
            public void onItemClik(Transaccion transaccion) {
                Intent intent = new Intent(getActivity(), SetTransaccionActivity.class);
                intent.putExtra("id", transaccion.getId());
                intent.putExtra("precio", transaccion.getPrecio());
                intent.putExtra("categoria", transaccion.getCategoria());
                intent.putExtra("tipoT", transaccion.getTipoTransaccion());
                intent.putExtra("titulo", transaccion.getTitulo());
                intent.putExtra("etiqueta", transaccion.getEtiqueta());
                intent.putExtra("fecha", transaccion.getFecha());
                intent.putExtra("info", transaccion.getInfo());
                startActivityForResult(intent, NRO_PEDIDO_SET);
            }
        });

        viewModelTransaccion = ViewModelProviders.of(getActivity()).get(ViewModelTransaccion.class);
        viewModelTransaccion.getAllTransacciones().observe(getActivity(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transaccions) {
                transacciones.clear();
                transacciones.addAll(transaccions);
                adapter.notifyDataSetChanged();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModelTransaccion.eliminarTransaccion(adapter.getTransaccionAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerTransacciones);

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

                float monto = Float.parseFloat(precio);

                if(id!=-1) {
                    Transaccion nueva = new Transaccion(titulo, etiqueta, monto, categoria, tipoTransaccion, fecha, info);
                    nueva.setId(id);
                    viewModelTransaccion.actualizarTransaccion(nueva);
                }
            }
            else if (resultCode == 0) {
                int id = data.getIntExtra("id",-1);
                if(id!=-1) {
                    Transaccion t = new Transaccion("","",0,"","","","");
                    t.setId(id);
                    viewModelTransaccion.actualizarTransaccion(t);
                    viewModelTransaccion.eliminarTransaccion(t);
                }
            }
        }
    }
}
