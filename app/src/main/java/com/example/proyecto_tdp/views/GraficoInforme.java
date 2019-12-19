package com.example.proyecto_tdp.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import com.example.proyecto_tdp.base_de_datos.entidades.Subcategoria;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import java.util.ArrayList;
import java.util.Iterator;
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

    public GraficoInforme inicializarGraficoInforme(GraficoInforme grafico, Map<Subcategoria,Float> gastos){
        calcularPorcentajes(gastos);
        grafico.getDescription().setText("");
        grafico.setBackgroundColor(Color.WHITE);
        grafico.animateY(400);
        grafico.setHoleRadius(50);
        grafico.setTransparentCircleRadius(60);
        grafico.setData(getDatos());
        grafico.setLegend(grafico);
        grafico.setUsePercentValues(true);
        grafico.getData().setValueFormatter(new PercentFormatter(grafico));
        grafico.getLegend().setCustom(new ArrayList<LegendEntry>());
        grafico.invalidate();
        return grafico;
    }

    private void calcularPorcentajes(Map<Subcategoria,Float> gastos){
        labels = new ArrayList<>();
        porcentajes = new ArrayList<>();
        colores = new ArrayList<>();
        float totalGasto = 0;
        Iterator it = gastos.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Subcategoria,Float> entry = (Map.Entry<Subcategoria, Float>) it.next();
            labels.add(entry.getKey().getNombreSubcategoria());
            colores.add(entry.getKey().getColorSubcategoria());
            totalGasto += entry.getValue();
        }
        Iterator iterator = gastos.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Subcategoria,Float> e = (Map.Entry<Subcategoria, Float>) iterator.next();
            porcentajes.add((e.getValue()*100)/totalGasto);
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

    private void setLegend(Chart chart){
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (int i=0; i<labels.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colores.get(i);
            entry.label = labels.get(i);
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<PieEntry> getPieEntries(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i=0; i<porcentajes.size(); i++) {
            entries.add(new PieEntry(porcentajes.get(i),i));
        }
        return entries;
    }
}
