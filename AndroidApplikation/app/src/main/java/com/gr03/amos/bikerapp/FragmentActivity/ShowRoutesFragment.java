package com.gr03.amos.bikerapp.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.widget.Spinner;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Adapters.ShowFriendsRoutesRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Adapters.ShowRoutesRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.Models.Address;
import com.gr03.amos.bikerapp.Models.User;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ShowRoutesFragment extends Fragment {

    RecyclerView showRoutesRecyclerView;
    ShowRoutesRecyclerViewAdapter showRoutesRecyclerViewAdapter;
    ShowFriendsRoutesRecyclerViewAdapter showFriendsRoutesRecyclerViewAdapter;
    private ImageView friendRoutesImage;
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
        friendRoutesImage = view.findViewById(R.id.friend_route_filter);
        Realm.init(container.getContext());
        Realm realm = Realm.getDefaultInstance();
        int userID = SaveSharedPreference.getUserID(container.getContext());
        Requests.getJsonResponseForUser("getUserById/" + userID, container.getContext());
        Requests.getJsonResponseForRoutes("getRoutes", container.getContext());
        User user = realm.where(User.class).equalTo("id_user", userID).findFirst();
        RealmResults<Route> routes = realm.where(Route.class).equalTo("start.address.city", user.getAddress().getCity()).findAll();
        populateRecyclerView(routes);

        friendRoutesImage.setOnClickListener(v -> getFriendsRoutes(container.getContext()));
        return view;
    }

    private void populateRecyclerView(RealmResults<Route> routes) {
        showRoutesRecyclerView = view.findViewById(R.id.showRoutes);
        showRoutesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showRoutesRecyclerViewAdapter = new ShowRoutesRecyclerViewAdapter(getContext(), routes);
        showRoutesRecyclerView.setAdapter(showRoutesRecyclerViewAdapter);
    }

    protected void getFriendsRoutes(Context context) {


        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        Requests.getJsonResponseForFriendsRoutes("getFriendsRoutes/" +
                SaveSharedPreference.getUserID(context), context);
        RealmResults<Friend> friends = realm.where(Friend.class).findAll();

        RealmList<Route> routes = new RealmList<>();;
        for (Friend friend : friends) {
            routes.add(friend.getRoute());
        }

        showRoutesRecyclerView = view.findViewById(R.id.showRoutes);
        showRoutesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showFriendsRoutesRecyclerViewAdapter = new ShowFriendsRoutesRecyclerViewAdapter(getContext(), routes);
        showRoutesRecyclerView.setAdapter(showFriendsRoutesRecyclerViewAdapter);
    }
}
