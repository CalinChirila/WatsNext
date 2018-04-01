package com.example.android.watsnext.widget;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.watsnext.activities.AddEventActivity;

public class WidgetItemService extends IntentService {

    public WidgetItemService() {
        super("WidgetItemService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            String action = intent.getAction();
            switch(action){
                case EventsWidget.ACTION_WIDGET_ITEM:
                    handleWidgetItemClick();
                    break;
            }
        }

    }

    private void handleWidgetItemClick(){
        Intent addEventActivityIntent = new Intent(this, AddEventActivity.class);
        this.startActivity(addEventActivityIntent);
    }
}
