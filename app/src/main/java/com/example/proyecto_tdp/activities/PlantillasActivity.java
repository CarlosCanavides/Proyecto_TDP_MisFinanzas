package com.example.proyecto_tdp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.agregar_datos.NuevaTransaccionActivity;
import com.example.proyecto_tdp.adapters.AdapterPlantillas;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.view_models.ViewModelPlantilla;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlantillasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton btnAgregarPlantilla;
    private AdapterPlantillas adapterPlantillas;
    private List<Plantilla> plantillas;
    private Map<Plantilla, Integer> mapColorCategoria;
    private ViewModelCategoria viewModelCategoria;
    private ViewModelPlantilla viewModelPlantilla;
    private Observer<List<Plantilla>> observer;
    private static final int PEDIDO_NUEVA_PLANTILLA = 18;
    private NumberFormat nf = NumberFormat.getInstance(new Locale("es", "ES"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantillas);
        setTitle("Plantillas");
        recyclerView = findViewById(R.id.recycler_plantillas);
        btnAgregarPlantilla = findViewById(R.id.btn_agregar_plantilla);
        inicializarListView();
        inicializarViewModels();
        inicializarBtnAgregarPlantilla();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModelPlantilla.removeOberver(observer);
    }

    private void inicializarListView(){
        plantillas = new ArrayList<>();
        mapColorCategoria = new HashMap<>();
        adapterPlantillas = new AdapterPlantillas(plantillas,mapColorCategoria);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(adapterPlantillas);
    }

    private void inicializarViewModels(){
        viewModelPlantilla = ViewModelProviders.of(this).get(ViewModelPlantilla.class);
        viewModelCategoria = ViewModelProviders.of(this).get(ViewModelCategoria.class);
        recopilarDatos();
    }

    private void recopilarDatos() {
        LiveData<List<Plantilla>> t = viewModelPlantilla.getAllPlantillas();
        if (t != null) {
            observer = new Observer<List<Plantilla>>() {
                @Override
                public void onChanged(List<Plantilla> transaccions) {
                    plantillas.clear();
                    mapColorCategoria.clear();
                    adapterPlantillas.refresh();
                    adapterPlantillas.notifyDataSetChanged();
                    plantillas.addAll(transaccions);
                    for(Plantilla t : transaccions){
                        Categoria subcategoria = viewModelCategoria.getCategoriaPorNombre(t.getCategoria());
                        mapColorCategoria.put(t,subcategoria.getColorCategoria());
                    }
                    adapterPlantillas.notifyDataSetChanged();
                }
            };
            t.observe(this, observer);
        }
    }

    private void inicializarBtnAgregarPlantilla(){
        btnAgregarPlantilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlantillasActivity.this, NuevaTransaccionActivity.class);
                startActivityForResult(intent,PEDIDO_NUEVA_PLANTILLA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PEDIDO_NUEVA_PLANTILLA){
            if(resultCode==RESULT_OK){
                String precio = data.getStringExtra("precio");
                String categoria = data.getStringExtra("categoria");
                String tipoTransaccion = data.getStringExtra("tipoT");
                String titulo = data.getStringExtra("titulo");
                String etiqueta = data.getStringExtra("etiqueta");
                String info = data.getStringExtra("info");

                float monto = 0;
                try {
                    monto = nf.parse(precio).floatValue();
                }catch (Exception e) {
                    e.printStackTrace();
                }
                if(titulo.equals("")){
                    titulo = "Sin título";
                }
                if(categoria.equals("Seleccionar categoria")){
                    categoria = "";
                }
                if(tipoTransaccion.equals(Constantes.GASTO)){
                    monto = monto*(-1);
                }
                Plantilla nueva = new Plantilla(titulo, etiqueta, monto, categoria, tipoTransaccion, info);
                viewModelPlantilla.insertarPlantilla(nueva);
            }
        }
    }
}