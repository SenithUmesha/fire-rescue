package com.blackeyedghoul.firefighters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

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

        init();

        PreCreateDB.copyDB(this);
        databaseAdapter = new DatabaseAdapter(this);
        contactsList = databaseAdapter.getAllContacts();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        contactsAdapter = new ContactsAdapter(this, contactsList, recyclerView);
        recyclerView.setAdapter(contactsAdapter);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    String phone_number1 = contactsList.get(position).getPhone_number();
                    Uri sms_uri = Uri.parse("smsto:"+phone_number1);
                    Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                    startActivity(sms_intent);
                    contactsAdapter.notifyItemChanged(position);
                    break;

                case ItemTouchHelper.RIGHT:
                    String phone_number2 = contactsList.get(position).getPhone_number();
                    Intent call_intent = new Intent(Intent.ACTION_DIAL);
                    call_intent.setData(Uri.parse("tel:"+phone_number2));
                    startActivity(call_intent);
                    contactsAdapter.notifyItemChanged(position);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ContactList.this, R.color.dark_red))
                    .addSwipeLeftActionIcon(R.drawable.sms)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ContactList.this, R.color.dark_green))
                    .addSwipeRightActionIcon(R.drawable.call)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void filter(String newText) {
        List<Contacts> filteredList = new ArrayList<>();
        for(Contacts contact : contactsList) {
            if(contact.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(contact);
            }
        }
        contactsAdapter.filterList(filteredList);
    }

    private void init() {
        searchView = findViewById(R.id.cl_search_view);
        title = findViewById(R.id.cl_title);
        back = findViewById(R.id.cl_back);
        recyclerView = findViewById(R.id.cl_recycler_view);
    }
}