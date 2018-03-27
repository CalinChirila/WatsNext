package com.example.android.watsnext.widget;

import android.content.Context;
import android.database.Cursor;
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

    WidgetDataProvider(Context context){
        mContext = context;

        Log.v("IMPORTANT", "RemoteViewsFactory constructor called");

        String[] projection = new String[]{
                EventsEntry.COLUMN_EVENT_TYPE,
                EventsEntry.COLUMN_EVENT_TEXT,
                EventsEntry.COLUMN_EVENT_DATE,
                EventsEntry.COLUMN_EVENT_TIME
        };
        String sortOrder = EventsEntry.COLUMN_EVENT_DATE_AND_TIME + " ASC";
        mCursor = mContext.getContentResolver().query(EventsEntry.CONTENT_URI, projection, null, null, sortOrder);
    }

    @Override
    public void onCreate() {
        Log.v("TAG", "RemoteViewsFactory onCreate called");
    }

    @Override
    public void onDataSetChanged() {
        mCursor.moveToPosition(0);
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
        return 3;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_event_item_layout);

        mCursor.moveToPosition(position);
        String eventDate = EventUtils.convertEventDateToString(mContext, mCursor.getLong(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_DATE)));
        String eventTime = EventUtils.convertEventTimeToString(mCursor.getLong(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TIME)));

        String eventText = mCursor.getString(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TEXT));
        if(eventText == null || TextUtils.isEmpty(eventText)){
            // Replace event text with the event type
            eventText = EventUtils.convertEventTypeToString(mContext, mCursor.getInt(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TYPE)));
        }

        rv.setTextViewText(R.id.tv_widget_event_date, eventDate);
        rv.setTextViewText(R.id.tv_widget_event_time, eventTime);
        rv.setTextViewText(R.id.tv_widget_event_description, eventText);

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
