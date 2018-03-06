package com.example.android.watsnext.Utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.android.watsnext.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Calin-Cristian Chirila on 3/6/2018.
 */

public class EventUtils {

    public static final long MILLIS_IN_A_DAY = 86400000;
    public static final long MILLIS_IN_TWO_DAYS = MILLIS_IN_A_DAY * 2;

    /**
     * Helper method that converts the event date to a string
     */
    public static String convertEventDateToString(Context context, long eventDate){

        String formattedDate;
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        long timeBeforeEvent = eventDate - currentTimeInMillis;

        if(timeBeforeEvent <= MILLIS_IN_A_DAY){
            // Display the word TODAY
            formattedDate = context.getResources().getString(R.string.today);
        } else if (timeBeforeEvent > MILLIS_IN_A_DAY && timeBeforeEvent <= MILLIS_IN_TWO_DAYS){
            // Display the word TOMORROW
            formattedDate = context.getResources().getString(R.string.tomorrow);
        } else {
            // Display the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            formattedDate = dateFormat.format(eventDate);
        }
        return formattedDate;
    }

    /**
     * Helper method that converts the event time to a string
     */
    public static String convertEventTimeToString(long eventTime){
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        return timeFormat.format(eventTime);
    }

    /**
     * Helper method that sets the correct icon for the reminder type
     */
    public static void setReminderIcon(ImageView icon, int reminderType){
        switch(reminderType){
            case 0:
                icon.setVisibility(View.INVISIBLE);
                break;
            case 1:
                icon.setImageResource(R.drawable.ic_assignment);
                break;
            case 2:
                icon.setImageResource(R.drawable.ic_alarm);
                break;
        }
    }

    /**
     * Helper method that converts event types into strings
     */
    public static String convertEventTypeToString(Context context, int type){
        String eventType;
        switch(type){
            case 0:
                eventType = context.getResources().getString(R.string.event_type_other);
                break;
            case 1:
                eventType = context.getResources().getString(R.string.event_type_appointment);
                break;
            case 2:
                eventType = context.getResources().getString(R.string.event_type_meeting);
                break;
            case 3:
                eventType = context.getResources().getString(R.string.event_type_social);
                break;
            default:
                eventType = context.getResources().getString(R.string.event_type_other);
                break;
        }

        return eventType;
    }
}
