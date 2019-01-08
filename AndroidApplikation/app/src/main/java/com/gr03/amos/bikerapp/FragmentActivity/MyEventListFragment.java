package com.gr03.amos.bikerapp.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.widget.Spinner;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Models.Address;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.Adapters.ShowEventRecylerViewAdapter;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;


public class MyEventListFragment extends Fragment {
    RecyclerView myEventsRecyclerView;
    ShowEventRecylerViewAdapter myEventRecyclerViewAdapter;
    private View view;

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

        Requests.getJsonResponse("myEvents/" + SaveSharedPreference.getUserID(container.getContext()), container.getContext());
        RealmResults<Event> events = realm.where(Event.class).findAll();
        populateRecyclerView(events);

        return view;
    }

    private void populateRecyclerView(RealmResults<Event> events) {
        myEventsRecyclerView = view.findViewById(R.id.myEvents);
        myEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myEventRecyclerViewAdapter = new ShowEventRecylerViewAdapter(getContext(), events);
        myEventsRecyclerView.setAdapter(myEventRecyclerViewAdapter);
    }

}
