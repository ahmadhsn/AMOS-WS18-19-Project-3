package com.gr03.amos.bikerapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;


public class createEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    EditText eventName;
    EditText eventDescr;
    EditText eventDate;
    EditText eventTime;
    EditText eventLocation;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventName = findViewById(R.id.eventname);
        eventDescr = findViewById(R.id.eventdescr);
        eventDate = findViewById(R.id.eventdate);
        eventTime = findViewById(R.id.eventtime);
        eventLocation = findViewById(R.id.eventlocation);
        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        eventDate.setOnClickListener(v -> {
            showDatePicker(1980, 0, 1);
        });

        eventTime.setOnClickListener(v -> {
            showTimePicker(10, 44);
        });

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        eventDate.setText(year+"/"+month+"/"+day);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
        eventTime.setText(hour + ":" + minutes);
    }


    public void showDatePicker(int year, int month, int day) {
        new DatePickerDialog(this, R.style.DateTimePicker, this, year, month, day)
                .show();
    }

    private void showTimePicker(int hour, int minute) {
        new TimePickerDialog(this, R.style.DateTimePicker, this, hour, minute, true).show();
    }

    public void newEvent(View view) throws JSONException {
        //TODO check all values are valid

        JSONObject json = new JSONObject();
        json.put("name", eventName.getText().toString());
        json.put("description", eventDescr.getText().toString());
        json.put("date", eventDate.getText().toString());
        json.put("time", eventTime.getText().toString());
        json.put("location", eventLocation.getText().toString());

        try {
            JSONObject response;

            FutureTask<String> task = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getResponse("createEvent", json);
                    return threadResponse.toString();
                }
            });

            new Thread(task).start();
            Log.i("Response", task.get());
            response = new JSONObject(task.get());
        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }
    }


}
