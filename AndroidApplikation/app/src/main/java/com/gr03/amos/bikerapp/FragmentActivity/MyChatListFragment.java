package com.gr03.amos.bikerapp.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gr03.amos.bikerapp.Adapters.ShowConversationWithFriendsListRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Models.Chat;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.Models.Message;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class MyChatListFragment extends Fragment implements ResponseHandler {

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
        //Realm.init(container.getContext());
        //Realm realm = Realm.getDefaultInstance();
        //RealmResults<Friend> friends = realm.where(Friend.class).findAll();
        Requests.getJSONResponseForChats(SaveSharedPreference.getUserID(getContext()), getContext(), this);
        //populateRecyclerView(friends);
        return view;
    }

    private void populateRecyclerView(RealmResults<Chat> chats) {
        messageListRecyclerViewAdapter = view.findViewById(R.id.myChats);
        messageListRecyclerViewAdapter.setLayoutManager(new LinearLayoutManager(getContext()));
        chats = chats.sort("last_send", Sort.DESCENDING);
        //friendConversation = friendConversation.sort("last_message_time", Sort.DESCENDING);
        showConversationWithFriendsListRecyclerViewAdapter = new ShowConversationWithFriendsListRecyclerViewAdapter(getContext(), chats);
        messageListRecyclerViewAdapter.setAdapter(showConversationWithFriendsListRecyclerViewAdapter);
    }

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if (SocketUtility.checkRequestSuccessful(getContext(), response)) {

            if (urlTail.contains("getChats")) {
                Realm.init(getContext());
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Chat> chats = realm.where(Chat.class).findAll();
                populateRecyclerView(chats);
            }

        }
    }
}
