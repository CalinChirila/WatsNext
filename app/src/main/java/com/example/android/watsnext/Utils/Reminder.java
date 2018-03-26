package com.example.android.watsnext.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;

import com.example.android.watsnext.BroadcastReceivers.AlarmReceiver;
import com.example.android.watsnext.BroadcastReceivers.NotificationReceiver;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Calin-Cristian Chirila on 3/18/2018.
 */

public class Reminder {
    private static int mReminderDays = 0;
    private static int mReminderHours = 0;
    private static int mReminderMinutes = 0;

    private static long mEventDateAndTime;
    private static int daysBeforeEvent;
    private static int mReminderType;

    public static final int REMINDER_NOTIFICATION = 1;
    public static final int REMINDER_ALARM = 2;
    private static final int REQUEST_CODE_ALARM = 123;
    private static final int REQUEST_CODE_NOTIFICATION = 122;

    private static PendingIntent mReminderPendingIntent;

    /**
     * Reminder constructor
     * @param eventDateAndTime - the date and time(in millis) of the event
     */
    public Reminder(int reminderType, long eventDateAndTime){
        // The Reminder should be created only when the add / save event button is pressed
        mEventDateAndTime = eventDateAndTime;
        mReminderType = reminderType;
        daysBeforeEvent = EventUtils.getDaysBeforeEvent(mEventDateAndTime);
    }

    public void setReminderDays(int days){
        mReminderDays = days;
    }

    public void setReminderHours(int hours){
        mReminderHours = hours;
    }

    public void setReminderMinutes(int minutes){
        mReminderMinutes = minutes;
    }

    public static long getReminderTimeInMillis(int days, int hours, int minutes){
        return (days * DatePickerUtils.MILLIS_IN_A_DAY) + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000);
    }

    /**
     * Helper method that creates the reminder based on its type
     */
    public void createReminder(Context context){
        // Create an alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Calendar calendar = new GregorianCalendar();
        int gmtOffSet = calendar.getTimeZone().getRawOffset();

        // Set the reminder time taking the gmt into consideration
        long alarmTime = mEventDateAndTime - (getReminderTimeInMillis(mReminderDays, mReminderHours, mReminderMinutes) + gmtOffSet);

        if(mReminderType == REMINDER_ALARM) {
            // Create the alarm intent
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            mReminderPendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_ALARM, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, mReminderPendingIntent);
            }
        }
        else if (mReminderType == REMINDER_NOTIFICATION){
            // Create the notification intent
            Intent notificationIntent = new Intent(context, NotificationReceiver.class);
            mReminderPendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_NOTIFICATION, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC, alarmTime, mReminderPendingIntent);
            }
        }
    }

    /**
     * This method cancels the pending intents when the event is removed before the reminder goes off
     */
    public static void cancelReminder(){
        if(mReminderPendingIntent != null){
            mReminderPendingIntent.cancel();
        }
    }

    /**
     * Increase the number of days before the event at which the reminder will be set
     * @param reminderDayTextView - the reminder days display
     */
    public static void increaseReminderDays(TextView reminderDayTextView){
        daysBeforeEvent = EventUtils.getDaysBeforeEvent(mEventDateAndTime);
        // The user cannot set a reminder at a number of days greater than the days before the event
        if(daysBeforeEvent > mReminderDays) {
            mReminderDays += 1;
            reminderDayTextView.setText(String.valueOf(mReminderDays));
        }
    }

    /**
     * Decrease the number of days before the event at which the reminder will be set
     * @param reminderDayTextView - the reminder days display
     */
    public static void decreaseReminderDays(TextView reminderDayTextView){
        if(mReminderDays > 0){
            mReminderDays -= 1;
            reminderDayTextView.setText(String.valueOf(mReminderDays));
        }
    }

    /**
     * Increase the number of hours before the event at which the reminder will be set
     * @param reminderHourTextView - the reminder hours display
     */
    public static void increaseReminderHours(TextView reminderHourTextView){
        //TODO: validate reminder time so that it can't be greater than the current time before event
        mReminderHours += 1;
        reminderHourTextView.setText(String.valueOf(mReminderHours));
    }

    /**
     * Decrease the number of hours before the event at which the reminder will be set
     * @param reminderHourTextView - the reminder hours display
     */
    public static void decreaseReminderHours(TextView reminderHourTextView){
        if(mReminderHours > 0){
            mReminderHours -= 1;
            reminderHourTextView.setText(String.valueOf(mReminderHours));
        }
    }

    /**
     * Increase the number of minutes before the event at which the reminder will be set
     * @param reminderMinutesTextView - the reminder minutes display
     */
    public static void increaseReminderMinutes(TextView reminderMinutesTextView){
        mReminderMinutes += 1;
        reminderMinutesTextView.setText(String.valueOf(mReminderMinutes));
    }

    /**
     * Decrease the number of minutes before the event at which the reminder will be set
     * @param reminderMinutesTextView - the reminder minutes display
     */
    public static void decreaseReminderMinutes(TextView reminderMinutesTextView){
        if(mReminderMinutes > 0){
            mReminderMinutes -= 1;
            reminderMinutesTextView.setText(String.valueOf(mReminderMinutes));
        }
    }

}
