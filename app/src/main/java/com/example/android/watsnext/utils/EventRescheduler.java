package com.example.android.watsnext.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.example.android.watsnext.data.EventContract.EventsEntry;

import java.util.Calendar;

public class EventRescheduler {

    public static final int ONE_MONTH = 30;
    public static final long PLUS_ONE_WEEK = 7 * DatePickerUtils.MILLIS_IN_A_DAY;
    public static final long PLUS_ONE_MONTH = 30 * DatePickerUtils.MILLIS_IN_A_DAY;

    public static long eventDate;
    public static long reminderTime;

    public static void rescheduleEvent(Context context, long eventId) {
        // Get the uri for the event in question
        Uri eventUri = ContentUris.withAppendedId(EventsEntry.CONTENT_URI, eventId);

        // Read the database for that specific event
        Cursor cursor = context.getContentResolver().query(eventUri, null, null, null, null);
        cursor.moveToFirst();

        // Get the repeat days of the event
        String repeatDays = cursor.getString(cursor.getColumnIndex(EventsEntry.COLUMN_EVENT_REPEAT));
        // Exit early if event is unique
        if(repeatDays == null || TextUtils.isEmpty(repeatDays)) return;

        // Get event information
        int eventType = cursor.getInt(cursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TYPE));
        String eventText = cursor.getString(cursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TEXT));
        eventDate = cursor.getLong(cursor.getColumnIndex(EventsEntry.COLUMN_EVENT_DATE));
        long eventTime = cursor.getLong(cursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TIME));
        String eventLocation = cursor.getString(cursor.getColumnIndex(EventsEntry.COLUMN_EVENT_LOCATION));
        int reminderType = cursor.getInt(cursor.getColumnIndex(EventsEntry.COLUMN_EVENT_REMINDER));
        reminderTime = cursor.getLong(cursor.getColumnIndex(EventsEntry.COLUMN_EVENT_REMINDER_TIME));

        // Check if we reschedule in 1 week or 1 month
        if (repeatDays.contains(String.valueOf(Calendar.DAY_OF_WEEK))) {
            eventDate = eventDate + PLUS_ONE_WEEK;
            reminderTime = reminderTime + PLUS_ONE_WEEK;
        } else if (repeatDays.contains(String.valueOf(ONE_MONTH))) {
            eventDate = eventDate + PLUS_ONE_MONTH;
            reminderTime = reminderTime + PLUS_ONE_MONTH;
        }

        long eventDateAndTime = eventDate + eventTime;

        // Create content values
        ContentValues values = new ContentValues();
        values.put(EventsEntry.COLUMN_EVENT_TYPE, eventType);
        values.put(EventsEntry.COLUMN_EVENT_TEXT, eventText);
        values.put(EventsEntry.COLUMN_EVENT_DATE, eventDate);
        values.put(EventsEntry.COLUMN_EVENT_TIME, eventTime);
        values.put(EventsEntry.COLUMN_EVENT_DATE_AND_TIME, eventDateAndTime);
        values.put(EventsEntry.COLUMN_EVENT_LOCATION, eventLocation);
        values.put(EventsEntry.COLUMN_EVENT_REMINDER, reminderType);
        values.put(EventsEntry.COLUMN_EVENT_REMINDER_TIME, reminderTime);
        values.put(EventsEntry.COLUMN_EVENT_REPEAT, repeatDays);

        // Insert event into database
        context.getContentResolver().insert(EventsEntry.CONTENT_URI, values);


        // Setup the reminder
        Reminder reminder = new Reminder(reminderType, eventDateAndTime);
        reminder.createReminder(context, eventId);
        cursor.close();
    }
}
