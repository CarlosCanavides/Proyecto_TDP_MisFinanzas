package com.example.proyecto_tdp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.androidkun.xtablayout.XTabLayout;
import com.example.proyecto_tdp.R;
import com.example.proyecto_tdp.adapters.AdapterViewpagerPaginasMes;
import org.joda.time.LocalDate;

public class InformesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private XTabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterViewpagerPaginasMes adapterViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Análisis de gastos");
        setContentView(R.layout.activity_informes);

        toolbar   = findViewById(R.id.informes_toolbar);
        tabLayout = findViewById(R.id.informes_tabLayout);
        viewPager = findViewById(R.id.informes_viewpager);

        adapterViewpager = new AdapterViewpagerPaginasMes(getSupportFragmentManager(),13, LocalDate.now().getYear());
        viewPager.setAdapter(adapterViewpager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout.setupWithViewPager(viewPager);
    }
}
