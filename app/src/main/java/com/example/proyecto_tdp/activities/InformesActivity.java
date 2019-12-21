package com.example.proyecto_tdp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.androidkun.xtablayout.XTabLayout;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterViewpagerPaginasMes;
import com.example.proyecto_tdp.base_de_datos.entidades.Transaccion;
import java.util.List;

public class InformesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private XTabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterViewpagerPaginasMes adapterViewpager;
    private List<Transaccion> transacciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Análisis de gastos");
        setContentView(R.layout.activity_informes);

        toolbar   = findViewById(R.id.informes_toolbar);
        tabLayout = findViewById(R.id.informes_tabLayout);
        viewPager = findViewById(R.id.informes_viewpager);

        adapterViewpager = new AdapterViewpagerPaginasMes(getSupportFragmentManager(),13, 2019);
        viewPager.setAdapter(adapterViewpager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
    }

    /*private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int count = 10;
        for (int i=0; i<count; i++){
            OneFragment fView = new OneFragment();
            View view = fView.getView();
            TextView txtTabItemNumber = (TextView)view.findViewById(R.id.txtTabItemNumber);
            txtTabItemNumber.setText("TAB " + i);
            adapter.addFrag(fView,"TAB " + i);
        }
        viewPager.setAdapter(adapter);
    }*/
}
