package com.gr03.amos.bikerapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.R;

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
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView routeName;
        TextView routeDescription;

        ViewHolder(View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.route_name);
            routeDescription = itemView.findViewById(R.id.route_description);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
        }

    }
}

