package com.gr03.amos.bikerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Models.BasicUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import io.realm.Realm;

public class ShowFriendsActivity extends AppCompatActivity {

    EditText userName;

    ListView listView;

    JSONArray user_results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends);

        userName = findViewById(R.id.userName);
        listView = findViewById(R.id.user_result);
    }

    public void searchUsers(View view) throws JSONException {
        String url = "searchUser/" + userName.getText();

        try {
            JSONObject response;

            FutureTask<String> task = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getGETResponse(url);
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
                arrayUsers.add(new BasicUser(curr.getLong("user_id"), curr.getString("firstName"), curr.getString("lastName"), curr.getString("email")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        UserAdapter userAdapter = new UserAdapter(this, arrayUsers);
        listView.setAdapter(userAdapter);
    }

    class UserAdapter extends ArrayAdapter<BasicUser> {

        public UserAdapter(Context context, ArrayList<BasicUser> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            BasicUser user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.show_user_row, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.user_name);
            TextView tvHome = (TextView) convertView.findViewById(R.id.user_mail);
            // Populate the data into the template view using the data object
            tvName.setText(user.getName());
            tvHome.setText(user.getEmail());
            // Return the completed view to render on screen
            return convertView;
        }


    }


}


