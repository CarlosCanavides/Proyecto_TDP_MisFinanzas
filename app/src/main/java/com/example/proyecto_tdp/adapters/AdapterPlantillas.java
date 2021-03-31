package com.example.proyecto_tdp.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import java.util.List;
import java.util.Map;

public class AdapterPlantillas extends RecyclerView.Adapter<AdapterPlantillas.ViewHolderPlantilla>{

    private List<Plantilla> plantillas;
    private Map<Plantilla, Categoria> mapCategoriasDePlantillas;
    private OnPlantillaListener onPlantillaListener;

    public AdapterPlantillas(List<Plantilla> plantillas, Map<Plantilla,Categoria> mapCategoriasDePlantillasa, OnPlantillaListener onPlantillaListener) {
        this.plantillas = plantillas;
        this.mapCategoriasDePlantillas = mapCategoriasDePlantillasa;
        this.onPlantillaListener = onPlantillaListener;
    }

    @NonNull
    @Override
    public AdapterPlantillas.ViewHolderPlantilla onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion, null, false);
        return new AdapterPlantillas.ViewHolderPlantilla(view,onPlantillaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlantillas.ViewHolderPlantilla holder, int position) {
        Plantilla plantilla = plantillas.get(position);
        holder.tvIdentificacion.setText(plantilla.getEtiqueta());
        Categoria categoria = mapCategoriasDePlantillas.get(plantilla);
        if(categoria!=null){
            String nombreCategoria = categoria.getNombreCategoria();
            holder.tvCategoria.setText(nombreCategoria);
            if(nombreCategoria.length()>0){
                holder.tvLetra.setText(nombreCategoria.charAt(0)+"");
            }
        }
        else {
            holder.tvLetra.setText("S");
            holder.tvCategoria.setText(Constantes.SIN_CATEGORIA);
        }
        if(plantilla.getTitulo().equals("")){
            holder.tvNombre.setText("Sin titulo");
        }
        else {
            holder.tvNombre.setText(plantilla.getTitulo());
        }
        float precio = plantilla.getPrecio();
        if(precio>=0){
            holder.tvPrecio.setText("+ $ "+String.format( "%.2f",precio));
            holder.tvPrecio.setTextColor(Color.parseColor("#0EB87A"));
        }
        else {
            holder.tvPrecio.setText("- $ "+String.format( "%.2f",Math.abs(precio)));
            holder.tvPrecio.setTextColor(Color.parseColor("#E12E48"));
        }
        Drawable bg = holder.tvLetra.getBackground();
        int colorCategoria = categoria.getColorCategoria();
        bg.setColorFilter(colorCategoria, PorterDuff.Mode.SRC);
    }

    public void refresh(){
        plantillas.clear();
        mapCategoriasDePlantillas.clear();
    }

    @Override
    public int getItemCount() {
        return plantillas.size();
    }

    public class ViewHolderPlantilla extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvNombre;
        private TextView tvCategoria;
        private TextView tvIdentificacion;
        private TextView tvLetra;
        private TextView tvPrecio;
        private OnPlantillaListener onPlantillaListener;

        public ViewHolderPlantilla(@NonNull View itemView, OnPlantillaListener onPlantillaListener) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.itemTransaccion_nombre);
            tvCategoria = itemView.findViewById(R.id.itemTransaccion_categoria);
            tvIdentificacion = itemView.findViewById(R.id.itemTransaccion_etiqueta);
            tvLetra = itemView.findViewById(R.id.idImagen);
            tvPrecio = itemView.findViewById(R.id.itemTransaccion_precio);
            itemView.setOnClickListener(this);
            this.onPlantillaListener = onPlantillaListener;
        }

        @Override
        public void onClick(View v) {
            onPlantillaListener.onPlantillaClick(getAdapterPosition() );
        }
    }

    public interface OnPlantillaListener{
        void onPlantillaClick(int position);
    }
}
