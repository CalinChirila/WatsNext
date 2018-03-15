package com.example.android.watsnext.Utils;

import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Calin-Cristian Chirila on 3/7/2018.
 */

public class DatePickerUtils {

    private static final String TAG = DatePickerUtils.class.getSimpleName();

    public static final long MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000;

    public static int mEventMonth;
    public static int mEventYear;
    public static int mEventDay;


    private static Calendar calendar = Calendar.getInstance();

    private static int currentYear = calendar.get(Calendar.YEAR);

    /**
     * This function will be called by the increase month button from the add/edit event activity
     */
    public static void increaseMonth(TextView monthTextView) {
        if (mEventMonth < 12) {
            mEventMonth = mEventMonth + 1;
        } else {
            mEventMonth = 1;
        }
        monthTextView.setText(convertMonthToString(mEventMonth));

    }

    /**
     * This function will be called by the increase year button from the add/edit event activity
     */
    public static void increaseYear(TextView yearTextView) {
        mEventYear = mEventYear + 1;
        yearTextView.setText(String.valueOf(mEventYear));
    }

    /**
     * This function will be called by the increase day button from the add/edit event activity
     */
    public static void increaseDay(TextView dayTextView){
        int maxDaysInMonth = getMaxDaysInMonth(mEventMonth, mEventYear);
        if(mEventDay < maxDaysInMonth){
            mEventDay = mEventDay + 1;
        } else {
            mEventDay = 1;
        }
        dayTextView.setText(String.valueOf(mEventDay));
    }

    /**
     * This function will be called by the decrease month button from the add/edit event activity
     */
    public static void decreaseMonth(TextView monthTextView){
        if(mEventMonth > 1){
            mEventMonth = mEventMonth - 1;
        } else {
            mEventMonth = 12;
        }
        monthTextView.setText(convertMonthToString(mEventMonth));
    }

    /**
     * This function will be called by the decrease year button from the add/edit event activity
     */
    public static void decreaseYear(TextView yearTextView){
        if(mEventYear > currentYear){
            mEventYear = mEventYear - 1;
            yearTextView.setText(String.valueOf(mEventYear));
        }
    }

    /**
     * This function will be called by the decrease day button from the add/edit event activity
     */
    public static void decreaseDay(TextView dayTextView){
        int maxDaysInMonth = getMaxDaysInMonth(mEventMonth, mEventYear);
        if(mEventDay > 1){
            mEventDay = mEventDay - 1;
        } else {
            mEventDay = maxDaysInMonth;
        }
        dayTextView.setText(String.valueOf(mEventDay));
    }



    /**
     * This function will set the display date picker date to the present date
     * @param dayTextView - the day display
     * @param monthTextView - the month display
     * @param yearTextView - the year display
     */
    public static void setDatePickerDefaults(TextView dayTextView, TextView monthTextView, TextView yearTextView){
        mEventDay = calendar.get(Calendar.DATE);
        mEventMonth = calendar.get(Calendar.MONTH) + 1;     // BECAUSE REASONS!
        mEventYear = calendar.get(Calendar.YEAR);

        dayTextView.setText(String.valueOf(mEventDay));
        monthTextView.setText(convertMonthToString(mEventMonth));
        yearTextView.setText(String.valueOf(mEventYear));
    }

    /**
     * Replacement for SimpleDateFormatter that converted month 3 to Jan...
     */
    public static String convertMonthToString(int month){
        String formattedMonth;
        switch(month){
            case 1:
                formattedMonth = "Jan";
                break;
            case 2:
                formattedMonth = "Feb";
                break;
            case 3:
                formattedMonth = "Mar";
                break;
            case 4:
                formattedMonth = "Apr";
                break;
            case 5:
                formattedMonth = "May";
                break;
            case 6:
                formattedMonth = "Jun";
                break;
            case 7:
                formattedMonth = "Jul";
                break;
            case 8:
                formattedMonth = "Aug";
                break;
            case 9:
                formattedMonth = "Sep";
                break;
            case 10:
                formattedMonth = "Oct";
                break;
            case 11:
                formattedMonth = "Nov";
                break;
            case 12:
                formattedMonth = "Dec";
                break;
            default:
                throw new UnsupportedOperationException("Couldn't convert month " + month);
        }
        return formattedMonth;
    }

    /**
     * Helper method that returns the total number of days in a specified year
     */
    public static int getDaysInCurrentYear(int currentYear){
        if(currentYear % 4 == 0){
            return 366;
        } else {
            return 365;
        }
    }

    /**
     * Setter methods
     */
    public static void setEventDay(int day){ mEventDay = day; }
    public static void setEventMonth(int month){ mEventMonth = month; }
    public static void setEventYear(int year){ mEventYear = year; }


    /**
     * Method that returns the date set on the date picker in milliseconds
     * @return selected date in unix epoch time
     */
    public static long getDateInMillis(){
        // When validating user input, this + time (hours and minutes) in milliseconds will need to be greater than currentTimeInMillis

        int yearsSinceUnix = mEventYear - 1970;
        int yearsSinceFirstLeapDay = mEventYear - 1972;
        Calendar calendar = Calendar.getInstance();

        calendar.set(mEventYear, mEventMonth - 1, mEventDay);
        int daysFromYearToEvent = calendar.get(Calendar.DAY_OF_YEAR);

        long daysSinceUnix = (yearsSinceUnix * 365) + (yearsSinceFirstLeapDay / 4) + daysFromYearToEvent;

        return daysSinceUnix * MILLIS_IN_A_DAY ;
    }

    /**
     * Helper method that returns the number of days in given month for the given year
     * @param month the month in question
     * @param year the year in question
     * @return the total number of days for the specified month in the specified year
     */
    private static int getMaxDaysInMonth(int month, int year){
        int maxDaysInMonth;
        switch(month){
            case 1:
                maxDaysInMonth = 31;
                break;
            case 2:
                if(year % 4 == 0){
                    maxDaysInMonth = 29;
                } else {
                    maxDaysInMonth = 28;
                }
                break;
            case 3:
                maxDaysInMonth = 31;
                break;
            case 4:
                maxDaysInMonth = 30;
                break;
            case 5:
                maxDaysInMonth = 31;
                break;
            case 6:
                maxDaysInMonth = 30;
                break;
            case 7:
                maxDaysInMonth = 31;
                break;
            case 8:
                maxDaysInMonth = 31;
                break;
            case 9:
                maxDaysInMonth = 30;
                break;
            case 10:
                maxDaysInMonth = 31;
                break;
            case 11:
                maxDaysInMonth = 30;
                break;
            case 12:
                maxDaysInMonth = 31;
                break;
            default:
                throw new UnsupportedOperationException("Couldn't get max number of days in month: " + month);
        }

        return maxDaysInMonth;
    }
}
