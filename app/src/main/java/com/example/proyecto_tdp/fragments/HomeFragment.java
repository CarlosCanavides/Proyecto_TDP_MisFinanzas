package com.example.proyecto_tdp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.PlantillasActivity;
import com.example.proyecto_tdp.activities.TransaccionesFijasActivity;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.base_de_datos.entidades.TransaccionFija;
import com.example.proyecto_tdp.view_models.ViewModelPlantilla;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccionFija;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import java.util.List;

public class HomeFragment extends Fragment{

    private DecoView barraProgreso;
    private ProgressBar barraProgresoHorizontal;
    private TextView tvBalance;
    private TextView tvIngresoTotal;
    private TextView tvGastoPromedio;
    private TextView tvIngresoPromedio;
    private TextView tvCantidadPlantillas;
    private TextView tvCantidadGastosFijos;
    private TextView tvCantidadIngresosFijos;
    private TextView tvPorcentajeTransaccionesGasto;
    private LinearLayout panelPlantillas;
    private LinearLayout panelGastosFijos;
    private LinearLayout panelIngresosFijos;
    private ViewModelTransaccion viewModelTransaccion;
    private ViewModelPlantilla viewModelPlantilla;
    private ViewModelTransaccionFija viewModelTransaccionFija;
    private float gastoTotal;
    private float ingresoTotal;
    private int seriePrincipalIndex;
    private Observer<List<Plantilla>> observerPlantillas;
    private Observer<List<Transaccion>> observerTransacciones;
    private Observer<List<TransaccionFija>> observerTransaccionesFijas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        barraProgreso = view.findViewById(R.id.progress_bar_circular);
        barraProgresoHorizontal = view.findViewById(R.id.barra_progreso_horizontal);
        tvBalance = view.findViewById(R.id.tv_balance);
        tvIngresoTotal = view.findViewById(R.id.tv_ingreso_total);
        tvGastoPromedio = view.findViewById(R.id.tv_gasto_promedio);
        tvIngresoPromedio = view.findViewById(R.id.tv_ingreso_promedio);
        tvCantidadPlantillas = view.findViewById(R.id.tv_cantidad_plantillas);
        tvCantidadGastosFijos = view.findViewById(R.id.tv_cantidad_gastos_fijos);
        tvCantidadIngresosFijos = view.findViewById(R.id.tv_cantidad_ingresos_fijos);
        tvPorcentajeTransaccionesGasto = view.findViewById(R.id.porcentaje_transacciones_gasto);
        panelPlantillas = view.findViewById(R.id.panel_plantillas);
        panelGastosFijos = view.findViewById(R.id.panel_gastos_fijos);
        panelIngresosFijos = view.findViewById(R.id.panel_ingresos_fijos);
        inicializarCampos();
        inicializarBarraProgreso();
        inicializarPanelesTransaccionesProgramadas();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inicializarViewModel();
    }

    private void inicializarCampos(){
        tvCantidadPlantillas.setText("0");
        tvCantidadGastosFijos.setText("0");
        tvCantidadIngresosFijos.setText("0");
        tvIngresoPromedio.setText("$ 0");
        tvGastoPromedio.setText("$ 0");
        tvPorcentajeTransaccionesGasto.setText("0 %");
        gastoTotal=0;
        ingresoTotal=0;
    }

    private void inicializarBarraProgreso(){
        barraProgreso.configureAngles(230,0);
        SeriesItem serieDeFondo = new SeriesItem.Builder(Color.parseColor("#BDBDBD"))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .build();
        barraProgreso.addSeries(serieDeFondo);
        final SeriesItem seriePrincipal = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, 100, 0)
                .setInitialVisibility(true)
                .setSeriesLabel(new SeriesLabel.Builder("%.0f%%").build())
                .build();
        seriePrincipalIndex = barraProgreso.addSeries(seriePrincipal);
        seriePrincipal.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriePrincipal.getMinValue()) / (seriePrincipal.getMaxValue() - seriePrincipal.getMinValue()));
            }
            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {}
        });
        barraProgresoHorizontal.setProgress(100);
    }

    private void inicializarViewModel() {
        inicializarObservers();
        viewModelPlantilla = new ViewModelProvider(getActivity()).get(ViewModelPlantilla.class);
        viewModelTransaccion = new ViewModelProvider(getActivity()).get(ViewModelTransaccion.class);
        viewModelTransaccionFija = new ViewModelProvider(getActivity()).get(ViewModelTransaccionFija.class);
        viewModelPlantilla.getAllPlantillas().observe(getViewLifecycleOwner(),observerPlantillas);
        viewModelTransaccion.getAllTransacciones().observe(getViewLifecycleOwner(), observerTransacciones);
        viewModelTransaccionFija.getAllTransaccionesFijas().observe(getViewLifecycleOwner(),observerTransaccionesFijas);
    }

    private void inicializarObservers() {
        observerTransacciones = new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transaccions) {
                gastoTotal=0;
                ingresoTotal=0;
                int cantidadTransaccionesGasto = 0;
                int cantidadTransaccionesIngreso = 0;
                for (Transaccion t : transaccions){
                    String tipoTransaccion = t.getTipoTransaccion();
                    if(tipoTransaccion.equals(Constantes.INGRESO)){
                        ingresoTotal = ingresoTotal+t.getPrecio();
                        cantidadTransaccionesIngreso++;
                    }
                    else if(tipoTransaccion.equals(Constantes.GASTO)){
                        gastoTotal = gastoTotal+t.getPrecio();
                        cantidadTransaccionesGasto++;
                    }
                }
                tvIngresoTotal.setText("$"+String.format( "%.2f",ingresoTotal)+" ");
                float balance = ingresoTotal+gastoTotal;
                if(balance<0){
                    tvBalance.setText("- $ "+String.format( "%.2f",Math.abs(balance)));
                }
                else {
                    tvBalance.setText("$ "+String.format( "%.2f",balance));
                }
                actualizarBarraDeProgreso();
                if(cantidadTransaccionesGasto>0) {
                    tvGastoPromedio.setText("$ "+String.format( "%.2f",Math.abs(gastoTotal)/cantidadTransaccionesGasto));
                }
                if(cantidadTransaccionesIngreso>0){
                    tvIngresoPromedio.setText("$ "+String.format( "%.2f",ingresoTotal/cantidadTransaccionesIngreso));
                }
                int cantidadTotalTransacciones = cantidadTransaccionesGasto+cantidadTransaccionesIngreso;
                if(cantidadTransaccionesGasto==0){
                    barraProgresoHorizontal.setProgress(100);
                    tvPorcentajeTransaccionesGasto.setText("0 %");
                }
                else {
                    if (cantidadTransaccionesIngreso == 0) {
                        barraProgresoHorizontal.setProgress(0);
                        barraProgresoHorizontal.refreshDrawableState();
                        tvPorcentajeTransaccionesGasto.setText("100 %");
                    }
                    else {
                        int porcentaje = (cantidadTransaccionesGasto*100)/cantidadTotalTransacciones;
                        barraProgresoHorizontal.setProgress(100-porcentaje);
                        tvPorcentajeTransaccionesGasto.setText(porcentaje+" %");
                    }
                }
            }
        };
        observerTransaccionesFijas = new Observer<List<TransaccionFija>>() {
            @Override
            public void onChanged(List<TransaccionFija> transaccionFijas) {
                int cantidadGF = 0;
                int cantidadIF = 0;
                for (TransaccionFija t : transaccionFijas){
                    if(t.getTipoTransaccion().equals(Constantes.GASTO)){
                        cantidadGF++;
                    }
                    else {
                        cantidadIF++;
                    }
                }
                tvCantidadGastosFijos.setText(""+cantidadGF);
                tvCantidadIngresosFijos.setText(""+cantidadIF);
            }
        };
        observerPlantillas = new Observer<List<Plantilla>>() {
            @Override
            public void onChanged(List<Plantilla> plantillas) {
                tvCantidadPlantillas.setText(""+plantillas.size());
            }
        };
    }

    private void inicializarPanelesTransaccionesProgramadas(){
        panelPlantillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlantillasActivity.class);
                startActivity(intent);
            }
        });
        panelGastosFijos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransaccionesFijasActivity.class);
                startActivity(intent);
            }
        });
        panelIngresosFijos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransaccionesFijasActivity.class);
                startActivity(intent);
            }
        });
    }

    private void actualizarBarraDeProgreso(){
        float porcentajeRestante = 100;
        if((ingresoTotal+gastoTotal)>0){
            porcentajeRestante = porcentajeRestante-((Math.abs(gastoTotal)*100)/ingresoTotal);
        }
        else {
            porcentajeRestante=0;
        }
        int colorBarraProgreso;
        if(porcentajeRestante<33){
            colorBarraProgreso = Color.parseColor("#C62828");
        } else if (porcentajeRestante >= 33 && porcentajeRestante < 66) {
                colorBarraProgreso = Color.parseColor("#F6CE55");
               } else {
                    colorBarraProgreso = Color.parseColor("#4FEBA2");
               }
        barraProgreso.addEvent(new DecoEvent.Builder((int) porcentajeRestante)
                .setIndex(seriePrincipalIndex)
                .setDelay(400)
                .setColor(colorBarraProgreso)
                .build());
        barraProgreso.refreshDrawableState();
    }
}
