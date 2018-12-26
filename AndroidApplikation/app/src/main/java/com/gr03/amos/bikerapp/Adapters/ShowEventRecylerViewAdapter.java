package com.gr03.amos.bikerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gr03.amos.bikerapp.EventDetailsActivity;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;
import com.gr03.amos.bikerapp.ShowEventActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import io.realm.Realm;
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

        holder.eventDropDownButton.setOnClickListener(v -> {
            holder.eventDescription.setVisibility(View.VISIBLE);
            holder.eventDropDownButton.setVisibility(View.GONE);
            holder.eventDropUpButton.setVisibility(View.VISIBLE);
            holder.dividerView.setVisibility(View.VISIBLE);
        });

        holder.eventDropUpButton.setOnClickListener(v -> {
            holder.eventDescription.setVisibility(View.GONE);
            holder.eventDropDownButton.setVisibility(View.VISIBLE);
            holder.eventDropUpButton.setVisibility(View.GONE);
            holder.dividerView.setVisibility(View.GONE);
        });

        if (mData.get(position).getId_user() == SaveSharedPreference.getUserID(this.context)) {
            holder.adminTag.setVisibility(View.VISIBLE);
            holder.controlLinearLayout.setVisibility(View.VISIBLE);
        }

        holder.eventDelete.setOnClickListener(v -> {
            try {
                deleteEvent(mData.get(position).getId_event());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Realm realmDelete = Realm.getDefaultInstance();
            final Event event1 = realmDelete
                    .where(Event.class)
                    .equalTo("id_event", mData.get(position).getId_event())
                    .findFirst();

            realmDelete.beginTransaction();
            event1.deleteFromRealm();
            Log.i("After Transaction from Realm 1", "Deleted");
            realmDelete.commitTransaction();
            realmDelete.close();
            notifyDataSetChanged();
        });
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
        ImageView eventDropDownButton;
        ImageView eventDropUpButton;
        LinearLayout controlLinearLayout;
        View dividerView;
        TextView adminTag;
        ImageButton eventDelete;
        ImageButton eventEdit;

        ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
            eventDropDownButton = itemView.findViewById(R.id.arrow_down);
            eventDropUpButton = itemView.findViewById(R.id.arrow_up);
            eventDelete = itemView.findViewById(R.id.event_delete);
            eventEdit = itemView.findViewById(R.id.event_edit);
            controlLinearLayout = itemView.findViewById(R.id.controls_button);
            dividerView = itemView.findViewById(R.id.divider);
            adminTag = itemView.findViewById(R.id.admin_tag);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Long id = mData.get(getAdapterPosition()).getId_event();
            Intent intent = new Intent(context, EventDetailsActivity.class);
            intent.putExtra("id", id);
            context.startActivity(intent);
        }
    }

    private void deleteEvent(long eventId) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id_event", eventId);
        try {
            FutureTask<String> task = new FutureTask((Callable<String>) () -> {
                JSONObject threadResponse = Requests.getResponse("deleteEvent", json);
                return threadResponse.toString();
            });
            new Thread(task).start();
            Log.i("Response", task.get());
        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }
    }

}