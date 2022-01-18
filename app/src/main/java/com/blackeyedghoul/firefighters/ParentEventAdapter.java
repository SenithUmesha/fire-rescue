package com.blackeyedghoul.firefighters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParentEventAdapter extends RecyclerView.Adapter<ParentEventAdapter.ViewHolder> {

    List<ParentEvent> parentEventList;

    public ParentEventAdapter(List<ParentEvent> parentEventList) {
        this.parentEventList = parentEventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.day_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParentEvent parentEvent = parentEventList.get(position);
        String event_date_month = parentEvent.getEvent_date_month();
        ChildEvent events = parentEvent.getChildEvent();

        holder.dateMonth.setText(event_date_month);

        ChildEventAdapter childEventAdapter = new ChildEventAdapter(events);
        holder.childRecyclerView.setAdapter(childEventAdapter);
    }

    @Override
    public int getItemCount() {
        return parentEventList.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder {

        TextView dateMonth;
        RecyclerView childRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateMonth = itemView.findViewById(R.id.ew_date_and_month);
            childRecyclerView = itemView.findViewById(R.id.ew_child_recycler_view);
        }
    }
}
