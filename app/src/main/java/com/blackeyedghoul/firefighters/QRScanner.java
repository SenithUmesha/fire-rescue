package com.blackeyedghoul.firefighters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

public class QRScanner extends AppCompatActivity {

    ScannerLiveView scannerLiveView;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        getWindow().setStatusBarColor(ContextCompat.getColor(QRScanner.this, R.color.dark_red));

        init();

        scannerLiveView.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) { }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) { }

            @Override
            public void onScannerError(Throwable err) {
                Toast.makeText(QRScanner.this, "Scanning error ...", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onCodeScanned(String data) {

                scannerLiveView.stopScanner();

                @SuppressLint("SimpleDateFormat")
                DateFormat df = new SimpleDateFormat("d MMM yyyy 'at' h:mm a");
                String time = df.format(Calendar.getInstance().getTime());

                Intent intent = new Intent(QRScanner.this, QRScannerResults.class);
                intent.putExtra("data", data);
                intent.putExtra("time", time);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void init() {
        scannerLiveView = findViewById(R.id.qr_cam_view);
        back = findViewById(R.id.qr_back);
    }

    @Override
    protected void onPause() {
        scannerLiveView.stopScanner();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        decoder.setScanAreaPercent(0.8);
        scannerLiveView.setDecoder(decoder);
        scannerLiveView.startScanner();
    }
}