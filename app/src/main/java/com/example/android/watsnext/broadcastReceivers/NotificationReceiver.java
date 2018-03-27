package com.example.android.watsnext.broadcastReceivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.example.android.watsnext.activities.AddEventActivity;
import com.example.android.watsnext.R;

/**
 * Created by Calin-Cristian Chirila on 3/21/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION_CHANNEL_ID = "notificationChannel";
    private static final int NOTIFICATION_ID = 42;

    private Vibrator mVibrator;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Start vibration without delay
        // Don't repeat

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mVibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            mVibrator.vibrate(200);
        }

        if (notificationManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Versions greater than Oreo require notification channels
                CharSequence name = context.getString(R.string.notification_channel_name);
                String description = context.getString(R.string.notification_channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
                channel.setDescription(description);
                notificationManager.createNotificationChannel(channel);

            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

            // When the notification is clicked, the event details activity should be launched
            Intent notificationIntent = new Intent(context, AddEventActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //TODO: add all event details in this intent
            PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

            notificationBuilder
                    .setAutoCancel(true)
                    .setContentTitle(AddEventActivity.mEventTypeString)
                    .setContentText(AddEventActivity.mEventText)
                    .setSmallIcon(R.drawable.ic_assignment)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(notificationPendingIntent);

            // Show the notification

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        }
    }
}
