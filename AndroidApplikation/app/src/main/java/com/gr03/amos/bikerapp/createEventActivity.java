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

        JSONObject json = new JSONObject();
        json.put("name", eventName.getText().toString());
        json.put("name", eventDescr.getText().toString());
        json.put("name", eventDate.getText().toString());
        json.put("name", eventTime.getText().toString());
        json.put("name", eventLocation.getText().toString());

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
