package com.example.android.watsnext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
        setSupportActionBar(toolbar);

    }
}

//TODO: Create the Events class and database
//TODO: Check if recyclerView items are placed correctly inside the main screen
//TODO: FAB should add fake events to the database
//TODO: Add option to delete entire db from main screen
//TODO: Add empty state view
