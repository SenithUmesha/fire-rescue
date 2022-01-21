package com.blackeyedghoul.firefighters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    Context context;
    List<Job> jobList;
    RecyclerView recyclerView;
    final View.OnClickListener onClickListener = new MyOnClickListener();

    public JobAdapter(Context context, List<Job> jobList, RecyclerView recyclerView) {
        this.context = context;
        this.jobList = jobList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public JobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.job_card, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdapter.ViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.jobName.setText(job.position);
        holder.location.setText(job.location);
        holder.work_time.setText(job.work_time);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobName, work_time, location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.c_card_name);
            work_time = itemView.findViewById(R.id.c_card_work_time);
            location = itemView.findViewById(R.id.c_card_location);
        }
    }

    public void filterList(List<Job> filteredList) {
        jobList = filteredList;
        notifyDataSetChanged();
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int itemPosition = recyclerView.getChildLayoutPosition(view);

            Intent intent = new Intent(context, CareerDetails.class);
            intent.putExtra("position", jobList.get(itemPosition).getPosition());
            intent.putExtra("date", jobList.get(itemPosition).getPosted_date());
            intent.putExtra("location", jobList.get(itemPosition).getLocation());
            intent.putExtra("age", jobList.get(itemPosition).getAge());
            intent.putExtra("apply", jobList.get(itemPosition).getApply());
            intent.putExtra("description", jobList.get(itemPosition).getDescription());
            intent.putExtra("salary", jobList.get(itemPosition).getSalary());
            intent.putExtra("work_time", jobList.get(itemPosition).getWork_time());
            intent.putExtra("education", jobList.get(itemPosition).getEducation());
            context.startActivity(intent);
        }
    }
}
