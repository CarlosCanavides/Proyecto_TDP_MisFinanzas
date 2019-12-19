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
import com.example.proyecto_tdp.views.GraficoInforme;
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

    private GraficoInforme pieChart;

    private View vista;

    private String[] meses = new String[]{"Enero","Febrero","Marzo","Abril","Mayo"};
    private int[] datos = new int[]{25,20,38,10,7};
    private int[] colores = new int[]{Color.BLUE,Color.RED,Color.BLUE,Color.YELLOW, Color.LTGRAY};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_informes, container, false);

        //pieChart = vista.findViewById(R.id.pieChart);

        return vista;
    }

}
