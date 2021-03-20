package com.example.proyecto_tdp.fragments;

public class GastoHistoricoFragment extends GastoMesFregment{

    public GastoHistoricoFragment(int nroMes, int nroAnio) {
        super(nroMes, nroAnio);
    }

    @Override
    protected void setPeriodoTiempo(int nroAnio) {
        anio = nroAnio;
        localDate = localDate.withYear(anio);
        localDate = localDate.withDayOfMonth(1);
        localDate = localDate.withMonthOfYear(localDate.monthOfYear().getMinimumValue());
        primerDia = formatFecha.print(localDate.toDate().getTime());
        localDate = localDate.withMonthOfYear(localDate.monthOfYear().getMaximumValue());
        localDate = localDate.withDayOfMonth(localDate.dayOfMonth().getMaximumValue());
        ultimoDia = formatFecha.print(localDate.toDate().getTime());
    }
}
