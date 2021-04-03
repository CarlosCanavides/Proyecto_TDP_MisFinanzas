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
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.views.GraficoInforme;
import java.util.List;
import java.util.Map;

public class AdapterInformeMes extends RecyclerView.Adapter<AdapterInformeMes.ViewHolderInformeMes> {

    private List<Categoria> categorias;
    private Map<String, Float> gastoPorSubcategoria;
    private static final int TYPE_GRAFICO = 0;
    private static final int TYPE_SUBCATEGORIA = 1;

    public AdapterInformeMes(List<Categoria> categorias, Map<String, Float> gastoPorSubcategoria) {
        this.categorias = categorias;
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
            if(categorias.size()>0) {
                holder.graficoInforme.setVisibility(View.VISIBLE);
                holder.graficoInforme.inicializarGraficoInforme(holder.graficoInforme, categorias, gastoPorSubcategoria);
            }
            else {
                holder.graficoInforme.setVisibility(View.INVISIBLE);
            }
        }
        else {
            Categoria categoria = categorias.get(position-1);
            if (categoria != null) {
                holder.tvNombreCategoria.setText(categoria.getNombreCategoria());
                holder.tvGastoCategoria.setText("-$" + String.format( "%.2f",gastoPorSubcategoria.get(categoria.getId())));
                holder.tvIconoCategoria.setText(categoria.getNombreCategoria().charAt(0)+"");
                Drawable bg = holder.tvIconoCategoria.getBackground();
                bg.setColorFilter(categoria.getColorCategoria(), PorterDuff.Mode.SRC);
            }
        }
    }

    @Override
    public int getItemCount() {
        return categorias.size()+1;
    }

    public void refresh(){
        categorias.clear();
        gastoPorSubcategoria.clear();
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
