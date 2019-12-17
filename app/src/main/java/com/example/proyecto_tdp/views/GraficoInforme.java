package com.example.proyecto_tdp.views;

import android.graphics.Color;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

public class GraficoInforme {
    private String[] resultadoDelMes = new String[]{"Gasto","Ingreso"};
    private int[] colores = new int[]{Color.RED,Color.GREEN};
    private float[] porcentajes = new float[]{100,0};

    public GraficoResumen inicializarGrafico(GraficoResumen grafico, float ingreso, float gasto){
        calcularPorcentajes(ingreso,gasto);
        grafico.getDescription().setText("");
        grafico.getDescription().setTextSize(10);
        grafico.setBackgroundColor(Color.WHITE);
        grafico.animateY(2000);
        grafico.setHoleRadius(20);
        grafico.setData(getDatos());
        grafico.invalidate();
        setLegend(grafico);
        return grafico;
    }

    private void calcularPorcentajes(float ingreso, float gasto){
        float total = ingreso+Math.abs(gasto);
        float porcentajeIngreso = (ingreso*100)/total;
        float porcentajeGasto   = (gasto*100)/total;
        porcentajes[0] = porcentajeIngreso;
        porcentajes[1] = porcentajeGasto;
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
        for (int i=0; i<resultadoDelMes.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colores[i];
            entry.label = resultadoDelMes[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<PieEntry> getPieEntries(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i=0; i<porcentajes.length; i++) {
            entries.add(new PieEntry(i, porcentajes[i]));
        }
        return entries;
    }
}
