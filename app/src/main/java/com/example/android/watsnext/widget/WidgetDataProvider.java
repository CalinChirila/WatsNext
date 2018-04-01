package com.example.android.watsnext.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.watsnext.R;
import com.example.android.watsnext.data.EventContract.EventsEntry;
import com.example.android.watsnext.utils.EventUtils;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;

    private String[] projection = new String[]{
            EventsEntry._ID,
            EventsEntry.COLUMN_EVENT_TYPE,
            EventsEntry.COLUMN_EVENT_TEXT,
            EventsEntry.COLUMN_EVENT_DATE,
            EventsEntry.COLUMN_EVENT_TIME
    };
    private String sortOrder = EventsEntry.COLUMN_EVENT_DATE_AND_TIME + " ASC";

    WidgetDataProvider(Context context){
        mContext = context;
        mCursor = mContext.getContentResolver().query(EventsEntry.CONTENT_URI, projection, null, null, sortOrder);
    }

    @Override
    public void onCreate() {
        Log.v("TAG", "RemoteViewsFactory onCreate called");
    }

    @Override
    public void onDataSetChanged() {
        // Query the database for updated information
        mCursor = mContext.getContentResolver().query(EventsEntry.CONTENT_URI, projection, null, null, sortOrder);
    }

    @Override
    public void onDestroy() {
        // When the widget is destroyed, close the cursor
        if(mCursor != null){
            mCursor.close();
            mCursor = null;
        }
    }

    @Override
    public int getCount() {
        // We will only show the upcoming 3 events maximum
        if(mCursor.getCount() >= 3){
            return 3;
        } else {
            return mCursor.getCount();
        }

    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Create the remote view for the widget collection item
        if(mCursor == null || mCursor.getCount() == 0) return null;

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_event_item_layout);

        // These variables contain the information to be displayed in the widget list item
        String eventDate = "";
        String eventTime = "";
        String eventText = "";
        int eventId = -1;

        // Move the cursor to the current position and extract the event information
        if(mCursor.moveToPosition(position)) {

            // Get the information from the database and convert it to readable strings
            eventDate = EventUtils.convertEventDateToString(mContext, mCursor.getLong(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_DATE)));
            eventTime = EventUtils.convertEventTimeToString(mCursor.getLong(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TIME)));
            eventText = mCursor.getString(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TEXT));
            eventId = mCursor.getInt(mCursor.getColumnIndex(EventsEntry._ID));

            // If the event text is not available, display the event type
            if (eventText == null || TextUtils.isEmpty(eventText)) {
                eventText = EventUtils.convertEventTypeToString(mContext, mCursor.getInt(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TYPE)));
            }
        }

        // Set the strings to the remote view
        rv.setTextViewText(R.id.tv_widget_event_date, eventDate);
        rv.setTextViewText(R.id.tv_widget_event_time, eventTime);
        rv.setTextViewText(R.id.tv_widget_event_description, eventText);

        Bundle extras = new Bundle();
        extras.putInt("extraEventId", eventId);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        // No loading view necessary
        return null;
    }

    @Override
    public int getViewTypeCount() {
        // We only have 1 type of view in the widget collection
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
