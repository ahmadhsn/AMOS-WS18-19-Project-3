package com.gr03.amos.bikerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Adapters.UserAdapter;
import com.gr03.amos.bikerapp.Models.BasicUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class SearchUserActivity extends AppCompatActivity {

    EditText userName;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends);

        userName = findViewById(R.id.userName);
        listView = findViewById(R.id.user_result);
    }

    public void searchUsers(View view) {
        String url = "searchUser";

        try {
            JSONObject response;

            //TODO remove and replace with session id

            FutureTask<String> taskID = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getResponse("getUserID/" + SaveSharedPreference.getUserEmail(getApplicationContext()), null,"GET");
                    return threadResponse.toString();
                }
            });

            new Thread(taskID).start();
            Log.i("Response", taskID.get());

            response = new JSONObject(taskID.get());

            int userID;
            if(response.has("getUserID") && response.getJSONObject("getUserID").has("user_id")){
                userID= response.getJSONObject("getUserID").getInt("user_id");
            }else{
                Toast.makeText(getApplicationContext(), "Internal Problem.", Toast.LENGTH_LONG).show();

                Log.i("GetUserByID", "No User found with session mail.");
                return;
            }
            //TODO end remove and replace with session id

            JSONObject request = new JSONObject();
            request.put("id", userID);
            request.put("input",userName.getText());

            FutureTask<String> task = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getResponse(url, request,"POST");
                    return threadResponse.toString();
                }
            });


            new Thread(task).start();
            Log.i("Response", task.get());

            /* show all users clickable */

            response = new JSONObject(task.get());

            if (response.has("foundUser")) {
                String statusEv = (String) response.get("foundUser");
                if (statusEv.equals("unsuccessful")) {
                    listUsers(new JSONArray());
                    Toast.makeText(getApplicationContext(), "No such User.", Toast.LENGTH_LONG).show();
                    return;
                }

                Log.i("allUser:", response.toString());
                listUsers(response.getJSONArray("user"));
            }

        } catch (Exception e) {
            //TODO: Error-Handling
            Log.i("Exception --- not requested", e.toString());
        }
    }


    private void listUsers(JSONArray users){
        ArrayList<BasicUser> arrayUsers = new ArrayList<>();

        for(int i=0; i<users.length(); i++){
            try {
                JSONObject curr = users.getJSONObject(i);
                if(curr.has("friendstatus") && curr.getString("friendstatus").equals("friends")){
                    arrayUsers.add(new BasicUser(curr.getLong("id"), curr.getString("first_name"), curr.getString("last_name"), curr.getString("email"), true));
                }else {
                    arrayUsers.add(new BasicUser(curr.getLong("id"), curr.getString("first_name"), curr.getString("last_name"), curr.getString("email")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        UserAdapter userAdapter = new UserAdapter(this, arrayUsers);
        listView.setAdapter(userAdapter);
        listView.setOnItemClickListener(userAdapter);
    }



}


