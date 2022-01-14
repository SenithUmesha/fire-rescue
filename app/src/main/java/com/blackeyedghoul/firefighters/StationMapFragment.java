package com.blackeyedghoul.firefighters;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class StationMapFragment extends Fragment{

    View rootView, fragmentMap;
    Context mContext;
    TextView allow, notNow, title, subTitle;
    OptRoundCardView card;
    ConstraintLayout layout;

    public StationMapFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_station_map, container, false);

        init();

        fragmentMap.setVisibility(View.GONE);

        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkMyPermission();
            }
        });

        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        checkMyPermission();

        return rootView;
    }

    private void init() {
        fragmentMap = rootView.findViewById(R.id.fsm_s_map_);
        allow = rootView.findViewById(R.id.fsm_s_allow_access);
        notNow = rootView.findViewById(R.id.fsm_s_allow_access_notNow);
        card = rootView.findViewById(R.id.fsm_s_card);
        title = rootView.findViewById(R.id.fsm_s_title);
        subTitle = rootView.findViewById(R.id.fsm_s_subTitle);
        layout = rootView.findViewById(R.id.fsm_s_layout);
    }

    private void checkMyPermission() {

        Dexter.withContext(mContext).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                card.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                subTitle.setVisibility(View.GONE);
                allow.setVisibility(View.GONE);
                notNow.setVisibility(View.GONE);
                layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                fragmentMap.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mContext.getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}