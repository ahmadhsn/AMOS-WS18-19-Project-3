package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gr03.amos.bikerapp.Model.Event;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditEventActivity extends AppCompatActivity {
    Intent intent;
    String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        intent = getIntent();
        eventId = intent.getStringExtra("id");

        Requests.getEventById("getEventById/" + eventId, this);
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        final RealmResults<Event> event = realm.where(Event.class).findAll();
        Log.i("name", String.valueOf(event.size()));


        final Event eventRealm = realm.where(Event.class).equalTo("id_event", Long.parseLong(eventId)).findFirst();
        Log.i("name", eventRealm.getName());

    }
}
