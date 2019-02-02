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
import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.Models.User;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.Adapters.ShowEventRecylerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class ShowEventsFragment extends Fragment implements ResponseHandler {
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

        city.add("Choose a City");
        country.add("Choose a Country");


        Requests.getJsonResponseForEvents("getEvents", SaveSharedPreference.getUserID(container.getContext()), container.getContext(), this);

        return view;
    }


    private void populateRecyclerView(RealmResults<Event> events) {
        showEventsRecyclerView = view.findViewById(R.id.showEvents);
        showEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showEventRecylerViewAdapter = new ShowEventRecylerViewAdapter(getContext(), events);
        showEventsRecyclerView.setAdapter(showEventRecylerViewAdapter);
    }

    protected void showInputDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.event_filter_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        countries = promptView.findViewById(R.id.country_spinner);
        cities = promptView.findViewById(R.id.city_spinner);

        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false).setTitle("Select Filter")
                .setPositiveButton("OK", (dialog, id) -> {
                    Realm.init(Objects.requireNonNull(getContext()));
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Event> result = null;
                    if (!countries.getSelectedItem().toString().equals("Choose a Country")
                            && cities.getSelectedItem().toString().equals("Choose a City")) {
                        result = realm.where(Event.class)
                                .equalTo("address.country", countries.getSelectedItem().toString()).findAll();
                        populateRecyclerView(result);
                    } else if (!cities.getSelectedItem().toString().equals("Choose a City")
                            && countries.getSelectedItem().toString().equals("Choose a Country")) {
                        result = realm.where(Event.class)
                                .equalTo("address.city", cities.getSelectedItem().toString()).findAll();
                        populateRecyclerView(result);
                    } else if (!cities.getSelectedItem().toString().equals("Choose a City")
                            && !countries.getSelectedItem().toString().equals("Choose a Country")) {
                        result = realm.where(Event.class)
                                .equalTo("address.city", cities.getSelectedItem().toString())
                                .and()
                                .equalTo("address.country", countries.getSelectedItem().toString())
                                .findAll();
                        populateRecyclerView(result);
                        if (result.size() == 0) {
                            Toast.makeText(getContext(),
                                    "No Result(s) Found",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(),
                                "Nothing Selected",
                                Toast.LENGTH_LONG).show();
                    }

                })
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

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if (SocketUtility.checkRequestSuccessful(getContext(), response)) {

            if (urlTail.contains("getEvents")) {
                Realm.init(getContext());
                Realm realm = Realm.getDefaultInstance();

                RealmResults<Event> events = realm.where(Event.class).sort("id_user_type", Sort.DESCENDING).findAll();

                RealmResults<Address> countries = realm.where(Address.class).distinct("country").findAll();
                RealmResults<Address> cities = realm.where(Address.class).distinct("city").findAll();

                for (Address address : countries) {
                    country.add(address.getCountry());
                }

                for (Address address : cities) {
                    city.add(address.getCity());
                }

                if (SaveSharedPreference.getUserType(getContext()) == 2) {
                    RealmResults<Event> businessUserEvents = realm
                            .where(Event.class)
                            .equalTo("id_user", SaveSharedPreference.getUserID(getContext()))
                            .findAll();

                    populateRecyclerView(businessUserEvents);
                } else {
                    populateRecyclerView(events);
                    eventFilterImage = view.findViewById(R.id.event_filter);
                    eventFilterImage.setOnClickListener(v -> showInputDialog());
                }

            }
        }
    }


}
