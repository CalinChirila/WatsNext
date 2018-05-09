package com.example.android.watsnext.activities;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.watsnext.R;
import com.example.android.watsnext.data.EventContract;
import com.example.android.watsnext.data.EventContract.EventsEntry;
import com.example.android.watsnext.utils.EventRescheduler;
import com.example.android.watsnext.utils.Reminder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmActivity extends AppCompatActivity {
    @BindView(R.id.tv_alarm_message)
    TextView mAlarmMessageTextView;
    @BindView(R.id.button_stop_alarm)
    Button mStopAlarmButton;

    private MediaPlayer mRingTonePlayer;
    private static PowerManager.WakeLock mScreenLock;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the window to full screen and show it while screen is locked
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);

        // Get the event id and use it to get the correct alarm message text
        long eventId = getIntent().getLongExtra(Reminder.ALARM_EVENT_ID, -1);
        String selection = EventsEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(eventId)};
        Cursor cursor = getContentResolver().query(EventsEntry.CONTENT_URI, null, selection, selectionArgs, null);
        cursor.moveToFirst();

        // When the activity is created, start playing the alarm
        // Also, start phone vibration

        mRingTonePlayer = MediaPlayer.create(getApplicationContext(), R.raw.ringtone1);
        mRingTonePlayer.start();

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        // Start vibration without delay
        // Vibrate for 1 second
        // Pause 2 seconds between vibrations
        // Repeat until canceled
        long[] vibrationPattern = {0, 1000, 2000};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mVibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 1));
        } else {
            mVibrator.vibrate(vibrationPattern, 1);
        }

        // Wakeup phone screen
        wakeupPhoneScreen(this);

        // Get the event message that the user set and display it in the alarm activity
        String alarmMessage = cursor.getString(cursor.getColumnIndex(EventContract.EventsEntry.COLUMN_EVENT_TEXT));
        if(TextUtils.isEmpty(alarmMessage)){
            alarmMessage = getString(R.string.event);
        }
        mAlarmMessageTextView.setText(alarmMessage);

        // When the Stop Alarm button is clicked:
        // Stop the alarm
        // Release media player resources
        // Stop the vibration
        // Exit the alarm activity
        mStopAlarmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                releaseMediaPlayerResources();
                mScreenLock.release();
                mVibrator.cancel();
                finish();
            }
        });


        String repeat = cursor.getString(cursor.getColumnIndex(EventsEntry.COLUMN_EVENT_REPEAT));
        // Setup the next event if the user set the event to repeat itself
        if(!repeat.equals("[]")) {
            EventRescheduler.rescheduleEvent(this, eventId);
        }
        cursor.close();
    }

    /**
     * Helper method to wakeup the phone screen
     */
    public static void wakeupPhoneScreen(Context context){
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        if(powerManager != null) {
            mScreenLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            // Keep the screen awake for 1 minute
            mScreenLock.acquire(60*1000L /* 1 minute */);
        }
    }

    /**
     * Helper method to stop the media playback and release resources
     */
    private void releaseMediaPlayerResources(){
        if(mRingTonePlayer != null){
            mRingTonePlayer.stop();
            mRingTonePlayer.release();
            mRingTonePlayer = null;
        }
    }

    // When the activity is destroyed, release media player resources
    @Override
    public void onDestroy(){
        super.onDestroy();
        releaseMediaPlayerResources();
    }
}
