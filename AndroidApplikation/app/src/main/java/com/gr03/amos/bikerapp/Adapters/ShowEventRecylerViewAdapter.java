package com.gr03.amos.bikerapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gr03.amos.bikerapp.EditEventActivity;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.Models.EventParticipation;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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
        } else {
            holder.adminTag.setVisibility(View.INVISIBLE);
            holder.controlLinearLayout.setVisibility(View.INVISIBLE);
        }

        if (SaveSharedPreference.getUserType(this.context) != 2
                && mData.get(position).getId_user_type() == 2) {
            holder.adminTag.setVisibility(View.VISIBLE);
            holder.adminTag.setText("Ad");
        }

        holder.eventDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this Event?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
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
                    })
                    .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                    .show();
        });

        holder.eventEdit.setOnClickListener(v -> {
            try {
                editEvent(mData.get(position).getId_event());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        if (SaveSharedPreference.getUserType(context) == 2) {
            holder.joinEvent.setVisibility(View.GONE);
        } else {
            holder.joinEvent.setOnClickListener(v -> {

                JSONObject json = new JSONObject();
                try {

                    json.put("event_id", mData.get(position).getId_event());
                    json.put("user_id", SaveSharedPreference.getUserID(context));

                    FutureTask<String> task = new FutureTask((Callable<String>) () -> {
                        JSONObject threadResponse = Requests.getResponse("addmyeventlist", json);
                        return threadResponse.toString();
                    });

                    Realm.init(context);
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    EventParticipation eventParticipation = realm.createObject(EventParticipation.class);
                    eventParticipation.setId_event(mData.get(position).getId_event());
                    eventParticipation.setId_user(SaveSharedPreference.getUserID(context));
                    realm.commitTransaction();
                    realm.close();


                    new Thread(task).start();
                    Log.i("Response", task.get());
                } catch (JSONException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }


            });
        }
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
        Button joinEvent;

        ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
            joinEvent = itemView.findViewById(R.id.join_event);
            eventDropDownButton = itemView.findViewById(R.id.arrow_down);
            eventDropUpButton = itemView.findViewById(R.id.arrow_up);
            eventDelete = itemView.findViewById(R.id.event_delete);
            eventEdit = itemView.findViewById(R.id.event_edit);
            controlLinearLayout = itemView.findViewById(R.id.controls_button);
            dividerView = itemView.findViewById(R.id.divider);
            adminTag = itemView.findViewById(R.id.admin_tag);

            itemView.setOnClickListener(this);

        }


        public void onClick(View view) {

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

    private void editEvent(long eventId) throws JSONException {
        Intent intent = new Intent(context, EditEventActivity.class);
        intent.putExtra("id", eventId);
        context.startActivity(intent);
    }

}

