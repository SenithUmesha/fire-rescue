package com.blackeyedghoul.firefighters;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PushNotificationService extends FirebaseMessagingService {

    String title, body, tag;
    final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
    DatabaseAdapter databaseAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        title = remoteMessage.getNotification().getTitle();
        body = remoteMessage.getNotification().getBody();
        tag = remoteMessage.getNotification().getTag();
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Heads Up Notification",
                NotificationManager.IMPORTANCE_HIGH
        );

        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("d MMM yyyy 'at' h:mm a");
        String date = df.format(Calendar.getInstance().getTime());

        try {
            databaseAdapter = new DatabaseAdapter(this);
            databaseAdapter.insertData(new com.blackeyedghoul.firefighters.Notification(title, body, date));
            Log.d("isNotificationRecorded", "True");
        } catch (Exception e) {
            Log.d("isNotificationRecorded", "False");
        }

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1, notification.build());
        super.onMessageReceived(remoteMessage);
    }
}