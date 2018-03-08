package com.example.android.watsnext.Activities;

import android.content.ContentValues;
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
import android.widget.TextView;

import com.example.android.watsnext.Adapters.EventTypesAdapter;
import com.example.android.watsnext.R;
import com.example.android.watsnext.Utils.DatePickerUtils;
import com.example.android.watsnext.Utils.EventUtils;
import com.example.android.watsnext.Utils.TimePickerUtils;
import com.example.android.watsnext.data.EventContract.EventsEntry;

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
    @BindView(R.id.tv_date_picker_day)
            TextView mDayTextView;
    @BindView(R.id.tv_date_picker_month)
            TextView mMonthTextView;
    @BindView(R.id.tv_date_picker_year)
            TextView mYearTextView;
    @BindView(R.id.iv_day_plus_button)
            ImageView mDayPlusButton;
    @BindView(R.id.iv_day_minus_button)
            ImageView mDayMinusButton;
    @BindView(R.id.iv_month_plus_button)
            ImageView mMonthPlusButton;
    @BindView(R.id.iv_month_minus_button)
            ImageView mMonthMinusButton;
    @BindView(R.id.iv_year_plus_button)
            ImageView mYearPlusButton;
    @BindView(R.id.iv_year_minus_button)
            ImageView mYearMinusButton;
    @BindView(R.id.tv_time_picker_hour)
            TextView mHourTextView;
    @BindView(R.id.tv_time_picker_minute)
            TextView mMinuteTextView;
    @BindView(R.id.tv_time_picker_ampm)
            TextView mAmPmTextView;
    @BindView(R.id.iv_hour_plus_button)
            ImageView mHourPlusButton;
    @BindView(R.id.iv_hour_minus_button)
            ImageView mHourMinusButton;
    @BindView(R.id.iv_minute_plus_button)
            ImageView mMinutePlusButton;
    @BindView(R.id.iv_minute_minus_button)
            ImageView mMinuteMinusButton;
    @BindView(R.id.iv_ampm_plus_button)
            ImageView mAmPmPlusButton;
    @BindView(R.id.iv_ampm_minus_button)
            ImageView mAmPmMinusButton;



    EventTypesAdapter mEventTypesAdapter;
    private boolean eventTypesAreVisible = false;

    private int mEventTypeInt;
    private String mEventTypeString;
    private String mEventText;
    private long mEventDate;
    private long mEventTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);


        //TODO: add content transitions between activities. when add event fab is pushed, move it to the correct place and morph the icon
        //TODO: transition between activities should be also morph-ish

        if(getIntent().getExtras() != null){
            // User clicked on an existing event
            // TODO: populate the event fields with corresponding data
            toolbar.setTitle(R.string.edit_event);
        } else {
            // User clicked the add event button
            toolbar.setTitle(R.string.add_event);

            // Initialize date picker with current date
            DatePickerUtils.setDatePickerDefaults(mDayTextView, mMonthTextView, mYearTextView);

            // Initialize time picker with current time
            TimePickerUtils.setTimePickerDefaults(mHourTextView, mMinuteTextView, mAmPmTextView);
        }

        View.OnClickListener buttonClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int buttonId = v.getId();
                switch(buttonId){
                    case R.id.button_show_event_types:
                        if(!eventTypesAreVisible) {
                            showEventTypes();
                        } else {
                            hideEventTypes();
                        }
                        break;
                    case R.id.fab_save_event:
                        // Gather event data
                        mEventText = mEventEditText.getText().toString();

                        mEventDate = DatePickerUtils.getDateInMillis();
                        EventUtils.convertEventDateToString(getApplicationContext(), mEventDate);

                        mEventTime = TimePickerUtils.getTimeInMillis();
                        EventUtils.convertEventTimeToString(mEventTime);

                        addEventToDatabase();
                        finish();

                        //TODO: before adding the event into the db, validate data: check if event date is in future!
                        break;
                    case R.id.iv_day_plus_button:
                        DatePickerUtils.increaseDay(mDayTextView);
                        break;
                    case R.id.iv_day_minus_button:
                        DatePickerUtils.decreaseDay(mDayTextView);
                        break;
                    case R.id.iv_month_plus_button:
                        DatePickerUtils.increaseMonth(mMonthTextView);
                        break;
                    case R.id.iv_month_minus_button:
                        DatePickerUtils.decreaseMonth(mMonthTextView);
                        break;
                    case R.id.iv_year_plus_button:
                        DatePickerUtils.increaseYear(mYearTextView);
                        break;
                    case R.id.iv_year_minus_button:
                        DatePickerUtils.decreaseYear(mYearTextView);
                        break;
                    case R.id.iv_hour_plus_button:
                        TimePickerUtils.increaseHour(mHourTextView);
                        break;
                    case R.id.iv_hour_minus_button:
                        TimePickerUtils.decreaseHour(mHourTextView);
                        break;
                    case R.id.iv_minute_plus_button:
                        TimePickerUtils.increaseMinute(mMinuteTextView);
                        break;
                    case R.id.iv_minute_minus_button:
                        TimePickerUtils.decreaseMinute(mMinuteTextView);
                        break;
                    case R.id.iv_ampm_minus_button:
                    case R.id.iv_ampm_plus_button:
                        TimePickerUtils.switchAmPm(mAmPmTextView);
                        break;
                }
            }
        };

        mDayPlusButton.setOnClickListener(buttonClickListener);
        mDayMinusButton.setOnClickListener(buttonClickListener);
        mMonthPlusButton.setOnClickListener(buttonClickListener);
        mMonthMinusButton.setOnClickListener(buttonClickListener);
        mYearPlusButton.setOnClickListener(buttonClickListener);
        mYearMinusButton.setOnClickListener(buttonClickListener);
        mShowEventsButton.setOnClickListener(buttonClickListener);
        mSaveEventButton.setOnClickListener(buttonClickListener);
        mHourPlusButton.setOnClickListener(buttonClickListener);
        mHourMinusButton.setOnClickListener(buttonClickListener);
        mMinutePlusButton.setOnClickListener(buttonClickListener);
        mMinuteMinusButton.setOnClickListener(buttonClickListener);
        mAmPmPlusButton.setOnClickListener(buttonClickListener);
        mAmPmMinusButton.setOnClickListener(buttonClickListener);

        //Set the toolbar
        setSupportActionBar(toolbar);



        setupEventTypesRecyclerView();



    }

    private void addEventToDatabase(){
        ContentValues values = new ContentValues();
        values.put(EventsEntry.COLUMN_EVENT_TYPE, mEventTypeInt);
        values.put(EventsEntry.COLUMN_EVENT_TEXT, mEventText);
        values.put(EventsEntry.COLUMN_EVENT_DATE, mEventDate);
        values.put(EventsEntry.COLUMN_EVENT_TIME, mEventTime);
        values.put(EventsEntry.COLUMN_EVENT_DATE_AND_TIME, mEventDate + mEventTime);


        getContentResolver().insert(EventsEntry.CONTENT_URI, values);
    }

    @Override
    public void onEventTypeClick(int position) {
        // Set the button text to the selected option
        mEventTypeInt = position;
        mEventTypeString = EventUtils.convertEventTypeToString(getApplicationContext(), position);
        mShowEventsButton.setText(mEventTypeString);

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
