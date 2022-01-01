package com.blackeyedghoul.firefighters;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.dark_red));

        Init();

        bottomNavigationView.setBackground(null);

        MenuItem item1 = bottomNavigationView.getMenu().findItem(R.id.alerts);
        item1.setChecked(false);
        MenuItem item2 = bottomNavigationView.getMenu().findItem(R.id.news);
        item2.setChecked(false);
        MenuItem item3 = bottomNavigationView.getMenu().findItem(R.id.events);
        item3.setChecked(false);
        MenuItem item4 = bottomNavigationView.getMenu().findItem(R.id.social_media);
        item4.setChecked(false);
        MenuItem item5 = bottomNavigationView.getMenu().findItem(R.id.holder);
        item5.setChecked(false);
    }

    private void Init() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
    }
}