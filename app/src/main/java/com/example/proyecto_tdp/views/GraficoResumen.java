package com.example.proyecto_tdp.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;

import com.example.proyecto_tdp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GraficoResumen extends PieChart{

    private String[] resultadoDelMes = new String[]{"Ingreso","Gasto"};
    private int[] colores = new int[]{Color.GREEN,Color.RED};
    private float[] porcentajes = new float[]{1,0};

    public GraficoResumen(Context context) {
        super(context);
    }

    public GraficoResumen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraficoResumen(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GraficoResumen inicializarGrafico(GraficoResumen grafico, float ingreso, float gasto, Resources recursos){
        setColores(recursos);
        calcularPorcentajes(ingreso,Math.abs(gasto));
        grafico.getDescription().setText("");
        grafico.setBackgroundColor(Color.WHITE);
        grafico.animateY(500);
        grafico.setHoleRadius(0);
        grafico.setTransparentCircleRadius(0);
        grafico.setData(getDatos());
        grafico.setUsePercentValues(true);
        grafico.getData().setValueFormatter(new PercentFormatter(grafico));
        grafico.getLegend().setCustom(new ArrayList<LegendEntry>());
        grafico.invalidate();
        return grafico;
    }

    private void setColores(Resources recursos){
        colores[0] = recursos.getColor(R.color.color_precios_positivos);
        colores[1] = recursos.getColor(R.color.color_precios_negativos);
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
        dataSet.setValueTextSize(10);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setSliceSpace(2);
        return new PieData(dataSet);
    }

    private ArrayList<PieEntry> getPieEntries(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (float porcentaje : porcentajes) {
            entries.add(new PieEntry(porcentaje,""));
        }
        return entries;
    }
}
