package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Adapters.MessageListRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.Models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

public class ChatActivity extends AppCompatActivity {

    EditText msg;
    int chatId;
    ArrayList<Integer> userIds;
    RecyclerView showMessagesRecyclerView;
    MessageListRecyclerViewAdapter messageListRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Messages");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        msg = findViewById(R.id.user_message);

        //load ids of chat user
        Intent intent = getIntent();
        userIds = new ArrayList<>();
        userIds = intent.getIntegerArrayListExtra("chatUser");
        showMessagesRecyclerView = findViewById(R.id.reyclerview_message_list);
        Log.i("ids", String.valueOf(userIds));

        //load chat
        chatId = loadChat();

//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//
//                Requests.getJsonResponseForChat("getChat", chatId, getApplicationContext());
//            }
//        }, 0, 1000);

        getAllChat();
    }

    private int loadChat() {
        JSONObject request = new JSONObject();

        try {
            JSONArray jsonUserIds = new JSONArray();
            for (int userId : userIds) {
                jsonUserIds.put(userId);
            }
            request.put("id_users", jsonUserIds);

            JSONObject response = Requests.getJSONResponse("loadChat", request, "PUT");

            if (response.has("id_chat")) {
                return response.getInt("id_chat");
            } else {
                Toast.makeText(this, "Error loading chat", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;

    }

    public void sendMessage(View view) {
        JSONObject json = new JSONObject();

        String message = msg.getText().toString();

        if (message == null || message.isEmpty()) {
            //no message to send
            Toast.makeText(view.getContext(), "Type in a message!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            json.put("id_user", SaveSharedPreference.getUserID(this));
            json.put("id_chat", chatId);
            json.put("message", msg.getText().toString());

            String currTime = getCurrentTimestampString();
            json.put("time", currTime);

            JSONObject response = Requests.getJSONResponse("saveMessage", json, "PUT");

            //handle response
            if (response.has("saveMessage") && response.getString("saveMessage").equals("successful")) {

                Log.i("IGI", String.valueOf(response));
                Log.i("CHAT", "Message send from " + SaveSharedPreference.getUserID(this));
                msg.setText("");
            } else {
                Toast.makeText(view.getContext(), "Could not send message", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Toast.makeText(view.getContext(), "Error sending message", Toast.LENGTH_LONG).show();
            Log.i("Exception --- not requested", e.toString());
            return;
        }
    }

    private String getCurrentTimestampString() {
        Calendar calendar = Calendar.getInstance();

        Date now = calendar.getTime();
        Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

        return currentTimestamp.toString();
    }

    private void getAllChat() {
        Requests.getJsonResponseForChat("getChat", chatId, getApplicationContext());

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Message> messages = realm.where(Message.class).equalTo("id_chat", chatId).findAll();

        //set last messages timestamp as last_messages_time from friend in model
        String lastMessagesTime = messages.last().getTime_created();
        for (Integer friendsID : this.userIds) {
            if (!friendsID.equals(SaveSharedPreference.getUserID(this))) {
                Friend friend = realm.where(Friend.class).equalTo("id", friendsID).findFirst();
                realm.beginTransaction();
                friend.setLast_message_time(lastMessagesTime);
                realm.commitTransaction();
            }
        }

        showMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageListRecyclerViewAdapter = new MessageListRecyclerViewAdapter(this, messages);
        showMessagesRecyclerView.setAdapter(messageListRecyclerViewAdapter);
    }


}
