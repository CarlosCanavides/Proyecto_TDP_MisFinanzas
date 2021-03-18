package com.example.proyecto_tdp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterInformeMes;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GastoMesFregment extends Fragment {

    private String mes;
    protected int anio;
    protected int mesNumero;
    private View vista;
    protected List<Categoria> categoriasMes;
    protected Map<String,Float> mapCategoriasGasto;
    protected AdapterInformeMes adapterInforme;

    private RecyclerView recyclerCategorias;
    protected ViewModelTransaccion viewModelTransaccion;
    protected ViewModelCategoria viewModelCategoria;
    protected Calendar calendar;
    protected DateFormat formatFecha;
    protected String primerDia;
    protected String ultimoDia;

    public GastoMesFregment(int nroMes, int nroAnio){
        mesNumero = nroMes;
        switch (nroMes){
            case 0 : mes = Constantes.ENERO; break;
            case 1 : mes = Constantes.FEBRERO; break;
            case 2 : mes = Constantes.MARZO; break;
            case 3 : mes = Constantes.ABRIL; break;
            case 4 : mes = Constantes.MAYO; break;
            case 5 : mes = Constantes.JUNIO; break;
            case 6 : mes = Constantes.JULIO; break;
            case 7 : mes = Constantes.AGOSTO; break;
            case 8 : mes = Constantes.SEPTIEMBRE; break;
            case 9 : mes = Constantes.OCTUBRE; break;
            case 10 : mes = Constantes.NOVIEMBRE; break;
            case 11 : mes = Constantes.DICIEMBRE; break;
            case 12 : mes = Constantes.HISTORICO;
        }
        formatFecha = new SimpleDateFormat(Constantes.FORMATO_FECHA);
        calendar = Calendar.getInstance();
        setPeriodoTiempo(nroAnio);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        vista = inflater.inflate(R.layout.fragment_gasto_mes, container, false);
        recyclerCategorias = vista.findViewById(R.id.recycler_informe_categorias);
        inicializarLisViewCategorias();
        inicializarViewModels();
        return vista;
    }

    private void inicializarLisViewCategorias(){
        categoriasMes = new ArrayList<>();
        mapCategoriasGasto = new HashMap<>();
        adapterInforme = new AdapterInformeMes(categoriasMes, mapCategoriasGasto);
        recyclerCategorias.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerCategorias.setAdapter(adapterInforme);
    }

    private void inicializarViewModels(){
        viewModelTransaccion = ViewModelProviders.of(getActivity()).get(ViewModelTransaccion.class);
        viewModelCategoria = ViewModelProviders.of(getActivity()).get(ViewModelCategoria.class);
        recopilarDatos();
    }

    protected void recopilarDatos(){
        LiveData<List<Transaccion>> transaccionesDelMes = viewModelTransaccion.getTransaccionesDesdeHasta(primerDia,ultimoDia);
        if(transaccionesDelMes!=null) {
            transaccionesDelMes.observe(getActivity(), new Observer<List<Transaccion>>(){
                @Override
                public void onChanged(List<Transaccion> transaccions) {
                    categoriasMes.clear();
                    mapCategoriasGasto.clear();
                    for (Transaccion t : transaccions) {
                        Categoria subcategoria = viewModelCategoria.getCategoriaPorNombre(t.getCategoria());
                        Float gastoCategoria = mapCategoriasGasto.get(subcategoria.getNombreCategoria());
                        if (gastoCategoria == null) {
                            categoriasMes.add(subcategoria);
                            mapCategoriasGasto.put(subcategoria.getNombreCategoria(), Math.abs(t.getPrecio()));
                        } else {
                            mapCategoriasGasto.remove(subcategoria.getNombreCategoria());
                            mapCategoriasGasto.put(subcategoria.getNombreCategoria(), gastoCategoria + Math.abs(t.getPrecio()));
                        }
                    }
                    adapterInforme.notifyDataSetChanged();
                }
            });
        }
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }

    protected void setPeriodoTiempo(int nroAnio){
        anio = nroAnio;
        calendar.set(anio,mesNumero,1);
        primerDia = formatFecha.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        ultimoDia = formatFecha.format(calendar.getTime());
    }

    public void setDatos(int anio){
        setPeriodoTiempo(anio);
        categoriasMes.clear();
        mapCategoriasGasto.clear();
        recopilarDatos();
    }

    public String getMes(){
        return mes;
    }
}
