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
import com.example.proyecto_tdp.base_de_datos.entidades.Subcategoria;
import java.util.List;
import java.util.Map;

public class AdapterInformeMes extends RecyclerView.Adapter<AdapterInformeMes.ViewHolderInformeMes> {

    private List<Subcategoria> subcategorias;
    private Map<Subcategoria, Float> gastoPorSubcategoria;

    public AdapterInformeMes(List<Subcategoria> subcategorias, Map<Subcategoria, Float> gastoPorSubcategoria) {
        this.subcategorias = subcategorias;
        this.gastoPorSubcategoria = gastoPorSubcategoria;
    }

    @NonNull
    @Override
    public ViewHolderInformeMes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_informe_gasto_por_categoria, null, false);
        return new ViewHolderInformeMes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInformeMes holder, int position) {
        Subcategoria subcategoria = subcategorias.get(position);
        if(subcategoria!=null) {
            holder.tvNombreCategoria.setText(subcategoria.getNombreSubcategoria());
            holder.tvGastoCategoria.setText(gastoPorSubcategoria.get(subcategoria) + "");
            //holder.tvIconoCategoria.setText(subcategoria.getNombreSubcategoria().charAt(0));
            Drawable bg = holder.tvIconoCategoria.getBackground();
            bg.setColorFilter(subcategoria.getColorSubcategoria(), PorterDuff.Mode.SRC);
        }
    }

    @Override
    public int getItemCount() {
        return subcategorias.size();
    }

    public class ViewHolderInformeMes extends RecyclerView.ViewHolder {

        private TextView tvNombreCategoria;
        private TextView tvGastoCategoria;
        private TextView tvIconoCategoria;

        public ViewHolderInformeMes(@NonNull View itemView) {
            super(itemView);
            tvNombreCategoria = itemView.findViewById(R.id.informes_nombre_categroia);
            tvGastoCategoria = itemView.findViewById(R.id.informes_gasto_categoria);
            tvIconoCategoria = itemView.findViewById(R.id.informes_icono_categroia);
        }
    }
}
