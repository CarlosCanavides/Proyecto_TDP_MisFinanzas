package com.example.proyecto_tdp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.proyecto_tdp.activities.CategoriaActivity;
import com.example.proyecto_tdp.activities.InformesActivity;
import com.example.proyecto_tdp.activities.agregar_datos.NuevaTransaccionActivity;
import com.example.proyecto_tdp.fragments.HomeFragment;
import com.example.proyecto_tdp.fragments.ResumenFragment;
import com.example.proyecto_tdp.fragments.TransaccionesFragment;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaDeVerificacion;
import com.example.proyecto_tdp.verificador_estrategia.EstrategiaIngresoDeDatos;
import com.example.proyecto_tdp.view_models.ViewModelPlantilla;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import com.example.proyecto_tdp.view_models.ViewModelTransaccionFija;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private Fragment fragmentHome;
    private Fragment fragmentResumen;
    private Fragment fragmentTransacciones;
    private ViewModelPlantilla viewModelPlantilla;
    private ViewModelTransaccion viewModelTransaccion;
    private ViewModelTransaccionFija viewModelTransaccionFija;
    private EstrategiaDeVerificacion estrategiaDeVerificacion;
    private Toolbar toolbar;
    private NavigationView navigationDrawer;
    private FloatingActionButton btnAgregar;
    private BottomNavigationView barraNavegacion;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Wallet");
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        btnAgregar = findViewById(R.id.floatingActionButton);
        barraNavegacion = findViewById(R.id.curvedBottomBar);
        navigationDrawer = findViewById(R.id.navigation_drawer);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        fragmentHome = new HomeFragment();
        fragmentResumen = new ResumenFragment();
        fragmentTransacciones = new TransaccionesFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragmentHome).commit();
        inicializarViewModels();
        inicializarBotonPrincipal();
        inicializarComponentesDeNavegacion();
    }

    private void inicializarViewModels(){
        viewModelPlantilla = ViewModelProviders.of(this).get(ViewModelPlantilla.class);
        viewModelTransaccion = ViewModelProviders.of(this).get(ViewModelTransaccion.class);
        viewModelTransaccionFija = ViewModelProviders.of(this).get(ViewModelTransaccionFija.class);
        estrategiaDeVerificacion = new EstrategiaIngresoDeDatos(viewModelPlantilla,viewModelTransaccion,viewModelTransaccionFija);
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
                        selectedFragment = fragmentHome;
                        titulo = R.string.app_name;
                        break;
                    case R.id.nav_transacciones:
                        selectedFragment = fragmentTransacciones;
                        titulo = R.string.item_transacciones;
                        break;
                    case R.id.nav_resumen:
                        selectedFragment = fragmentResumen;
                        titulo = R.string.item_resumen;
                        break;
                    case R.id.nav_informes:
                        Intent intent1 = new Intent(MainActivity.this, InformesActivity.class);
                        startActivity(intent1);
                        titulo = R.string.item_informes;
                        break;
                }
                setTitle(titulo);
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
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
                        selectedFragment = fragmentHome;
                        titulo = R.string.app_name;
                        break;
                    case R.id.drawer_transacciones:
                        selectedFragment = fragmentTransacciones;
                        titulo = R.string.item_transacciones;
                        break;
                    case R.id.drawer_resumen:
                        selectedFragment = fragmentResumen;
                        titulo = R.string.item_resumen;
                        break;
                    case R.id.drawer_informes:
                        Intent intent1 = new Intent(MainActivity.this, InformesActivity.class);
                        startActivity(intent1);
                        titulo = R.string.item_informes;
                        break;
                }
                setTitle(titulo);
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_home:
                Toast.makeText(this, "Escritorio", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_transacciones:
                Toast.makeText(this, "Transacciones", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_resumen:
                Toast.makeText(this, "Resumen", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_informes:
                Toast.makeText(this, "Informes", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
