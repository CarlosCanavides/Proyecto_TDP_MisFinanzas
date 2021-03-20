package com.example.proyecto_tdp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    protected DateTimeFormatter formatFecha;
    protected LocalDate localDate;
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
        formatFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);
        localDate = LocalDate.now();
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
        LiveData<List<Transaccion>> liveData = viewModelTransaccion.getLiveTransaccionesDesdeHasta(primerDia,ultimoDia);
        if(liveData!=null) {
            liveData.observe(getActivity(), new Observer<List<Transaccion>>() {
                @Override
                public void onChanged(List<Transaccion> transaccionesDelMes) {
                    categoriasMes.clear();
                    mapCategoriasGasto.clear();
                    adapterInforme.refesh();
                    adapterInforme.notifyDataSetChanged();
                    for (Transaccion t : transaccionesDelMes) {
                        Categoria categoria = viewModelCategoria.getCategoriaPorNombre(t.getCategoria());
                        Float gastoCategoria = mapCategoriasGasto.get(categoria.getNombreCategoria());
                        if (gastoCategoria == null) {
                            categoriasMes.add(categoria);
                            mapCategoriasGasto.put(categoria.getNombreCategoria(), Math.abs(t.getPrecio()));
                        } else {
                            mapCategoriasGasto.remove(categoria.getNombreCategoria());
                            mapCategoriasGasto.put(categoria.getNombreCategoria(), gastoCategoria + Math.abs(t.getPrecio()));
                        }
                    }
                    adapterInforme.notifyDataSetChanged();
                }
            });
        }
    }

    protected void setPeriodoTiempo(int nroAnio){
        anio = nroAnio;
        localDate = localDate.withDayOfMonth(1);
        localDate = localDate.withMonthOfYear(mesNumero+1);
        localDate = localDate.withYear(nroAnio);
        primerDia = formatFecha.print(localDate.toDate().getTime());
        localDate = localDate.withDayOfMonth(localDate.dayOfMonth().getMaximumValue());
        ultimoDia = formatFecha.print(localDate.toDate().getTime());
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
