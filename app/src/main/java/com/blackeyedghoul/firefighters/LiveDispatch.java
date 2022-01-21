package com.blackeyedghoul.firefighters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LiveDispatch extends AppCompatActivity {

    String updatedStatus, updatedUrl;
    ImageView play, pause, back;
    SeekBar volumeBar;
    LottieAnimationView ripple;
    AudioManager audioManager;
    TextView feedStatus;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_dispatch);
        getWindow().setStatusBarColor(ContextCompat.getColor(LiveDispatch.this, R.color.dark_red));

        Init();

        pause.setVisibility(View.GONE);
        MediaPlayer mp = new MediaPlayer();

        setUrlAndStatus();

        play.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                ripple.setAnimation(R.raw.green_ripple);
                ripple.loop(true);
                ripple.playAnimation();

                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {
                    mp.setDataSource(updatedUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    feedStatus.setText("Feed Status : " + updatedStatus);
                    mp.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mp.start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                ripple.setAnimation(R.raw.red_ripple);
                ripple.loop(true);
                ripple.playAnimation();

                mp.stop();
                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                onBackPressed();
            }
        });

        try {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUrlAndStatus() {
        databaseReference = FirebaseDatabase.getInstance().getReference("live_dispatch");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                updatedUrl = snapshot
                        .child("url")
                        .getValue().toString();

                updatedStatus = snapshot
                        .child("status")
                        .getValue().toString();

                Log.d("hello", "url: "+updatedUrl+" status: "+updatedStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            volumeBar.setProgress(Math.min(volumeBar.getProgress() + 1, volumeBar.getMax()));
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumeBar.setProgress(Math.max(volumeBar.getProgress() - 1, 0));
        }

        return super.onKeyDown(keyCode, event);
    }

    private void Init() {
        play = findViewById(R.id.ld_play);
        pause = findViewById(R.id.ld_pause);
        volumeBar = findViewById(R.id.ld_seekBar);
        ripple = findViewById(R.id.ld_lottieAnimationView);
        back = findViewById(R.id.ld_back);
        feedStatus = findViewById(R.id.ld_feed_status);
    }
}