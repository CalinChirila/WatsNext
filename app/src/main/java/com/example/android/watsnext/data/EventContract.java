package com.example.android.watsnext.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Astraeus on 2/26/2018.
 */

//TODO: test if using Room is more efficient

public class EventContract {
    public static final String AUTHORITY = "com.example.android.watsnext.data";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_EVENTS = "events";

    public static final class EventsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_EVENTS)
                .build();

        public static final String TABLE_NAME = "events";

        public static final String COLUMN_EVENT_TYPE = "type";
        public static final String COLUMN_EVENT_TEXT = "text";
        public static final String COLUMN_EVENT_DATE = "date";
        public static final String COLUMN_EVENT_TIME = "time";
        public static final String COLUMN_EVENT_LOCATION = "location";
        public static final String COLUMN_EVENT_REPEAT = "repeat";
        public static final String COLUMN_EVENT_REMINDER = "reminder";
        public static final String COLUMN_EVENT_REMINDER_TIME = "reminderTime";
    }
}
