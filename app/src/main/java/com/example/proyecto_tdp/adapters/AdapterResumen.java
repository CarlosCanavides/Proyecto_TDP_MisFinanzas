package com.example.proyecto_tdp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.ResumenMes;

import java.util.ArrayList;

public class AdapterResumen extends RecyclerView.Adapter<AdapterResumen.ViewHolderResumen> {

    private ArrayList<ResumenMes> listaResumen;

    public AdapterResumen(ArrayList<ResumenMes> listaResumen) {
        this.listaResumen = listaResumen;
    }

    @NonNull
    @Override
    public ViewHolderResumen onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_resumen, null, false);
        return new ViewHolderResumen(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderResumen viewHolderResumen, int i) {
        viewHolderResumen.etiFecha.setText(listaResumen.get(i).getFecha());
        viewHolderResumen.etiCantTransacciones.setText(""+listaResumen.get(i).getCantTransacciones());
        viewHolderResumen.etiAviso.setText(listaResumen.get(i).getAviso());
        viewHolderResumen.grafico.setImageResource(listaResumen.get(i).getGrafico());
    }

    @Override
    public int getItemCount() {
        return listaResumen.size();
    }

    public class ViewHolderResumen extends RecyclerView.ViewHolder {

        private TextView etiFecha;
        private TextView etiCantTransacciones;
        private TextView etiAviso;
        private ImageView grafico;


        public ViewHolderResumen(@NonNull View itemView) {
            super(itemView);
            etiFecha = itemView.findViewById(R.id.resumen_fecha);
            etiCantTransacciones = itemView.findViewById(R.id.resumen_cantidadTransacciones);
            etiAviso = itemView.findViewById(R.id.resumen_aviso);
            grafico = itemView.findViewById(R.id.resumen_grafico);
        }
    }
}
