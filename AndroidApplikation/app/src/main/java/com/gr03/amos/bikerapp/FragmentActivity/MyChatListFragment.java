package com.gr03.amos.bikerapp.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gr03.amos.bikerapp.Adapters.MessageListRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Adapters.ShowConversationWithFriendsListRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.Models.Message;
import com.gr03.amos.bikerapp.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyChatListFragment extends Fragment {

    RecyclerView messageListRecyclerViewAdapter;
    ShowConversationWithFriendsListRecyclerViewAdapter showConversationWithFriendsListRecyclerViewAdapter;
    private View view;
    Message message;


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

      /*  TODO

               RealmList<Event> events = new RealmList<>();
        for (EventParticipation eventParticipation : eventParticipations) {
            message = realm.where(Event.class)
                    .equalTo("id_event", eventParticipation.getId_event())
                    .findFirst();
            Log.i("EventDesc", message.toString());
            events.add(message);
        }

        populateRecyclerView(events);
        */
        populateRecyclerView(friends);
        return view;
    }

    private void populateRecyclerView(RealmResults<Friend> friendConversation) {
        messageListRecyclerViewAdapter = view.findViewById(R.id.myChats);
        messageListRecyclerViewAdapter.setLayoutManager(new LinearLayoutManager(getContext()));
        showConversationWithFriendsListRecyclerViewAdapter = new ShowConversationWithFriendsListRecyclerViewAdapter(getContext(), friendConversation);
        messageListRecyclerViewAdapter.setAdapter(showConversationWithFriendsListRecyclerViewAdapter);
    }

}
