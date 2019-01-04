package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ChatActivity extends AppCompatActivity {

    EditText msg;
    int chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        msg = (EditText) findViewById(R.id.user_message);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //TODO: init message button
        //TODO replace with real chatId
        chatId = -1;

    }

    public void sendMessage(){
        JSONObject json = new JSONObject();

        try {
            json.put("id_user", SaveSharedPreference.getUserID(this));
            json.put("id_chat", chatId);
            json.put("message", msg.getText().toString());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
            String currTime = simpleDateFormat.format(new Date());
            json.put("time", currTime);

            JSONObject response;

            FutureTask<String> task = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getResponse("saveMessage", json);
                    return threadResponse.toString();
                }
            });
            new Thread(task).start();
            Log.i("Response", task.get());
            response = new JSONObject(task.get());
            Log.i("this is response", String.valueOf(response));
            //handle response
            if (response.has("saveMessage") && response.getString("saveMessage").equals("success")) {

                Log.i("IGI", String.valueOf(response));
                Log.i("CHAT", "Message send from " + SaveSharedPreference.getUserID(this));
            }else{
                Toast.makeText(getApplicationContext(), "Could not send message", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error sending message", Toast.LENGTH_LONG).show();
            Log.i("Exception --- not requested", e.toString());
            return;
        }
    }

}
