package com.example.android.watsnext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.watsnext.data.EventContract.EventsEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsList extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        ButterKnife.bind(this);


        // Setup the toolbar
        toolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int itemID = menuItem.getItemId();
        switch(itemID){
            case R.id.menu_delete_all_events:
                getContentResolver().delete(EventsEntry.CONTENT_URI, null, null);
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}

//TODO: Check if recyclerView items are placed correctly inside the main screen
//TODO: FAB should add fake events to the database
//TODO: Add option to delete entire db from main screen
//TODO: Add empty state view
