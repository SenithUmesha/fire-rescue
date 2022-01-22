package com.blackeyedghoul.firefighters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CareerDetails extends AppCompatActivity {

    TextView position, date, location, age, description, salary, work_time, education;
    Button apply;
    ImageView back;
    String p, d, l, a, de, s, w, ed, ap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_details);
        getWindow().setStatusBarColor(ContextCompat.getColor(CareerDetails.this, R.color.white));

        init();

        Intent intent = getIntent();
        p = intent.getStringExtra("position");
        d = intent.getStringExtra("date");
        l = intent.getStringExtra("location");
        a = intent.getStringExtra("age");
        de = intent.getStringExtra("description");
        s = intent.getStringExtra("salary");
        w = intent.getStringExtra("work_time");
        ed = intent.getStringExtra("education");
        ap = intent.getStringExtra("apply");

        position.setText(p);
        date.setText(d);
        location.setText(l);
        age.setText(a);
        description.setText(de);
        salary.setText(s + " annual");
        education.setText(ed);

        try {
            work_time.setText(w);
        } catch (Exception e) {
            Log.d("CareerDetails: ", ""+e);
        }

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+ap));
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void init() {
        position = findViewById(R.id.cd_name);
        date = findViewById(R.id.cd_posted_date);
        location = findViewById(R.id.cd_location);
        age = findViewById(R.id.cd_age);
        apply = findViewById(R.id.cd_apply);
        description = findViewById(R.id.cd_description);
        salary = findViewById(R.id.cd_salary);
        work_time = findViewById(R.id.c_card_work_time);
        education = findViewById(R.id.cd_education);
        back = findViewById(R.id.cd_back);
    }
}