package com.blackeyedghoul.firefighters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Careers extends AppCompatActivity {

    SearchView searchView;
    ImageView back;
    RecyclerView recyclerView;
    List<Job> jobList;
    JobAdapter adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_careers);
        getWindow().setStatusBarColor(ContextCompat.getColor(Careers.this, R.color.dark_red));

        init();

        jobList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("careers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Job job = ds.getValue(Job.class);
                    jobList.add(job);
                }
                adapter = new JobAdapter(Careers.this, jobList, recyclerView);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<Job> filteredList = new ArrayList<>();
        for(Job job : jobList) {
            if(job.getPosition().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(job);
            }
        }
        adapter.filterList(filteredList);
    }

    public void init() {
        searchView = findViewById(R.id.c_search_view);
        recyclerView = findViewById(R.id.c_recycler_view);
        back = findViewById(R.id.c_back);
    }
}