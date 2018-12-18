package com.gr03.amos.bikerapp.FragmentActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Adapters.UserAdapter;
import com.gr03.amos.bikerapp.Models.BasicUser;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.RecylerViewAdapter.ShowFriendsListRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;
import com.gr03.amos.bikerapp.SearchUserActivity;
import com.gr03.amos.bikerapp.ShowEventActivity;
import com.gr03.amos.bikerapp.ShowEventRecylerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import io.realm.Realm;
import io.realm.RealmResults;

public class ShowFriendsFragment extends Fragment {
    RecyclerView showFriendsRecyclerView;
    ShowFriendsListRecyclerViewAdapter showFriendsListRecyclerViewAdapter;

    EditText userName;
    ListView listView;

    public ShowFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_friends, container, false);

        userName = view.findViewById(R.id.userName);
        listView = view.findViewById(R.id.user_result);
        Activity currActivity = getActivity();
        //Set Title to My Friends
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Friendslist");

        Requests.getJsonResponseForFriends("getFriends/" + SaveSharedPreference.getUserID(container.getContext()), container.getContext());

        Realm.init(container.getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Friend> friends = realm.where(Friend.class).findAll();

        showFriendsRecyclerView = view.findViewById(R.id.showFriends);
        showFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        showFriendsListRecyclerViewAdapter = new ShowFriendsListRecyclerViewAdapter(container.getContext(), friends);
        showFriendsRecyclerView.setAdapter(showFriendsListRecyclerViewAdapter);

        //set onClickListener for Button
        Button btSearch = (Button) view.findViewById(R.id.searchUsers);
        btSearch.setOnClickListener(new SearchOnClickListener());
        return view;
    }

    class SearchOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String url = "searchUser";

            try {
                JSONObject response;

                JSONObject request = new JSONObject();
                request.put("id", SaveSharedPreference.getUserID(v.getContext()));
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
                        listUsers(new JSONArray(), v);
                        Toast.makeText(v.getContext(), "No such User.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Log.i("allUser:", response.toString());
                    listUsers(response.getJSONArray("user"), v);
                }

            } catch (Exception e) {
                Log.i("Exception --- not requested", e.toString());
            }

        }

        private void listUsers(JSONArray users, View view){
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
            UserAdapter userAdapter = new UserAdapter(view.getContext(), arrayUsers, showFriendsRecyclerView, showFriendsListRecyclerViewAdapter);
            listView.setAdapter(userAdapter);
            listView.setOnItemClickListener(userAdapter);
        }
    }

}
