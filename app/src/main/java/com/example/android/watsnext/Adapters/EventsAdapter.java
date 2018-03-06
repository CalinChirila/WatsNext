package com.example.android.watsnext.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.watsnext.R;
import com.example.android.watsnext.Utils.EventUtils;
import com.example.android.watsnext.data.EventContract.EventsEntry;

/**
 * Created by Calin-Cristian Chirila on 2/27/2018.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsAdapterViewHolder> {

    private Cursor mEventsCursor;
    private Context mContext;
    private String mComparedDate;




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
        String eventTypeString = EventUtils.convertEventTypeToString(mContext, mEventsCursor.getInt(mEventsCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TYPE)));
        holder.mEventTypeTextView.setText(eventTypeString);

        // Set the event text
        holder.mEventTextTextView.setText(mEventsCursor.getString(mEventsCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TEXT)));

        // Set the event time
        String eventTimeString = EventUtils.convertEventTimeToString(mEventsCursor.getLong(mEventsCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_TIME)));
        holder.mEventTimeTextView.setText(eventTimeString);

        // Set the reminder icon
        int eventReminderType = mEventsCursor.getInt(mEventsCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_REMINDER));
        EventUtils.setReminderIcon(holder.mEventReminderIcon, eventReminderType);

        // Set the event date
        String eventDateString = EventUtils.convertEventDateToString(mContext, mEventsCursor.getLong(mEventsCursor.getColumnIndex(EventsEntry.COLUMN_EVENT_DATE)));
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
}
