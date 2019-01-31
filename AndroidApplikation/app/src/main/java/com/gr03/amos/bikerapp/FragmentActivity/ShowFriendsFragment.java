package com.gr03.amos.bikerapp.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Adapters.UserAdapter;
import com.gr03.amos.bikerapp.Models.BasicUser;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Adapters.ShowFriendsListRecyclerViewAdapter;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

public class ShowFriendsFragment extends Fragment implements SearchView.OnQueryTextListener, ResponseHandler {
    Context context;

    RecyclerView showFriendsRecyclerView;
    ShowFriendsListRecyclerViewAdapter showFriendsListRecyclerViewAdapter;

    SearchView searchUser;
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
        context = container.getContext();

        searchUser = view.findViewById(R.id.searchUser);
        searchUser.setOnQueryTextListener(this);

        //userName = view.findViewById(R.id.userName);
        listView = view.findViewById(R.id.user_result);
        //Set Title to My Friends
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");
        showFriendsRecyclerView = view.findViewById(R.id.showFriends);
        showFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        Requests.getJsonResponseForFriends("getFriends/" + SaveSharedPreference.getUserID(container.getContext()), container.getContext(), this);

        return view;
    }

    private void listUsers(JSONArray users) {
        ArrayList<BasicUser> arrayUsers = new ArrayList<>();
        OrderedRealmCollection<Friend> friends = ShowFriendsFragment.this.showFriendsListRecyclerViewAdapter.getData();

        for (int i = 0; i < users.length(); i++) {

            try {
                JSONObject curr = users.getJSONObject(i);
                if (curr.has("friendstatus") && curr.getString("friendstatus").equals("friends")) {
                    //is already shown in friendslist
                    //do nothing
                    //arrayUsers.add(new BasicUser(curr.getLong("id"), curr.getString("first_name"), curr.getString("last_name"), curr.getString("email"), true));
                } else {
                    arrayUsers.add(new BasicUser(curr.getLong("id"), curr.getString("first_name"), curr.getString("last_name"), curr.getString("email")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        UserAdapter userAdapter = new UserAdapter(context, arrayUsers, showFriendsRecyclerView, showFriendsListRecyclerViewAdapter);
        listView.setAdapter(userAdapter);
        listView.setOnItemClickListener(userAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String url = "searchUser";

        try {
            JSONObject response;

            JSONObject request = new JSONObject();
            request.put("id", SaveSharedPreference.getUserID(context));
            request.put("input", query);

            Requests.executeRequest(this, "POST", url, request);

        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }

        //TODO remove boolean
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if (SocketUtility.hasSocketError(response)) {
            Toast.makeText(getContext(), "No response from server.", Toast.LENGTH_LONG).show();
            return;
        }

        /* show all users clickable */
        if(urlTail.equals("searchUser")) {
            try {
                if (response != null && response.has("foundUser")) {
                    String statusEv = (String) response.get("foundUser");
                    if (statusEv.equals("unsuccessful")) {
                        listUsers(new JSONArray());
                        Toast.makeText(context, "No such User.", Toast.LENGTH_LONG).show();
                    }

                    Log.i("allUser:", response.toString());
                    listUsers(response.getJSONArray("user"));
                }

            } catch (Exception e) {
                Log.i("Exception --- not requested", e.toString());
            }
        }else if(urlTail.equals("getFriends/" + SaveSharedPreference.getUserID(getContext()))){
            onResponseFriends(response);
        }
    }

    private void onResponseFriends(JSONObject response){
        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Friend> friends = realm.where(Friend.class).findAll();

        showFriendsListRecyclerViewAdapter = new ShowFriendsListRecyclerViewAdapter(getContext(), friends);
        showFriendsRecyclerView.setAdapter(showFriendsListRecyclerViewAdapter);


    }
}
