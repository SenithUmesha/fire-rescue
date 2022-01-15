package com.blackeyedghoul.firefighters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FAQs extends AppCompatActivity {

    RecyclerView recyclerView;
    List<FAQ> faqList;
    ImageView back;
    List<Tip> tipsList = new ArrayList<>();
    DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        getWindow().setStatusBarColor(ContextCompat.getColor(FAQs.this, R.color.dark_red));

        init();

        PreCreateDB.copyDB(this);
        databaseAdapter = new DatabaseAdapter(this);
        tipsList = databaseAdapter.getAllTips();

        setData();
        setRecycleView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setRecycleView() {
        FAQAdapter faqAdapter = new FAQAdapter(faqList);
        recyclerView.setAdapter(faqAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void setData() {
        faqList = new ArrayList<>();

        for (int i = 0; i < tipsList.size(); i++) {
            faqList.add(new FAQ(tipsList.get(i).getTopic(), tipsList.get(i).getSub_topic()));
        }
    }

    private void init() {
        recyclerView = findViewById(R.id.faq_recyclerView);
        back = findViewById(R.id.faq_back);
    }
}