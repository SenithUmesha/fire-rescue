package com.blackeyedghoul.firefighters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;

public class ChildEventAdapter extends RecyclerView.Adapter<ChildEventAdapter.ViewHolder> {

    ChildEvent childEventList;
    final View.OnClickListener onClickListener = new MyOnClickListener();

    public ChildEventAdapter(ChildEvent childEventList) {
        this.childEventList = childEventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChildEvent childEvent = childEventList;
        String event_name = childEvent.getEvent_name();
        String event_s_time = childEvent.getEvent_s_time();
        String event_e_time = childEvent.getEvent_e_time();
        String event_location = childEvent.getEvent_location();

        holder.name.setText(event_name);
        holder.s_time.setText(event_s_time);
        holder.e_time.setText(event_e_time);
        holder.location.setText(event_location);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, s_time, e_time, location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ew_event_name);
            s_time = itemView.findViewById(R.id.ew_event_starting_time);
            e_time = itemView.findViewById(R.id.ew_event_ending_time);
            location = itemView.findViewById(R.id.ew_event_location);
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            //intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("allDay", false);
            intent.putExtra("rrule", "FREQ=YEARLY");
            //intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
            intent.putExtra("title", childEventList.event_name);
            view.getContext().startActivity(intent);
        }
    }
}