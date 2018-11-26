package com.gr03.amos.bikerapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gr03.amos.bikerapp.Models.Event;

import io.realm.RealmResults;


public class ShowEventRecylerViewAdapter extends RecyclerView.Adapter<ShowEventRecylerViewAdapter.ViewHolder> {

    private RealmResults<Event> mData;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    public ShowEventRecylerViewAdapter(Context context, RealmResults<Event> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.show_event_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventName.setText(mData.get(position).getName());
        holder.eventDescription.setText(mData.get(position).getDescription());
        holder.eventDate.setText(mData.get(position).getDate());
        holder.eventTime.setText(mData.get(position).getTime());
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
            Long id = mData.get(getAdapterPosition()).getId_event();
            Intent intent = new Intent(context, EditEventActivity.class);
            intent.putExtra("id", id);
            context.startActivity(intent);
        }
    }
}
