package com.example.android.watsnext.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.watsnext.data.EventContract.EventsEntry;

/**
 * Created by Astraeus on 2/26/2018.
 */

public class EventDbHelper extends SQLiteOpenHelper {
    //TODO: might create another table for past events, with less information (only type, text and date)

    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 2;

    // The SQL statement for creating the events table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EventsEntry.TABLE_NAME + " ("
                    + EventsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + EventsEntry.COLUMN_EVENT_TYPE + " INTEGER NOT NULL DEFAULT 0, "
                    + EventsEntry.COLUMN_EVENT_TEXT + " TEXT, "
                    + EventsEntry.COLUMN_EVENT_DATE + " INTEGER NOT NULL, "
                    + EventsEntry.COLUMN_EVENT_TIME + " INTEGER, "
                    + EventsEntry.COLUMN_EVENT_DATE_AND_TIME + " INTEGER, "
                    + EventsEntry.COLUMN_EVENT_LOCATION + " TEXT, "
                    + EventsEntry.COLUMN_EVENT_REPEAT + " TEXT, "
                    + EventsEntry.COLUMN_EVENT_REMINDER + " INTEGER DEFAULT 0, "
                    + EventsEntry.COLUMN_EVENT_REMINDER_TIME + " INTEGER);";

    // The SQL statement for deleting the events table
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + EventsEntry.TABLE_NAME;

    // The constructor
    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
