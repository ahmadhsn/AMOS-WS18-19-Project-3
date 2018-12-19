package com.gr03.amos.bikerapp.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gr03.amos.bikerapp.Models.Address;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.ShowEventRecylerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class ShowEventsFragment extends Fragment {
    RecyclerView showEventsRecyclerView;
    ShowEventRecylerViewAdapter showEventRecylerViewAdapter;
    private ImageView eventFilterImage;
    private Spinner countries;
    private Spinner cities;
    private View view;
    List<String> country = new ArrayList<>();
    List<String> city = new ArrayList<>();

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
        view = inflater.inflate(R.layout.fragment_show_events, container, false);

        eventFilterImage = view.findViewById(R.id.event_filter);
        city.add("Choose a City");
        country.add("Choose a Country");

        Realm.init(container.getContext());
        Realm realm = Realm.getDefaultInstance();

        Requests.getJsonResponse("getEvents", container.getContext());
        RealmResults<Event> events = realm.where(Event.class).findAll();

        RealmResults<Address> countries = realm.where(Address.class).distinct("country").findAll();
        RealmResults<Address> cities = realm.where(Address.class).distinct("city").findAll();

        for (Address address : countries) {
            country.add(address.getCountry());
        }

        for (Address address : cities) {
            city.add(address.getCity());
        }

        populateRecyclerView(events);
        eventFilterImage.setOnClickListener(v -> showInputDialog());

        return view;
    }

    private void populateRecyclerView(RealmResults<Event> events) {
        showEventsRecyclerView = view.findViewById(R.id.showEvents);
        showEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showEventRecylerViewAdapter = new ShowEventRecylerViewAdapter(getContext(), events);
        showEventsRecyclerView.setAdapter(showEventRecylerViewAdapter);
    }

    protected void showInputDialog() {


    }


}
