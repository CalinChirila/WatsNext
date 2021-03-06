package com.example.android.watsnext.utils;

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

    /**
     * Helper method that converts the event date to a string
     */
    public static String convertEventDateToString(Context context, long eventDate){

        String formattedDate;

        int daysBeforeEvent = getDaysBeforeEvent(eventDate);

        if(daysBeforeEvent == 0){
            // Display the word TODAY
            formattedDate = context.getResources().getString(R.string.today);
        } else if (daysBeforeEvent == 1){
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
     * Helper method that returns how many days are left until the event
     * @param eventDate = the date of the event
     * @return the number of days before the event
     */
    public static int getDaysBeforeEvent(long eventDate){

        Calendar calendar = Calendar.getInstance();
        int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        int currentYear = calendar.get(Calendar.YEAR);
        int daysInCurrentYear = DatePickerUtils.getDaysInCurrentYear(currentYear);

        calendar.setTimeInMillis(eventDate);
        int eventDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        int daysBeforeEvent = eventDayOfYear - currentDayOfYear;

        if(daysBeforeEvent < 0){
            // If the days before the event are negative, then the event is in another year
            daysBeforeEvent = daysInCurrentYear + eventDayOfYear;
        }

        return daysBeforeEvent;
    }

    /**
     * Helper method that converts the event time to a string
     */
    public static String convertEventTimeToString(long eventTime){
        String formattedTime;
        long eventTimeInMins = eventTime / 60000;
        int hours =(int) eventTimeInMins / 60;
        int minutes =(int) eventTimeInMins - (hours * 60);

        String ampm;
        if(hours > 12){
            ampm = " pm";
            hours = hours - 12;
        } else {
            ampm = " am";
        }

        String hoursString = String.valueOf(hours);
        String minutesString = String.valueOf(minutes);
        if(hoursString.length() == 1) hoursString = "0" + hoursString;
        if(minutesString.length() == 1) minutesString = "0" + minutesString;

        formattedTime = hoursString + " : " + minutesString + ampm;
        return formattedTime;

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
