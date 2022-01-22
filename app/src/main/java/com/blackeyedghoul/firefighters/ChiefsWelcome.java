package com.blackeyedghoul.firefighters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

public class ChiefsWelcome extends AppCompatActivity {

    VideoView videoview;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chiefs_welcome);
        getWindow().setStatusBarColor(ContextCompat.getColor(ChiefsWelcome.this, R.color.dark_red));

        init();

        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.firefighters));
        videoview.requestFocus();
        videoview.start();

        videoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoview.isPlaying()) {
                    videoview.pause();
                }
                else {
                    videoview.start();
                }
            }
        });

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
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
        videoview = findViewById(R.id.cw_video_view);
        back = findViewById(R.id.cw_back);
    }
}