package com.blackeyedghoul.firefighters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRScannerResults extends AppCompatActivity {

    DatabaseAdapter databaseAdapter;
    ImageView back;
    TextView contentText;
    ConstraintLayout constraintLayout;
    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner_results);
        getWindow().setStatusBarColor(ContextCompat.getColor(QRScannerResults.this, R.color.white));

        init();

        Intent intent = getIntent();
        String content = intent.getStringExtra("data");
        String time = intent.getStringExtra("time");

        contentText.setText(content);

        databaseAdapter = new DatabaseAdapter(this);
        long isSuccess = databaseAdapter.insertDataScans(new Scan(content, time));
        if (isSuccess != -1) {
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pattern p = Pattern.compile(URL_REGEX);
                    Matcher m = p.matcher(content);
                    if (m.find()) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(content)));
                    } else {
                        Toast.makeText(QRScannerResults.this, "Content is not openable!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Scan record: Fail", Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void init() {
        contentText = findViewById(R.id.qr_result_contents);
        constraintLayout = findViewById(R.id.qr_result_red_dash);
        back = findViewById(R.id.qrr_back);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}