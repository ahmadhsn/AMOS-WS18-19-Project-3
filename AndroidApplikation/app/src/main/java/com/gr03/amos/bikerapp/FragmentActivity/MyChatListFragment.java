package com.gr03.amos.bikerapp.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gr03.amos.bikerapp.Adapters.ShowConversationWithFriendsListRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.Models.Message;
import com.gr03.amos.bikerapp.R;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class MyChatListFragment extends Fragment {

    RecyclerView messageListRecyclerViewAdapter;
    ShowConversationWithFriendsListRecyclerViewAdapter showConversationWithFriendsListRecyclerViewAdapter;
    private View view;

    public MyChatListFragment() {
    }

    public static MyEventListFragment newInstance(String param1, String param2) {
        MyEventListFragment fragment = new MyEventListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_chat_list, container, false);
        Realm.init(container.getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Friend> friends = realm.where(Friend.class).findAll();
        populateRecyclerView(friends);
        return view;
    }

    private void populateRecyclerView(RealmResults<Friend> friendConversation) {
        messageListRecyclerViewAdapter = view.findViewById(R.id.myChats);
        messageListRecyclerViewAdapter.setLayoutManager(new LinearLayoutManager(getContext()));
        friendConversation = friendConversation.sort("last_message_time", Sort.DESCENDING);
        showConversationWithFriendsListRecyclerViewAdapter = new ShowConversationWithFriendsListRecyclerViewAdapter(getContext(), friendConversation);
        messageListRecyclerViewAdapter.setAdapter(showConversationWithFriendsListRecyclerViewAdapter);
    }

}
