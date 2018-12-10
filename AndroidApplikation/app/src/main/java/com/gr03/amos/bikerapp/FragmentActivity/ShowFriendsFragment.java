package com.gr03.amos.bikerapp.FragmentActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.RecylerViewAdapter.ShowFriendsListRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.ShowEventActivity;
import com.gr03.amos.bikerapp.ShowEventRecylerViewAdapter;

import io.realm.Realm;
import io.realm.RealmResults;

public class ShowFriendsFragment extends Fragment {
    RecyclerView showFriendsRecyclerView;
    ShowFriendsListRecyclerViewAdapter showFriendsListRecyclerViewAdapter;

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



        Requests.getJsonResponseForFriends("getFriends/1", container.getContext());

        Realm.init(container.getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Friend> friends = realm.where(Friend.class).findAll();


        showFriendsRecyclerView = view.findViewById(R.id.showFriends);
        showFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        showFriendsListRecyclerViewAdapter = new ShowFriendsListRecyclerViewAdapter(container.getContext(), friends);
        showFriendsRecyclerView.setAdapter(showFriendsListRecyclerViewAdapter);

        return view;
    }

}
