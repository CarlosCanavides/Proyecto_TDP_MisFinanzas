package com.example.proyecto_tdp.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraficoInforme extends PieChart {

    private List<String> labels;
    private List<Float> porcentajes;
    private List<Integer> colores;

    public GraficoInforme(Context context) {
        super(context);
    }

    public GraficoInforme(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraficoInforme(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GraficoInforme inicializarGraficoInforme(GraficoInforme grafico, List<Categoria> categorias, Map<String,Float> gastos){
        calcularPorcentajes(categorias,gastos);
        grafico.getDescription().setText("");
        grafico.setBackgroundColor(Color.WHITE);
        grafico.animateY(400);
        grafico.setHoleRadius(50);
        grafico.setTransparentCircleRadius(60);
        grafico.setData(getDatos());
        grafico.setUsePercentValues(true);
        grafico.getData().setValueFormatter(new PercentFormatter(grafico));
        grafico.getLegend().setCustom(new ArrayList<LegendEntry>());
        grafico.invalidate();
        return grafico;
    }

    private void calcularPorcentajes(List<Categoria> categorias, Map<String, Float> gastos){
        labels = new ArrayList<>();
        porcentajes = new ArrayList<>();
        colores = new ArrayList<>();
        float totalGasto = 0;
        float gastoPorCategoria;
        for(Categoria c : categorias){
            gastoPorCategoria = gastos.get(c.getNombreCategoria());
            labels.add(c.getNombreCategoria());
            colores.add(c.getColorCategoria());
            totalGasto += gastoPorCategoria;
        }
        for(Categoria c : categorias) {
            gastoPorCategoria = gastos.get(c.getNombreCategoria());
            porcentajes.add((gastoPorCategoria*100)/totalGasto);
        }
    }

    private PieData getDatos(){
        PieDataSet dataSet = new PieDataSet(getPieEntries(),"");
        dataSet.setColors(colores);
        dataSet.setValueTextSize(Color.WHITE);
        dataSet.setValueTextSize(10);
        dataSet.setSliceSpace(2);
        dataSet.setValueFormatter(new PercentFormatter());
        return new PieData(dataSet);
    }

    private ArrayList<PieEntry> getPieEntries(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i=0; i<porcentajes.size(); i++) {
            entries.add(new PieEntry(porcentajes.get(i),i));
        }
        return entries;
    }
}
