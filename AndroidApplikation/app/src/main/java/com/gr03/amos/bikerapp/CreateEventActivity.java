package com.gr03.amos.bikerapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;


public class CreateEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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
            showDatePicker();
        });

        eventTime.setOnClickListener(v -> {
            showTimePicker();
        });

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        eventDate.setText(year+"/"+(month +1)+"/"+day);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
        eventTime.setText(hour + ":" + minutes);
    }


    public void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog newDatePickerDialog = new DatePickerDialog(this, R.style.DateTimePicker, this, year, month, day);
        newDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        newDatePickerDialog.show();

    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog newTimePickerDialog = new TimePickerDialog(this, R.style.DateTimePicker, this, hour, minute, true);
        newTimePickerDialog.show();
    }

    public void newEvent(View view) throws JSONException {

        if (eventName.getText().toString().isEmpty()){
            Log.i("VALIDATIONEVENT","event name is empty");
            Toast.makeText(getApplicationContext(),"Please enter a event name",Toast.LENGTH_LONG).show();
            return;
        }
        if (eventDescr.getText().toString().isEmpty()){
            Log.i("VALIDATIONEVENT", "event description is empty");
            Toast.makeText(getApplicationContext(),"Please enter a event discription",Toast.LENGTH_LONG).show();
            return;
        }
        if (eventDate.getText().toString().isEmpty()){
            Log.i("VALIDATIONEVENT","event date is empty");
            Toast.makeText(getApplicationContext(),"Please enter a event date",Toast.LENGTH_LONG).show();
            return;
        }
        if (eventTime.getText().toString().isEmpty()){
            Log.i("VALIDATIONEVENT","event time is empty");
            Toast.makeText(getApplicationContext(),"Please enter a event time",Toast.LENGTH_LONG).show();
            return;
        }
        if (eventLocation.getText().toString().isEmpty()) {
            Log.i("VALIDATIONEVENT","event location is empty");
            Toast.makeText(getApplicationContext(),"Please enter a event location",Toast.LENGTH_LONG).show();
            return;
        }

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
            Intent intent = new Intent(this, ShowEventActivity.class);
            startActivity(intent);
            response = new JSONObject(task.get());

            if (response.has("createEvent")) {
                String statusEv = (String) response.get("createEvent");
                if (statusEv.equals("successful")) {
                    Toast.makeText(getApplicationContext(), "Successful created Event.", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            //TODO: Error-Handling
            Log.i("Exception --- not requested", e.toString());
        }
    }
}
