package com.example.proyecto_tdp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import com.example.proyecto_tdp.activities.CategoriaActivity;
import com.example.proyecto_tdp.activities.InformesActivity;
import com.example.proyecto_tdp.activities.PlantillasActivity;
import com.example.proyecto_tdp.activities.TransaccionesFijasActivity;
import com.example.proyecto_tdp.activities.agregar_datos.NuevaTransaccionActivity;
import com.example.proyecto_tdp.fragments.HomeFragment;
import com.example.proyecto_tdp.fragments.ResumenFragment;
import com.example.proyecto_tdp.fragments.TransaccionesFragment;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaDeVerificacion;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaIngresoDeDatos;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaTFPendientes;
import com.example.proyecto_tdp.view_models.ViewModelPlantilla;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccionFija;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    private Fragment fragmentHome;
    private Fragment fragmentResumen;
    private Fragment fragmentTransacciones;
    private ViewModelPlantilla viewModelPlantilla;
    private ViewModelTransaccion viewModelTransaccion;
    private ViewModelTransaccionFija viewModelTransaccionFija;
    private EstrategiaDeVerificacion estrategiaDeVerificacion;
    private EstrategiaTFPendientes estrategiaTFPendientes;
    private Toolbar toolbar;
    private NavigationView navigationDrawer;
    private FloatingActionButton btnAgregar;
    private BottomNavigationView barraNavegacion;
    private DrawerLayout drawerLayout;
    private SwitchMaterial switchNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        btnAgregar = findViewById(R.id.floatingActionButton);
        barraNavegacion = findViewById(R.id.curvedBottomBar);
        navigationDrawer = findViewById(R.id.navigation_drawer);
        switchNightMode = navigationDrawer.getMenu().getItem(8).getActionView().findViewById(R.id.switch_mode);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        fragmentHome = new HomeFragment();
        fragmentResumen = new ResumenFragment();
        fragmentTransacciones = new TransaccionesFragment();
        replaceFragment(fragmentHome);
        inicializarModo();
        inicializarViewModels();
        inicializarBotonPrincipal();
        inicializarComponentesDeNavegacion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        barraNavegacion.setSelectedItemId(R.id.nav_escritorio);
        navigationDrawer.setCheckedItem(R.id.drawer_principal);
    }

    private void inicializarModo(){
        SharedPreferences ajustesDePreferencia = getSharedPreferences("AppAjustesDePreferencia",MODE_PRIVATE);
        final SharedPreferences.Editor ajustesDePreferenciaEditor = ajustesDePreferencia.edit();
        final boolean modoNocturnoEncendido = ajustesDePreferencia.getBoolean("ModoNocturno",false);
        if(modoNocturnoEncendido){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchNightMode.setChecked(true);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switchNightMode.setChecked(false);
        }
        switchNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(modoNocturnoEncendido){
                    switchNightMode.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    ajustesDePreferenciaEditor.putBoolean("ModoNocturno",false);
                }
                else {
                    switchNightMode.setChecked(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    ajustesDePreferenciaEditor.putBoolean("ModoNocturno",true);
                }
                ajustesDePreferenciaEditor.apply();
            }
        });
    }

    private void inicializarViewModels(){
        viewModelPlantilla = ViewModelProviders.of(this).get(ViewModelPlantilla.class);
        viewModelTransaccion = ViewModelProviders.of(this).get(ViewModelTransaccion.class);
        viewModelTransaccionFija = ViewModelProviders.of(this).get(ViewModelTransaccionFija.class);
        estrategiaDeVerificacion = new EstrategiaIngresoDeDatos(viewModelPlantilla,viewModelTransaccion,viewModelTransaccionFija);
        estrategiaTFPendientes = new EstrategiaTFPendientes(viewModelTransaccion,viewModelTransaccionFija);
        estrategiaTFPendientes.insertarTFPendientes();
    }

    private void inicializarBotonPrincipal(){
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NuevaTransaccionActivity.class);
                startActivityForResult(intent, Constantes.PEDIDO_NUEVA_TRANSACCION);
            }
        });
    }

    private void inicializarComponentesDeNavegacion(){
        barraNavegacion.setBackground(null);
        barraNavegacion.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                int titulo = R.string.app_name;

                switch (menuItem.getItemId()) {
                    case R.id.nav_escritorio:
                        navigationDrawer.setCheckedItem(R.id.drawer_principal);
                        selectedFragment = fragmentHome;
                        titulo = R.string.app_name;
                        break;
                    case R.id.nav_transacciones:
                        navigationDrawer.setCheckedItem(R.id.drawer_transacciones);
                        selectedFragment = fragmentTransacciones;
                        titulo = R.string.item_transacciones;
                        break;
                    case R.id.nav_resumen:
                        navigationDrawer.setCheckedItem(R.id.drawer_resumen);
                        selectedFragment = fragmentResumen;
                        titulo = R.string.item_resumen;
                        break;
                    case R.id.nav_informes:
                        navigationDrawer.setCheckedItem(R.id.drawer_informes);
                        Intent intent1 = new Intent(MainActivity.this, InformesActivity.class);
                        startActivity(intent1);
                        break;
                }
                setTitle(titulo);
                replaceFragment(selectedFragment);
                return  true;
            }
        });

        navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                int titulo = R.string.app_name;

                switch (menuItem.getItemId()) {
                    case R.id.drawer_categorias:
                        Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_principal:
                        barraNavegacion.setSelectedItemId(R.id.nav_escritorio);
                        selectedFragment = fragmentHome;
                        titulo = R.string.app_name;
                        break;
                    case R.id.drawer_transacciones:
                        barraNavegacion.setSelectedItemId(R.id.nav_transacciones);
                        selectedFragment = fragmentTransacciones;
                        titulo = R.string.item_transacciones;
                        break;
                    case R.id.drawer_resumen:
                        barraNavegacion.setSelectedItemId(R.id.nav_resumen);
                        selectedFragment = fragmentResumen;
                        titulo = R.string.item_resumen;
                        break;
                    case R.id.drawer_informes:
                        barraNavegacion.setSelectedItemId(R.id.nav_informes);
                        Intent intent1 = new Intent(MainActivity.this, InformesActivity.class);
                        startActivity(intent1);
                        titulo = R.string.item_informes;
                        break;
                    case R.id.drawer_plantillas:
                        Intent intent2 = new Intent(MainActivity.this, PlantillasActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.drawer_gastos_fijos:
                        Intent intent3 = new Intent(MainActivity.this, TransaccionesFijasActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.drawer_ingresos_fijos:
                        Intent intent4 = new Intent(MainActivity.this, TransaccionesFijasActivity.class);
                        startActivity(intent4);
                        break;
                }
                setTitle(titulo);
                replaceFragment(selectedFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                return  true;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constantes.PEDIDO_NUEVA_TRANSACCION) {
            estrategiaDeVerificacion.verificar(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}
