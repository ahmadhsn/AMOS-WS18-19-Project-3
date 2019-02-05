package com.gr03.amos.bikerapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;

public class EditEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ResponseHandler {
    Intent intent;
    Long eventId;
    EditText event_name, event_description, event_date, event_time;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.activity_edit_event);
        intent = getIntent();
        eventId = intent.getLongExtra("id", 0);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        final Event event = realm.where(Event.class).equalTo("id_event", eventId).findFirst();

        event_name = (EditText) findViewById(R.id.event_name);
        event_description = (EditText) findViewById(R.id.event_description);
        event_date = (EditText) findViewById(R.id.event_date);
        event_time = (EditText) findViewById(R.id.event_time);

        event_name.setText(event.getName());
        event_description.setText(event.getDescription());
        event_date.setText(event.getDate());
        event_time.setText(event.getTime());

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        event_date.setOnClickListener(v -> {
            showDatePicker(year, month, day);
        });

        event_time.setOnClickListener(v -> {
            showTimePicker(hour, minute);
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
        DatePickerDialog newDatePickerDialog = new DatePickerDialog(this, R.style.DateTimePicker, this, year, month, day);
        newDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        newDatePickerDialog.show();
    }

    private void showTimePicker(int hour, int minute) {
        new TimePickerDialog(this, R.style.DateTimePicker, this, hour, minute, true).show();
    }

    boolean isTextEmpty(EditText text) {
        CharSequence string = text.getText().toString();
        return TextUtils.isEmpty(string);
    }

    public void updateEvent(View view) throws JSONException {

        if (isTextEmpty(event_name)) {
            Log.i("VALIDATIONEVENT", "event name is empty");
            event_name.setError("Please enter a event name");
            return;
        }
        if (isTextEmpty(event_description)) {
            Log.i("VALIDATIONEVENT", "event description is empty");
            event_description.setError("Please enter a event discription");
            return;
        }
        if (isTextEmpty(event_date)) {
            Log.i("VALIDATIONEVENT", "event date is empty");
            event_date.setError("Please enter a event date");
            return;
        }
        if (isTextEmpty(event_time)) {
            Log.i("VALIDATIONEVENT", "event time is empty");
            event_time.setError("Please enter a event time");
            return;
        }

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);

        String event_Date = event_date.getText().toString();
        String system_Date = (year + "/" + (month + 1) + "/" + day).toString();

        if (event_Date.split("/")[0].equals(year)) {
            if (Integer.parseInt(event_Date.split("/")[1]) < month + 1 || (Integer.parseInt(event_Date.split("/")[2]) < day && event_Date.split("/")[1].equals(month + 1))) {
                event_date.setText(system_Date);
                event_date.setError("Please enter a future date.");
                Log.i("VALIDATION", "event date is not in the future");
                Toast.makeText(getApplicationContext(), "Please enter a future date.", Toast.LENGTH_LONG).show();
            }
        }

        if (event_Date.equals(system_Date)) {
            int hour_ev = Integer.parseInt(event_time.getText().toString().split(":")[0]);
            int minutes_ev = Integer.parseInt(event_time.getText().toString().split(":")[1]);
            if (hour_ev < hour || minutes_ev < minutes) {
                event_time.setText(hour + ":" + minutes);
                event_time.setError("Please enter a future time.");
                Log.i("VALIDATIONEVENT", "event time is not in the future");
                Toast.makeText(getApplicationContext(), "Please enter a future time.", Toast.LENGTH_LONG).show();
                return;
            }

        }


        JSONObject json = new JSONObject();
        json.put("id_event", eventId);
        json.put("name", event_name.getText().toString());
        json.put("description", event_description.getText().toString());
        json.put("date", event_date.getText().toString());
        json.put("time", event_time.getText().toString());

        Log.i("EditEventActivity", event_name + " " + eventId);

        try {
            Requests.executeRequest(this, "POST", "updateEvent", json, getApplicationContext());

        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }
    }

    public void cancel(View view) {
        finish();
    }


    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if (SocketUtility.checkRequestSuccessful(getApplicationContext(), response)) {

            switch (urlTail) {
                case "eventUpdate":
                    if (response == null) {
                        Intent intent = new Intent(this, ShowEventActivity.class);
                        startActivity(intent);
                        return;
                    }
                    Intent intent = new Intent(this, ShowEventActivity.class);
                    startActivity(intent);

                    if (response.has("eventUpdate")) {
                        try {
                            String statusEv = (String) response.get("eventUpdate");
                            if (statusEv.equals("successfullUpdation")) {
                                Toast.makeText(getApplicationContext(), "Successfully updated Event.", Toast.LENGTH_LONG).show();
                            }
                            if (statusEv.equals("InvalidRequestBody")) {
                                Toast.makeText(getApplicationContext(), "Invalid Request Body", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
            }
        }
    }
}