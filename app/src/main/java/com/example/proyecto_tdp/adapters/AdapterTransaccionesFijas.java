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
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class AdapterTransaccionesFijas extends BaseExpandableListAdapter {

    private List<String> frecuencias;
    private Map<String,List<TransaccionFija>> mapTransaccionesFijas;
    private Map<String, Categoria> mapCategorias;
    private DateTimeFormatter formatoFecha;

    public AdapterTransaccionesFijas(List<String> frecuencias, Map<String,List<TransaccionFija>> mapTransaccionesFijas,  Map<String, Categoria> mapCategorias) {
        this.frecuencias = frecuencias;
        this.mapTransaccionesFijas = mapTransaccionesFijas;
        this.mapCategorias = mapCategorias;
        formatoFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA_PARA_VISUALIZAR);
    }

    @Override
    public int getGroupCount() {
        return frecuencias.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = 0;
        List<TransaccionFija> transaccionFijas = mapTransaccionesFijas.get(frecuencias.get(groupPosition));
        if(transaccionFijas!=null){
            size = transaccionFijas.size();
        }
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return frecuencias.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mapTransaccionesFijas.get(frecuencias.get(groupPosition)).get(childPosition);
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
        String frecuencia = frecuencias.get(groupPosition);
        if(mapTransaccionesFijas.get(frecuencias.get(groupPosition))!=null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encabezado_tf_frecuencia, null, false);
            TextView tvFrecuencia = convertView.findViewById(R.id.encabezado_frecuencia);
            tvFrecuencia.setText(frecuencia);
        }
        else {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invisible, null, false);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TransaccionFija transaccionFija = mapTransaccionesFijas.get(frecuencias.get(groupPosition)).get(childPosition);
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transacciones_fijas, null, false);
        TextView tvLetra = convertView.findViewById(R.id.transaccion_fija_icono);
        TextView tvFechas = convertView.findViewById(R.id.transaccion_fija_fechas);
        TextView tvTitulo = convertView.findViewById(R.id.transaccion_fija_nombre);
        TextView tvPrecio = convertView.findViewById(R.id.transaccion_fija_precio);
        TextView tvProximaEjecucion = convertView.findViewById(R.id.transaccion_fija_proxima_ejecucion);
        if(transaccionFija.getCategoria()!=null){
            Categoria categoria = mapCategorias.get(transaccionFija.getCategoria());
            String nombreCategoria = categoria.getNombreCategoria();
            tvLetra.setText(nombreCategoria.charAt(0)+"");
            Drawable bg = tvLetra.getBackground();
            bg.setColorFilter(categoria.getColorCategoria(), PorterDuff.Mode.SRC);
        }
        else {
            tvLetra.setText("S");
        }
        if(transaccionFija.getTitulo()!=null && !transaccionFija.getTitulo().equals("")){
            tvTitulo.setText(transaccionFija.getTitulo());
        }
        else {
            tvTitulo.setText(Constantes.SIN_TITULO);
        }
        tvPrecio.setText(transaccionFija.getPrecio()+"");
        float precio = transaccionFija.getPrecio();
        if(precio>=0){
            tvPrecio.setText("+ $ "+String.format( "%.2f",precio));
            tvPrecio.setTextColor(convertView.getResources().getColor(R.color.color_precios_positivos));
        }
        else {
            tvPrecio.setText("- $ "+String.format( "%.2f",Math.abs(precio)));
            tvPrecio.setTextColor(convertView.getResources().getColor(R.color.color_precios_negativos));
        }
        tvFechas.setText(formatoFecha.print(transaccionFija.getFecha().getTime())+" - "+formatoFecha.print(transaccionFija.getFechaFinal().getTime()));
        Date fechaProximaEjecucion = transaccionFija.getFechaProximaEjecucion();
        if(fechaProximaEjecucion!=null){
            tvProximaEjecucion.setText("Proxima Ejecucion: "+formatoFecha.print(fechaProximaEjecucion.getTime()));
        }
        else {
            tvProximaEjecucion.setText("Transaccion Fija Finalizada");
        }
        return convertView;
    }

    public void refresh(){
        mapTransaccionesFijas.clear();
        mapCategorias.clear();
        this.notifyDataSetChanged();
    }
}
