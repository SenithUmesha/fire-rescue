package com.blackeyedghoul.firefighters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Events extends AppCompatActivity {

    ArrayList<ParentEvent> parentEventList = new ArrayList<>();
    ImageView back;
    RecyclerView parentRecyclerView, childRecyclerView;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        getWindow().setStatusBarColor(ContextCompat.getColor(Events.this, R.color.dark_red));

        init();
        getData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getData() {
        DatabaseReference eventRef = rootRef
                .child("events");

        ValueEventListener eventListener = new ValueEventListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String event_date_month = ds
                            .child("event_date_month")
                            .getValue().toString();

                    String event_e_time = ds
                            .child("event_e_time")
                            .getValue().toString();

                    String event_location = ds
                            .child("event_location")
                            .getValue().toString();

                    String event_name = ds
                            .child("event_name")
                            .getValue().toString();

                    String event_s_time = ds
                            .child("event_s_time")
                            .getValue().toString();

                    try {
                        if (new SimpleDateFormat("dd MMM, yyyy").parse(event_date_month).before(new Date())) {
                            Log.d("Old Date Check", "True");
                        }
                        else{
                            ChildEvent childEvent = new ChildEvent(event_name, event_s_time, event_e_time, event_location);
                            parentEventList.add(new ParentEvent(event_date_month, childEvent));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                ParentEventAdapter parentEventAdapter = new ParentEventAdapter(parentEventList);
                parentRecyclerView.setAdapter(parentEventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        eventRef.addListenerForSingleValueEvent(eventListener);
    }

    private void init() {
        back = findViewById(R.id.ew_back);
        parentRecyclerView = findViewById(R.id.ew_parent_recycler_view);
        childRecyclerView = findViewById(R.id.ew_child_recycler_view);
    }
}