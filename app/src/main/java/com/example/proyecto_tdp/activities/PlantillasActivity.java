package com.example.proyecto_tdp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import com.example.proyecto_tdp.Constantes;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.activities.agregar_datos.NuevaPlantillaActivity;
import com.example.proyecto_tdp.activities.modificar_datos.SetPlantillaActivity;
import com.example.proyecto_tdp.adapters.AdapterPlantillas;
import com.example.proyecto_tdp.base_de_datos.entidades.Categoria;
import com.example.proyecto_tdp.base_de_datos.entidades.Plantilla;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaDeVerificacion;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaSoloPlantillas;
import com.example.proyecto_tdp.view_models.ViewModelCategoria;
import com.example.proyecto_tdp.view_models.ViewModelPlantilla;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantillasActivity extends AppCompatActivity implements AdapterPlantillas.OnPlantillaListener {

    protected List<Plantilla> plantillas;
    protected Map<Plantilla, Categoria> mapCategoriasDePlantillas;
    protected AdapterPlantillas adapterPlantillas;
    protected RecyclerView recyclerView;
    protected FloatingActionButton btnAgregarPlantilla;
    protected ViewModelCategoria viewModelCategoria;
    protected ViewModelPlantilla viewModelPlantilla;
    protected Observer<List<Plantilla>> observer;
    protected EstrategiaDeVerificacion estrategiaDeVerificacion;

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
        intent.putExtra(Constantes.CAMPO_ID_CATEGORIA, plantilla.getCategoria());
        intent.putExtra(Constantes.CAMPO_NOMBRE_CATEGORIA, mapCategoriasDePlantillas.get(plantilla).getNombreCategoria());
        startActivityForResult(intent, Constantes.PEDIDO_SET_PLANTILLA);
    }

    private void inicializarListView(){
        plantillas = new ArrayList<>();
        mapCategoriasDePlantillas = new HashMap<>();
        adapterPlantillas = new AdapterPlantillas(plantillas,mapCategoriasDePlantillas,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(adapterPlantillas);
    }

    private void inicializarViewModels(){
        viewModelPlantilla = new ViewModelProvider(this).get(ViewModelPlantilla.class);
        viewModelCategoria = new ViewModelProvider(this).get(ViewModelCategoria.class);
        estrategiaDeVerificacion = new EstrategiaSoloPlantillas(viewModelPlantilla);
        recopilarDatos();
    }

    private void recopilarDatos() {
        LiveData<List<Plantilla>> liveDataPlantillas = viewModelPlantilla.getAllPlantillas();
        if (liveDataPlantillas != null) {
            observer = new Observer<List<Plantilla>>() {
                @Override
                public void onChanged(List<Plantilla> plantillaList) {
                    plantillas.clear();
                    mapCategoriasDePlantillas.clear();
                    adapterPlantillas.refresh();
                    adapterPlantillas.notifyDataSetChanged();
                    plantillas.addAll(plantillaList);
                    for(Plantilla t : plantillaList){
                        Categoria subcategoria = null;
                        String idCategoria = t.getCategoria();
                        if(idCategoria!=null){
                            subcategoria =  viewModelCategoria.getCategoriaPorID(t.getCategoria());
                        }
                        if(subcategoria==null){
                            subcategoria = new Categoria(Constantes.SIN_CATEGORIA,null, Color.parseColor("#FF5722"),Constantes.GASTO);
                        }
                        mapCategoriasDePlantillas.put(t,subcategoria);
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
                Intent intent = new Intent(PlantillasActivity.this, NuevaPlantillaActivity.class);
                startActivityForResult(intent, Constantes.PEDIDO_NUEVA_PLANTILLA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constantes.PEDIDO_NUEVA_PLANTILLA || requestCode==Constantes.PEDIDO_SET_PLANTILLA){
            estrategiaDeVerificacion.verificar(requestCode,resultCode,data);
        }
    }
}
