package com.example.proyecto_tdp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import java.util.List;

public class HomeFragment extends Fragment{

    private DecoView barraProgreso;
    private TextView tvBalance;
    private TextView tvIngresoTotal;
    private TextView tvGastoPromedio;
    private TextView tvIngresoPromedio;
    private View vista;
    private ViewModelTransaccion viewModelTransaccion;
    private float gastoTotal;
    private float ingresoTotal;
    private int seriePrincipalIndex;
    private Observer<List<Transaccion>> observer;
    private LiveData<List<Transaccion>> liveData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        vista = inflater.inflate(R.layout.fragment_home, container, false);
        barraProgreso = vista.findViewById(R.id.progress_bar_circular);
        tvBalance = vista.findViewById(R.id.tv_balance);
        tvIngresoTotal = vista.findViewById(R.id.tv_ingreso_total);
        tvGastoPromedio = vista.findViewById(R.id.tv_ingreso_promedio);
        tvIngresoPromedio = vista.findViewById(R.id.tv_gasto_promedio);
        tvIngresoPromedio.setText("$0");
        tvGastoPromedio.setText("$0");
        inicializarBarraProgreso();
        inicializarViewModel();
        return vista;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        liveData.removeObserver(observer);
    }

    private void inicializarBarraProgreso(){
        barraProgreso.configureAngles(230,0);
        SeriesItem serieDeFondo = new SeriesItem.Builder(Color.parseColor("#DBE4F0"))
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
    }

    private void inicializarViewModel() {
        gastoTotal=0;
        ingresoTotal=0;
        observer = new Observer<List<Transaccion>>() {
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
                tvIngresoTotal.setText("$"+ingresoTotal);
                float balance = ingresoTotal+gastoTotal;
                if(balance<0){
                    tvBalance.setText("-$"+Math.abs(balance));
                }
                else {
                    tvBalance.setText("$"+balance);
                }
                actualizarBarraDeProgreso();
                Log.e("AQUIIIIIIIIIIIIIIIII",""+seriePrincipalIndex);
                if(cantidadTransaccionesGasto!=0 && cantidadTransaccionesIngreso!=0) {
                    tvIngresoPromedio.setText("$" + ingresoTotal / cantidadTransaccionesIngreso);
                    tvGastoPromedio.setText("$" + Math.abs(gastoTotal) / cantidadTransaccionesGasto);
                }
            }
        };

        viewModelTransaccion = ViewModelProviders.of(getActivity()).get(ViewModelTransaccion.class);
        liveData = viewModelTransaccion.getAllTransacciones();
        liveData.observe(getActivity(), observer);
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
                    colorBarraProgreso = Color.parseColor("#00C853");
               }
        barraProgreso.addEvent(new DecoEvent.Builder((int) porcentajeRestante)
                .setIndex(seriePrincipalIndex)
                .setDelay(300)
                .setColor(colorBarraProgreso)
                .build());
        barraProgreso.refreshDrawableState();
    }
}
