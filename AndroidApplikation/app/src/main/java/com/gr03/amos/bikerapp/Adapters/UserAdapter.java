package com.gr03.amos.bikerapp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Models.BasicUser;
import com.gr03.amos.bikerapp.ProfileBasicUserActivity;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<BasicUser> implements AdapterView.OnItemClickListener{
    ShowFriendsListRecyclerViewAdapter showFriendsListRecyclerViewAdapter;

    public UserAdapter(Context context, ArrayList<BasicUser> users, RecyclerView showFriendsRecyclerView, ShowFriendsListRecyclerViewAdapter showFriendsListRecyclerViewAdapter ) {
        super(context, 0, users);
        this.showFriendsListRecyclerViewAdapter = showFriendsListRecyclerViewAdapter;
    }

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

        //enable and disable addFriend and isFriend buttons
        if(user.isFriends()){
            ImageButton btAddFriend = (ImageButton) convertView.findViewById(R.id.add_friend);
            btAddFriend.setVisibility(View.GONE);
        }else{
            ImageView btIsFriend = (ImageView) convertView.findViewById(R.id.is_friend);
            btIsFriend.setVisibility(View.GONE);
        }

        //listener for add friend button
        ImageButton btAddFriend = (ImageButton) convertView.findViewById(R.id.add_friend);
        btAddFriend.setTag(position);
        btAddFriend.setOnClickListener(new AddFriendOnClickListener());

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Long userId = super.getItem(position).getUser_id();
        Intent intent = new Intent(super.getContext(), ProfileBasicUserActivity.class);
        intent.putExtra("id", userId);
        super.getContext().startActivity(intent);
    }

    class AddFriendOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            int position = (Integer) view.getTag();
            BasicUser user = getItem(position);

            JSONObject friendRequest = new JSONObject();

            try {
                JSONObject response;

                friendRequest.put("idUser", SaveSharedPreference.getUserID(view.getContext()));
                friendRequest.put("idFollower", Long.toString(user.getUser_id()));
                response = Requests.getResponse("addFriend", friendRequest, "POST", getContext());

                //handle response
                if(response != null && response.has("friendship")){
                    String friendshipStatus = response.getString("friendship");
                    if(friendshipStatus.equals("successful")){
                        String msg = String.format("You added %s as a friend!", user.getName());
                        //delete user from search results
                        UserAdapter.this.remove(getItem(position));
                        UserAdapter.this.notifyDataSetChanged();
                        //add user to friendslist
                        UserAdapter.this.showFriendsListRecyclerViewAdapter.addFriend(user);
                    }else if(friendshipStatus.equals("internalProblems")){
                        Toast.makeText(UserAdapter.super.getContext(), "Internal Problem", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(UserAdapter.super.getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}