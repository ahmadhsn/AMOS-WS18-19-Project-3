package com.gr03.amos.bikerapp.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gr03.amos.bikerapp.Adapters.ShowMyRouteRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.Models.RouteParticipation;
import com.gr03.amos.bikerapp.R;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class MyRouteListFragment extends Fragment {
    RecyclerView myRoutesRecyclerView;
    ShowMyRouteRecyclerViewAdapter showMyRoutesRecyclerViewAdapter;
    private View view;
    Route route;


    public MyRouteListFragment() {
    }

    public static MyRouteListFragment newInstance(String param1, String param2) {
        MyRouteListFragment fragment = new MyRouteListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_route_list, container, false);
        Realm.init(container.getContext());
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Route> routesLiked = realm.where(Route.class).equalTo("isLiked", true).findAll();

        RealmList<Route> routes = new RealmList<>();
        routes.addAll(routesLiked);

        populateRecyclerView(routes);

        return view;
    }

    private void populateRecyclerView(RealmList<Route> routes) {
        myRoutesRecyclerView = view.findViewById(R.id.myRoutes);
        myRoutesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showMyRoutesRecyclerViewAdapter = new ShowMyRouteRecyclerViewAdapter(getContext(), routes);
        myRoutesRecyclerView.setAdapter(showMyRoutesRecyclerViewAdapter);
    }

}
