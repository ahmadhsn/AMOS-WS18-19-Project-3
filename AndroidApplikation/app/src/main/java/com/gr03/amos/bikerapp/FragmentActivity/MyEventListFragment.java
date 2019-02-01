package com.gr03.amos.bikerapp.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gr03.amos.bikerapp.Adapters.ShowMyEventRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.R;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class MyEventListFragment extends Fragment {
    RecyclerView myEventsRecyclerView;
    ShowMyEventRecyclerViewAdapter showMyEventRecyclerViewAdapter;
    private View view;
    Event event;


    public MyEventListFragment() {
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
        view = inflater.inflate(R.layout.fragment_my_event_list, container, false);
        Realm.init(container.getContext());
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Event> eventParticipating = realm.where(Event.class).equalTo("is_participant", true).findAll();
        RealmList<Event> events = new RealmList<>();
        events.addAll(eventParticipating);

        populateRecyclerView(events);

        return view;
    }

    private void populateRecyclerView(RealmList<Event> events) {
        myEventsRecyclerView = view.findViewById(R.id.myEvents);
        myEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showMyEventRecyclerViewAdapter = new ShowMyEventRecyclerViewAdapter(getContext(), events);
        myEventsRecyclerView.setAdapter(showMyEventRecyclerViewAdapter);
    }

}
