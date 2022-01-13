package com.blackeyedghoul.firefighters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.Locale;

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.ViewHolder>{

    Context context;
    List<Station> stationList;
    RecyclerView recyclerView;
    final View.OnClickListener onClickListener = new MyOnClickListener();

    public StationsAdapter(Context context, List<Station> stationList, RecyclerView recyclerView) {
        this.context = context;
        this.stationList = stationList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public StationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.station_row, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationsAdapter.ViewHolder holder, int position) {
        Station station = stationList.get(position);
        holder.rowStationName.setText(station.getName());
        holder.rowStationPhoneNumber.setText(station.getPhone_number());
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rowStationName;
        TextView rowStationPhoneNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowStationName = itemView.findViewById(R.id.s_station_name);
            rowStationPhoneNumber = itemView.findViewById(R.id.s_phone_number);
        }
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
                            R.layout.station_details_alert_box,
                            bottomSheetDialog.findViewById(R.id.s_alert_box)
                    );

            TextView txtName = bottomSheetView.findViewById(R.id.s_alert_name_txt);
            txtName.setText(stationList.get(itemPosition).getName());
            TextView txtAddress = bottomSheetView.findViewById(R.id.s_alert_address_txt);
            txtAddress.setText("Address : "+stationList.get(itemPosition).getAddress());
            TextView txtPhone = bottomSheetView.findViewById(R.id.s_alert_phone_txt);
            txtPhone.setText("Phone Number : "+stationList.get(itemPosition).getPhone_number());
            TextView txtTotalF = bottomSheetView.findViewById(R.id.s_alert_total_f_txt);
            txtTotalF.setText("Total Firefighters : "+stationList.get(itemPosition).getTotal_fighters());
            TextView txtTotalV = bottomSheetView.findViewById(R.id.s_alert_total_v_txt);
            txtTotalV.setText("Total Vehicles : "+stationList.get(itemPosition).getTotal_vehicles());

            bottomSheetView.findViewById(R.id.s_alert_red_dash).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double lat = Double.parseDouble(stationList.get(itemPosition).getLat());
                    Double lon = Double.parseDouble(stationList.get(itemPosition).getLon());
                    String flag = stationList.get(itemPosition).getName();

                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", lat, lon, flag);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    context.startActivity(intent);

                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        }
    }
}
