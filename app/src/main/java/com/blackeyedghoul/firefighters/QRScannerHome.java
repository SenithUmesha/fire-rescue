package com.blackeyedghoul.firefighters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;


public class QRScannerHome extends AppCompatActivity {

    CardView capture;
    ImageView back;
    BottomSheetBehavior bottomSheetBehavior;
    RecyclerView recyclerView;
    List<Scan> scanList;
    QRHistoryAdapter adapter;
    View bottomSheet;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner_home);
        getWindow().setStatusBarColor(ContextCompat.getColor(QRScannerHome.this, R.color.dark_red));

        init();

        scanList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("scans");
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MainActivity.isConnected(QRScannerHome.this)) {
                    checkMyPermission();
                } else {
                    Toast.makeText(QRScannerHome.this, "Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!scanList.isEmpty()) {
                    scanList.clear();
                }
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Scan data = ds.getValue(Scan.class);
                    scanList.add(data);
                }
                adapter = new QRHistoryAdapter(recyclerView, QRScannerHome.this, scanList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void init() {
        capture = findViewById(R.id.qrh_color_ring_inner);
        back = findViewById(R.id.qrh_back);
        bottomSheet = findViewById(R.id.qrh_bottomSheet);
        recyclerView = findViewById(R.id.qrh_recycler_view);
    }

    private void checkMyPermission() {

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent intent = new Intent(QRScannerHome.this, QRScanner.class);
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", QRScannerHome.this.getPackageName(), "");
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