package com.example.android.watsnext.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import com.example.android.watsnext.activities.AddEventActivity;
import com.example.android.watsnext.activities.AlarmActivity;
import com.example.android.watsnext.utils.Reminder;

import java.util.ArrayList;

/**
 * Created by Calin-Cristian Chirila on 3/18/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static ArrayList<Long> alarmsReceivedWithIds = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        long eventId = intent.getLongExtra(Reminder.ALARM_EVENT_ID, -1);

        if(alarmsReceivedWithIds.contains(eventId)){
            this.abortBroadcast();
            return;
        }
        alarmsReceivedWithIds.add(eventId);

        String alarmMessage = AddEventActivity.mEventText;

        // When the alarm is received, start the alarm activity
        Intent alarm = new Intent(context, AlarmActivity.class);

        alarm.putExtra(AlarmClock.EXTRA_MESSAGE, alarmMessage);
        alarm.putExtra(Reminder.ALARM_EVENT_ID, eventId);
        context.startActivity(alarm);
    }
}
