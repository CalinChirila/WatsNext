package com.example.android.watsnext.activities;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.watsnext.R;
import com.example.android.watsnext.adapters.EventsAdapter;
import com.example.android.watsnext.data.EventContract.EventsEntry;
import com.example.android.watsnext.utils.EventUtils;
import com.example.android.watsnext.utils.Reminder;
import com.example.android.watsnext.utils.RepeaterTextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, EventsAdapter.EventClickHandler {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_add_event)
    FloatingActionButton mAddEventButton;
    @BindView(R.id.rv_events_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_state)
    TextView mEmptyState;
    @BindView(R.id.ad_view_banner)
    AdView mAdView;

    public Cursor mCursor;
    private EventsAdapter mAdapter;
    private LoaderManager mEventsLoaderManager;

    private static final int EVENTS_LOADER_ID = 1337;

    public static final String EXTRA_EVENT_ID = "eventID";
    public static final String EXTRA_EVENT_TYPE = "eventType";
    public static final String EXTRA_EVENT_TEXT = "eventText";
    public static final String EXTRA_EVENT_DATE = "eventDate";
    public static final String EXTRA_EVENT_TIME = "eventTime";
    public static final String EXTRA_EVENT_LOCATION = "eventLocation";
    public static final String EXTRA_EVENT_REPEAT = "eventRepeat";
    public static final String EXTRA_EVENT_REMINDER = "eventReminder";
    public static final String EXTRA_EVENT_REMINDER_TIME = "eventReminderTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        ButterKnife.bind(this);

        // Load the add into the events list activity
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Setup the toolbar
        toolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(toolbar);

        // Setup the Add Event Button
        mAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the add event activity
                Intent intent = new Intent(EventsListActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });

        mEventsLoaderManager = getSupportLoaderManager();
        mEventsLoaderManager.initLoader(EVENTS_LOADER_ID, null, this);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new EventsAdapter(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Helper method that shows the empty state and hides the RecyclerView
     */
    private void showEmptyState(){
        mEmptyState.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    /**
     * Helper method that shows the RecyclerView and hides the empty state
     */
    private void hideEmptyState(){
        mEmptyState.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemID = menuItem.getItemId();
        switch (itemID) {
            case R.id.menu_delete_all_events:
                getContentResolver().delete(EventsEntry.CONTENT_URI, null, null);
                Reminder.cancelReminder();
                RepeaterTextView.resetRepeatDays();
                mEventsLoaderManager.restartLoader(EVENTS_LOADER_ID, null, EventsListActivity.this);
                showEmptyState();
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this) {

            @Override
            public void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                String sortOrder = EventsEntry.COLUMN_EVENT_DATE_AND_TIME + " ASC";
                mCursor = getContentResolver().query(EventsEntry.CONTENT_URI, null, null, null, sortOrder);
                return mCursor;
            }

            @Override
            public void deliverResult(Cursor data) {
                mCursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // If data is empty, show empty state view
        if (data.getCount() == 0) {
            showEmptyState();
        } else {
            deletePastEvents(data);
            setupRecyclerView();
            mAdapter.setEventsData(this, data);
            hideEmptyState();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onEventClick(int position) {
        // Gather event data for the clicked item and build an intent with it

        Intent eventDetailsIntent = createEventDetailsIntent(position);

        // Launch the activity
        startActivity(eventDetailsIntent);
    }

    /**
     * Helper method that creates an intent filled with the event information
     * @param position => the position in the list that was clicked
     * @return => the intent for the details activity(AddEventActivity)
     */
    private Intent createEventDetailsIntent(int position){
        // Move cursor to the clicked position
        mCursor.moveToPosition(position);

        // Get only the information needed for display and event ID
        // The event id will be used when the save event button is clicked,
        // But instead of adding a new event, we will update this one
        int eventID = mCursor.getInt(mCursor.getColumnIndex(EventsEntry._ID));
        String eventType = EventUtils.convertEventTypeToString(this, mCursor.getInt(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TYPE)));
        String eventText = mCursor.getString(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TEXT));
        long eventDate = mCursor.getLong(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_DATE));
        long eventTime = mCursor.getLong(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TIME));
        String eventLocation = mCursor.getString(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_LOCATION));
        String eventRepeat = mCursor.getString(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_REPEAT));
        int eventReminder = mCursor.getInt(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_REMINDER));
        long eventReminderTime = mCursor.getLong(mCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_REMINDER_TIME));

        Intent eventDetailsIntent = new Intent(EventsListActivity.this, AddEventActivity.class);

        eventDetailsIntent.putExtra(EXTRA_EVENT_ID, eventID);
        eventDetailsIntent.putExtra(EXTRA_EVENT_TYPE, eventType);
        eventDetailsIntent.putExtra(EXTRA_EVENT_TEXT, eventText);
        eventDetailsIntent.putExtra(EXTRA_EVENT_DATE, eventDate);
        eventDetailsIntent.putExtra(EXTRA_EVENT_TIME, eventTime);
        eventDetailsIntent.putExtra(EXTRA_EVENT_LOCATION, eventLocation);
        eventDetailsIntent.putExtra(EXTRA_EVENT_REPEAT, eventRepeat);
        eventDetailsIntent.putExtra(EXTRA_EVENT_REMINDER, eventReminder);
        eventDetailsIntent.putExtra(EXTRA_EVENT_REMINDER_TIME, eventReminderTime);

        //TODO: delete gmt todos after debug

        return eventDetailsIntent;
    }

    /**
     * Delete events that are in the past
     * @param cursor - the db cursor
     */
    private void deletePastEvents(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) return;

        Calendar calendar = Calendar.getInstance();
        long currentDateAndTime = calendar.getTimeInMillis();

        while (cursor.moveToNext()) {
            long eventDateAndTime = cursor.getLong(cursor.getColumnIndex(EventsEntry.COLUMN_EVENT_DATE_AND_TIME));
            // Take GMT into consideration
            eventDateAndTime = eventDateAndTime - (calendar.getTimeZone().getRawOffset() / 60 * 60 * 1000); // TODO: gmt here
            if (eventDateAndTime < currentDateAndTime) {
                Log.v("IMPORTANT", "Delete");
                long eventId = cursor.getLong(cursor.getColumnIndex(EventsEntry._ID));
                Uri pastEventUri = ContentUris.withAppendedId(EventsEntry.CONTENT_URI, eventId);
                String selection = EventsEntry._ID + "=?";
                String[] selectionArgs = {String.valueOf(eventId)};
                getContentResolver().delete(pastEventUri, selection, selectionArgs);
            }
        }
    }
}


//TODO: Improve the visuals
