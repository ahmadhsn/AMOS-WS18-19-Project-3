package com.gr03.amos.bikerapp.FragmentActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.ShowEventActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CreateEventFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private EditText eventName;
    private EditText eventDescr;
    private EditText eventDate;
    private EditText eventTime;
    private EditText eventLocation;
    private Button createEvent;
    SimpleDateFormat simpleDateFormat;

    private Spinner spinner;
    private static final String[] paths = {"Choose Event Type","Expo", "Rally", "Party"};

    public CreateEventFragment() {
    }

    public static CreateEventFragment newInstance() {
        return new CreateEventFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        spinner = view.findViewById(R.id.spinner);
        eventName = view.findViewById(R.id.eventname);
        eventDescr = view.findViewById(R.id.eventdescr);
        eventDate = view.findViewById(R.id.eventdate);
        eventTime = view.findViewById(R.id.eventtime);
        eventLocation = view.findViewById(R.id.eventlocation);
        createEvent = view.findViewById(R.id.createEvent);
        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);


        eventDate.setOnClickListener(v -> {
            showDatePicker();
        });

        eventTime.setOnClickListener(v -> {
            showTimePicker();
        });

        createEvent.setOnClickListener(v -> {
            try {
                createEvent();
            } catch (JSONException ignored) {
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(),
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return view;
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        eventDate.setText(year + "/" + (month + 1) + "/" + day);
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
        DatePickerDialog newDatePickerDialog = new DatePickerDialog(getContext(), R.style.DateTimePicker, this, year, month, day);
        newDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        newDatePickerDialog.show();

    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog newTimePickerDialog = new TimePickerDialog(getContext(), R.style.DateTimePicker, this, hour, minute, true);
        newTimePickerDialog.show();
    }

    public void createEvent() throws JSONException {

        if (eventName.getText().toString().isEmpty()) {
            Log.i("VALIDATIONEVENT", "event name is empty");
            Toast.makeText(getContext(), "Please enter a event name", Toast.LENGTH_LONG).show();
            return;
        }
        if (eventDescr.getText().toString().isEmpty()) {
            Log.i("VALIDATIONEVENT", "event description is empty");
            Toast.makeText(getContext(), "Please enter a event discription", Toast.LENGTH_LONG).show();
            return;
        }
        if (eventDate.getText().toString().isEmpty()) {
            Log.i("VALIDATIONEVENT", "event date is empty");
            Toast.makeText(getContext(), "Please enter a event date", Toast.LENGTH_LONG).show();
            return;
        }
        if (eventTime.getText().toString().isEmpty()) {
            Log.i("VALIDATIONEVENT", "event time is empty");
            Toast.makeText(getContext(), "Please enter a event time", Toast.LENGTH_LONG).show();
            return;
        }
        if (eventLocation.getText().toString().isEmpty()) {
            Log.i("VALIDATIONEVENT", "event location is empty");
            Toast.makeText(getContext(), "Please enter a event location", Toast.LENGTH_LONG).show();
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

            FutureTask<String> task = new FutureTask((Callable<String>) () -> {
                JSONObject threadResponse = Requests.getResponse("createEvent", json);
                return threadResponse.toString();
            });

            new Thread(task).start();
            Log.i("Response", task.get());
            Intent intent = new Intent(getContext(), ShowEventActivity.class);
            startActivity(intent);
            response = new JSONObject(task.get());

            if (response.has("createEvent")) {
                String statusEv = (String) response.get("createEvent");
                if (statusEv.equals("successful")) {
                    Toast.makeText(getContext(), "Successful created Event.", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            //TODO: Error-Handling
            Log.i("Exception --- not requested", e.toString());
        }
    }
}
