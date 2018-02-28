package com.example.android.watsnext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEventActivity extends AppCompatActivity {
    @BindView(R.id.add_event_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);

        //TODO: this will change if the user clicked add event or edit event
        toolbar.setTitle("Add Event");
        //Set the toolbar
        setSupportActionBar(toolbar);

    }
}
