package com.example.proyecto_tdp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.activities.modificar_datos.SetTransaccionActivity;
import com.example.proyecto_tdp.adapters.AdapterTransacciones;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaDeVerificacion;
import com.example.proyecto_tdp.verificador_estrategia.VerificadorParaTransacciones;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.view_models.ViewModelPlantilla;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccionFija;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransaccionesFragment extends Fragment {

    private ExpandableListView expTransacciones;
    private List<String> fechas;
    private Map<String, List<Transaccion>> mapTransacciones;
    private Map<Transaccion, Integer> mapColorCategoria;
    private AdapterTransacciones adapter;
    private ImageButton btnMesAnterior;
    private ImageButton btnMesSiguiente;
    private TextView tvMesTransacciones;
    private TextView tvGastoPorMes;
    private DateTimeFormatter formatFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);
    private DateTimeFormatter formatFechaMes =  DateTimeFormat.forPattern("MM/yyyy");
    private EstrategiaDeVerificacion estrategiaDeVerificacion;

    private String fechaInicio;
    private String fechaFin;
    private String mesActual;
    private LocalDate localDate;

    private float gastoPorMes;
    private NumberFormat nf = NumberFormat.getInstance(new Locale("es", "ES"));

    private View vista;
    private static final int NRO_PEDIDO_SET = 1827;

    private ViewModelTransaccion viewModelTransaccion;
    private ViewModelCategoria viewModelCategoria;
    private ViewModelPlantilla viewModelPlantilla;
    private ViewModelTransaccionFija viewModelTransaccionFija;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_transacciones, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expTransacciones = view.findViewById(R.id.expTransacciones);
        btnMesAnterior = view.findViewById(R.id.btn_mes_anterior);
        btnMesSiguiente = view.findViewById(R.id.btn_mes_siguiente);
        tvMesTransacciones = view.findViewById(R.id.transaccion_mes);
        tvGastoPorMes = view.findViewById(R.id.transaccion_gasto_por_mes);
        inicializarListViewTransacciones();
        inicializarPeriodoDeTiempo();
        inicializarViewModel();
        listenerBotonesPrincipales();
        estrategiaDeVerificacion = new VerificadorParaTransacciones(viewModelPlantilla,viewModelTransaccion,viewModelTransaccionFija);
    }

    private void inicializarListViewTransacciones(){
        fechas = new ArrayList<>();
        mapTransacciones = new HashMap<>();
        mapColorCategoria = new HashMap<>();
        adapter = new AdapterTransacciones(fechas,mapTransacciones,mapColorCategoria);
        expTransacciones.setAdapter(adapter);

        expTransacciones.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        expTransacciones.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Transaccion transaccion = mapTransacciones.get(fechas.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(getActivity(), SetTransaccionActivity.class);
                intent.putExtra("id", transaccion.getId());
                intent.putExtra("precio", String.format( "%.2f", Math.abs(transaccion.getPrecio())));
                intent.putExtra("categoria", transaccion.getCategoria());
                intent.putExtra("tipoT", transaccion.getTipoTransaccion());
                intent.putExtra("titulo", transaccion.getTitulo());
                intent.putExtra("etiqueta", transaccion.getEtiqueta());
                intent.putExtra("fecha", transaccion.getFecha());
                intent.putExtra("info", transaccion.getInfo());
                startActivityForResult(intent, NRO_PEDIDO_SET);
                return true;
            }
        });
    }

    private void inicializarPeriodoDeTiempo(){
        localDate = LocalDate.now();
        mesActual = formatFechaMes.print(localDate.toDate().getTime());
        setIntervaloTiempo();
    }

    private void setIntervaloTiempo(){
        String mes = formatFechaMes.print(localDate.toDate().getTime());
        tvMesTransacciones.setText(mes);
        localDate = localDate.withDayOfMonth(1);
        fechaInicio = formatFecha.print(localDate.toDate().getTime());
        localDate = localDate.withDayOfMonth(localDate.dayOfMonth().getMaximumValue());
        fechaFin = formatFecha.print(localDate.toDate().getTime());
    }

    private void inicializarViewModel(){
        viewModelPlantilla = ViewModelProviders.of(getActivity()).get(ViewModelPlantilla.class);
        viewModelTransaccion = ViewModelProviders.of(getActivity()).get(ViewModelTransaccion.class);
        viewModelTransaccionFija = ViewModelProviders.of(getActivity()).get(ViewModelTransaccionFija.class);
        viewModelCategoria = ViewModelProviders.of(getActivity()).get(ViewModelCategoria.class);
        viewModelTransaccion.getAllTransacciones().observe(getActivity(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transaccions) {
                gastoPorMes = 0;
                fechas.clear();
                mapTransacciones.clear();
                mapColorCategoria.clear();
                adapter.refrescar();
                for(Transaccion t : transaccions) {
                    if(formatFechaMes.print(t.getFecha().getTime()).equals(tvMesTransacciones.getText())) {
                        actualizarDatos(t);
                    }
                }
                actualizarTVGastoPorMes();
                adapter.notifyDataSetChanged();
                for (int i=0; i<fechas.size(); i++){
                    expTransacciones.expandGroup(i);
                }
            }
        });
        recopilarDatos();
    }

    private void listenerBotonesPrincipales(){
        btnMesAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localDate = localDate.withDayOfMonth(1);
                localDate = localDate.minusMonths(1);
                setIntervaloTiempo();
                recopilarDatos();
                btnMesSiguiente.setEnabled(true);
                btnMesSiguiente.setVisibility(View.VISIBLE);
            }
        });
        btnMesSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localDate = localDate.withDayOfMonth(1);
                localDate = localDate.plusMonths(1);
                setIntervaloTiempo();
                recopilarDatos();
                if(formatFechaMes.print(localDate.toDate().getTime()).equals(mesActual)) {
                    btnMesSiguiente.setEnabled(false);
                    btnMesSiguiente.setVisibility(View.GONE);
                }
            }
        });
        btnMesSiguiente.setEnabled(false);
        btnMesSiguiente.setVisibility(View.GONE);
    }

    private void recopilarDatos(){
        List<Transaccion> transaccionesDelMes = viewModelTransaccion.getTransaccionesDesdeHasta(fechaInicio,fechaFin);
        gastoPorMes = 0;
        fechas.clear();
        mapTransacciones.clear();
        mapColorCategoria.clear();
        adapter.refrescar();
        for(Transaccion t : transaccionesDelMes) {
            actualizarDatos(t);
        }
        actualizarTVGastoPorMes();
        adapter.notifyDataSetChanged();
        for (int i=0; i<fechas.size(); i++){
            expTransacciones.expandGroup(i);
        }
    }

    private void actualizarDatos(Transaccion t){
        String fechaTransaccion = formatFecha.print(t.getFecha().getTime());
        List<Transaccion> transaccionesRealizadas = mapTransacciones.get(fechaTransaccion);
        if(transaccionesRealizadas==null){
            transaccionesRealizadas = new ArrayList<>();
            transaccionesRealizadas.add(t);
            fechas.add(fechaTransaccion);
            mapTransacciones.put(fechaTransaccion,transaccionesRealizadas);
        }
        else {
            transaccionesRealizadas.add(t);
        }
        Categoria categoria = viewModelCategoria.getCategoriaPorNombre(t.getCategoria());
        if(categoria!=null) {
            mapColorCategoria.put(t, categoria.getColorCategoria());
        }
        gastoPorMes += t.getPrecio();
    }

    private void actualizarTVGastoPorMes(){
        if (gastoPorMes >= 0) {
            tvGastoPorMes.setText("+ $ "+String.format( "%.2f", Math.abs(gastoPorMes)));
        } else {
            tvGastoPorMes.setText("- $ "+String.format( "%.2f", Math.abs(gastoPorMes)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }
}
