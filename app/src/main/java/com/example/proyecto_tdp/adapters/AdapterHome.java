package com.example.proyecto_tdp.adapters;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import java.util.List;
import java.util.Map;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolderHome>{

    private List<TransaccionFija> transaccionesFijas;
    private Map<TransaccionFija, Integer> mapColorCategoria;

    public AdapterHome(List<TransaccionFija> transaccionesFijas, Map<TransaccionFija, Integer> mapColorCategoria) {
        this.transaccionesFijas = transaccionesFijas;
        this.mapColorCategoria = mapColorCategoria;
    }

    @NonNull
    @Override
    public AdapterHome.ViewHolderHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion, null, false);
        return new ViewHolderHome(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderHome holder, int position) {
        TransaccionFija transaccion = transaccionesFijas.get(position);
        holder.tvNombre.setText(transaccion.getTitulo());
        holder.tvCategoria.setText(transaccion.getCategoria());
        holder.tvIdentificacion.setText(transaccion.getEtiqueta());
        if(transaccion.getCategoria()!=null && transaccion.getCategoria().length()>0) {
            holder.tvLetra.setText(transaccion.getCategoria().charAt(0) + "");
        }
        float precio = transaccion.getPrecio();
        if(precio>=0){
            holder.tvPrecio.setText("+$"+precio);
        }
        else {
            holder.tvPrecio.setText("-$"+Math.abs(precio));
        }
        Drawable bg = holder.tvLetra.getBackground();
        int colorCategoria = mapColorCategoria.get(transaccion);
        bg.setColorFilter(colorCategoria, PorterDuff.Mode.SRC);
    }

    @Override
    public int getItemCount() {
        return transaccionesFijas.size();
    }

    public class ViewHolderHome extends RecyclerView.ViewHolder{

        private TextView tvNombre;
        private TextView tvCategoria;
        private TextView tvIdentificacion;
        private TextView tvLetra;
        private TextView tvPrecio;

        public ViewHolderHome(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.itemTransaccion_nombre);
            tvCategoria = itemView.findViewById(R.id.itemTransaccion_categoria);
            tvIdentificacion = itemView.findViewById(R.id.itemTransaccion_etiqueta);
            tvLetra = itemView.findViewById(R.id.idImagen);
            tvPrecio = itemView.findViewById(R.id.itemTransaccion_precio);
        }
    }
}
