package com.example.proyecto_tdp.base_de_datos;

import androidx.room.TypeConverter;
import com.example.proyecto_tdp.Constantes;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter{

    private static final DateFormat formatFecha = new SimpleDateFormat(Constantes.FORMATO_FECHA);

    @TypeConverter
    public static Date obtenetFormatoDate(String fechaString){
        Date fechaDate = null;
        if(fechaString != null){
            try{
                fechaDate = formatFecha.parse(fechaString);
            }catch (ParseException e){
                e.printStackTrace();
            }
        }
        return fechaDate;
    }

    @TypeConverter
    public static String obtenerFormatoString(Date date){
        return date == null ? null : formatFecha.format(date);
    }
}