package com.example.android.watsnext.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.android.watsnext.R;

import java.util.ArrayList;

/**
 * Created by Calin-Cristian Chirila on 3/14/2018.
 */

public class RepeaterTextView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener{

    private int mIndex;

    public static boolean[] repeaterArray = new boolean[7];

    private boolean isSelected = false;

    public RepeaterTextView(Context context) {
        super(context);

        this.setOnClickListener(this);
        assignIndexToRepeaterView(this.getId());
    }

    public RepeaterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOnClickListener(this);
        assignIndexToRepeaterView(this.getId());
    }

    public RepeaterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setOnClickListener(this);
        assignIndexToRepeaterView(this.getId());
    }

    private void assignIndexToRepeaterView(int viewID){
        switch(viewID){
            case R.id.repeater_monday:
                mIndex = 0;
                break;
            case R.id.repeater_tuesday:
                mIndex = 1;
                break;
            case R.id.repeater_wednesday:
                mIndex = 2;
                break;
            case R.id.repeater_thursday:
                mIndex = 3;
                break;
            case R.id.repeater_friday:
                mIndex = 4;
                break;
            case R.id.repeater_saturday:
                mIndex = 5;
                break;
            case R.id.repeater_sunday:
                mIndex = 6;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(isSelected){

            // Remove repeated event
            v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            isSelected = false;

        } else {

            // Add repeated event
            v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            isSelected = true;

        }

        // Populate the repeaterArray with the correct values
        repeaterArray[mIndex] = isSelected;
    }

    //TODO Create a function updateEvents that will:
    // 1) Reschedule repeated events
    // 2) Remove past events


    public static ArrayList<Integer> getIndicesOfRepeatDays(){

        ArrayList<Integer> indicesArray = new ArrayList<>();

        // Add every index from the repeater array that has the value of true
        for(int i = 0; i < 7; i++){
            if(repeaterArray[i]){
                indicesArray.add(i);
            }
        }

        return indicesArray;
    }


}
