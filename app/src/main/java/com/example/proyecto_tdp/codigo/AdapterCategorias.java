package com.example.proyecto_tdp.codigo;

import android.graphics.Color;
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

import java.util.ArrayList;

public class AdapterCategorias extends RecyclerView.Adapter<AdapterCategorias.ViewHolderCategorias> {

    private ArrayList<Categoria> categorias;
    private OnItemClickListener listener;

    public AdapterCategorias(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
    }

    @NonNull
    @Override
    public AdapterCategorias.ViewHolderCategorias onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, null, false);
        return new ViewHolderCategorias(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategorias.ViewHolderCategorias holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.etiNombreCategoria.setText(categoria.getNombreCategoria());
        holder.etiFoto.setText(categoria.getNombreCategoria().charAt(0)+"");

        Drawable bg = holder.etiFoto.getBackground();
        bg.setColorFilter(categoria.getColorCategoria(), PorterDuff.Mode.SRC);
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class ViewHolderCategorias extends RecyclerView.ViewHolder {

        private TextView etiNombreCategoria;
        private TextView etiFoto;

        public ViewHolderCategorias(@NonNull View itemView) {
            super(itemView);
            etiNombreCategoria = itemView.findViewById(R.id.categoria_nombre);
            etiFoto = itemView.findViewById(R.id.categoria_letra);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(listener!=null && pos!=RecyclerView.NO_POSITION)
                        listener.onItemClik(categorias.get(pos));
                }
            });

        }

    }

    public interface OnItemClickListener{
        void onItemClik(Categoria categoria);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
