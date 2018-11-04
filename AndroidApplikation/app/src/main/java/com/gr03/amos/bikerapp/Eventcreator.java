package com.gr03.amos.bikerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Eventcreator extends AppCompatActivity {
    TextView _letsride;
    EditText _eventname,_eventdescr,_eventdate,_eventlocation;
    Button _eventsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventcreator);
        _letsride=(TextView) findViewById(R.id.letsride);
        _eventname=(EditText) findViewById(R.id.eventname);
        _eventdescr=(EditText) findViewById(R.id.eventdescr);
        _eventdate=(EditText) findViewById(R.id.eventdate);
        _eventlocation=(EditText) findViewById(R.id.eventlocation);
        _eventsubmit=(Button)findViewById(R.id.eventsubmit);
    }
}
