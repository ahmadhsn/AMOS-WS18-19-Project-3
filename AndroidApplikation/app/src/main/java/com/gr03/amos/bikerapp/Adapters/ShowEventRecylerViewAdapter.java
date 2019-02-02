package com.gr03.amos.bikerapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Toast;

import com.gr03.amos.bikerapp.EditEventActivity;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.NetworkLayer.DefaultResponseHandler;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;

import io.realm.Realm;
import io.realm.RealmResults;


public class ShowEventRecylerViewAdapter extends RecyclerView.Adapter<ShowEventRecylerViewAdapter.ViewHolder> {

    private RealmResults<Event> mData;
    private LayoutInflater mInflater;
    private Context context;
    private String event_Location;
    private String map;

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
        holder.eventLocation
                .setText("Event Location : "
                        + mData.get(position).getAddress().getHouse_number()
                        + ", " + mData.get(position).getAddress().getStreet()
                        + ", " + mData.get(position).getAddress().getPostcode()
                        + ", " + mData.get(position).getAddress().getCity()
                        + ", " + mData.get(position).getAddress().getCountry());

        //set to joined if already participant
        if (mData.get(position).is_participant()) {
            holder.unjoinEvent.setVisibility(View.VISIBLE);
            holder.joinEvent.setVisibility(View.GONE);
        }

        holder.eventDropDownButton.setOnClickListener(v -> {
            holder.eventDescription.setVisibility(View.VISIBLE);
            holder.eventDropDownButton.setVisibility(View.GONE);
            holder.eventDropUpButton.setVisibility(View.VISIBLE);
            holder.eventLocation.setVisibility(View.VISIBLE);
            holder.map_Image.setVisibility(View.VISIBLE);
            holder.dividerView.setVisibility(View.VISIBLE);
        });

        holder.eventDropUpButton.setOnClickListener(v -> {
            holder.eventDescription.setVisibility(View.GONE);
            holder.eventDropDownButton.setVisibility(View.VISIBLE);
            holder.eventLocation.setVisibility(View.GONE);
            holder.map_Image.setVisibility(View.GONE);
            holder.eventDropUpButton.setVisibility(View.GONE);
            holder.dividerView.setVisibility(View.GONE);
        });

        if (mData.get(position).getId_user() == SaveSharedPreference.getUserID(this.context)) {
            holder.adminTag.setVisibility(View.VISIBLE);
            holder.controlLinearLayout.setVisibility(View.VISIBLE);
            holder.unjoinEvent.setOnClickListener( v -> {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Unjoin Event")
                        .setMessage("You are the administrator of the event. You cannot unjoin. If you can't" +
                                " participate, you have to delete the event. ")
                        .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Delete Event", (dialog, which) -> {
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
                        .show();
            });
        } else {
            holder.adminTag.setVisibility(View.INVISIBLE);
            holder.controlLinearLayout.setVisibility(View.INVISIBLE);
            holder.unjoinEvent.setOnClickListener(v -> {
                unjoinEventRequest(position);

                holder.joinEvent.setVisibility(View.VISIBLE);
                holder.unjoinEvent.setVisibility(View.GONE);
            });
        }

        if (SaveSharedPreference.getUserType(this.context) != 2
                && mData.get(position).getId_user_type() == 2) {
            holder.adminTag.setVisibility(View.VISIBLE);
            holder.adminTag.setText(R.string.business_customer_event);
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

        holder.map_Image.setOnClickListener(v -> {

            try {
                event_Location= mData.get(position).getAddress().getHouse_number()
                        + " " + mData.get(position).getAddress().getStreet()
                        + " " + mData.get(position).getAddress().getPostcode()
                        + " " + mData.get(position).getAddress().getCity()
                        + "," + mData.get(position).getAddress().getCountry();
                map = "http://maps.google.com/maps?q=" + event_Location;
                eventMap(map);

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

                    Requests.executeRequest(new DefaultResponseHandler(), "POST", "addmyeventlist", json);

                    Realm.init(context);
                    Realm realm = Realm.getDefaultInstance();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Event edit = realm.where(Event.class).equalTo("id_event", mData.get(position).getId_event()).findFirst();
                            edit.setParticipant(true);
                        }
                    });
                    realm.close();

                    holder.joinEvent.setVisibility(View.GONE);
                    holder.unjoinEvent.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
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
        TextView eventLocation;
        ImageView eventDropDownButton;
        ImageView eventDropUpButton;
        LinearLayout controlLinearLayout;
        View dividerView;
        TextView adminTag;
        ImageButton eventDelete;
        ImageButton eventEdit;
        Button map_Image;
        Button joinEvent;
        Button unjoinEvent;

        ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
            eventLocation = itemView.findViewById(R.id.event_location);
            joinEvent = itemView.findViewById(R.id.join_event);
            unjoinEvent = itemView.findViewById(R.id.unjoin_event);
            eventDropDownButton = itemView.findViewById(R.id.arrow_down);
            eventDropUpButton = itemView.findViewById(R.id.arrow_up);
            eventDelete = itemView.findViewById(R.id.event_delete);
            eventEdit = itemView.findViewById(R.id.event_edit);
            map_Image = itemView.findViewById(R.id.map_event);
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
        Requests.getResponse("deleteEvent", json, "POST", context);
    }

    private void editEvent(long eventId) throws JSONException {
        Intent intent = new Intent(context, EditEventActivity.class);
        intent.putExtra("id", eventId);
        context.startActivity(intent);
    }

    private void eventMap(String mapAddress) throws JSONException {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(mapAddress));
        context.startActivity(i);
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

}

