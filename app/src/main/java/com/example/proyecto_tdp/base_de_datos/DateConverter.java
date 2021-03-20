package com.example.proyecto_tdp.base_de_datos;

import androidx.room.TypeConverter;
import com.example.proyecto_tdp.Constantes;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConverter{

    private static final DateTimeFormatter formatFecha = DateTimeFormat.forPattern(Constantes.FORMATO_FECHA);

    @TypeConverter
    public static Date obtenerFormatoDate(String fechaString){
        DateTime fechaDate = null;
        if(fechaString != null) {
            fechaDate = formatFecha.parseDateTime(fechaString);
        }
        return fechaDate.toDate();
    }

    @TypeConverter
    public static String obtenerFormatoString(Date date){
        return date == null ? null : formatFecha.print(date.getTime());
    }
}