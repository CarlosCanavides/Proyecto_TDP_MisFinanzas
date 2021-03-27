package com.example.proyecto_tdp.verificador_estrategia;

import android.content.Intent;

public interface EstrategiaDeVerificacion {
    public void verificar(int codigoPedido, int estadoDelResultado, Intent datos);
}
