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
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.views.GraficoResumen;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AdapterResumen extends RecyclerView.Adapter<AdapterResumen.ViewHolderResumen> {

    private List<String> meses;
    private Map<String, List<Transaccion>> listaResumen;
    private Map<String,Integer> categoriaGastoPredominante;
    private Resources recursos;

    public AdapterResumen(List<String> meses, Map<String, List<Transaccion>> listaResumen, Map<String,Integer> categoriaGastoPredominante) {
        this.meses = meses;
        this.listaResumen = listaResumen;
        this.categoriaGastoPredominante = categoriaGastoPredominante;
    }

    @NonNull
    @Override
    public ViewHolderResumen onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_resumen, null, false);
        recursos = view.getResources();
        return new ViewHolderResumen(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderResumen viewHolderResumen, int i) {
        viewHolderResumen.tvMes.setText(meses.get(i));
        List<Transaccion> listaTransaccionesMes = listaResumen.get(meses.get(i));
        if(listaTransaccionesMes!=null){
            viewHolderResumen.tvCantidadTransacciones.setText(listaTransaccionesMes.size()+" transacciones");
        }
        float gastos = 0;
        float ingresos = 0;
        float resultado;
        for (Transaccion t : listaTransaccionesMes){
            if(t.getTipoTransaccion().equals("Gasto")){
                gastos += t.getPrecio();
            }
            else {
                ingresos += t.getPrecio();
            }
        }
        resultado = ingresos+gastos;
        viewHolderResumen.tvIngreso.setText("↑$ " + ingresos);
        viewHolderResumen.tvGasto.setText("↓$ " +Math.abs(gastos));
        if(resultado>0) {
            viewHolderResumen.tvBalance.setText("Σ -$ "+resultado);
        }
        else {
            viewHolderResumen.tvBalance.setText(" Σ -$ "+Math.abs(resultado));
        }
        viewHolderResumen.grafico.inicializarGrafico(viewHolderResumen.grafico,ingresos,gastos,recursos);

        Iterator it = categoriaGastoPredominante.entrySet().iterator();
        int mayor = 0;
        String categoriaMayor = "";
        while(it.hasNext()){
            Map.Entry<String,Integer> entry = (Map.Entry<String, Integer>) it.next();
            if(entry.getValue()>mayor){
                mayor = entry.getValue();
                categoriaMayor = entry.getKey();
            }
        }
        if(categoriaMayor.equals("")){
            viewHolderResumen.tvColorCategoria.setVisibility(View.GONE);
        }
        else {
            viewHolderResumen.tvColorCategoria.setVisibility(View.VISIBLE);
            Drawable bg = viewHolderResumen.tvColorCategoria.getBackground();
            bg.setColorFilter(Color.parseColor("#7373FF"), PorterDuff.Mode.SRC);
        }
        viewHolderResumen.tvCategoriaPredominante.setText(categoriaMayor);
    }

    @Override
    public int getItemCount() {
        return meses.size();
    }


    public class ViewHolderResumen extends RecyclerView.ViewHolder{

        private TextView tvMes;
        private TextView tvIngreso;
        private TextView tvGasto;
        private TextView tvBalance;
        private TextView tvCantidadTransacciones;
        private TextView tvCategoriaPredominante;
        private TextView tvColorCategoria;
        private GraficoResumen grafico;


        public ViewHolderResumen(@NonNull View itemView) {
            super(itemView);
            tvMes = itemView.findViewById(R.id.resumen_mes);
            tvIngreso = itemView.findViewById(R.id.resumen_ingreso);
            tvGasto = itemView.findViewById(R.id.resumen_gasto);
            tvBalance = itemView.findViewById(R.id.resumen_balance);
            tvCantidadTransacciones = itemView.findViewById(R.id.resumen_cantidad_transacciones);
            tvCategoriaPredominante = itemView.findViewById(R.id.resumen_categoria_predominante);
            tvColorCategoria = itemView.findViewById(R.id.circulo_color_categoria);
            grafico = itemView.findViewById(R.id.resumen_grafico);
        }
    }
}
