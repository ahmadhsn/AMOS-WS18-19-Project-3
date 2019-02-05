package com.gr03.amos.bikerapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.NetworkLayer.DefaultResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmList;


public class ShowMyRouteRecyclerViewAdapter extends RecyclerView.Adapter<ShowMyRouteRecyclerViewAdapter.ViewHolder> {

    private RealmList<Route> mData;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    public ShowMyRouteRecyclerViewAdapter(Context context, RealmList<Route> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.show_my_route_row, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.routeName.setText(mData.get(position).getName());
        holder.routeDescription.setText(mData.get(position).getDescription());

        holder.unlikeRoute.setOnClickListener(v -> {
            unlikeRoute(position);
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size());
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void unlikeRoute(int position) {
        JSONObject json = new JSONObject();
        try {
            json.put("route_id", mData.get(position).getId_route());
            json.put("user_id", SaveSharedPreference.getUserID(context));

            Requests.executeRequest(new DefaultResponseHandler(), "POST", "unlikeRoute", json, context);

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();

            //delete event participation in Realm Database
            realm.executeTransaction(realm1 -> {
                Route toEdit = realm1.where(Route.class)
                        .equalTo("id_route", mData.get(position).getId_route())
                        .findFirst();
                toEdit.setLiked(false);
            });
            Log.i("After Transaction from Realm 1", "Unliked Route");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView routeName;
        TextView routeDescription;
        Button unlikeRoute;

        ViewHolder(View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.route_name);
            routeDescription = itemView.findViewById(R.id.route_description);
            unlikeRoute = itemView.findViewById(R.id.unlike_route);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
        }

    }
}

