package com.blackeyedghoul.firefighters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    Context context;
    List<Contacts> contactsList;
    RecyclerView recyclerView;
    //final View.OnClickListener onClickListener = new MyOnClickListener();

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowName;
        TextView rowPosition;
        TextView rowStation;
        TextView rowPhoneNumber;
        TextView rowEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowName = itemView.findViewById(R.id.cl_name);
            rowPosition = itemView.findViewById(R.id.cl_position);
            rowStation = itemView.findViewById(R.id.cl_station);
            rowPhoneNumber = itemView.findViewById(R.id.cl_phone_number);
            rowEmail = itemView.findViewById(R.id.cl_email);
        }
    }

    public ContactsAdapter(Context context, List<Contacts> contactsList, RecyclerView recyclerView) {
        this.context = context;
        this.contactsList = contactsList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_row, parent, false);
        //view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder viewHolder, int i) {
        Contacts contact = contactsList.get(i);
        viewHolder.rowName.setText(contact.getName());
        viewHolder.rowPosition.setText(contact.getPosition());
        viewHolder.rowStation.setText(contact.getStation());
        viewHolder.rowPhoneNumber.setText(contact.getPhone_number());
        viewHolder.rowEmail.setText(contact.getEmail());
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void filterList(List<Contacts> filteredList) {
        contactsList = filteredList;
        notifyDataSetChanged();
    }

    /*
    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            String item = contactsList.get(itemPosition).getName();
            Toast.makeText(context, item, Toast.LENGTH_SHORT).show();
        }
    }
    */
}
