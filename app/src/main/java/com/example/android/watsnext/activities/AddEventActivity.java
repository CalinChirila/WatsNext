package com.example.android.watsnext.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.android.watsnext.adapters.EventTypesAdapter;
import com.example.android.watsnext.R;
import com.example.android.watsnext.utils.DatePickerUtils;
import com.example.android.watsnext.utils.EventUtils;
import com.example.android.watsnext.utils.Reminder;
import com.example.android.watsnext.utils.RepeaterTextView;
import com.example.android.watsnext.utils.TimePickerUtils;
import com.example.android.watsnext.data.EventContract.EventsEntry;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEventActivity extends AppCompatActivity implements EventTypesAdapter.EventTypeClickHandler {
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
    @BindView(R.id.et_event_location)
    EditText mEventLocationEditText;
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
    @BindView(R.id.iv_pick_date_button)
    ImageView mCalendarButton;
    @BindView(R.id.iv_map_button)
    ImageView mMapButton;
    @BindView(R.id.rb_noReminder)
    RadioButton mNoReminderButton;
    @BindView(R.id.rb_notification)
    RadioButton mNotificationReminderButton;
    @BindView(R.id.rb_alarm)
    RadioButton mAlarmNotificationButton;
    @BindView(R.id.reminder_time_picker_layout)
    ConstraintLayout mReminderTimePickerLayout;
    @BindView(R.id.tv_reminder_text)
    TextView mReminderTextView;
    @BindView(R.id.iv_reminder_days_plus_button)
    ImageView mReminderDaysPlusButton;
    @BindView(R.id.iv_reminder_days_minus_button)
    ImageView mReminderDaysMinusButton;
    @BindView(R.id.iv_reminder_hours_plus_button)
    ImageView mReminderHoursPlusButton;
    @BindView(R.id.iv_reminder_hours_minus_button)
    ImageView mReminderHoursMinusButton;
    @BindView(R.id.iv_reminder_minutes_plus_button)
    ImageView mReminderMinutesPlusButton;
    @BindView(R.id.iv_reminder_minutes_minus_button)
    ImageView mReminderMinutesMinusButton;
    @BindView(R.id.tv_reminder_time_picker_day)
    TextView mReminderDayTextView;
    @BindView(R.id.tv_reminder_time_picker_hour)
    TextView mReminderHourTextView;
    @BindView(R.id.tv_reminder_time_picker_minutes)
    TextView mReminderMinuteTextView;


    EventTypesAdapter mEventTypesAdapter;
    private boolean eventTypesAreVisible = false;
    private boolean isAlarmPermissionGranted = false;

    private int mEventID;
    private int mEventTypeInt;
    public static String mEventTypeString;
    public static String mEventText;
    private String mEventLocation;
    private long mEventDate;
    private long mEventTime;
    private long mEventDateAndTime;
    private ArrayList<Integer> mRepeatDays;
    private String mRepeatDaysString;
    private int mEventReminderType = 0; // 0 => no reminder, 1 => notification; 2 => alarm
    private Reminder mReminder;
    private long mEventReminderTime;

    public static final int REQUEST_CODE_CALENDAR = 198;
    public static final int REQUEST_CODE_MAP = 272;
    public static final int REQUEST_PERMISSION_ALARM = 999;

    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);
        // Set the toolbar
        setSupportActionBar(toolbar);
        // Set back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Change the back button color to white
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        //TODO: add content transitions between activities. when add event fab is pushed, move it to the correct place and morph the icon
        //TODO: transition between activities should be also morph-ish
        mIntent = getIntent();

        if (mIntent.hasExtra(EventsListActivity.EXTRA_EVENT_ID)) {
            // User clicked on an existing event
            // TODO: populate the event fields with corresponding data
            toolbar.setTitle(R.string.edit_event);
            extractEventInformation();

            mShowEventsButton.setText(mEventTypeString);
            mEventEditText.setText(mEventText);
            mEventLocationEditText.setText(mEventLocation);

            DatePickerUtils.setDatePickerAtDate(mEventDate, mDayTextView, mMonthTextView, mYearTextView);
            TimePickerUtils.setTimePickerAtTime(mEventTime, mHourTextView, mMinuteTextView, mAmPmTextView);

            setRepeatDaysInterface(mRepeatDaysString);
            setReminderChoiceInterface(mEventReminderType);
            setReminderValues(mEventReminderTime);


            // TODO: finish populating the repeater and reminder and modify save to update event in the db
            // TODO: find out why event IDs are 70+

        } else {
            // User clicked the add event button
            toolbar.setTitle(R.string.add_event);

            // Initialize date picker with current date
            DatePickerUtils.setDatePickerDefaults(mDayTextView, mMonthTextView, mYearTextView);

            // Initialize time picker with current time
            TimePickerUtils.setTimePickerDefaults(mHourTextView, mMinuteTextView, mAmPmTextView);
        }


        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buttonId = v.getId();
                switch (buttonId) {
                    case R.id.button_show_event_types:
                        if (!eventTypesAreVisible) {
                            showEventTypes();
                        } else {
                            hideEventTypes();
                        }
                        break;
                    case R.id.fab_save_event:
                        // Gather event data
                        mEventText = mEventEditText.getText().toString();
                        mEventLocation = mEventLocationEditText.getText().toString();

                        mEventDate = DatePickerUtils.getDateInMillis();
                        EventUtils.convertEventDateToString(getApplicationContext(), mEventDate);

                        mEventTime = TimePickerUtils.getTimeInMillis();
                        EventUtils.convertEventTimeToString(mEventTime);

                        mEventDateAndTime = mEventDate + mEventTime;
                        mRepeatDays = RepeaterTextView.getRepeatDays();

                        // ADD this to db, when getting predetermined repeat days, check if i from 0 to 7 is contained in the string
                        String repeatDaysString = mRepeatDays.toString();
                        Log.v("IMPORTANT", repeatDaysString);

                        setupEventReminder();

                        addEventToDatabase();
                        setupRepeatedEvents();

                        finish();

                        //TODO: before adding the event into the db, validate data: check if event date is in future!
                        //TODO: The list of events will need to be updated with new data every day at midnight!
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
                    case R.id.iv_pick_date_button:
                        Intent calendarIntent = new Intent(AddEventActivity.this, CalendarActivity.class);
                        startActivityForResult(calendarIntent, REQUEST_CODE_CALENDAR);
                        break;
                    case R.id.iv_map_button:
                        Intent mapIntent = new Intent(AddEventActivity.this, MapsActivity.class);
                        startActivityForResult(mapIntent, REQUEST_CODE_MAP);
                        break;
                    case R.id.iv_reminder_days_plus_button:
                        Reminder.increaseReminderDays(mReminderDayTextView);
                        break;
                    case R.id.iv_reminder_days_minus_button:
                        Reminder.decreaseReminderDays(mReminderDayTextView);
                        break;
                    case R.id.iv_reminder_hours_plus_button:
                        Reminder.increaseReminderHours(mReminderHourTextView);
                        break;
                    case R.id.iv_reminder_hours_minus_button:
                        Reminder.decreaseReminderHours(mReminderHourTextView);
                        break;
                    case R.id.iv_reminder_minutes_plus_button:
                        Reminder.increaseReminderMinutes(mReminderMinuteTextView);
                        break;
                    case R.id.iv_reminder_minutes_minus_button:
                        Reminder.decreaseReminderMinutes(mReminderMinuteTextView);
                        break;
                    case R.id.rb_noReminder:
                        // If the user chooses to have no reminder, hide the reminder time picker layout
                        //TODO: add animations for reminder options
                        if (mReminderTimePickerLayout.getVisibility() == View.VISIBLE) {
                            mReminderTimePickerLayout.setVisibility(View.GONE);
                            mReminderTextView.setVisibility(View.GONE);
                        }

                        mEventReminderType = 0;

                        break;
                    case R.id.rb_notification:
                        if (mReminderTimePickerLayout.getVisibility() == View.GONE) {
                            showReminderTimeInterface();
                        }

                        mEventReminderType = 1;

                        break;
                    case R.id.rb_alarm:
                        // Request alarm permission
                        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SET_ALARM)
                                != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(AddEventActivity.this, new String[]{Manifest.permission.SET_ALARM}, REQUEST_PERMISSION_ALARM);
                        }

                        // Show the Reminder UI
                        if (mReminderTimePickerLayout.getVisibility() == View.GONE) {
                            showReminderTimeInterface();
                        }

                        // Set the type of reminder
                        mEventReminderType = 2;

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
        mCalendarButton.setOnClickListener(buttonClickListener);
        mMapButton.setOnClickListener(buttonClickListener);
        mReminderDaysPlusButton.setOnClickListener(buttonClickListener);
        mReminderDaysMinusButton.setOnClickListener(buttonClickListener);
        mReminderHoursPlusButton.setOnClickListener(buttonClickListener);
        mReminderHoursMinusButton.setOnClickListener(buttonClickListener);
        mReminderMinutesPlusButton.setOnClickListener(buttonClickListener);
        mReminderMinutesMinusButton.setOnClickListener(buttonClickListener);
        mNoReminderButton.setOnClickListener(buttonClickListener);
        mNotificationReminderButton.setOnClickListener(buttonClickListener);
        mAlarmNotificationButton.setOnClickListener(buttonClickListener);

    }

    private void setupEventReminder(){

            mReminder = new Reminder(mEventReminderType, mEventDateAndTime);

            int reminderDays;
            int reminderHours;
            int reminderMinutes;

            // Default the reminder values to 0 if the corresponding TextViews are empty.
            // Otherwise, get the int value from them.
            if (TextUtils.isEmpty(mReminderDayTextView.getText())) {
                reminderDays = 0;
            } else {
                reminderDays = Integer.parseInt(mReminderDayTextView.getText().toString());
            }

            if (TextUtils.isEmpty(mReminderHourTextView.getText())) {
                reminderHours = 0;
            } else {
                reminderHours = Integer.parseInt(mReminderHourTextView.getText().toString());
            }

            if (TextUtils.isEmpty(mReminderMinuteTextView.getText())) {
                reminderMinutes = 0;
            } else {
                reminderMinutes = Integer.parseInt(mReminderMinuteTextView.getText().toString());
            }

            mReminder.setReminderDays(reminderDays);
            mReminder.setReminderHours(reminderHours);
            mReminder.setReminderMinutes(reminderMinutes);

            mEventReminderTime = (reminderDays * DatePickerUtils.MILLIS_IN_A_DAY) + (reminderHours * 3600 * 1000) + (reminderMinutes * 60 * 1000);

            mReminder.createReminder(getApplicationContext());

    }

    /**
     * If the user clicked on an event in the list, extract all the event information from that intent
     */
    private void extractEventInformation(){
        mEventID = mIntent.getIntExtra(EventsListActivity.EXTRA_EVENT_ID, -1);
        mEventTypeString = mIntent.getStringExtra(EventsListActivity.EXTRA_EVENT_TYPE);
        mEventText = mIntent.getStringExtra(EventsListActivity.EXTRA_EVENT_TEXT);
        mEventDate = mIntent.getLongExtra(EventsListActivity.EXTRA_EVENT_DATE, -1);
        mEventTime = mIntent.getLongExtra(EventsListActivity.EXTRA_EVENT_TIME, -1);
        mEventLocation = mIntent.getStringExtra(EventsListActivity.EXTRA_EVENT_LOCATION);
        mEventReminderType = mIntent.getIntExtra(EventsListActivity.EXTRA_EVENT_REMINDER, -1);
        mEventReminderTime = mIntent.getIntExtra(EventsListActivity.EXTRA_EVENT_REMINDER_TIME, -1);
        mRepeatDaysString = mIntent.getStringExtra(EventsListActivity.EXTRA_EVENT_REPEAT);
        mEventReminderType = mIntent.getIntExtra(EventsListActivity.EXTRA_EVENT_REMINDER, -1);
        mEventReminderTime = mIntent.getLongExtra(EventsListActivity.EXTRA_EVENT_REMINDER_TIME, -1);
    }

    /**
     * Method for setting up the event that the user chose to repeat on certain days
     */
    private void setupRepeatedEvents() {
        ArrayList<Integer> eventRepeats = RepeaterTextView.getRepeatDays();

        // If the event doesn't repeat, exit early
        if (eventRepeats.size() == 0) return;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mEventDate);
        int eventDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;

        long repeatDate;

        // For every ticked box in the Repeat section, add another event with the corresponding date
        for (int i = 0; i < eventRepeats.size(); i++) {
            int dayOfWeekOfRepeatedEvent = eventRepeats.get(i);

            int numberOfDaysUntilEventRepeats = dayOfWeekOfRepeatedEvent - eventDayOfWeek;
            if (numberOfDaysUntilEventRepeats <= 0)
                numberOfDaysUntilEventRepeats = 7 + numberOfDaysUntilEventRepeats;

            repeatDate = mEventDate + (numberOfDaysUntilEventRepeats * DatePickerUtils.MILLIS_IN_A_DAY);

            EventUtils.convertEventDateToString(getApplicationContext(), repeatDate);
            addRepeatedEventToDatabase(repeatDate);
        }
    }

    /**
     * Add copies of the original event according to the repeat options
     *
     * @param repeatDate - the date at which the event will repeat
     */
    private void addRepeatedEventToDatabase(long repeatDate) {
        ContentValues values = new ContentValues();
        values.put(EventsEntry.COLUMN_EVENT_TYPE, mEventTypeInt);
        values.put(EventsEntry.COLUMN_EVENT_TEXT, mEventText);
        values.put(EventsEntry.COLUMN_EVENT_DATE, repeatDate);
        values.put(EventsEntry.COLUMN_EVENT_TIME, mEventTime);
        values.put(EventsEntry.COLUMN_EVENT_DATE_AND_TIME, repeatDate + mEventTime);
        values.put(EventsEntry.COLUMN_EVENT_LOCATION, mEventLocation);
        values.put(EventsEntry.COLUMN_EVENT_REMINDER, mEventReminderType);
        values.put(EventsEntry.COLUMN_EVENT_REMINDER_TIME, mEventReminderTime);
        values.put(EventsEntry.COLUMN_EVENT_REPEAT, mRepeatDays.toString());


        getContentResolver().insert(EventsEntry.CONTENT_URI, values);
    }

    /**
     * Add the event set up by the user to the database
     */
    private void addEventToDatabase() {
        ContentValues values = new ContentValues();
        values.put(EventsEntry.COLUMN_EVENT_TYPE, mEventTypeInt);
        values.put(EventsEntry.COLUMN_EVENT_TEXT, mEventText);
        values.put(EventsEntry.COLUMN_EVENT_DATE, mEventDate);
        values.put(EventsEntry.COLUMN_EVENT_TIME, mEventTime);
        values.put(EventsEntry.COLUMN_EVENT_DATE_AND_TIME, mEventDateAndTime);
        values.put(EventsEntry.COLUMN_EVENT_LOCATION, mEventLocation);
        values.put(EventsEntry.COLUMN_EVENT_REMINDER, mEventReminderType);
        values.put(EventsEntry.COLUMN_EVENT_REMINDER_TIME, mEventReminderTime);
        values.put(EventsEntry.COLUMN_EVENT_REPEAT, mRepeatDays.toString());


        getContentResolver().insert(EventsEntry.CONTENT_URI, values);
    }


    @Override
    public void onResume() {
        super.onResume();
        // Update information with data from calendar if any.
        setupEventTypesRecyclerView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CALENDAR:
                if (resultCode == Activity.RESULT_OK) {

                    Bundle calendarBundle = data.getBundleExtra(CalendarActivity.KEY_CALENDAR_BUNDLE);

                    int day = calendarBundle.getInt(CalendarActivity.KEY_DAY);
                    int month = calendarBundle.getInt(CalendarActivity.KEY_MONTH);
                    int year = calendarBundle.getInt(CalendarActivity.KEY_YEAR);

                    String dayString = String.valueOf(day);
                    String monthString = DatePickerUtils.convertMonthToString(month);
                    String yearString = String.valueOf(year);

                    DatePickerUtils.setEventDay(day);
                    DatePickerUtils.setEventMonth(month);
                    DatePickerUtils.setEventYear(year);

                    mDayTextView.setText(dayString);
                    mMonthTextView.setText(monthString);
                    mYearTextView.setText(yearString);
                }
                break;
            case REQUEST_CODE_MAP:
                // Set the location edit text with the obtained address
                if (resultCode == Activity.RESULT_OK) {
                    mEventLocation = data.getStringExtra(MapsActivity.EXTRA_ADDRESS);
                    mEventLocationEditText.setText(mEventLocation);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_PERMISSION_ALARM:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    isAlarmPermissionGranted = true;
                }
        }

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
    private void setupEventTypesRecyclerView() {
        mEventTypesAdapter = new EventTypesAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEventTypesRecyclerView.setHasFixedSize(true);
        mEventTypesRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Helper method for displaying the event types
     */
    private void showEventTypes() {
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
    private void hideEventTypes() {
        mEventTypesRecyclerView.setVisibility(View.GONE);
        eventTypesAreVisible = false;
        mEventTypeExpandArrow.animate().rotationBy(180).setDuration(300).start();

    }

    /**
     * This method gets called when a user clicks on an event in the list
     * Its purpose is to set the event repeater interface to the values the user selected for
     * that specific event
     * @param repeatDays - the repeat days array taken from the database
     * We will use a for loop to cycle through all the possible values ( 0 - 6)
     * If any value is found withing the provided String, get that RepeatTextView by the index and
     * alter its background color
     */
    private void setRepeatDaysInterface(String repeatDays){
        if(mRepeatDays == null) mRepeatDays = new ArrayList<>();
        for(int i = 0; i < 7; i++){

            if(repeatDays.contains(String.valueOf(i))){
                RepeaterTextView v = RepeaterTextView.getRepeaterViewAtIndex(AddEventActivity.this, i);
                v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                v.isSelected = true;
                mRepeatDays.add(i);
            }
        }
    }

    /**
     * Preset the reminder choice if the user clicked on an event
     */
    private void setReminderChoiceInterface(int reminderType){
        switch(reminderType){
            case 0:
                mNoReminderButton.setChecked(true);
                break;
            case 1:
                mNotificationReminderButton.setChecked(true);
                showReminderTimeInterface();
                break;
            case 2:
                mAlarmNotificationButton.setChecked(true);
                showReminderTimeInterface();
                break;
        }
    }

    private void setReminderValues(long timeInMillis){
        int days =(int) (timeInMillis / DatePickerUtils.MILLIS_IN_A_DAY);
        long hoursInMillis = timeInMillis - (days * DatePickerUtils.MILLIS_IN_A_DAY);
        int hours =(int) (hoursInMillis / 3600000);
        long minutesInMillis = hoursInMillis - (hours * 3600000);
        int minutes = (int) (minutesInMillis / 60000);

        mReminderDayTextView.setText(String.valueOf(days));
        mReminderHourTextView.setText(String.valueOf(hours));
        mReminderMinuteTextView.setText(String.valueOf(minutes));
    }

    private void showReminderTimeInterface(){
        mReminderTimePickerLayout.setVisibility(View.VISIBLE);
        mReminderTextView.setVisibility(View.VISIBLE);
    }
}
