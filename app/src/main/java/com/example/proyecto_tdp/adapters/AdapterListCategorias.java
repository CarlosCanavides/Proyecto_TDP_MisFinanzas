package com.example.proyecto_tdp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.proyecto_tdp.R;

import java.util.List;
import java.util.Map;

public class AdapterListCategorias extends BaseExpandableListAdapter {

    private List<String> categorias;
    private Map<String, List<String>> mapHijos;
    private Context context;

    public AdapterListCategorias(List<String> categorias, Map<String, List<String>> mapHijos, Context context) {
        this.categorias = categorias;
        this.mapHijos = mapHijos;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return categorias.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mapHijos.get(categorias.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categorias.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mapHijos.get(categorias.get(groupPosition)).get(childPosition);
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
        String tituloCategoria = (String) getGroup(groupPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_categoria, null);
        TextView tvGroup = convertView.findViewById(R.id.categoria_nombre);
        tvGroup.setText(tituloCategoria);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String tituloCategoriaHijo = (String) getChild(groupPosition, childPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_subcategoria, null);
        TextView tvChild = convertView.findViewById(R.id.subcategoria_nombre);
        tvChild.setText(tituloCategoriaHijo);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
