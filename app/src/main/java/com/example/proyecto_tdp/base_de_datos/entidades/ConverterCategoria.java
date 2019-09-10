package com.example.proyecto_tdp.base_de_datos.entidades;

import androidx.room.TypeConverter;
import java.util.ArrayList;

public class ConverterCategoria {

    @TypeConverter
    public static String toStringSubcategorias(ArrayList<String> idsCategorias) {
        String resultado = "";
        for(int i=0; i<idsCategorias.size(); i++){
            resultado.concat(idsCategorias.get(i)+"/");
        }
        return resultado;
    }

    @TypeConverter
    public static ArrayList<String> toListSubcategorias(String idsSubcategorias) {
        boolean termine = false;
        ArrayList<String> list = new ArrayList<>();
        int i = 0;
        while (!termine) {
            String id = "";
            char ch = idsSubcategorias.charAt(i);
            if(ch!='/'){
                for (int j=i; j<idsSubcategorias.length() && ch!='/'; j++){
                    id.concat(ch+"");
                    if(j==idsSubcategorias.length()){
                        termine = true;
                    }
                    i=j+1;
                }
                i++;
                list.add(id);
            }
        }
        return list;
    }

}
