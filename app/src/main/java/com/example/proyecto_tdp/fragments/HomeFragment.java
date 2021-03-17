package com.example.proyecto_tdp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterTransaccionesFijas;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{

    private DecoView barraProgreso;
    private TextView tvPorcentaje;
    private RecyclerView transaccionesFijas;
    private AdapterTransaccionesFijas adapterTransaccionesFijas;
    private View vista;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        vista = inflater.inflate(R.layout.fragment_home, container, false);
        barraProgreso = vista.findViewById(R.id.progress_bar_circular);
        tvPorcentaje = vista.findViewById(R.id.tv_porcentaje);
        transaccionesFijas = vista.findViewById(R.id.recycler_transacciones_fijas);
        List lista = new ArrayList();
        lista.add("Ingresos Fijos");
        lista.add("Gastos Fijos");
        lista.add("Plantillas");
        transaccionesFijas.setLayoutManager(new GridLayoutManager(getActivity(),1));
        adapterTransaccionesFijas = new AdapterTransaccionesFijas(lista);
        transaccionesFijas.setAdapter(adapterTransaccionesFijas);
        inicializarBarraProgreso();
        return vista;
    }

    private void inicializarBarraProgreso(){
        barraProgreso.configureAngles(230,0);
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .build();
        int backIndex = barraProgreso.addSeries(seriesItem);
        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, 100, 0)
                .setInitialVisibility(true)
                .setSeriesLabel(new SeriesLabel.Builder("%.0f%%").build())
                .build();
        int series1Index = barraProgreso.addSeries(seriesItem1);
        barraProgreso.addEvent(new DecoEvent.Builder(25).setIndex(series1Index).setDelay(1000).build());

        seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem1.getMinValue()) / (seriesItem1.getMaxValue() - seriesItem1.getMinValue()));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
    }
}
