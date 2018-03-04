package com.example.android.watsnext;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by thewh on 3/4/2018.
 */

public class EventTypesAdapter extends RecyclerView.Adapter<EventTypesAdapter.EventTypesViewHolder> {

    private static final int EVENT_TYPE_OTHER = 0;
    private static final int EVENT_TYPE_APPOINTMENT = 1;
    private static final int EVENT_TYPE_MEETING = 2;
    private static final int EVENT_TYPE_SOCIAL = 3;

    private int[] mEventTypes = {
            EVENT_TYPE_OTHER,
            EVENT_TYPE_APPOINTMENT,
            EVENT_TYPE_MEETING,
            EVENT_TYPE_SOCIAL
    };

    private Context mContext;

    //TODO: get the movie catalog app from github. add on click methods to this. when item is clicked, hide rv and set text to selected event

    @Override
    public EventTypesAdapter.EventTypesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_type_item, parent, false);
        return new EventTypesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventTypesAdapter.EventTypesViewHolder holder, int position) {
        // Set string values for each event type
        String eventType = EventsAdapter.convertEventTypeToString(mContext, mEventTypes[position]);
        holder.mEventTypeTextView.setText(eventType);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.event_types_animation);
        animation.setDuration((position + 1) * 200);
        holder.mEventTypeTextView.setAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return mEventTypes.length;
    }

    public class EventTypesViewHolder extends RecyclerView.ViewHolder {

        TextView mEventTypeTextView;
        public EventTypesViewHolder(View itemView) {
            super(itemView);
            mEventTypeTextView = itemView.findViewById(R.id.tv_event_type);

        }
    }
}
