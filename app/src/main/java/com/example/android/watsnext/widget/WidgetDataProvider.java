package com.example.android.watsnext.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.watsnext.R;
import com.example.android.watsnext.activities.AddEventActivity;
import com.example.android.watsnext.data.EventContract.EventsEntry;
import com.example.android.watsnext.utils.EventUtils;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;
    private static final int REQUEST_FROM_WIDGET = 142;
    private static final String EXTRA_WIDGET_ITEM_POSITION = "widgetItemPosition";

    private String[] projection = new String[]{
            EventsEntry.COLUMN_EVENT_TYPE,
            EventsEntry.COLUMN_EVENT_TEXT,
            EventsEntry.COLUMN_EVENT_DATE,
            EventsEntry.COLUMN_EVENT_TIME
    };
    private String sortOrder = EventsEntry.COLUMN_EVENT_DATE_AND_TIME + " ASC";

    WidgetDataProvider(Context context){
        mContext = context;
        Log.v("IMPORTANT", "RemoteViewsFactory constructor called");
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
        if(mCursor != null){
            mCursor.close();
            mCursor = null;
        }
    }

    @Override
    public int getCount() {
        // We will only show the upcoming 3 events
        if(mCursor.getCount() >= 3){
            return 3;
        } else {
            return mCursor.getCount();
        }

    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_event_item_layout);
        String eventDate;
        String eventTime;
        String eventText;
        // Move the cursor to the current position and extract the event information
        if(mCursor.moveToPosition(position)) {
            eventDate = EventUtils.convertEventDateToString(mContext, mCursor.getLong(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_DATE)));
            eventTime = EventUtils.convertEventTimeToString(mCursor.getLong(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TIME)));

            eventText = mCursor.getString(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TEXT));
            if (eventText == null || TextUtils.isEmpty(eventText)) {
                // Replace event text with the event type
                eventText = EventUtils.convertEventTypeToString(mContext, mCursor.getInt(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TYPE)));
            }
        } else {
            eventDate = "";
            eventTime = "";
            eventText = mContext.getString(R.string.no_more_events);
        }

        rv.setTextViewText(R.id.tv_widget_event_date, eventDate);
        rv.setTextViewText(R.id.tv_widget_event_time, eventTime);
        rv.setTextViewText(R.id.tv_widget_event_description, eventText);

        Intent launchEventDetailsIntent = new Intent(mContext, AddEventActivity.class);
        launchEventDetailsIntent.setAction(EventsWidget.ACTION_WIDGET_ITEM);
        rv.setOnClickFillInIntent(R.id.widget_list_item, launchEventDetailsIntent);

        return rv;

    }

    @Override
    public RemoteViews getLoadingView() {
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
