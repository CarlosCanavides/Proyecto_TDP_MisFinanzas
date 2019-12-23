package com.example.proyecto_tdp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.activities.SetTransaccionActivity;
import com.example.proyecto_tdp.adapters.AdapterTransacciones;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.base_de_datos.entidades.Subcategoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.view_models.ViewModelSubcategoria;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private DateFormat formatFecha = new SimpleDateFormat(Constantes.FORMATO_FECHA);
    private DateFormat formatFechaMes = new SimpleDateFormat("MM/yyyy");

    private String fechaInicio;
    private String fechaFin;
    private String mesActual;
    private Calendar calendario;

    private float gastoPorMes;
    private NumberFormat nf = NumberFormat.getInstance(new Locale("es", "ES"));

    private View vista;
    private static final int NRO_PEDIDO_SET = 1827;

    private ViewModelTransaccion viewModelTransaccion;
    private ViewModelSubcategoria viewModelSubcategoria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_transacciones, container, false);
        expTransacciones = vista.findViewById(R.id.expTransacciones);
        btnMesAnterior = vista.findViewById(R.id.btn_mes_anterior);
        btnMesSiguiente = vista.findViewById(R.id.btn_mes_siguiente);
        tvMesTransacciones = vista.findViewById(R.id.transaccion_mes);
        tvGastoPorMes = vista.findViewById(R.id.transaccion_gasto_por_mes);
        inicializarListViewTransacciones();
        inicializarPeriodoDeTiempo();
        inicializarViewModel();
        listenerBotonesPrincipales();
        return vista;
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
        calendario = Calendar.getInstance();
        mesActual = formatFechaMes.format(calendario.getTime());
        setIntervaloTiempo();
    }

    private void setIntervaloTiempo(){
        String mes = formatFechaMes.format(calendario.getTime());
        tvMesTransacciones.setText(mes);
        calendario.set(Calendar.DAY_OF_MONTH,1);
        fechaInicio = formatFecha.format(calendario.getTime());
        calendario.set(Calendar.DAY_OF_MONTH, calendario.getActualMaximum(Calendar.DAY_OF_MONTH));
        fechaFin = formatFecha.format(calendario.getTime());
    }

    private void inicializarViewModel(){
        viewModelTransaccion = ViewModelProviders.of(getActivity()).get(ViewModelTransaccion.class);
        viewModelSubcategoria = ViewModelProviders.of(getActivity()).get(ViewModelSubcategoria.class);
        recopilarDatos();
    }

    private void listenerBotonesPrincipales(){
        btnMesAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendario.set(Calendar.DAY_OF_MONTH,1);
                calendario.set(Calendar.MONTH, calendario.get(Calendar.MONTH)-1);
                setIntervaloTiempo();
                recopilarDatos();
                btnMesSiguiente.setEnabled(true);
                btnMesSiguiente.setVisibility(View.VISIBLE);
            }
        });
        btnMesSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendario.set(Calendar.DAY_OF_MONTH,1);
                calendario.set(Calendar.MONTH, calendario.get(Calendar.MONTH)+1);
                setIntervaloTiempo();
                recopilarDatos();
                if(formatFechaMes.format(calendario.getTime()).equals(mesActual)) {
                    btnMesSiguiente.setEnabled(false);
                    btnMesSiguiente.setVisibility(View.GONE);
                }
            }
        });
        btnMesSiguiente.setEnabled(false);
        btnMesSiguiente.setVisibility(View.GONE);
    }

    private void recopilarDatos(){
        gastoPorMes = 0;
        LiveData<List<Transaccion>> liveData =  viewModelTransaccion.getTransaccionesDesdeHasta(fechaInicio,fechaFin);
        liveData.observe(getActivity(), new Observer<List<Transaccion>>(){
            @Override
            public void onChanged(List<Transaccion> transaccions) {
                fechas.clear();
                mapTransacciones.clear();
                mapColorCategoria.clear();
                gastoPorMes = 0;
                adapter.notifyDataSetChanged();
                for(Transaccion t : transaccions){
                    actualizarDatos(t);
                }
                actualizarTVGastoPorMes();
                adapter.notifyDataSetChanged();
                for (int i=0; i<fechas.size(); i++){
                    expTransacciones.expandGroup(i);
                }
            }
        });
    }

    private void actualizarDatos(Transaccion t){
        String fechaTransaccion = formatFecha.format(t.getFecha());
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
        Subcategoria subcategoria = viewModelSubcategoria.getSubcategoriaPorNombre(t.getCategoria());
        mapColorCategoria.put(t,subcategoria.getColorSubcategoria());
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
        if (requestCode == NRO_PEDIDO_SET) {
            if (resultCode == -1) {
                int id = data.getIntExtra("id",-1);
                String precio = data.getStringExtra("precio");
                String categoria = data.getStringExtra("categoria");
                String tipoTransaccion = data.getStringExtra("tipoT");
                String titulo = data.getStringExtra("titulo");
                String etiqueta = data.getStringExtra("etiqueta");
                String fecha = data.getStringExtra("fecha");
                String info = data.getStringExtra("info");

                float monto = 0;
                try {
                    monto = nf.parse(precio).floatValue();
                }catch (Exception e) {
                    mostrarMensaje("El monto ingresado debe ser mayor a 0");
                }

                if(id!=-1) {
                    if(tipoTransaccion.equals("Gasto")){
                        monto = monto*(-1);
                    }
                    Transaccion nueva = new Transaccion(titulo, etiqueta, monto, categoria, tipoTransaccion, new Date(), info);
                    nueva.setId(id);
                    viewModelTransaccion.actualizarTransaccion(nueva);
                }
            }
            else if (resultCode == 0) {
                int id = data.getIntExtra("id",-1);
                if(id!=-1) {
                    Transaccion t = new Transaccion("","",0,"","",new Date(),"");
                    t.setId(id);
                    viewModelTransaccion.actualizarTransaccion(t);
                    viewModelTransaccion.eliminarTransaccion(t);
                }
            }
        }
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
