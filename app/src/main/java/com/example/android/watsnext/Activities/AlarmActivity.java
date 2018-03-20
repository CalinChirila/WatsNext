package com.example.android.watsnext.Activities;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.watsnext.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmActivity extends AppCompatActivity {
    @BindView(R.id.tv_alarm_message)
    TextView mAlarmMessageTextView;
    @BindView(R.id.button_stop_alarm)
    Button mStopAlarmButton;

    private MediaPlayer mRingTonePlayer;
    private PowerManager.WakeLock mScreenLock;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);

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
        wakeupPhoneScreen();

        String alarmMessage = getIntent().getStringExtra(AlarmClock.EXTRA_MESSAGE);

        mAlarmMessageTextView.setText(alarmMessage);


        mStopAlarmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Stop the alarm
                releaseMediaPlayerResources();
                mScreenLock.release();
                mVibrator.cancel();
                finish();
            }
        });

    }

    private void wakeupPhoneScreen(){
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if(powerManager != null) {
            mScreenLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            mScreenLock.acquire(60*1000L /* 1 minute */);
        }
    }

    private void releaseMediaPlayerResources(){
        if(mRingTonePlayer != null){
            mRingTonePlayer.stop();
            mRingTonePlayer.release();
            mRingTonePlayer = null;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        releaseMediaPlayerResources();
    }
}
