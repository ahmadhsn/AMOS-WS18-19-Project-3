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

import io.realm.Realm;
import io.realm.RealmResults;


public class ShowEventsFragment extends Fragment {
    RecyclerView showEventsRecyclerView;
    ShowEventRecylerViewAdapter showEventRecylerViewAdapter;
    private ImageView eventFilterImage;
    private Spinner countries;
    private Spinner cities;

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
        View view = inflater.inflate(R.layout.fragment_show_events, container, false);

        eventFilterImage = view.findViewById(R.id.event_filter);


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


        showEventsRecyclerView = view.findViewById(R.id.showEvents);
        showEventsRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        showEventRecylerViewAdapter = new ShowEventRecylerViewAdapter(container.getContext(), events);
        showEventsRecyclerView.setAdapter(showEventRecylerViewAdapter);


        eventFilterImage.setOnClickListener(v -> showInputDialog());

        return view;
    }

    protected void showInputDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.event_filter_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        countries = promptView.findViewById(R.id.country_spinner);
        cities = promptView.findViewById(R.id.city_spinner);

        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false).setTitle("Select Filter")
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss())
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());


        ArrayAdapter countryAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, country);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countries.setAdapter(countryAdapter);

        ArrayAdapter cityAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, city);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cities.setAdapter(cityAdapter);

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }


}
