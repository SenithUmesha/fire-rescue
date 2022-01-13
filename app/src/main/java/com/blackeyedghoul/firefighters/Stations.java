package com.blackeyedghoul.firefighters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Stations extends AppCompatActivity {

    Button list, map;
    View list_view, map_view;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        getWindow().setStatusBarColor(ContextCompat.getColor(Stations.this, R.color.dark_red));

        init();

        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.s_frameLayout, new StationListFragment()).commit();
        }

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setTextColor(getResources().getColor(R.color.light_pink));
                list.setTextColor(getResources().getColor(R.color.white));
                map_view.setBackgroundResource(R.color.dark_red);
                list_view.setBackgroundResource(R.color.white);

                replaceFragment(new StationListFragment());
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setTextColor(getResources().getColor(R.color.light_pink));
                map.setTextColor(getResources().getColor(R.color.white));
                list_view.setBackgroundResource(R.color.dark_red);
                map_view.setBackgroundResource(R.color.white);

                replaceFragment(new StationMapFragment());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.s_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void init() {
        list = findViewById(R.id.s_list);
        map = findViewById(R.id.s_map);
        list_view = findViewById(R.id.s_list_view);
        map_view = findViewById(R.id.s_map_view);
        back = findViewById(R.id.s_back);
    }
}