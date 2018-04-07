package com.example.android.watsnext.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.example.android.watsnext.R;
import com.example.android.watsnext.activities.AddEventActivity;
import com.example.android.watsnext.data.EventContract.EventsEntry;

/**
 * Implementation of App Widget functionality.
 */
public class EventsWidget extends AppWidgetProvider {

    public static int mWidgetId;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Store the widget id into a global variable
        mWidgetId = appWidgetId;

        // The widget title
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_events);
        // Set the widget title
        views.setTextViewText(R.id.tv_widget_title, widgetText);

        // Query the events database
        Cursor cursor = context.getContentResolver().query(EventsEntry.CONTENT_URI, null, null, null, null);

        // Create an intent for the RemoteViews Factory
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Set the adapter for the widget collection
        views.setRemoteAdapter(R.id.widget_events_list, intent);


        // Set the Pending Intent template
        Intent appIntent = new Intent(context, AddEventActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_events_list, appPendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        if (cursor != null) {
            cursor.close();
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        switch (action) {
            case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_events);
                AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, EventsWidget.class), rv);
                break;
        }

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

