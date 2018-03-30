package com.example.android.watsnext.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.android.watsnext.R;
import com.example.android.watsnext.activities.AddEventActivity;
import com.example.android.watsnext.data.EventContract.EventsEntry;

/**
 * Implementation of App Widget functionality.
 */
public class EventsWidget extends AppWidgetProvider {

    static RemoteViews views;
    static int mWidgetId;

    public static final String ACTION_WIDGET_ITEM = "com.example.android.watsnext.widget.EventsWidget.ACTION_WIDGET_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        mWidgetId = appWidgetId;

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.widget_events);
        views.setTextViewText(R.id.tv_widget_title, widgetText);

        Cursor cursor = context.getContentResolver().query(EventsEntry.CONTENT_URI, null, null, null, null);
        if(cursor != null && cursor.getCount() != 0) {

            cursor.moveToFirst();

            Intent intent = new Intent(context, WidgetService.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            views.setRemoteAdapter(R.id.widget_events_list, intent);

            // Set the Pending Intent template
            Intent widgetItemIntent = new Intent(context, AddEventActivity.class);
            // TODO: add correct information for selected event in the widget
            widgetItemIntent.setAction(ACTION_WIDGET_ITEM);
            widgetItemIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent widgetItemPendingIntent = PendingIntent.getBroadcast(context, 0, widgetItemIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_events_list, widgetItemPendingIntent);


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            cursor.close();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        switch(action){
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

