package com.example.proyecto_tdp.adapters.view_types;

import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import java.util.Date;

public class HeaderOrRow {

    private Transaccion row;
    private Date header;
    private boolean isRow;

    private HeaderOrRow(){}

    public static HeaderOrRow createRow(Transaccion row) {
        HeaderOrRow ret = new HeaderOrRow();
        ret.row = row;
        ret.isRow = true;
        return ret;
    }

    public static HeaderOrRow createHeader(Date header) {
        HeaderOrRow ret = new HeaderOrRow();
        ret.header = header;
        ret.isRow = false;
        return ret;
    }

    public Transaccion getRow() {
        return row;
    }

    public Date getHeader() {
        return header;
    }

    public boolean isRow() {
        return isRow;
    }
}
