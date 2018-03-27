package com.example.android.watsnext.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.v("IMPORTANT","Remote Views service called");
        return new WidgetDataProvider(this.getApplicationContext());
    }
}
