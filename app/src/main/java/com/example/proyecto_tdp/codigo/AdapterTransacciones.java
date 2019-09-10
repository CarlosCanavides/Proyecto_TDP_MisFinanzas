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
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;

import java.util.ArrayList;

public class AdapterTransacciones extends RecyclerView.Adapter<AdapterTransacciones.ViewHolderTransacciones> {

    private ArrayList<Transaccion> listDatos;
    private OnItemClickListener listener;

    public AdapterTransacciones(ArrayList<Transaccion> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderTransacciones onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null, false);
        return new ViewHolderTransacciones(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTransacciones viewHolderDatos, int i) {
        Transaccion transaccion = listDatos.get(i);
        viewHolderDatos.etiCategoria.setText(transaccion.getCategoria());
        viewHolderDatos.etiNombre.setText(transaccion.getTitulo());
        viewHolderDatos.etiIdentificacion.setText(transaccion.getEtiqueta());

        if(transaccion.getTipoTransaccion().equals("gasto")){
            viewHolderDatos.etiPrecio.setText("- "+transaccion.getPrecio());
            viewHolderDatos.etiPrecio.setTextColor(Color.parseColor("#FF5722"));
        }
        else{
            viewHolderDatos.etiPrecio.setText("+ "+transaccion.getPrecio());
            viewHolderDatos.etiPrecio.setTextColor(Color.parseColor("#303F9F"));
        }

        if(transaccion.getCategoria()!=null && transaccion.getCategoria().length() > 0) {
            viewHolderDatos.foto.setText(transaccion.getCategoria().charAt(0)+"");
        }
        else {
            viewHolderDatos.foto.setText("S");
        }
        Drawable bg = viewHolderDatos.foto.getBackground();
        bg.setColorFilter(Color.parseColor("#7373FF"), PorterDuff.Mode.SRC);
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public Transaccion getTransaccionAt(int pos){
        return listDatos.get(pos);
    }


    public class ViewHolderTransacciones extends RecyclerView.ViewHolder {

        private TextView etiNombre;
        private TextView etiCategoria;
        private TextView etiIdentificacion;
        private TextView etiPrecio;
        private TextView foto;

        public ViewHolderTransacciones(@NonNull View itemView) {
            super(itemView);
            etiCategoria = itemView.findViewById(R.id.itemTransaccion_categoria);
            etiNombre = itemView.findViewById(R.id.itemTransaccion_nombre);
            etiIdentificacion = itemView.findViewById(R.id.itemTransaccion_etiqueta);
            etiPrecio = itemView.findViewById(R.id.itemTransaccion_precio);
            foto = itemView.findViewById(R.id.idImagen);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(listener!=null && pos!=RecyclerView.NO_POSITION)
                    listener.onItemClik(listDatos.get(pos));
                }
            });
        }

    }

    public interface OnItemClickListener{
        void onItemClik(Transaccion transaccion);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
