package com.example.proyecto_tdp.adapters;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import java.util.List;
import java.util.Map;

public class AdapterTransacciones extends BaseExpandableListAdapter {

    private List<String> fechas;
    private Map<String, List<Transaccion>> mapTransacciones;
    private Map<Transaccion,Categoria> mapCategoriaDeTransaccion;

    public AdapterTransacciones(List<String> fechas, Map<String, List<Transaccion>> mapTransacciones, Map<Transaccion,Categoria> mapCategoriaDeTransaccion) {
        this.fechas = fechas;
        this.mapTransacciones = mapTransacciones;
        this.mapCategoriaDeTransaccion = mapCategoriaDeTransaccion;
    }

    @Override
    public int getGroupCount() {
        return fechas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = 0;
        List<Transaccion> transacciones = mapTransacciones.get(fechas.get(groupPosition));
        if(transacciones!=null) {
            size = transacciones.size();
        }
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return fechas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mapTransacciones.get(fechas.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String fecha = fechas.get(groupPosition);
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encabezado_transacciones_fecha,null,false);
        TextView tvFecha = convertView.findViewById(R.id.encabezado_section_fecha);
        TextView tvGP = convertView.findViewById(R.id.encabezado_section_gp);
        tvFecha.setText(fecha);
        float resultado = 0;
        List<Transaccion> transacciones =  mapTransacciones.get(fechas.get(groupPosition));
        if(transacciones!=null) {
            for (Transaccion t : transacciones) {
                resultado += t.getPrecio();
            }
        }
        String resultadoFinal = String.format( "%.2f", Math.abs(resultado));
        if(resultado<0){
            tvGP.setText("- $ "+resultadoFinal);
            tvGP.setTextColor(convertView.getResources().getColor(R.color.color_precios_negativos));
        }
        else {
            tvGP.setText("+ $ "+resultadoFinal);
            tvGP.setTextColor(convertView.getResources().getColor(R.color.color_precios_positivos));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Transaccion transaccion = mapTransacciones.get(fechas.get(groupPosition)).get(childPosition);
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion,null,false);
        TextView tvTitulo = convertView.findViewById(R.id.itemTransaccion_nombre);
        TextView tvCategoria = convertView.findViewById(R.id.itemTransaccion_categoria);
        TextView tvIdentificacion = convertView.findViewById(R.id.itemTransaccion_etiqueta);
        TextView tvLetra = convertView.findViewById(R.id.idImagen);
        TextView tvPrecio = convertView.findViewById(R.id.itemTransaccion_precio);

        Categoria categoria = mapCategoriaDeTransaccion.get(transaccion);
        String titulo = transaccion.getTitulo();
        if(categoria==null){
            tvLetra.setText("S");
            tvCategoria.setText(Constantes.SIN_CATEGORIA);
        }
        else {
            tvCategoria.setText(categoria.getNombreCategoria());
            Drawable bg = tvLetra.getBackground();
            bg.setColorFilter(categoria.getColorCategoria(), PorterDuff.Mode.SRC);
            if(categoria.getNombreCategoria().length() > 0) {
                tvLetra.setText(categoria.getNombreCategoria().charAt(0)+"");
            }
        }
        if(titulo.equals("")){
            tvTitulo.setText("Sin titulo");
        }
        else {
            tvTitulo.setText(titulo);
        }
        if(!transaccion.getEtiqueta().equals("")) {
            tvIdentificacion.setText(" " + transaccion.getEtiqueta() + " ");
        }

        String monto = String.format( "%.2f", Math.abs(transaccion.getPrecio()));
        if(transaccion.getTipoTransaccion().equals("Gasto")){
            tvPrecio.setText("- $ "+monto);
            tvPrecio.setTextColor(convertView.getResources().getColor(R.color.color_precios_negativos));
        }
        else{
            tvPrecio.setText("+ $ "+monto);
            tvPrecio.setTextColor(convertView.getResources().getColor(R.color.color_precios_positivos));
        }
        return convertView;
    }

    public void refrescar(){
        fechas.clear();
        mapTransacciones.clear();
        mapCategoriaDeTransaccion.clear();
        this.notifyDataSetChanged();
    }
}
