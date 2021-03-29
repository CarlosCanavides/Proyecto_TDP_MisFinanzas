package com.example.proyecto_tdp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class AdapterTransaccionesFijas extends BaseExpandableListAdapter {

    private List<String> frecuencias;
    private Map<String,List<TransaccionFija>> mapTransaccionesFijas;
    private DateTimeFormatter formatoFecha;

    public AdapterTransaccionesFijas(List<String> frecuencias, Map<String,List<TransaccionFija>> mapTransaccionesFijas) {
        this.frecuencias = frecuencias;
        this.mapTransaccionesFijas = mapTransaccionesFijas;
        formatoFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);
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
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encabezado_tf_frecuencia,null,false);
        TextView tvFrecuencia = convertView.findViewById(R.id.encabezado_frecuencia);
        tvFrecuencia.setText(frecuencia);
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
        tvLetra.setText(transaccionFija.getCategoria().charAt(0)+"");
        tvTitulo.setText(transaccionFija.getTitulo());
        tvPrecio.setText(transaccionFija.getPrecio()+"");
        float precio = transaccionFija.getPrecio();
        if(precio>=0){
            tvPrecio.setText("+ $ "+precio);
        }
        else {
            tvPrecio.setText("- $ "+Math.abs(precio));
        }
        Log.e("AQUIIIIIIIIIIIIIIIII","inserte una nueva transaccion fija en el mapeo");
        tvFechas.setText(formatoFecha.print(transaccionFija.getFecha().getTime())+" | "+formatoFecha.print(transaccionFija.getFechaFinal().getTime()));
        return convertView;
    }

    public void refresh(){
        mapTransaccionesFijas.clear();
        this.notifyDataSetChanged();
    }
}
