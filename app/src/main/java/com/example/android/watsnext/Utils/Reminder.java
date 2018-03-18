package com.example.android.watsnext.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;

import com.example.android.watsnext.BroadcastReceivers.AlarmReceiver;

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





    public static int getReminderDays(){
        return mReminderDays;
    }

    public static int getReminderHours(){
        return mReminderHours;
    }

    public static int getReminderMinutes(){
        return mReminderMinutes;
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

    public void initReminder(Context context){
        //TODO: complete method

        if(mReminderType == REMINDER_ALARM) {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager == null) return;

            long alarmTime = mEventDateAndTime - getReminderTimeInMillis(mReminderDays, mReminderHours, mReminderMinutes);

            //TODO: delete the line below before release
            alarmTime = alarmTime - (2* 3600 * 1000); //TODO: EVRYKA!! Handle time-zones!

            //TODO: might have to convert all time variables to local time
            // TODO: problem with alarm not being called

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_ALARM, alarmIntent, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, alarmPendingIntent);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, alarmPendingIntent);
            }
        }
    }


    /**
     * Increase the number of days before the event at which the reminder will be set
     * @param reminderDayTextView - the reminder days display
     */
    public static void increaseReminderDays(TextView reminderDayTextView){

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
