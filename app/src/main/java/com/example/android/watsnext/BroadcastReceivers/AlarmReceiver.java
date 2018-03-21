package com.example.android.watsnext.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;

import com.example.android.watsnext.Activities.AddEventActivity;
import com.example.android.watsnext.Activities.AlarmActivity;

/**
 * Created by Calin-Cristian Chirila on 3/18/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("IMPORTANT", "Alarm received");

        // When the alarm is received, start the alarm activity
        Intent alarm = new Intent(context, AlarmActivity.class);
        String alarmMessage = AddEventActivity.mEventText;
        alarm.putExtra(AlarmClock.EXTRA_MESSAGE, alarmMessage);
        context.startActivity(alarm);
    }
}
