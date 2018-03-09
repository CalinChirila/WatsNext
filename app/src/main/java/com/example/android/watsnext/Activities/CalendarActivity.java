package com.example.android.watsnext.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;

import com.example.android.watsnext.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity {
    @BindView(R.id.datePicker)
    DatePicker datePicker;

    private int mYear;
    private int mMonth;
    private int mDay;

    public static final String KEY_DAY = "day";
    public static final String KEY_MONTH = "month";
    public static final String KEY_YEAR = "year";
    public static final String KEY_CALENDAR_BUNDLE = "bundleFromCalendar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear + 1;
                mDay = dayOfMonth;

                Bundle bundle = new Bundle();
                bundle.putInt(KEY_DAY, mDay);
                bundle.putInt(KEY_MONTH, mMonth);
                bundle.putInt(KEY_YEAR, mYear);

                Intent resultIntent = new Intent(CalendarActivity.this, AddEventActivity.class);
                resultIntent.putExtra(KEY_CALENDAR_BUNDLE, bundle);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
