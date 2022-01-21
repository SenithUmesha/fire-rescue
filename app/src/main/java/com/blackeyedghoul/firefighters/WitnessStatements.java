package com.blackeyedghoul.firefighters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class WitnessStatements extends AppCompatActivity {

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witness_statements);
        getWindow().setStatusBarColor(ContextCompat.getColor(WitnessStatements.this, R.color.dark_red));

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void init() {
        back = findViewById(R.id.ws_back);
    }
}