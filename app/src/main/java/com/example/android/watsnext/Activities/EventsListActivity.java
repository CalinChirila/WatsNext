package com.example.android.watsnext.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.watsnext.Adapters.EventsAdapter;
import com.example.android.watsnext.R;
import com.example.android.watsnext.data.EventContract.EventsEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_add_event)
    FloatingActionButton mAddEventButton;
    @BindView(R.id.rv_events_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_state)
    TextView mEmptyState;

    public Cursor mCursor;
    private EventsAdapter mAdapter;
    private LoaderManager mEventsLoaderManager;

    private static final int EVENTS_LOADER_ID = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        ButterKnife.bind(this);

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
        mAdapter = new EventsAdapter();
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
                //TODO: if nothing is changed, just show data. Save the state of the cursor
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                //TODO: After implementing time picker, make sort order take time into consideration for events happening on the same day
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
            setupRecyclerView();
            mAdapter.setEventsData(this, data);
            hideEmptyState();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}


//TODO: Improve the visuals
