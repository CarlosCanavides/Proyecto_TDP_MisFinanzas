package com.example.proyecto_tdp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.activities.modificar_datos.SetTransaccionActivity;
import com.example.proyecto_tdp.adapters.AdapterTransacciones;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.view_types.HeaderOrRow;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaDeVerificacion;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaSoloTransacciones;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransaccionesFragment extends Fragment implements AdapterTransacciones.OnTransaccionListener {

    private TextView tvGastoPorMes;
    private TextView tvMesTransacciones;
    private ImageButton btnMesAnterior;
    private ImageButton btnMesSiguiente;
    private RecyclerView recyclerTransaccionesDelMes;
    private Map<String,Categoria> mapCategoriaDeTransacciones;
    private List<HeaderOrRow> transaccionesPorFecha;
    private AdapterTransacciones adapter;
    private ViewModelCategoria viewModelCategoria;
    private ViewModelTransaccion viewModelTransaccion;
    private LocalDate localDate;
    private String fechaInicio;
    private String fechaFin;
    private String mesActual;
    private float gastoPorMes;
    private EstrategiaDeVerificacion estrategiaDeVerificacion;
    private DateTimeFormatter formatFechaMes =  DateTimeFormat.forPattern("MM/yyyy");
    private DateTimeFormatter formatFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transacciones, container, false);
        recyclerTransaccionesDelMes = view.findViewById(R.id.recycler_transacciones_por_fecha);
        btnMesAnterior = view.findViewById(R.id.btn_mes_anterior);
        btnMesSiguiente = view.findViewById(R.id.btn_mes_siguiente);
        tvMesTransacciones = view.findViewById(R.id.transaccion_mes);
        tvGastoPorMes = view.findViewById(R.id.transaccion_gasto_por_mes);
        inicializarListViewTransacciones();
        inicializarPeriodoDeTiempo();
        inicializarViewModel();
        listenerBotonesPrincipales();
        return view;
    }

    private void inicializarListViewTransacciones(){
        recyclerTransaccionesDelMes.setLayoutManager(new GridLayoutManager(getActivity(),1));
        transaccionesPorFecha = new ArrayList<>();
        mapCategoriaDeTransacciones = new HashMap<>();
        adapter = new AdapterTransacciones(transaccionesPorFecha,mapCategoriaDeTransacciones,this);
        recyclerTransaccionesDelMes.setAdapter(adapter);
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
        viewModelCategoria = new ViewModelProvider(this).get(ViewModelCategoria.class);
        viewModelTransaccion = new ViewModelProvider(this).get(ViewModelTransaccion.class);
        estrategiaDeVerificacion = new EstrategiaSoloTransacciones(viewModelTransaccion);
        viewModelTransaccion.getAllTransacciones().observe(getActivity(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transacciones) {
                gastoPorMes = 0;
                transaccionesPorFecha.clear();
                mapCategoriaDeTransacciones.clear();
                adapter.refrescar();
                for(int i=0; i<transacciones.size(); i++) {
                    Transaccion transaccion = transacciones.get(i);
                    Date fecha = transaccion.getFecha();
                    if(formatFechaMes.print(fecha.getTime()).equals(tvMesTransacciones.getText().toString())) {
                        transaccionesPorFecha.add(HeaderOrRow.createHeader(fecha));
                        transaccionesPorFecha.add(HeaderOrRow.createRow(transaccion));
                        gastoPorMes += transaccion.getPrecio();
                        agregarCategoria(transaccion);
                        boolean continuar = true;
                        for(int j=i; (j<transacciones.size()-1)&&continuar; j++){
                            Transaccion transaccionSiguiente = transacciones.get(j);
                            Date fechaTransaccionSiguiente = transaccionSiguiente.getFecha();
                            if(fecha.compareTo(fechaTransaccionSiguiente)==0) {
                                transaccionesPorFecha.add(HeaderOrRow.createRow(transaccionSiguiente));
                                agregarCategoria(transaccionSiguiente);
                                i++;
                            }
                            else {
                                continuar = false;
                            }
                        }
                    }
                }
                actualizarTVGastoPorMes();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void agregarCategoria(Transaccion transaccion){
        String idCategoria = transaccion.getCategoria();
        Categoria categoria;
        if(idCategoria==null){
            if(mapCategoriaDeTransacciones.get(Constantes.SIN_CATEGORIA)==null) {
                categoria = new Categoria(Constantes.SIN_CATEGORIA, null, Color.parseColor("#FF5722"), Constantes.GASTO);
                mapCategoriaDeTransacciones.put(Constantes.SIN_CATEGORIA,categoria);
            }
        }
        else {
            if(mapCategoriaDeTransacciones.get(idCategoria)==null) {
                categoria = viewModelCategoria.getCategoriaPorID(transaccion.getCategoria());
                mapCategoriaDeTransacciones.put(idCategoria,categoria);
            }
        }
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
        transaccionesPorFecha.clear();
        mapCategoriaDeTransacciones.clear();
        adapter.refrescar();
        for(int i=0; i<transaccionesDelMes.size(); i++) {
            Transaccion transaccion = transaccionesDelMes.get(i);
            Date fecha = transaccion.getFecha();
            if(formatFechaMes.print(fecha.getTime()).equals(tvMesTransacciones.getText().toString())) {
                transaccionesPorFecha.add(HeaderOrRow.createHeader(fecha));
                transaccionesPorFecha.add(HeaderOrRow.createRow(transaccion));
                gastoPorMes += transaccion.getPrecio();
                agregarCategoria(transaccion);
                boolean continuar = true;
                for(int j=i; (j<transaccionesDelMes.size()-1)&&continuar; j++){
                    Transaccion transaccionSiguiente = transaccionesDelMes.get(j);
                    Date fechaTransaccionSiguiente = transaccionSiguiente.getFecha();
                    if(fecha.compareTo(fechaTransaccionSiguiente)==0) {
                        transaccionesPorFecha.add(HeaderOrRow.createRow(transaccionSiguiente));
                        agregarCategoria(transaccionSiguiente);
                        i++;
                    }
                    else {
                        continuar = false;
                    }
                }
            }
        }
        actualizarTVGastoPorMes();
        adapter.notifyDataSetChanged();
    }

    private void actualizarTVGastoPorMes(){
        if (gastoPorMes >= 0) {
            tvGastoPorMes.setText("+ $ "+String.format( "%.2f", Math.abs(gastoPorMes)));
        } else {
            tvGastoPorMes.setText("- $ "+String.format( "%.2f", Math.abs(gastoPorMes)));
        }
    }

    @Override
    public void onTransaccionClick(int position) {
        Transaccion transaccion = transaccionesPorFecha.get(position).getRow();
        Intent intent = new Intent(getActivity(), SetTransaccionActivity.class);
        intent.putExtra(Constantes.CAMPO_ID, transaccion.getId());
        intent.putExtra(Constantes.CAMPO_ID_TF_PADRE, transaccion.getTransaccionFijaPadre());
        intent.putExtra(Constantes.CAMPO_PRECIO, String.format( "%.2f", Math.abs(transaccion.getPrecio())));
        intent.putExtra(Constantes.CAMPO_TIPO, transaccion.getTipoTransaccion());
        intent.putExtra(Constantes.CAMPO_TITULO, transaccion.getTitulo());
        intent.putExtra(Constantes.CAMPO_ETIQUETA, transaccion.getEtiqueta());
        intent.putExtra(Constantes.CAMPO_FECHA, formatFecha.print(transaccion.getFecha().getTime()));
        intent.putExtra(Constantes.CAMPO_INFO, transaccion.getInfo());
        intent.putExtra(Constantes.CAMPO_ID_CATEGORIA, transaccion.getCategoria());
        if(transaccion.getCategoria()!=null){
            intent.putExtra(Constantes.CAMPO_NOMBRE_CATEGORIA, mapCategoriaDeTransacciones.get(transaccion.getCategoria()).getNombreCategoria());
        }
        startActivityForResult(intent, Constantes.PEDIDO_SET_TRANSACCION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==Constantes.PEDIDO_SET_TRANSACCION){
            estrategiaDeVerificacion.verificar(requestCode,resultCode,data);
        }
    }
}
