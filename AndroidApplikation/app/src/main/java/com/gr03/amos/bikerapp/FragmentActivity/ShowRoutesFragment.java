package com.gr03.amos.bikerapp.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Adapters.ShowFriendsRoutesRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Adapters.ShowRoutesRecyclerViewAdapter;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.Models.User;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ShowRoutesFragment extends Fragment implements ResponseHandler {

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
        Requests.getJsonResponseForRoutes("getRoutes/" + userID, container.getContext(), this);

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
        Requests.getJsonResponseForFriendsRoutes("getFriendsRoutes/" +
                SaveSharedPreference.getUserID(context), context, this);
    }

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if (SocketUtility.checkRequestSuccessful(getContext(), response)) {
            int userId = SaveSharedPreference.getUserID((getContext()));
            if (urlTail.equals("getRoutes/" + userId)) {
                onResponseRoutes();
            } else if (urlTail.equals("getFriendsRoutes/" + userId)) {
                onResponseFriendRoutes();
            }
        }
    }

    private void onResponseRoutes() {
        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();

        User user = realm.where(User.class).equalTo("id_user", SaveSharedPreference.getUserID(getContext())).findFirst();
        RealmResults<Route> routes = realm.where(Route.class).equalTo("start.address.city", user.getAddress().getCity()).findAll();
        populateRecyclerView(routes);
    }

    private void onResponseFriendRoutes() {
        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Friend> friends = realm.where(Friend.class).findAll();

        RealmList<Route> routes = new RealmList<>();
        for (Friend friend : friends) {
            if (friend.getRoute() != null) {
                routes.add(friend.getRoute());
            }
        }

        showRoutesRecyclerView = view.findViewById(R.id.showRoutes);
        showRoutesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showFriendsRoutesRecyclerViewAdapter = new ShowFriendsRoutesRecyclerViewAdapter(getContext(), routes);
        showRoutesRecyclerView.setAdapter(showFriendsRoutesRecyclerViewAdapter);
    }
}
