package com.example.android.watsnext;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.watsnext.data.EventContract.EventsEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Astraeus on 2/27/2018.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsAdapterViewHolder> {

    private Cursor mEventsCursor;
    private Context mContext;
    private String mComparedDate;

    public static final long MILLIS_IN_A_DAY = 86400000;
    public static final long MILLIS_IN_TWO_DAYS = MILLIS_IN_A_DAY * 2;


    @Override
    public EventsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout and pass it to the View Holder constructor
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_item, parent, false);
        return new EventsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsAdapterViewHolder holder, int position) {
        mEventsCursor.moveToPosition(position);

        // Set the event type text
        String eventTypeString = convertEventTypeToString(mEventsCursor.getInt(mEventsCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TYPE)));
        holder.mEventTypeTextView.setText(eventTypeString);

        // Set the event text
        holder.mEventTextTextView.setText(mEventsCursor.getString(mEventsCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TEXT)));

        // Set the event time
        String eventTimeString = convertEventTimeToString(mEventsCursor.getLong(mEventsCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TIME)));
        holder.mEventTimeTextView.setText(eventTimeString);

        // Set the reminder icon
        int eventReminderType = mEventsCursor.getInt(mEventsCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_REMINDER));
        setReminderIcon(holder.mEventReminderIcon, eventReminderType);

        // Set the event date
        String eventDateString = convertEventDateToString(mEventsCursor.getLong(mEventsCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_DATE)));
        if(isSameDate(eventDateString)){
            holder.mEventDateTextView.setVisibility(View.GONE);
        } else {
            holder.mEventDateTextView.setText(eventDateString);
        }

    }

    @Override
    public int getItemCount() {
        if(mEventsCursor == null) return 0;
        return mEventsCursor.getCount();
    }

    public class EventsAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView mEventTypeTextView;
        TextView mEventTextTextView;
        TextView mEventTimeTextView;
        TextView mEventDateTextView;
        ImageView mEventReminderIcon;

        public EventsAdapterViewHolder(View itemView) {
            super(itemView);

            mEventTypeTextView = itemView.findViewById(R.id.tv_item_event_type);
            mEventTextTextView = itemView.findViewById(R.id.tv_item_event_text);
            mEventTimeTextView = itemView.findViewById(R.id.tv_item_event_time);
            mEventDateTextView = itemView.findViewById(R.id.tv_item_event_date);
            mEventReminderIcon = itemView.findViewById(R.id.iv_item_reminder_icon);

        }
    }

    public void setEventsData(Context context, Cursor data){
        mEventsCursor = data;
        mContext = context;
        notifyDataSetChanged();
    }

    private boolean isSameDate(String formattedDate){
        if(formattedDate.equals(mComparedDate)){
            return true;
        } else {
            mComparedDate = formattedDate;
            return false;
        }
    }

    /**
     * Helper method that converts the event date to a string
     */
    private String convertEventDateToString(long eventDate){

        String formattedDate;
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        long timeBeforeEvent = eventDate - currentTimeInMillis;

        if(timeBeforeEvent <= MILLIS_IN_A_DAY){
            // Display the word TODAY
            formattedDate = mContext.getResources().getString(R.string.today);
        } else if (timeBeforeEvent > MILLIS_IN_A_DAY && timeBeforeEvent <= MILLIS_IN_TWO_DAYS){
            // Display the word TOMORROW
            formattedDate = mContext.getResources().getString(R.string.tomorrow);
        } else {
            // Display the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            formattedDate = dateFormat.format(eventDate);
        }
        return formattedDate;
    }

    /**
     * Helper method that converts the event time to a string
     */
    private String convertEventTimeToString(long eventTime){
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        return timeFormat.format(eventTime);
    }

    /**
     * Helper method that sets the correct icon for the reminder type
     */
    private void setReminderIcon(ImageView icon, int reminderType){
        switch(reminderType){
            case 0:
                icon.setVisibility(View.INVISIBLE);
                break;
            case 1:
                icon.setImageResource(R.drawable.ic_assignment);
                break;
            case 2:
                icon.setImageResource(R.drawable.ic_alarm);
                break;
        }
    }

    /**
     * Helper method that converts event types into strings
     */
    private String convertEventTypeToString(int type){
        String eventType;
        switch(type){
            case 0:
                eventType = mContext.getResources().getString(R.string.event_type_other);
                break;
            case 1:
                eventType = mContext.getResources().getString(R.string.event_type_appointment);
                break;
            case 2:
                eventType = mContext.getResources().getString(R.string.event_type_meeting);
                break;
            case 3:
                eventType = mContext.getResources().getString(R.string.event_type_social);
                break;
            default:
                eventType = mContext.getResources().getString(R.string.event_type_other);
                break;
        }

        return eventType;
    }
}
