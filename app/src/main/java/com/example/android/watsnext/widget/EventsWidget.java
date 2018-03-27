package com.example.android.watsnext.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.watsnext.R;
import com.example.android.watsnext.data.EventContract.EventsEntry;
import com.example.android.watsnext.utils.EventUtils;

/**
 * Implementation of App Widget functionality.
 */
public class EventsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_events);
        views.setTextViewText(R.id.tv_widget_title, widgetText);


        //DEBUG SECTION -------------------------------------------------------------------------
        Cursor cursor = context.getContentResolver().query(EventsEntry.CONTENT_URI, null, null, null, null);
        int size = cursor.getCount();
        cursor.moveToFirst();
        String eventTime = EventUtils.convertEventTimeToString(cursor.getLong(cursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TIME)));

        Log.v("IMPORTANT", String.valueOf(size) + " " + eventTime);

        //---------------------------------------------------------------------------------------


        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra("cursorSize", size);
        views.setRemoteAdapter(R.id.widget_events_list, intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        cursor.close();
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_events_list);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

