package com.example.proyecto_tdp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterViewPagerHome;
import com.google.android.material.tabs.TabLayout;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class HomeFragment extends Fragment{

    private DecoView barraProgreso;
    private TextView tvPorcentaje;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View vista;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        vista = inflater.inflate(R.layout.fragment_home, container, false);
        barraProgreso = vista.findViewById(R.id.progress_bar_circular);
        tvPorcentaje = vista.findViewById(R.id.tv_porcentaje);
        tabLayout = vista.findViewById(R.id.principal_tabLayout);
        viewPager = vista.findViewById(R.id.principal_viewpager);
        viewPager.setAdapter(new AdapterViewPagerHome(getFragmentManager(),3));
        tabLayout.setupWithViewPager(viewPager);
        barraProgreso.configureAngles(230,0);
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                                .setRange(0, 100, 100)
                                .setInitialVisibility(true)
                                .build();
        int backIndex = barraProgreso.addSeries(seriesItem);
        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                                .setRange(0, 100, 0)
                                .setInitialVisibility(true)
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
        return vista;
    }
}
