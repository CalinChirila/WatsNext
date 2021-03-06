package com.example.android.watsnext.utils;

import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Calin-Cristian Chirila on 3/7/2018.
 */

public class TimePickerUtils {

    public static int mTimePickerHour = 0;
    public static int mTimePickerMinute = 0;
    public static int mTimePickerAmPm = 0;      // 0 => am ; 1 => pm


    /**
     * This function will be called by the increase hour button from the add/edit event activity
     */
    public static void increaseHour(TextView hourTextView){
        if(mTimePickerHour < 12){
            mTimePickerHour = mTimePickerHour + 1;
        } else {
            mTimePickerHour = 1;
        }

        String hourString = String.valueOf(mTimePickerHour);
        if(mTimePickerHour < 10) hourString = "0" + hourString;
        hourTextView.setText(hourString);
    }

    /**
     * This function will be called by the decrease hour button from the add/edit event activity
     */
    public static void decreaseHour(TextView hourTextView){
        if(mTimePickerHour > 1){
            mTimePickerHour = mTimePickerHour - 1;
        } else {
            mTimePickerHour = 12;
        }

        String hourString = String.valueOf(mTimePickerHour);
        if(mTimePickerHour < 10) hourString = "0" + hourString;
        hourTextView.setText(hourString);
    }

    /**
     * This function will be called by the increase minute button from the add/edit event activity
     */
    public static void increaseMinute(TextView minuteTextView){
        if(mTimePickerMinute < 59){
            mTimePickerMinute = mTimePickerMinute + 1;
        } else {
            mTimePickerMinute = 0;
        }

        String minuteString = String.valueOf(mTimePickerMinute);
        if(mTimePickerMinute < 10) minuteString = "0" + minuteString;
        minuteTextView.setText(minuteString);
    }

    /**
     * This function will be called by the decrease minute button from the add/edit event activity
     */
    public static void decreaseMinute(TextView minuteTextView){
        if(mTimePickerMinute > 0){
            mTimePickerMinute = mTimePickerMinute - 1;
        } else {
            mTimePickerMinute = 59;
        }

        String minuteString = String.valueOf(mTimePickerMinute);
        if(mTimePickerMinute < 10) minuteString = "0" + minuteString;
        minuteTextView.setText(minuteString);
    }

    /**
     * This function will be called when the arrows for switching am / pm are pressed
     * @param ampmTextView - the TextView to be updated
     */
    public static void switchAmPm(TextView ampmTextView){
        switch(mTimePickerAmPm){
            case 0:
                mTimePickerAmPm = 1;
                ampmTextView.setText(convertAmPmToString(mTimePickerAmPm));
                break;
            case 1:
                mTimePickerAmPm = 0;
                ampmTextView.setText(convertAmPmToString(mTimePickerAmPm));
                break;
            default:
                throw new UnsupportedOperationException("Error while switch am / pm for value: " + mTimePickerAmPm);
        }
    }

    /**
     * Convert am/pm values from int to string
     */
    public static String convertAmPmToString(int ampm){
        String ampmString;
        switch(ampm){
            case 0:
                ampmString = "am";
                break;
            case 1:
                ampmString = "pm";
                break;
            default:
                throw new UnsupportedOperationException("Couldn't convert am/pm for value: " + ampm);
        }

        return ampmString;
    }

    public static void setTimePickerAtTime(long time, TextView hourTextView, TextView minuteTextView, TextView ampmTextView){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        // Take gmt into consideration
        int gmtOffSet = calendar.getTimeZone().getRawOffset()  / (60 * 60 * 1000);
        mTimePickerHour = calendar.get(Calendar.HOUR_OF_DAY) - gmtOffSet;
        if(mTimePickerHour > 12) mTimePickerHour = mTimePickerHour - 12;
        mTimePickerMinute = calendar.get(Calendar.MINUTE);
        mTimePickerAmPm = calendar.get(Calendar.AM_PM);

        String minuteString = String.valueOf(mTimePickerMinute);
        if(mTimePickerMinute < 10) minuteString = "0" + minuteString;
        minuteTextView.setText(minuteString);
        String hourString = String.valueOf(mTimePickerHour);
        if(mTimePickerHour < 10) hourString = "0" + hourString;
        hourTextView.setText(hourString);

        hourTextView.setText(hourString);
        minuteTextView.setText(minuteString);
        ampmTextView.setText(convertAmPmToString(mTimePickerAmPm));
    }

    /**
     * Set the time picker defaults to the current time
     */
    public static void setTimePickerDefaults(TextView hourTextView, TextView minuteTextView, TextView ampmTextView){
        Calendar calendar = Calendar.getInstance();
        mTimePickerHour = calendar.get(Calendar.HOUR_OF_DAY);
        if(mTimePickerHour > 12) mTimePickerHour = mTimePickerHour - 12;
        mTimePickerMinute = calendar.get(Calendar.MINUTE);
        mTimePickerAmPm = calendar.get(Calendar.AM_PM);

        String minuteString = String.valueOf(mTimePickerMinute);
        if(mTimePickerMinute < 10) minuteString = "0" + minuteString;
        minuteTextView.setText(minuteString);
        String hourString = String.valueOf(mTimePickerHour);
        if(mTimePickerHour < 10) hourString = "0" + hourString;
        hourTextView.setText(hourString);

        hourTextView.setText(hourString);
        minuteTextView.setText(minuteString);
        ampmTextView.setText(convertAmPmToString(mTimePickerAmPm));
    }

    /**
     * Convert the time set by the user into milliseconds
     * @return - time in milliseconds
     */
    public static long getTimeInMillis(){
        long timeInMillis;
        if(mTimePickerAmPm == 1) mTimePickerHour = mTimePickerHour + 12;

        timeInMillis = (mTimePickerHour * 60 * 60 * 1000) + (mTimePickerMinute * 60 * 1000);
        return timeInMillis;
    }
}
