package com.example.proyecto_tdp.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AdapterT extends BaseExpandableListAdapter {

    private List<String> fechas;
    private Map<String, List<Transaccion>> mapTransacciones;

    public AdapterT(List<String> fechas, Map<String, List<Transaccion>> mapTransacciones) {
        this.fechas = fechas;
        this.mapTransacciones = mapTransacciones;
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String fecha = fechas.get(groupPosition);
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_fecha,null,false);
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
        if(resultado<0){
            tvGP.setText("-$ "+Math.abs(resultado));
        }
        else {
            tvGP.setText("+$ " + resultado);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Transaccion transaccion = mapTransacciones.get(fechas.get(groupPosition)).get(childPosition);
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        TextView tvNombre = convertView.findViewById(R.id.itemTransaccion_nombre);
        TextView tvCategoria = convertView.findViewById(R.id.itemTransaccion_categoria);
        TextView tvIdentificacion = convertView.findViewById(R.id.itemTransaccion_etiqueta);
        TextView tvLetra = convertView.findViewById(R.id.idImagen);
        TextView tvPrecio = convertView.findViewById(R.id.itemTransaccion_precio);

        tvCategoria.setText(transaccion.getCategoria());
        tvNombre.setText(transaccion.getTitulo());
        tvIdentificacion.setText(transaccion.getEtiqueta());

        if(transaccion.getTipoTransaccion().equals("gasto")){
            tvPrecio.setText("-$ "+transaccion.getPrecio());
            tvPrecio.setTextColor(Color.parseColor("#FF5722"));
        }
        else{
            tvPrecio.setText("+$ "+transaccion.getPrecio());
            tvPrecio.setTextColor(Color.parseColor("#303F9F"));
        }

        if(transaccion.getCategoria()!=null && transaccion.getCategoria().length() > 0) {
            tvLetra.setText(transaccion.getCategoria().charAt(0)+"");
        }
        else {
            tvLetra.setText("S");
        }
        Drawable bg = tvLetra.getBackground();
        bg.setColorFilter(Color.parseColor("#7373FF"), PorterDuff.Mode.SRC);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
