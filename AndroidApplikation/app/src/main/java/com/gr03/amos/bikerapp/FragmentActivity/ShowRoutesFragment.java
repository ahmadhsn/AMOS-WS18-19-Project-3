package com.gr03.amos.bikerapp.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.widget.Spinner;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Adapters.ShowRoutesRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.Models.Address;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import io.realm.Realm;
import io.realm.RealmResults;

public class ShowRoutesFragment extends Fragment {

    RecyclerView showRoutesRecyclerView;
    ShowRoutesRecyclerViewAdapter showRoutesRecyclerViewAdapter;
    private View view;

    public ShowRoutesFragment() {
    }

    public static ShowRoutesFragment newInstance(String param1, String param2) {
        ShowRoutesFragment fragment = new ShowRoutesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_show_routes, container, false);

        Realm.init(container.getContext());
        Realm realm = Realm.getDefaultInstance();
        Requests.getJsonResponseForRoutes("getRoutes", container.getContext());
        RealmResults<Route> routes = realm.where(Route.class).findAll();
        populateRecyclerView(routes);
        return view;
    }

    private void populateRecyclerView(RealmResults<Route> routes) {
        showRoutesRecyclerView = view.findViewById(R.id.showRoutes);
        showRoutesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showRoutesRecyclerViewAdapter = new ShowRoutesRecyclerViewAdapter(getContext(), routes);
        showRoutesRecyclerView.setAdapter(showRoutesRecyclerViewAdapter);
    }

}
