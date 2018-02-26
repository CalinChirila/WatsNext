package com.example.android.watsnext;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Calin-Cristian Chirila on 2/26/2018.
 */

public class Event {

    // Since the event types will be a predefined list, we can store them as integers.
    private int mEventType;

    // This will be the user's custom description of the event.
    private String mEventText;

    // Store this as longs. They will be converted into date string for displaying
    private static long mEventDate;
    private static long mEventTime;

    private String mEventLocation;
    // The days of the week when the event should repeat
    private ArrayList<Integer> mRepeatDays;

    // A boolean that defines if the event has a set reminder. TODO: might convert this to an int if it is more efficient
    private boolean mHasReminder;

    // The reminder type: 0 => no reminder; 1 => notification; 2 => alarm
    private int mReminderType;

    // Since we don't have to display reminder time, we can store it as timeInMillis
    private long mReminderTime;

    public static final long MILLIS_IN_A_DAY = 86400000;
    public static final long MILLIS_IN_TWO_DAYS = MILLIS_IN_A_DAY * 2;

    // The constructor. Only mandatory fields are date and event type
    public Event(int eventType, long eventDate){
        mEventType = eventType;
        mEventDate = eventDate;
    }

    /**
     * Setter methods
     */
    public void setEventType(int eventType){
        mEventType = eventType;
    }

    public void setEventText(String eventText){
        mEventText = eventText;
    }

    public void setEventDate(long eventDate){
        mEventDate = eventDate;
    }

    public void setEventTime(long eventTime){
        mEventTime = eventTime;
    }

    public void setEventLocation(String eventLocation){
        mEventLocation = eventLocation;
    }

    public void setRepeatDays(ArrayList<Integer> repeatDays){
        mRepeatDays = repeatDays;
    }

    public void setHasReminder(boolean hasReminder){
        mHasReminder = hasReminder;
    }

    public void setReminderType(int reminderType){
        mReminderType = reminderType;
    }

    public void setReminderTime(long reminderTime){
        mReminderTime = reminderTime;
    }

    /**
     * Getter methods
     */
    public int getEventType(){return mEventType; }
    public String getEventText(){return mEventText; }
    public long getEventDate(){return mEventDate;}
    public long getEventTime(){return mEventTime;}
    public String getEventLocation(){return mEventLocation;}
    public ArrayList<Integer> getRepeatDays(){return mRepeatDays;}
    public boolean getHasReminder(){return mHasReminder;}
    public int getReminderType(){return mReminderType;}
    public long getReminderTime(){return mReminderTime;}


    // A simple formatter to convert DATE from millis into a readable string
    public static String convertDateToString(Context context){
        String formattedDate;
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        long timeBeforeEvent = mEventDate - currentTimeInMillis;
        if(timeBeforeEvent <= MILLIS_IN_A_DAY){
            // Display the word TODAY
            formattedDate = context.getResources().getString(R.string.today);
        } else if (timeBeforeEvent > MILLIS_IN_A_DAY && timeBeforeEvent <= MILLIS_IN_TWO_DAYS){
            // Display the word TOMORROW
            formattedDate = context.getResources().getString(R.string.tomorrow);
        } else {
            // Display the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            formattedDate = dateFormat.format(mEventDate);
        }
        return formattedDate;
    }

    // A simple formatter to convert TIME from millis into a readable string
    public static String convertTimeToString(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        return timeFormat.format(mEventTime);
    }

}
