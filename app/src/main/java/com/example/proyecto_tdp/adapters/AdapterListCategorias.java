package com.example.proyecto_tdp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Subcategoria;

import java.util.List;
import java.util.Map;

public class AdapterListCategorias extends BaseExpandableListAdapter {

    private List<Categoria> categorias;
    private Map<Categoria, List<Subcategoria>> mapSubcategorias;

    public AdapterListCategorias(List<Categoria> categorias, Map<Categoria, List<Subcategoria>> mapSubcategorias) {
        this.categorias = categorias;
        this.mapSubcategorias = mapSubcategorias;
    }

    @Override
    public int getGroupCount() {
        return categorias.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = 0;
        List<Subcategoria> subcategorias = mapSubcategorias.get(categorias.get(groupPosition));
        if(subcategorias!=null){
            size = subcategorias.size();
        }
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categorias.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mapSubcategorias.get(categorias.get(groupPosition)).get(childPosition);
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
        String tituloCategoria = categorias.get(groupPosition).getNombreCategoria();
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, null, false);
        TextView tvGroup = convertView.findViewById(R.id.categoria_nombre);
        if(tituloCategoria!=null) {
            tvGroup.setText(tituloCategoria);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String tituloSubcategoria =  mapSubcategorias.get(categorias.get(groupPosition)).get(childPosition).getNombreSubcategoria();
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategoria, null, false);
        TextView tvChild = convertView.findViewById(R.id.subcategoria_nombre);
        if(tituloSubcategoria!=null) {
            tvChild.setText(tituloSubcategoria);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
