package com.example.proyecto_tdp.fragments;

import java.util.Calendar;

public class GastoHistoricoFragment extends GastoMesFregment{

    public GastoHistoricoFragment(int nroMes, int nroAnio) {
        super(nroMes, nroAnio);
    }

    @Override
    protected void setPeriodoTiempo(int nroAnio) {
        anio = nroAnio;
        calendar.set(anio, 0,1);
        primerDia = formatFecha.format(calendar.getTime());
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        ultimoDia = formatFecha.format(calendar.getTime());
    }
}
