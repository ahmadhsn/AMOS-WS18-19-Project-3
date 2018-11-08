package com.gr03.amos.bikerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.gr03.amos.bikerapp.Requests;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;


public class createEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    public void newEvent(View view) throws JSONException{
        //TODO check all values are valid

        EditText eventName = findViewById(R.id.eventname);
        EditText eventDescr = findViewById(R.id.eventdescr);
        EditText eventDate = findViewById(R.id.eventdate);
        EditText eventTime = findViewById(R.id.eventtime);
        EditText eventLocation = findViewById(R.id.eventlocation);


/*        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.event_type);
        //create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "three"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        */

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
