package com.example.lab6;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Notification_Receiver extends BroadcastReceiver {
    private final static int NOTIFY_ID = 101;
    private final static String CHANNEL_ID = "Event_channel";

    @Override
    public  void onReceive(Context context, Intent intent) {
        Event event = intent.getSerializableExtra("Event", Event.class);
        DataBase dataBase = new DataBase(context);
        dataBase.edit_event_activity(event.getId(), false);
        showNotification(context, event);
    }

    private void showNotification(Context context, Event event){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                Intent notificationIntent = new Intent(context, Show_event.class);
                notificationIntent.putExtra("Event", event);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.drawable.calendar)
                                .setContentTitle(event.getName())
                                .setContentText(event.getPlace())
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent);

                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(NOTIFY_ID, builder.build());
            }
        }
    }
}