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
import com.example.proyecto_tdp.activities.modificar_datos.SetPlantillaActivity;
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

public class PlantillasActivity extends AppCompatActivity implements AdapterPlantillas.OnPlantillaListener {

    private RecyclerView recyclerView;
    private FloatingActionButton btnAgregarPlantilla;
    private AdapterPlantillas adapterPlantillas;
    private List<Plantilla> plantillas;
    private Map<Plantilla, Integer> mapColorCategoria;
    private ViewModelCategoria viewModelCategoria;
    private ViewModelPlantilla viewModelPlantilla;
    private Observer<List<Plantilla>> observer;
    private NumberFormat formatoNumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantillas);
        setTitle("Plantillas");
        recyclerView = findViewById(R.id.recycler_plantillas);
        btnAgregarPlantilla = findViewById(R.id.btn_agregar_plantilla);
        formatoNumero = NumberFormat.getInstance(new Locale("es", "ES"));
        inicializarListView();
        inicializarViewModels();
        inicializarBtnAgregarPlantilla();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModelPlantilla.removeOberver(observer);
    }

    @Override
    public void onPlantillaClick(int position) {
        Plantilla plantilla = plantillas.get(position);
        Intent intent = new Intent(this, SetPlantillaActivity.class);
        intent.putExtra(Constantes.CAMPO_ID, plantilla.getId());
        intent.putExtra(Constantes.CAMPO_INFO, plantilla.getInfo());
        intent.putExtra(Constantes.CAMPO_TIPO, plantilla.getTipoTransaccion());
        intent.putExtra(Constantes.CAMPO_TITULO, plantilla.getTitulo());
        intent.putExtra(Constantes.CAMPO_PRECIO, String.format( "%.2f", Math.abs(plantilla.getPrecio())));
        intent.putExtra(Constantes.CAMPO_ETIQUETA, plantilla.getEtiqueta());
        intent.putExtra(Constantes.CAMPO_CATEGORIA, plantilla.getCategoria());
        startActivityForResult(intent, Constantes.PEDIDO_SET_PLANTILLA);
    }

    private void inicializarListView(){
        plantillas = new ArrayList<>();
        mapColorCategoria = new HashMap<>();
        adapterPlantillas = new AdapterPlantillas(plantillas,mapColorCategoria,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(adapterPlantillas);
    }

    private void inicializarViewModels(){
        viewModelPlantilla = ViewModelProviders.of(this).get(ViewModelPlantilla.class);
        viewModelCategoria = ViewModelProviders.of(this).get(ViewModelCategoria.class);
        recopilarDatos();
    }

    private void recopilarDatos() {
        LiveData<List<Plantilla>> liveDataPlantillas = viewModelPlantilla.getAllPlantillas();
        if (liveDataPlantillas != null) {
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
            liveDataPlantillas.observe(this, observer);
        }
    }

    private void inicializarBtnAgregarPlantilla(){
        btnAgregarPlantilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlantillasActivity.this, NuevaTransaccionActivity.class);
                startActivityForResult(intent, Constantes.PEDIDO_NUEVA_PLANTILLA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constantes.PEDIDO_NUEVA_PLANTILLA){
            if(resultCode==RESULT_OK){

            }
        }
    }
}