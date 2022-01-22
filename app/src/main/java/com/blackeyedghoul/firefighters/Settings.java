package com.blackeyedghoul.firefighters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {

    ImageView back;
    TextView version;
    String version_number;
    DatabaseReference databaseReference;
    SwitchCompat location, pushNotifications, camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setStatusBarColor(ContextCompat.getColor(Settings.this, R.color.dark_red));

        init();
        checkPermissions();

        if (MainActivity.isConnected(Settings.this)) {

            databaseReference = FirebaseDatabase.getInstance().getReference("about");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    version_number = snapshot
                            .child("version")
                            .getValue().toString();

                    version.setText(version_number);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });

        } else {
            Toast.makeText(Settings.this, "Getting version number failed, check your internet connection.", Toast.LENGTH_SHORT).show();
        }

        pushNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", Settings.this.getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", Settings.this.getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", Settings.this.getPackageName(), "");
                intent.setData(uri);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }

    public void checkPermissions() {

        String p_location = Manifest.permission.ACCESS_FINE_LOCATION;
        int checkLocation = Settings.this.checkCallingOrSelfPermission(p_location);

        String p_camera = Manifest.permission.CAMERA;
        int checkCamera = Settings.this.checkCallingOrSelfPermission(p_camera);

        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            pushNotifications.setChecked(true);
        }

        if (checkLocation == PackageManager.PERMISSION_GRANTED) {
            location.setChecked(true);
        }

        if (checkCamera == PackageManager.PERMISSION_GRANTED) {
            camera.setChecked(true);
        }
    }

    private void init() {
        back = findViewById(R.id.st_back);
        version = findViewById(R.id.st_version_number);
        location = findViewById(R.id.st_location);
        pushNotifications = findViewById(R.id.st_notifications);
        camera = findViewById(R.id.st_camera);
    }
}