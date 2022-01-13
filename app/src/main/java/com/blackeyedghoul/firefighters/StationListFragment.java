package com.blackeyedghoul.firefighters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class StationListFragment extends Fragment {

    Context mContext;
    RecyclerView recyclerView;
    DatabaseAdapter databaseAdapter;
    StationsAdapter stationsAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Station> stationsList = new ArrayList<>();
    View rootView;

    public StationListFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_station_list, container, false);

        recyclerView = rootView.findViewById(R.id.fsl_s_recycler_view);

        PreCreateDB.copyDB(mContext);
        databaseAdapter = new DatabaseAdapter(mContext);
        stationsList = databaseAdapter.getAllStations();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        stationsAdapter = new StationsAdapter(mContext, stationsList, recyclerView);
        recyclerView.setAdapter(stationsAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
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
                    String phone_number1 = stationsList.get(position).getPhone_number();
                    Uri sms_uri = Uri.parse("smsto:"+phone_number1);
                    Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                    startActivity(sms_intent);
                    stationsAdapter.notifyItemChanged(position);
                    break;

                case ItemTouchHelper.RIGHT:
                    String phone_number2 = stationsList.get(position).getPhone_number();
                    Intent call_intent = new Intent(Intent.ACTION_DIAL);
                    call_intent.setData(Uri.parse("tel:"+phone_number2));
                    startActivity(call_intent);
                    stationsAdapter.notifyItemChanged(position);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(mContext, R.color.dark_red))
                    .addSwipeLeftActionIcon(R.drawable.sms)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(mContext, R.color.dark_green))
                    .addSwipeRightActionIcon(R.drawable.call)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}