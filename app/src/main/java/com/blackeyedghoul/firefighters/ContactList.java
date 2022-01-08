package com.blackeyedghoul.firefighters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactList extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    TextView title;
    ImageView back;
    DatabaseAdapter databaseAdapter;
    ContactsAdapter contactsAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Contacts> contactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        getWindow().setStatusBarColor(ContextCompat.getColor(ContactList.this, R.color.dark_red));

        Init();

        PreCreateDB.copyDB(this);
        databaseAdapter = new DatabaseAdapter(this);
        contactsList = databaseAdapter.getAllContacts();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        contactsAdapter = new ContactsAdapter(this, contactsList, recyclerView);
        recyclerView.setAdapter(contactsAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void Init() {
        searchView = findViewById(R.id.cl_search_view);
        title = findViewById(R.id.cl_title);
        back = findViewById(R.id.cl_back);
        recyclerView = findViewById(R.id.cl_recycler_view);
    }
}