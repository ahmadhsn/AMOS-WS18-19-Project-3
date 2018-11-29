package com.gr03.amos.bikerapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.gr03.amos.bikerapp.Models.Event;

import io.realm.Realm;

public class EditEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Intent intent;
    Long eventId;
    EditText event_name, event_description, event_date, event_time;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        intent = getIntent();
        eventId = intent.getLongExtra("id", 0);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        final Event event = realm.where(Event.class).equalTo("id_event", eventId).findFirst();

        event_name = (EditText)findViewById(R.id.event_name);
        event_description = (EditText)findViewById(R.id.event_description);
        event_date = (EditText)findViewById(R.id.event_date);
        event_time = (EditText)findViewById(R.id.event_time);

        event_name.setText(event.getName());
        event_description.setText(event.getDescription());
        event_date.setText(event.getDate());
        event_time.setText(event.getTime());

        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        event_date.setOnClickListener(v -> {
            showDatePicker(1980, 0, 1);
        });

        event_time.setOnClickListener(v -> {
            showTimePicker(10, 44);
        });

        Log.i("EditEventActivity", event.getName() + event.getDate());

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        event_date.setText(year + "/" + (month + 1) + "/" + day);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
        event_time.setText(hour + ":" + minutes);
    }


    public void showDatePicker(int year, int month, int day) {
        new DatePickerDialog(this, R.style.DateTimePicker, this, year, month, day)
                .show();
    }

    private void showTimePicker(int hour, int minute) {
        new TimePickerDialog(this, R.style.DateTimePicker, this, hour, minute, true).show();
    }

    public void updateEvent(View view) throws JSONException {

        JSONObject json = new JSONObject();
        json.put("id_event", eventId);
        json.put("name", event_name.getText().toString());
        json.put("description", event_description.getText().toString());
        json.put("date", event_date.getText().toString());
        json.put("time", event_time.getText().toString());

        Log.i("EditEventActivity", event_name + " " + eventId);

        try {
            JSONObject response;

            FutureTask<String> task = new FutureTask((Callable<String>) () -> {
                JSONObject threadResponse = Requests.getResponse("updateEvent", json);
                return threadResponse.toString();
            });

            new Thread(task).start();
            Log.i("Response", task.get());
            Intent intent = new Intent(this, ShowEventActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }
    }



}