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
import com.example.proyecto_tdp.views.GraficoInforme;
import java.util.List;
import java.util.Map;

public class AdapterInformeMes extends RecyclerView.Adapter<AdapterInformeMes.ViewHolderInformeMes> {

    private List<Subcategoria> subcategorias;
    private Map<Subcategoria, Float> gastoPorSubcategoria;
    private static final int TYPE_GRAFICO = 0;
    private static final int TYPE_SUBCATEGORIA = 1;

    public AdapterInformeMes(List<Subcategoria> subcategorias, Map<Subcategoria, Float> gastoPorSubcategoria) {
        this.subcategorias = subcategorias;
        this.gastoPorSubcategoria = gastoPorSubcategoria;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_GRAFICO;
        }
        else {
            return TYPE_SUBCATEGORIA;
        }
    }

    @NonNull
    @Override
    public ViewHolderInformeMes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==TYPE_SUBCATEGORIA) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_informe_gasto_por_categoria, null, false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_informes_grafico, null, false);
        }
        return new ViewHolderInformeMes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInformeMes holder, int position) {
        if(position==0){
            holder.graficoInforme.inicializarGraficoInforme(holder.graficoInforme,gastoPorSubcategoria);
        }
        else {
            Subcategoria subcategoria = subcategorias.get(position-1);
            if (subcategoria != null) {
                holder.tvNombreCategoria.setText(subcategoria.getNombreSubcategoria());
                holder.tvGastoCategoria.setText("- $ " + gastoPorSubcategoria.get(subcategoria));
                holder.tvIconoCategoria.setText(subcategoria.getNombreSubcategoria().charAt(0)+"");
                Drawable bg = holder.tvIconoCategoria.getBackground();
                bg.setColorFilter(subcategoria.getColorSubcategoria(), PorterDuff.Mode.SRC);
            }
        }
    }

    @Override
    public int getItemCount() {
        return subcategorias.size()+1;
    }

    public class ViewHolderInformeMes extends RecyclerView.ViewHolder {

        private TextView tvNombreCategoria;
        private TextView tvGastoCategoria;
        private TextView tvIconoCategoria;
        private GraficoInforme graficoInforme;

        public ViewHolderInformeMes(@NonNull View itemView) {
            super(itemView);
            tvNombreCategoria = itemView.findViewById(R.id.informes_nombre_categroia);
            tvGastoCategoria = itemView.findViewById(R.id.informes_gasto_categoria);
            tvIconoCategoria = itemView.findViewById(R.id.informes_icono_categroia);
            graficoInforme = itemView.findViewById(R.id.informes_grafico);
        }
    }
}
