package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gr03.amos.bikerapp.Models.Event;

import io.realm.Realm;

public class EditEventActivity extends AppCompatActivity {
    Intent intent;
    Long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        intent = getIntent();
        eventId = intent.getLongExtra("id", 0);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        final Event event = realm.where(Event.class).equalTo("id_event", eventId).findFirst();
        Log.i("EditEventActivity", event.getName() + event.getDate());

    }
}
