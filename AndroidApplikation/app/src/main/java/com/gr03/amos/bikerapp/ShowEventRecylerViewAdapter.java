package com.gr03.amos.bikerapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ShowEventRecylerViewAdapter extends RecyclerView.Adapter<ShowEventRecylerViewAdapter.ViewHolder> {

    private List<List<String>> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    ShowEventRecylerViewAdapter(Context context, List<List<String>> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.show_event_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("recyler", mData.get(position).get(0));
        holder.eventName.setText(mData.get(position).get(0));
        holder.eventDescription.setText(mData.get(position).get(1));
        holder.eventDate.setText(mData.get(position).get(2));
        holder.eventTime.setText(mData.get(position).get(3));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventName;
        TextView eventDescription;
        TextView eventDate;
        TextView eventTime;

        ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String id = mData.get(getAdapterPosition()).get(4);
            //ToDo add the intent for the new activity here
            Log.i("ID", id);
        }
    }
}
