package com.example.android.watsnext.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);

        mRingTonePlayer = MediaPlayer.create(getApplicationContext(), R.raw.ringtone1);
        mRingTonePlayer.start();

        String alarmMessage = getIntent().getStringExtra(AlarmClock.EXTRA_MESSAGE);

        mAlarmMessageTextView.setText(alarmMessage);

        // When the activity is created, start playing the alarm
        // Also, start phone vibration
        mStopAlarmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Stop the alarm
                releaseMediaPlayerResources();
                finish();
            }
        });

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
