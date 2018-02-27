package com.example.android.watsnext;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.watsnext.data.EventContract.EventsEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsList extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_add_event)
    FloatingActionButton mAddEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        ButterKnife.bind(this);


        // Setup the toolbar
        toolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(toolbar);

        // Setup the Add Event Button
        mAddEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // PLACEHOLDER CODE ----------------------------------------------------------------
                addDummyData();
                Cursor cursor = getContentResolver().query(EventsEntry.CONTENT_URI, null, null, null, null);
                Toast.makeText(getApplicationContext(), "Items in database: " + cursor.getCount(), Toast.LENGTH_LONG).show();
                cursor.close();
                // ---------------------------------------------------------------------------------
            }
        });

    }

    /**
     * Helper method that adds dummy data to the database
     * TODO: must delete before release
     */
    private void addDummyData(){
        int eventType = 1;
        long eventDate = 765132478;
        ContentValues values = new ContentValues();
        values.put(EventsEntry.COLUMN_EVENT_TYPE, eventType);
        values.put(EventsEntry.COLUMN_EVENT_DATE, eventDate);
        getContentResolver().insert(EventsEntry.CONTENT_URI, values);
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
//TODO: Create recycler view adapter
//TODO: Check if recyclerView items are placed correctly inside the main screen
//TODO: FAB should add fake events to the database
//TODO: Add option to delete entire db from main screen
//TODO: Add empty state view
