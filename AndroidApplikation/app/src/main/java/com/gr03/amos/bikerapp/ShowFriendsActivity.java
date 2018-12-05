package com.gr03.amos.bikerapp;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        JSONObject json = new JSONObject();
        json.put("searchUsers", userName.getText());
        String url = "searchUser/" + userName.getText();

        try {
            JSONObject response;

            FutureTask<String> task = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getResponse(url, null);
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
                listUsers(response.getJSONArray("users"));
            }

        } catch (Exception e) {
            //TODO: Error-Handling
            Log.i("Exception --- not requested", e.toString());
        }
    }


    private void listUsers(JSONArray users){
        UserAdapter userAdapter = new UserAdapter();
        listView.setAdapter(userAdapter);
    }

    class UserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return user_results.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.show_user_row, null);

            TextView name = (TextView)convertView.findViewById(R.id.name);
            TextView email = (TextView)convertView.findViewById(R.id.email);

            try {
                JSONObject user = user_results.getJSONObject(position);
                name.setText(user.get("firstName") + " " + user.get("lastName"));
                email.setText((String)user.get("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}


