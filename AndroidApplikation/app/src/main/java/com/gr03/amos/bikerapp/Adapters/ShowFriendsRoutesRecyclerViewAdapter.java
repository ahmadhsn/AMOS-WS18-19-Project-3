package com.gr03.amos.bikerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.R;

import org.json.JSONException;

import io.realm.RealmList;

public class ShowFriendsRoutesRecyclerViewAdapter extends RecyclerView.Adapter<ShowFriendsRoutesRecyclerViewAdapter.ViewHolder> {

    private RealmList<Route> mData;
    private LayoutInflater mInflater;
    private Context context;
    private String startPoint;
    private String endPoint;
    private String map;

    // data is passed into the constructor
    public ShowFriendsRoutesRecyclerViewAdapter(Context context, RealmList<Route> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.show_route_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.likeRoute.setVisibility(View.GONE);
        holder.routeName.setText(mData.get(position).getName());
        holder.routeDescription.setText(mData.get(position).getDescription());
        holder.startPoint
                .setText("Start Point: "
                        + mData.get(position).getStart().getAddress().getStreet()
                        + ", " + mData.get(position).getStart().getAddress().getHouse_number()
                        + ", " + mData.get(position).getStart().getAddress().getPostcode()
                        + ", " + mData.get(position).getStart().getAddress().getCity()
                        + ", " + mData.get(position).getStart().getAddress().getCountry());
        holder.endPoint
                .setText("End Point: "
                        + mData.get(position).getEnd().getAddress().getStreet()
                        + ", " + mData.get(position).getEnd().getAddress().getHouse_number()
                        + ", " + mData.get(position).getEnd().getAddress().getPostcode()
                        + ", " + mData.get(position).getEnd().getAddress().getCity()
                        + ", " + mData.get(position).getEnd().getAddress().getCountry());

        holder.routeDropDownButton.setOnClickListener(v -> {
            holder.routeDescription.setVisibility(View.VISIBLE);
            holder.startPoint.setVisibility(View.VISIBLE);
            holder.endPoint.setVisibility(View.VISIBLE);
            holder.routeDropDownButton.setVisibility(View.GONE);
            holder.mapRoute.setVisibility(View.VISIBLE);
            holder.routeDropUpButton.setVisibility(View.VISIBLE);
            holder.dividerView.setVisibility(View.VISIBLE);
        });

        holder.routeDropUpButton.setOnClickListener(v -> {
            holder.routeDescription.setVisibility(View.GONE);
            holder.startPoint.setVisibility(View.GONE);
            holder.endPoint.setVisibility(View.GONE);
            holder.mapRoute.setVisibility(View.GONE);
            holder.routeDropDownButton.setVisibility(View.VISIBLE);
            holder.routeDropUpButton.setVisibility(View.GONE);
            holder.dividerView.setVisibility(View.GONE);
        });

        holder.mapRoute.setOnClickListener(v -> {

            try {
                startPoint= mData.get(position).getStart().getAddress().getStreet()
                        + " " + mData.get(position).getStart().getAddress().getHouse_number()
                        + " " + mData.get(position).getStart().getAddress().getPostcode()
                        + " " + mData.get(position).getStart().getAddress().getCity()
                        + "," + mData.get(position).getStart().getAddress().getCountry();

                endPoint= mData.get(position).getEnd().getAddress().getStreet()
                        + " " + mData.get(position).getEnd().getAddress().getHouse_number()
                        + " " + mData.get(position).getEnd().getAddress().getPostcode()
                        + " " + mData.get(position).getEnd().getAddress().getCity()
                        + "," + mData.get(position).getEnd().getAddress().getCountry();

                //  map = "http://maps.google.com/maps?saddr=" + startPoint + "&daddr=" + endPoint;
                map = "https://www.google.com/maps/dir/?api=1&origin="+ startPoint + "&destination=" + endPoint;

                routeMap(map);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView routeName;
        TextView routeDescription;
        TextView startPoint;
        TextView endPoint;
        ImageView routeDropDownButton;
        ImageView routeDropUpButton;
        LinearLayout controlLinearLayout;
        View dividerView;
        TextView adminTag;
        ImageButton route_delete;
        ImageButton routeEdit;
        Button likeRoute;
        Button mapRoute;

        ViewHolder(View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.route_name);
            routeDescription = itemView.findViewById(R.id.route_description);
            startPoint = itemView.findViewById(R.id.route_start_point);
            endPoint = itemView.findViewById(R.id.route_end_point);
            routeDropDownButton = itemView.findViewById(R.id.arrow_down);
            routeDropUpButton = itemView.findViewById(R.id.arrow_up);
            route_delete = itemView.findViewById(R.id.route_delete);
            routeEdit = itemView.findViewById(R.id.route_edit);
            controlLinearLayout = itemView.findViewById(R.id.controls_button);
            dividerView = itemView.findViewById(R.id.divider);
            adminTag = itemView.findViewById(R.id.admin_tag);
            likeRoute = itemView.findViewById(R.id.like_route);
            mapRoute= itemView.findViewById(R.id.map_route);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }

    }
    private void routeMap(String mapAddress) throws JSONException {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(mapAddress));
        context.startActivity(i);
    }
}

