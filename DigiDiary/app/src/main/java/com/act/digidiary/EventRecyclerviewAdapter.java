package com.act.digidiary;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventRecyclerviewAdapter extends RecyclerView.Adapter<EventRecyclerviewAdapter.EventViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public EventRecyclerviewAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        public TextView dateText;
        public TextView eventText;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            dateText = itemView.findViewById(R.id.eventLayoutDate);
            eventText = itemView.findViewById(R.id.eventLayoutEvent);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.event_layout, parent, false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }

        String event = mCursor.getString(mCursor.getColumnIndex(Event.EventEntry.COLUMN_EVENT));
        String date = mCursor.getString(mCursor.getColumnIndex(Event.EventEntry.COLUMN_DATE));

        holder.eventText.setText(event);
        holder.dateText.setText(date);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor != null){
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null){
            notifyDataSetChanged();
        }
    }

}
