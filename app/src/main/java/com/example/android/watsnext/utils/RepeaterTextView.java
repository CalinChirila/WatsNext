package com.example.android.watsnext.utils;

import android.app.Activity;
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

    public boolean isSelected;

    public static ArrayList<Integer> mRepeatDays = new ArrayList<>();


    /**
     * RepeaterTextView constructor
     */
    public RepeaterTextView(Context context) {
        super(context);

        this.setOnClickListener(this);
        assignIndexToRepeaterView(this.getId());

    }

    /**
     * RepeaterTextView constructor
     */
    public RepeaterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOnClickListener(this);
        assignIndexToRepeaterView(this.getId());


    }

    /**
     * RepeaterTextView constructor
     */
    public RepeaterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setOnClickListener(this);
        assignIndexToRepeaterView(this.getId());

    }

    /**
     * Return the coresponding RepeaterTextView according to its index
     */
    public static RepeaterTextView getRepeaterViewAtIndex(Activity activity, int index){
        RepeaterTextView view = null;
        switch(index){
            case 0:
                view = activity.findViewById(R.id.repeater_monday);
                break;
            case 1:
                view = activity.findViewById(R.id.repeater_tuesday);
                break;
            case 2:
                view = activity.findViewById(R.id.repeater_wednesday);
                break;
            case 3:
                view = activity.findViewById(R.id.repeater_thursday);
                break;
            case 4:
                view = activity.findViewById(R.id.repeater_friday);
                break;
            case 5:
                view = activity.findViewById(R.id.repeater_saturday);
                break;
            case 6:
                view = activity.findViewById(R.id.repeater_sunday);
                break;
        }
        return view;
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
            v.setBackgroundColor(getResources().getColor(R.color.dark_transparent_background));
            this.isSelected = false;

            if(mRepeatDays.contains(mIndex)) {
                mRepeatDays.remove(mRepeatDays.indexOf(mIndex));
            }

        } else {
            // Add repeated event
            v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            this.isSelected = true;

            if(!mRepeatDays.contains(mIndex)){
                mRepeatDays.add(mIndex);
            }

        }

    }

    /**
     * Returns the array of indices of the repeat days
     */
    public static ArrayList<Integer> getRepeatDays(){ return mRepeatDays;}

    public static void resetRepeatDays(){
        mRepeatDays.clear();
    }

}
