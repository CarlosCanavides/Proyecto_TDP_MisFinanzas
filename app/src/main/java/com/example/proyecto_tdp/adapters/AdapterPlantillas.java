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
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import java.util.List;
import java.util.Map;

public class AdapterPlantillas extends RecyclerView.Adapter<AdapterPlantillas.ViewHolderPlantilla>{

    private List<Plantilla> plantillas;
    private Map<Plantilla, Integer> mapColorCategoria;

    public AdapterPlantillas(List<Plantilla> plantillas, Map<Plantilla, Integer> mapColorCategoria) {
        this.plantillas = plantillas;
        this.mapColorCategoria = mapColorCategoria;
    }

    @NonNull
    @Override
    public AdapterPlantillas.ViewHolderPlantilla onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion, null, false);
        return new AdapterPlantillas.ViewHolderPlantilla(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlantillas.ViewHolderPlantilla holder, int position) {
        Plantilla plantilla = plantillas.get(position);
        holder.tvNombre.setText(plantilla.getTitulo());
        holder.tvCategoria.setText(plantilla.getCategoria());
        holder.tvIdentificacion.setText(plantilla.getEtiqueta());
        if(plantilla.getCategoria()!=null && plantilla.getCategoria().length()>0) {
            holder.tvLetra.setText(plantilla.getCategoria().charAt(0) + "");
        }
        float precio = plantilla.getPrecio();
        if(precio>=0){
            holder.tvPrecio.setText("+$"+precio);
        }
        else {
            holder.tvPrecio.setText("-$"+Math.abs(precio));
        }
        Drawable bg = holder.tvLetra.getBackground();
        int colorCategoria = mapColorCategoria.get(plantilla);
        bg.setColorFilter(colorCategoria, PorterDuff.Mode.SRC);
    }

    public void refresh(){
        plantillas.clear();
        mapColorCategoria.clear();
    }

    @Override
    public int getItemCount() {
        return plantillas.size();
    }

    public class ViewHolderPlantilla extends RecyclerView.ViewHolder{

        private TextView tvNombre;
        private TextView tvCategoria;
        private TextView tvIdentificacion;
        private TextView tvLetra;
        private TextView tvPrecio;

        public ViewHolderPlantilla(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.itemTransaccion_nombre);
            tvCategoria = itemView.findViewById(R.id.itemTransaccion_categoria);
            tvIdentificacion = itemView.findViewById(R.id.itemTransaccion_etiqueta);
            tvLetra = itemView.findViewById(R.id.idImagen);
            tvPrecio = itemView.findViewById(R.id.itemTransaccion_precio);
        }
    }
}
