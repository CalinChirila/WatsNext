package com.example.android.watsnext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEventActivity extends AppCompatActivity {
    @BindView(R.id.add_event_toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_event_types)
    RecyclerView mEventTypesRecyclerView;
    @BindView(R.id.button_show_event_types)
    Button mShowEventsButton;

    EventTypesAdapter mEventTypesAdapter;
    private boolean eventTypesAreVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);

        //TODO: this will change if the user clicked add event or edit event
        toolbar.setTitle("Add Event");
        //Set the toolbar
        setSupportActionBar(toolbar);

        mEventTypesAdapter = new EventTypesAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEventTypesRecyclerView.setHasFixedSize(true);
        mEventTypesRecyclerView.setLayoutManager(layoutManager);



        /**
         * When the Event Type button is clicked:
         * 1. Make the recycler view visible
         * 2. Start the animation of the list items
         */
        mShowEventsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!eventTypesAreVisible) {
                    mEventTypesRecyclerView.setVisibility(View.VISIBLE);
                    mEventTypesRecyclerView.setAdapter(mEventTypesAdapter);
                    eventTypesAreVisible = true;

                } else {
                    mEventTypesRecyclerView.setVisibility(View.GONE);
                    eventTypesAreVisible = false;
                }
            }
        });
    }

}
