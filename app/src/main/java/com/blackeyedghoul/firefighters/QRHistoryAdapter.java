package com.blackeyedghoul.firefighters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRHistoryAdapter extends RecyclerView.Adapter<QRHistoryAdapter.ViewHolder>{

    RecyclerView recyclerView;
    Context context;
    List<Scan> scanList;
    final View.OnClickListener onClickListener = new QRHistoryAdapter.MyOnClickListener();

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView data, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            data = itemView.findViewById(R.id.scan_h_title);
            time = itemView.findViewById(R.id.scan_h_time);
        }
    }

    public QRHistoryAdapter(RecyclerView recyclerView, Context context, List<Scan> scanList) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.scanList = scanList;
    }

    @NonNull
    @Override
    public QRHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.scan_history_row, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRHistoryAdapter.ViewHolder holder, int position) {
        Scan scan = scanList.get(position);
        holder.data.setText(scan.getData());
        holder.time.setText(scan.getTime());
    }

    @Override
    public int getItemCount() {
        return scanList.size();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            int itemPosition = recyclerView.getChildLayoutPosition(view);

            Pattern p = Pattern.compile("^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$");
            Matcher m = p.matcher(scanList.get(itemPosition).getData());
            if (m.find()) {
                AlertDialog alertDialog = new AlertDialog.Builder(context,
                        R.style.AlertDialog)
                        .setTitle("Open Link")
                        .setPositiveButton("Open", ((dialogInterface, i) -> {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(scanList.get(itemPosition).getData())));
                        }))
                        .setNegativeButton("Cancel", ((dialogInterface, i) -> {
                        }))
                        .setCancelable(false)
                        .show();
            } else {
                Toast.makeText(context, "Content is not openable!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
