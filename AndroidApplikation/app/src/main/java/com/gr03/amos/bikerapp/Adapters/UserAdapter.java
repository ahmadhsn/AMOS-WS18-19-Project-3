package com.gr03.amos.bikerapp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.EventDetailsActivity;
import com.gr03.amos.bikerapp.Models.BasicUser;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class UserAdapter extends ArrayAdapter<BasicUser> {

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

        //listener for add friend button
        ImageButton btAddFriend = (ImageButton) convertView.findViewById(R.id.add_friend);
        btAddFriend.setTag(position);
        btAddFriend.setOnClickListener(new AddFriendOnClickListener());

        //onitemlistenenr for each user
        ListView userList = (ListView) convertView.findViewById(R.id.user_result);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Long userId = UserAdapter.super.getItem(position).getUser_id();
                //Intent intent = new Intent(UserAdapter.super.getContext(), EventDetailsActivity.class);
                //intent.putExtra("id", userId);
                //UserAdapter.super.getContext().startActivity(intent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    class AddFriendOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            int position = (Integer) view.getTag();
            BasicUser user = getItem(position);

            JSONObject friendRequest = new JSONObject();

            try {
                JSONObject response;

                //TODO remove and take userID from session instead
                Requests.getResponse("getUserId/" + SaveSharedPreference.getUserEmail(getContext()), null,"GET");
                FutureTask<String> taskID = new FutureTask(new Callable<String>() {
                    public String call() {
                        JSONObject threadResponse = Requests.getResponse("getUserID/" + SaveSharedPreference.getUserEmail(getContext()), null,"GET");
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
                    Toast.makeText(UserAdapter.super.getContext(), "Internal Problem.", Toast.LENGTH_LONG).show();

                    Log.i("GetUserByID", "No User found with session mail.");
                    return;
                }
                //TODO end remove

                friendRequest.put("idUser", userID);
                friendRequest.put("idFollower", Long.toString(user.getUser_id()));

                FutureTask<String> task = new FutureTask(new Callable<String>() {
                    public String call() {
                        JSONObject threadResponse = Requests.getResponse("addFriend", friendRequest);
                        return threadResponse.toString();
                    }
                });
                new Thread(task).start();
                Log.i("Response", task.get());
                response = new JSONObject(task.get());

                //handle response
                if(response.has("friendship")){
                    String friendshipStatus = response.getString("friendship");
                    if(friendshipStatus.equals("successful")){
                        String msg = String.format("You added %s as a friend!", user.getName());
                        Toast.makeText(UserAdapter.super.getContext(), msg, Toast.LENGTH_LONG).show();
                    }else if(friendshipStatus.equals("internalProblems")){
                        Toast.makeText(UserAdapter.super.getContext(), "Internal Problem", Toast.LENGTH_LONG).show();
                    }
                }else{
                    //TODO remove only for debugging
                    Toast.makeText(UserAdapter.super.getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }
    }
}