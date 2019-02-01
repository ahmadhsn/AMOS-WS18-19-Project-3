package com.gr03.amos.bikerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.NetworkLayer.DefaultResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class ShowMyEventRecyclerViewAdapter extends RecyclerView.Adapter<ShowMyEventRecyclerViewAdapter.ViewHolder> {

    private RealmList<Event> mData;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    public ShowMyEventRecyclerViewAdapter(Context context, RealmList<Event> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.show_my_event_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventName.setText(mData.get(position).getName());
        holder.eventDescription.setText(mData.get(position).getDescription());
        holder.eventDate.setText(mData.get(position).getDate());
        holder.eventTime.setText(mData.get(position).getTime());

        holder.unjoinEvent.setOnClickListener(v -> {
            unjoinEventRequest(position);
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size());
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void unjoinEventRequest(int position) {
        JSONObject json = new JSONObject();
        try {
            json.put("event_id", mData.get(position).getId_event());
            json.put("user_id", SaveSharedPreference.getUserID(context));

            Requests.executeRequest(new DefaultResponseHandler(), "POST", "deleteEventParticipant", json);

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();

            //delete event participation in Realm Database
            realm.executeTransaction(realm1 -> {
                Event toEdit = realm1.where(Event.class)
                        .equalTo("id_event", mData.get(position).getId_event())
                        .findFirst();
                toEdit.setParticipant(false);
            });
            Log.i("After Transaction from Realm 1", "Deleted Event Participation");


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventName;
        TextView eventDescription;
        TextView eventDate;
        TextView eventTime;
        Button unjoinEvent;

        ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
            unjoinEvent = itemView.findViewById(R.id.unjoin_event);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }

    }
}

