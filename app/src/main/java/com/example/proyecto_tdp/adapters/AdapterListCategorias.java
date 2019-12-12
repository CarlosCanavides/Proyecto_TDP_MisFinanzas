package com.example.proyecto_tdp.adapters;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
        Categoria categoria = categorias.get(groupPosition);
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, null, false);
        TextView tvNombre = convertView.findViewById(R.id.categoria_nombre);
        TextView tvLetra = convertView.findViewById(R.id.categoria_letra);
        if(categoria!=null) {
            tvNombre.setText(categoria.getNombreCategoria());
            tvLetra.setText(categoria.getNombreCategoria().charAt(0)+"");
            Drawable bg = tvLetra.getBackground();
            bg.setColorFilter(categoria.getColorCategoria(), PorterDuff.Mode.SRC);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Subcategoria subcategoria =  mapSubcategorias.get(categorias.get(groupPosition)).get(childPosition);
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategoria, null, false);
        TextView tvNombre = convertView.findViewById(R.id.subcategoria_nombre);
        TextView tvLetra = convertView.findViewById(R.id.subcategoria_letra);
        if(subcategoria!=null) {
            tvNombre.setText(subcategoria.getNombreSubcategoria());
            tvLetra.setText(subcategoria.getNombreSubcategoria().charAt(0)+"");
            Drawable bg = tvLetra.getBackground();
            bg.setColorFilter(subcategoria.getColorSubcategoria(), PorterDuff.Mode.SRC);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
