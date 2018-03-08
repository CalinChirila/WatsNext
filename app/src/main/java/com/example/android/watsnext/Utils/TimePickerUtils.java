package com.example.android.watsnext.Utils;

import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Calin-Cristian Chirila on 3/7/2018.
 */

public class TimePickerUtils {

    public static int mTimePickerHour = 0;
    public static int mTimePickerMinute = 0;
    public static int mTimePickerAmPm = 0;      // 0 => am ; 1 => pm

    public static void increaseHour(TextView hourTextView){
        if(mTimePickerHour < 12){
            mTimePickerHour = mTimePickerHour + 1;
        } else {
            mTimePickerHour = 1;
        }
        hourTextView.setText(String.valueOf(mTimePickerHour));
    }

    public static void decreaseHour(TextView hourTextView){
        if(mTimePickerHour > 1){
            mTimePickerHour = mTimePickerHour - 1;
        } else {
            mTimePickerHour = 12;
        }
        hourTextView.setText(String.valueOf(mTimePickerHour));
    }

    public static void increaseMinute(TextView minuteTextView){
        if(mTimePickerMinute < 59){
            mTimePickerMinute = mTimePickerMinute + 1;
        } else {
            mTimePickerMinute = 0;
        }
        minuteTextView.setText(String.valueOf(mTimePickerMinute));
    }

    public static void decreaseMinute(TextView minuteTextView){
        if(mTimePickerMinute > 0){
            mTimePickerMinute = mTimePickerMinute - 1;
        } else {
            mTimePickerMinute = 59;
        }
        minuteTextView.setText(String.valueOf(mTimePickerMinute));
    }

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

    public static void setTimePickerDefaults(TextView hourTextView, TextView minuteTextView, TextView ampmTextView){
        Calendar calendar = Calendar.getInstance();
        mTimePickerHour = calendar.get(Calendar.HOUR_OF_DAY);
        mTimePickerMinute = calendar.get(Calendar.MINUTE);
        mTimePickerAmPm = calendar.get(Calendar.AM_PM);

        hourTextView.setText(String.valueOf(mTimePickerHour));
        minuteTextView.setText(String.valueOf(mTimePickerMinute));
        ampmTextView.setText(convertAmPmToString(mTimePickerAmPm));
    }

    public static long getTimeInMillis(){
        long timeInMillis;
        if(mTimePickerAmPm == 1) mTimePickerHour = mTimePickerHour + 12;

        timeInMillis = (mTimePickerHour * 60 * 60 * 1000) + (mTimePickerMinute * 60 * 1000);
        return timeInMillis;
    }
}
