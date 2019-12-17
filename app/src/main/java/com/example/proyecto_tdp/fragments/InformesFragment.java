package com.example.proyecto_tdp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.proyecto_tdp.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import java.util.ArrayList;

public class InformesFragment extends Fragment {

    private PieChart pieChart;

    private View vista;

    private String[] meses = new String[]{"Enero","Febrero","Marzo","Abril","Mayo"};
    private int[] datos = new int[]{25,20,38,10,7};
    private int[] colores = new int[]{Color.BLUE,Color.RED,Color.BLUE,Color.YELLOW, Color.LTGRAY};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_informes, container, false);

        pieChart = vista.findViewById(R.id.pieChart);
        createChart();

        return vista;
    }

    private Chart setChart(Chart chart, String descripcion, int textColor, int background, int animateY){
        chart.getDescription().setText(descripcion);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(background);
        chart.animateY(animateY);
        legend(chart);
        return chart;
    }

    private void legend(Chart chart){
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (int i=0; i<meses.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colores[i];
            entry.label = meses[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<PieEntry> getPieEntries(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i=0; i<datos.length; i++) {
            entries.add(new PieEntry(datos[i], i));
        }
        return entries;
    }

    private void axisX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(meses));
    }

    private void axisLeft(YAxis axis){
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
    }

    private void axisRight(YAxis axis){
        axis.setEnabled(false);
    }

    private void createChart(){
        pieChart = (PieChart) setChart(pieChart,"GRAFICO",Color.BLACK,Color.WHITE,3000);
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleRadius(20);
        pieChart.setData(getData());
        pieChart.invalidate();
    }

    private PieData getData(){
        PieDataSet dataSet = new PieDataSet(getPieEntries(),"");
        dataSet.setColors(colores);
        dataSet.setValueTextSize(Color.WHITE);
        dataSet.setValueTextSize(10);

        dataSet.setSliceSpace(2);
        dataSet.setValueFormatter(new PercentFormatter());
        return new PieData(dataSet);
    }
}
