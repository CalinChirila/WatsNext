package com.example.android.watsnext.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.watsnext.Adapters.EventTypesAdapter;
import com.example.android.watsnext.R;
import com.example.android.watsnext.Utils.EventUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEventActivity extends AppCompatActivity implements EventTypesAdapter.EventTypeClickHandler{
    @BindView(R.id.add_event_toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_event_types)
    RecyclerView mEventTypesRecyclerView;
    @BindView(R.id.button_show_event_types)
    Button mShowEventsButton;
    @BindView(R.id.iv_event_type_expand_arrow)
    ImageView mEventTypeExpandArrow;
    @BindView(R.id.et_event_text)
    EditText mEventEditText;
    @BindView(R.id.fab_save_event)
    FloatingActionButton mSaveEventButton;

    EventTypesAdapter mEventTypesAdapter;
    private boolean eventTypesAreVisible = false;
    private String mEventType;
    private String mEventText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);

        //TODO: this will change if the user clicked add event or edit event
        toolbar.setTitle("Add Event");
        //Set the toolbar
        setSupportActionBar(toolbar);

        setupEventTypesRecyclerView();

        mShowEventsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!eventTypesAreVisible) {
                    showEventTypes();
                } else {
                    hideEventTypes();
                }
            }
        });


        //TODO: setup the edit text and the save button
        mSaveEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mEventText = mEventEditText.getText().toString();
                Toast.makeText(getApplicationContext(), mEventText, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onEventTypeClick(int position) {
        // Set the button text to the selected option
        mEventType = EventUtils.convertEventTypeToString(getApplicationContext(), position);
        mShowEventsButton.setText(mEventType);

        hideEventTypes();
    }

    /**
     * Helper method for the setup of the RecyclerView
     */
    private void setupEventTypesRecyclerView(){
        mEventTypesAdapter = new EventTypesAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEventTypesRecyclerView.setHasFixedSize(true);
        mEventTypesRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Helper method for displaying the event types
     */
    private void showEventTypes(){
        // Make the recycler view visible
        mEventTypesRecyclerView.setVisibility(View.VISIBLE);

        // Set the adapter every time so the event types animation will start
        mEventTypesRecyclerView.setAdapter(mEventTypesAdapter);
        eventTypesAreVisible = true;

        // Animate the event types arrow
        mEventTypeExpandArrow.animate().rotationBy(180).setDuration(300).start();
    }

    /**
     * Helper method for hiding the event types
     */
    private void hideEventTypes(){
        mEventTypesRecyclerView.setVisibility(View.GONE);
        eventTypesAreVisible = false;
        mEventTypeExpandArrow.animate().rotationBy(180).setDuration(300).start();

    }
}
