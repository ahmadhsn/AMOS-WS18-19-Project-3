package com.gr03.amos.bikerapp.FragmentActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;
import com.gr03.amos.bikerapp.ShowEventActivity;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, ResponseHandler {
    private int EVENTTYPEID = 1;
    private EditText eventName, eventDescr, eventDate, eventTime, country, city, street, postcode, houseNr;
    private Button createEvent;
    private SimpleDateFormat simpleDateFormat;

    private Spinner spinner;

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
        createEvent = view.findViewById(R.id.createEvent);
        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);

        country = view.findViewById(R.id.country);
        city = view.findViewById(R.id.city);
        street = view.findViewById(R.id.street);
        postcode = view.findViewById(R.id.postcode);
        houseNr = view.findViewById(R.id.houseNr);

        if (SaveSharedPreference.getUserType(container.getContext()) == 2) {
            Requests.executeRequest(this, "GET", "getBusinessProfile/" + SaveSharedPreference.getUserID(container.getContext()));
        }

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

        List<String> eventType = getEventTypes();
        ArrayAdapter<String> adapter = new ArrayAdapter(container.getContext(),
                android.R.layout.simple_spinner_item, eventType);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return view;
    }

    List<String> getEventTypes() {
        JSONObject response = Requests.getResponse("get_event_type", null, "GET", getContext());
        List<String> eventTypesList = new ArrayList();
        if (response != null && response.has("result")) {
            try {
                JSONArray eventTypes = response.getJSONArray("result");
                for (int i = 0; i < eventTypes.length(); i++) {
                    JSONObject obj = eventTypes.getJSONObject(i);
                    eventTypesList.add(obj.getString("name"));
                }

            } catch (Exception ex) {
                Log.i("Exception --- not requested", ex.toString());
                eventTypesList.add("No event types found");
            }
        }
        return eventTypesList;
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        EVENTTYPEID = position + 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        eventDate.setText(year + "-" + (month + 1) + "-" + day);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
        String hourRepresentation = String.valueOf(hour);
        String minutesRepresentation = String.valueOf(minutes);
        if (minutes < 10) {
            minutesRepresentation = "0" + minutes;
        }
        if (hour < 10) {
            hourRepresentation = "0" + hour;
        }
        eventTime.setText(hourRepresentation + ":" + minutesRepresentation);
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

    boolean isTextEmpty(EditText text) {
        CharSequence string = text.getText().toString();
        return TextUtils.isEmpty(string);
    }

    boolean checkEnteredData() {
        boolean isDataNotSet = false;
        if (isTextEmpty(eventName)) {
            eventName.setError("Event name is required!");
            isDataNotSet = true;
        }
        if (isTextEmpty(eventDescr)) {
            eventDescr.setError("Please add a short description of the event!");
            isDataNotSet = true;
        }
        if (isTextEmpty(country)) {
            country.setError("Country is a required field!");
            isDataNotSet = true;
        }
        if (isTextEmpty(city)) {
            city.setError("City is a required field!");
            isDataNotSet = true;
        }
        if (isTextEmpty(eventDate)) {
            eventDate.setError("Date is a required field!");
            isDataNotSet = true;
        }
        if (isTextEmpty(eventTime)) {
            eventTime.setError("Date is a required field!");
            isDataNotSet = true;
        }

        return isDataNotSet;
    }


    public void createEvent() throws JSONException {
        if (checkEnteredData()) {
            return;
        }
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        String event_Date = eventDate.getText().toString();
        String system_Date = (year + "-" + (month + 1) + "-" + day);

        if (event_Date.equals(system_Date)) {
            int hour_ev = Integer.parseInt(eventTime.getText().toString().split(":")[0]);
            int minutes_ev = Integer.parseInt(eventTime.getText().toString().split(":")[1]);
            if (hour_ev < hour || minutes_ev < minutes) {
                eventTime.setText(hour + ":" + minutes);
                eventTime.setError("Please enter a future time.");
                Log.i("VALIDATIONEVENT", "event time is not in the future");
                Toast.makeText(getContext(), "Please enter a future time.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        JSONObject event = new JSONObject();
        JSONObject address = new JSONObject();
        JSONObject requestJSON = new JSONObject();
        event.put("name", eventName.getText().toString());
        event.put("description", eventDescr.getText().toString());
        event.put("date", eventDate.getText().toString());
        event.put("time", eventTime.getText().toString() + ":00");
        event.put("event_type_id", EVENTTYPEID);
        address.put("street", street.getText().toString());
        address.put("house_number", houseNr.getText().toString());
        address.put("country", country.getText().toString());
        address.put("state", "");
        address.put("city", city.getText().toString());
        address.put("postcode", postcode.getText().toString());

        requestJSON.put("address", address);
        requestJSON.put("event", event);
        requestJSON.put("user_id", SaveSharedPreference.getUserID(this.getContext()));

        JSONObject response = Requests.getResponse("createEvent", requestJSON, "POST", getContext());


        if (response.has("eventCreation")) {

            Toast.makeText(getContext(), "Successful created Event.", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(getContext(), ShowEventActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if (SocketUtility.hasSocketError(response)) {
            Toast.makeText(getContext(), "No response from server.", Toast.LENGTH_LONG).show();
            return;
        }

        if (urlTail.equals("getBusinessProfile/" + SaveSharedPreference.getUserID(getContext()))) {
            try {
                if (response.get("business_profile").equals("no_profile")) {
                    createEvent.setEnabled(false);
                    Toast.makeText(getContext(), "Please add address first", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject responseProfile = response.getJSONObject("business_profile");
                    JSONObject address = responseProfile.getJSONObject("business_address");
                    street.setText(address.optString("street"));
                    houseNr.setText(address.optString("housenumber"));
                    postcode.setText(address.optString("postcode"));
                    city.setText(address.optString("city"));
                    country.setText(address.optString("country"));


                    houseNr.setEnabled(false);
                    street.setEnabled(false);
                    postcode.setEnabled(false);
                    city.setEnabled(false);
                    country.setEnabled(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
