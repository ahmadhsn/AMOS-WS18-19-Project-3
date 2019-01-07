package com.gr03.amos.bikerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.AddRoute;
import com.gr03.amos.bikerapp.EventDetailsActivity;
import com.gr03.amos.bikerapp.FragmentActivity.MyEventListFragment;
import com.gr03.amos.bikerapp.FragmentActivity.ShowEventsFragment;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;
import com.gr03.amos.bikerapp.ShowEventActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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

       /* holder.joinEvent.setOnClickListener(v -> {
            JSONObject json = new JSONObject();
            try {
                json.put("event_name", mData.get(position).getName());
                json.put("event_descr", mData.get(position).getDescription());
                json.put("event_date", mData.get(position).getDate());
                json.put("evenr_time", mData.get(position).getTime());
                json.put("event_id", mData.get(position).getId_event());
                json.put("user_id", SaveSharedPreference.getUserID(context));

                FutureTask<String> task = new FutureTask((Callable<String>) () -> {
                    JSONObject threadResponse = Requests.getResponse("addmyeventlist", json);
                    return threadResponse.toString();
                });
                new Thread(task).start();
                Log.i("Response", task.get());
            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        });*/
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
        Button joinEvent;

        ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
            joinEvent = itemView.findViewById(R.id.join_event);
            itemView.setOnClickListener(this);

            Button joinEvent = itemView.findViewById(R.id.join_event);
            joinEvent.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    JSONObject json = new JSONObject();
                    try {

                        json.put("event_id", mData.get(getAdapterPosition()).getId_event());
                        json.put("user_id", SaveSharedPreference.getUserID(context));

                        FutureTask<String> task = new FutureTask((Callable<String>) () -> {
                            JSONObject threadResponse = Requests.getResponse("addmyeventlist", json);
                            return threadResponse.toString();
                        });
                        new Thread(task).start();
                        Log.i("Response", task.get());
                    } catch (JSONException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    /*((ShowEventActivity) v.getContext()).getFragmentManager().beginTransaction()
                    .replace(R.id.create_event_fragment, new MyEventListFragment())
                            .commit();*/
                }
            });
        }

        @Override
        public void onClick(View view) {
            Long id = mData.get(getAdapterPosition()).getId_event();
            Intent intent = new Intent(context, EventDetailsActivity.class);
            intent.putExtra("id", id);
            context.startActivity(intent);
        }

    }
}

