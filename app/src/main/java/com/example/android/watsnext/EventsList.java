package com.example.android.watsnext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        // Current date and time in millis
        long currentDate = Calendar.getInstance().getTimeInMillis();



        /* The input date should be stored as a string to be shown in text view
         * It should be converted into timeInMillis for calculations then add the equivalent
         * in millis of event time (if event time unknown, consider it 00:00)
         * Make 2 constants, MILLIS_IN_A_DAY = 86400000 millis and MILLIS_IN_TWO_DAYS = MILLIS_IN_A_DAY * 2
         * If input date - current date (in millis) <= MILLIS_IN_A_DAY => event is TODAY
         * If input date - current date (in millis) > MILLIS_IN_A_DAY but <= MILLIS_IN_TWO_DAYS => event is TOMORROW
         * Reminder date and time = input date - input reminder
         */
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String inputDate = "22-03-2018";
        try {
            Date formatInputDate = dateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Setup the toolbar
        setSupportActionBar(toolbar);

    }
}

//TODO: Create the Events class and database
//TODO: Check if recyclerView items are placed correctly inside the main screen
//TODO: FAB should add fake events to the database
//TODO: Add option to delete entire db from main screen
//TODO: Add empty state view
