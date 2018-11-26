package com.gr03.amos.bikerapp.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.ShowEventRecylerViewAdapter;

import io.realm.Realm;
import io.realm.RealmResults;


public class ShowEventsFragment extends Fragment {
    RecyclerView showEventsRecyclerView;
    ShowEventRecylerViewAdapter showEventRecylerViewAdapter;

    public ShowEventsFragment() {
    }

    public static ShowEventsFragment newInstance(String param1, String param2) {
        ShowEventsFragment fragment = new ShowEventsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_events, container, false);
        Realm.init(container.getContext());
        Realm realm = Realm.getDefaultInstance();

        Requests.getJsonResponse("getEvents", container.getContext());
        RealmResults<Event> events = realm.where(Event.class).findAll();

        showEventsRecyclerView = view.findViewById(R.id.showEvents);
        showEventsRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        showEventRecylerViewAdapter = new ShowEventRecylerViewAdapter(container.getContext(), events);
        showEventsRecyclerView.setAdapter(showEventRecylerViewAdapter);

        return view;
    }


}
