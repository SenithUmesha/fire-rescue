package com.blackeyedghoul.firefighters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    RecyclerView recyclerView;
    Context context;
    List<Notification> notificationList;
    final View.OnClickListener onClickListener = new MyOnClickListener();

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.n_notification_title);
            time = itemView.findViewById(R.id.n_notification_time);
        }
    }

    public NotificationAdapter(RecyclerView recyclerView, Context context, List<Notification> notificationList) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notification_card, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.title.setText(notification.getTitle());
        holder.time.setText(notification.getDate());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View view) {

            int itemPosition = recyclerView.getChildLayoutPosition(view);

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    context, R.style.BottomSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(context.getApplicationContext())
                    .inflate(
                            R.layout.notification_alert_box,
                            bottomSheetDialog.findViewById(R.id.n_alert_box)
                    );

            TextView txtTitle = bottomSheetView.findViewById(R.id.n_alert_title);
            txtTitle.setText("Title : "+notificationList.get(itemPosition).getTitle());
            TextView txtBody = bottomSheetView.findViewById(R.id.n_alert_body);
            txtBody.setText("Body : "+notificationList.get(itemPosition).getBody());
            TextView txtDate = bottomSheetView.findViewById(R.id.n_alert_time);
            txtDate.setText("Date : "+notificationList.get(itemPosition).getDate());

            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        }
    }
}
