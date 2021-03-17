package com.example.proyecto_tdp.adapters;

import android.content.res.Resources;
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
import java.util.List;

public class AdapterTransaccionesFijas extends RecyclerView.Adapter<AdapterTransaccionesFijas.ViewHolderTransaccionesFijas>{

    private List<String> transaccionesFijas;
    private Resources recursos;

    public AdapterTransaccionesFijas(List<String> transaccionesFijas) {
        this.transaccionesFijas = transaccionesFijas;
    }

    @NonNull
    @Override
    public ViewHolderTransaccionesFijas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transacciones_fijas, null, false);
        recursos = view.getResources();
        return new ViewHolderTransaccionesFijas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTransaccionesFijas holder, int position) {
        holder.tvNombreTransaccionFija.setText(transaccionesFijas.get(position));
        holder.tvInfoTransaccionFija.setText("");
        holder.tvIconoTransaccionFija.setText("T");
        holder.tvMontoTransaccionFija.setText("$352,56");
        Drawable bg = holder.tvIconoTransaccionFija.getBackground();
        switch (position){
            case 0 : bg.setColorFilter(Color.blue(10), PorterDuff.Mode.SRC); break;
            case 1 : bg.setColorFilter(Color.blue(10), PorterDuff.Mode.SRC); break;
            case 2 : bg.setColorFilter(Color.parseColor("#F6DFE1"), PorterDuff.Mode.SRC); break;
            default: bg.setColorFilter(Color.parseColor("#F6DFE1"), PorterDuff.Mode.SRC);
        }

        bg.setColorFilter(Color.parseColor("#7373FF"), PorterDuff.Mode.SRC);
    }

    @Override
    public int getItemCount() {
        return transaccionesFijas.size();
    }

    public class ViewHolderTransaccionesFijas extends RecyclerView.ViewHolder {

        private TextView tvNombreTransaccionFija;
        private TextView tvInfoTransaccionFija;
        private TextView tvMontoTransaccionFija;
        private TextView tvIconoTransaccionFija;

        public ViewHolderTransaccionesFijas(@NonNull View itemView) {
            super(itemView);
            tvNombreTransaccionFija = itemView.findViewById(R.id.transaccion_fija_nombre);
            tvInfoTransaccionFija = itemView.findViewById(R.id.transaccion_fija_info);
            tvMontoTransaccionFija = itemView.findViewById(R.id.transaccion_fija_monto);
            tvIconoTransaccionFija = itemView.findViewById(R.id.transaccion_fija_icono);
        }
    }
}
