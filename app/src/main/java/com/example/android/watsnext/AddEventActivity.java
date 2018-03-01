package com.example.android.watsnext;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEventActivity extends AppCompatActivity {
    @BindView(R.id.add_event_toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_event_type)
    Spinner eventTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);

        //TODO: this will change if the user clicked add event or edit event
        toolbar.setTitle("Add Event");
        //Set the toolbar
        setSupportActionBar(toolbar);

        //Setup the spinner
        setupEventTypeSpinner();

    }


    //TODO: make spinner corners rounded
    //TODO: Improve visuals
    private void setupEventTypeSpinner(){
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.event_types)){



            // Use the first item in the array as a hint for the spinner
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    return false;
                } else {
                    return true;
                }
            }

            // The first item in the spinner needs to have a different color.
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent){
                TextView spinnerItem = (TextView) super.getDropDownView(position, convertView, parent);
                if(position == 0){
                    spinnerItem.setTextColor(Color.BLACK);
                } else {
                    
                    spinnerItem.setTextColor(Color.WHITE);
                }
                return spinnerItem;
            }

        };

        // Set what happens when a user clicked on an item
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO: this is a placeholder. Replace before release
                if(position == 0){
                    return;
                }
                String selectedItem = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(getApplicationContext(), "Selected: " + selectedItem, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };


        eventTypeSpinner.setAdapter(spinnerAdapter);
        eventTypeSpinner.setOnItemSelectedListener(spinnerListener);
    }
}
