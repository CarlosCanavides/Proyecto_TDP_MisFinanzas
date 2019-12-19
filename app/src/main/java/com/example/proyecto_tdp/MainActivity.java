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
import com.akshay.library.CurveBottomBar;
import com.example.proyecto_tdp.activities.CategoriaActivity;
import com.example.proyecto_tdp.activities.InformesActivity;
import com.example.proyecto_tdp.activities.NuevaTransaccionActivity;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import com.example.proyecto_tdp.fragments.HomeFragment;
import com.example.proyecto_tdp.fragments.InformesFragment;
import com.example.proyecto_tdp.fragments.ResumenFragment;
import com.example.proyecto_tdp.fragments.TransaccionesFragment;
import com.example.proyecto_tdp.view_models.ViewModelTransaccion;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Fragment fragmentHome;
    private Fragment fragmentResumen;
    private Fragment fragmentInformes;
    private Fragment fragmentTransacciones;
    private ViewModelTransaccion viewModelTransaccion;
    private FloatingActionButton btnAgregar;
    private DrawerLayout drawerLayout;
    private static final int NRO_PEDIDO = 1826;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModelTransaccion = ViewModelProviders.of(this).get(ViewModelTransaccion.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationDrawer = findViewById(R.id.navigation_drawer);
        navigationDrawer.setNavigationItemSelectedListener(navListenerDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        fragmentHome = new HomeFragment();
        fragmentTransacciones = new TransaccionesFragment();
        fragmentResumen = new ResumenFragment();
        fragmentInformes = new InformesFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragmentHome).commit();

        btnAgregar = findViewById(R.id.floatingActionButton);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NuevaTransaccionActivity.class);
                startActivityForResult(intent, NRO_PEDIDO);
            }
        });

        CurveBottomBar cbb = findViewById(R.id.curvedBottomBar);
        cbb.inflateMenu(R.menu.menu);
        cbb.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                    selectedFragment = fragmentInformes;
                    titulo = R.string.item_informes;
                    break;
            }
            setTitle(titulo);
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return  true;
        }
    };

    private NavigationView.OnNavigationItemSelectedListener navListenerDrawer = new NavigationView.OnNavigationItemSelectedListener() {
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
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NRO_PEDIDO) {
            if (resultCode == RESULT_OK) {
                String precio = data.getStringExtra("precio");
                String categoria = data.getStringExtra("categoria");
                String tipoTransaccion = data.getStringExtra("tipoT");
                String titulo = data.getStringExtra("titulo");
                String etiqueta = data.getStringExtra("etiqueta");
                String fecha = data.getStringExtra("fecha");
                String info = data.getStringExtra("info");

                float monto = 0;
                try {
                    Locale spanish = new Locale("es", "ES");
                    NumberFormat nf = NumberFormat.getInstance(spanish);
                    monto = nf.parse(precio).floatValue();
                }catch (Exception e) {
                    mostrarMensaje("El monto ingresado debe ser mayor a 0");
                }

                if(tipoTransaccion.equals("Gasto")){
                    monto = monto*(-1);
                }
                if(titulo.equals("")){
                    titulo = "Sin título";
                }
                if(categoria.equals("Seleccionar categoria")){
                    categoria = "";
                }
                Transaccion nuevaTransaccion = new Transaccion(titulo, etiqueta, monto, categoria, tipoTransaccion, new Date(), info);
                viewModelTransaccion.insertarTransaccion(nuevaTransaccion);
            }
        }
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
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
