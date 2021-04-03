package com.example.proyecto_tdp.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.view_types.HeaderOrRow;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AdapterTransacciones extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HeaderOrRow> transaccionesPorFecha;
    private Map<String,Categoria> mapCategoriaDeTransaccion;
    private OnTransaccionListener onTransaccionListener;
    private DateTimeFormatter formatoFecha;

    public AdapterTransacciones(List<HeaderOrRow> transaccionesPorFecha, Map<String,Categoria> mapCategoriaDeTransaccion, OnTransaccionListener onTransaccionListener) {
        this.transaccionesPorFecha = transaccionesPorFecha;
        this.mapCategoriaDeTransaccion = mapCategoriaDeTransaccion;
        this.onTransaccionListener = onTransaccionListener;
        formatoFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA_PARA_VISUALIZAR);
    }

    @Override
    public int getItemCount() {
        return transaccionesPorFecha.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if(transaccionesPorFecha.get(position).isRow()){
            viewType = 1;
        }
        else {
            viewType = 0;
        }
        return  viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encabezado_transacciones_fecha, parent, false);
            return new ViewHolderHeader(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion, parent, false);
            return new ViewHolderRow(v,onTransaccionListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HeaderOrRow item = transaccionesPorFecha.get(position);
        if(item.isRow()) {
            ViewHolderRow holderRow = (ViewHolderRow) holder;
            Transaccion transaccion = item.getRow();
            holderRow.tvEtiqueta.setText(transaccion.getEtiqueta());
            String titulo = transaccion.getTitulo();
            String idCategoria = transaccion.getCategoria();
            float precio = transaccion.getPrecio();
            if(titulo==null || titulo.equals("")){
                titulo = Constantes.SIN_TITULO;
            }
            holderRow.tvTitulo.setText(titulo);
            if(precio>0){
                holderRow.tvPrecio.setText("+$"+String.format( "%.2f",precio));
                holderRow.tvPrecio.setTextColor(Color.parseColor("#0EB87A"));
            }
            else if(precio<0){
                holderRow.tvPrecio.setText("-$"+String.format( "%.2f",Math.abs(precio)));
                holderRow.tvPrecio.setTextColor(Color.parseColor("#E12E48"));
            }
            else {
                holderRow.tvPrecio.setText("+$0,00");
                holderRow.tvPrecio.setTextColor(Color.parseColor("#FFC22B"));
            }
            if(idCategoria==null){
                idCategoria = Constantes.SIN_CATEGORIA;
            }
            Categoria categoria = mapCategoriaDeTransaccion.get(idCategoria);
            if(categoria!=null){
                String nombreCategoria = categoria.getNombreCategoria();
                holderRow.tvCategoria.setText(nombreCategoria);
                if(nombreCategoria.length()>0){
                    holderRow.tvLetarCategoria.setText(nombreCategoria.charAt(0)+"");
                }
                holderRow.tvLetarCategoria.getBackground().setColorFilter(categoria.getColorCategoria(), PorterDuff.Mode.SRC);
            }
        } else {
            ViewHolderHeader holderHeader = (ViewHolderHeader) holder;
            Date fecha = item.getHeader();
            float balance = 0;
            boolean continuar = true;
            HeaderOrRow headerOrRow;
            for(int i=position+1; i<transaccionesPorFecha.size()&&continuar; i++){
                headerOrRow = transaccionesPorFecha.get(i);
                if(headerOrRow.isRow()){
                    balance = balance+headerOrRow.getRow().getPrecio();
                }
                else {
                    continuar = false;
                }
            }
            holderHeader.tvFecha.setText(formatoFecha.print(fecha.getTime()));
            if(balance>0) {
                holderHeader.tvBalance.setText("+$"+String.format( "%.2f",balance));
                holderHeader.tvBalance.setTextColor(Color.parseColor("#0EB87A"));
            }
            else if(balance<0){
                holderHeader.tvBalance.setText("-$"+String.format( "%.2f",Math.abs(balance)));
                holderHeader.tvBalance.setTextColor(Color.parseColor("#E12E48"));
            }
            else {
                holderHeader.tvBalance.setText("$0,00");
                holderHeader.tvBalance.setTextColor(Color.parseColor("#FFC22B"));
            }
        }
    }

    public void refrescar(){
        transaccionesPorFecha.clear();
        mapCategoriaDeTransaccion.clear();
        this.notifyDataSetChanged();
    }

    public class ViewHolderRow extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTitulo;
        private TextView tvCategoria;
        private TextView tvEtiqueta;
        private TextView tvPrecio;
        private TextView tvLetarCategoria;
        private OnTransaccionListener onTransaccionListener;

        public ViewHolderRow(@NonNull View itemView, OnTransaccionListener onTransaccionListener) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.itemTransaccion_nombre);
            tvCategoria = itemView.findViewById(R.id.itemTransaccion_categoria);
            tvEtiqueta = itemView.findViewById(R.id.itemTransaccion_etiqueta);
            tvPrecio = itemView.findViewById(R.id.itemTransaccion_precio);
            tvLetarCategoria = itemView.findViewById(R.id.idImagen);
            itemView.setOnClickListener(this);
            this.onTransaccionListener = onTransaccionListener;
        }

        @Override
        public void onClick(View v) {
            onTransaccionListener.onTransaccionClick(getAdapterPosition());
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder{

        private TextView tvFecha;
        private TextView tvBalance;

        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.encabezado_section_fecha);
            tvBalance = itemView.findViewById(R.id.encabezado_section_gp);
        }
    }

    public interface OnTransaccionListener{
        void onTransaccionClick(int position);
    }
}
