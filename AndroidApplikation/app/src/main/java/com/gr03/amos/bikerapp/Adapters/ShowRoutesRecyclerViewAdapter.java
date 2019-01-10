package com.gr03.amos.bikerapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gr03.amos.bikerapp.EditRouteActivity;
import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import io.realm.Realm;
import io.realm.RealmResults;

public class ShowRoutesRecyclerViewAdapter extends RecyclerView.Adapter<ShowRoutesRecyclerViewAdapter.ViewHolder> {

    private RealmResults<Route> mData;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    public ShowRoutesRecyclerViewAdapter(Context context, RealmResults<Route> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public ShowRoutesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.show_route_row, parent, false);
        return new ShowRoutesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowRoutesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.routeName.setText(mData.get(position).getName());
        holder.routeDescription.setText(mData.get(position).getDescription());
        holder.startPoint
                .setText("Start Point: "
                        + mData.get(position).getStart().getAddress().getHouse_number()
                        + ", " + mData.get(position).getStart().getAddress().getStreet()
                        + ", " + mData.get(position).getStart().getAddress().getPostcode()
                        + ", " + mData.get(position).getStart().getAddress().getCity()
                        + ", " + mData.get(position).getStart().getAddress().getCountry());

        holder.routeDropDownButton.setOnClickListener(v -> {
            holder.routeDescription.setVisibility(View.VISIBLE);
            holder.startPoint.setVisibility(View.VISIBLE);
            holder.routeDropDownButton.setVisibility(View.GONE);
            holder.routeDropUpButton.setVisibility(View.VISIBLE);
            holder.dividerView.setVisibility(View.VISIBLE);
        });

        holder.routeDropUpButton.setOnClickListener(v -> {
            holder.routeDescription.setVisibility(View.GONE);
            holder.startPoint.setVisibility(View.GONE);
            holder.routeDropDownButton.setVisibility(View.VISIBLE);
            holder.routeDropUpButton.setVisibility(View.GONE);
            holder.dividerView.setVisibility(View.GONE);
        });

        if (mData.get(position).getId_user() == SaveSharedPreference.getUserID(this.context)) {
            holder.adminTag.setVisibility(View.VISIBLE);
            holder.controlLinearLayout.setVisibility(View.VISIBLE);
        }

        holder.routeEdit.setOnClickListener(v -> {
            try {
                editRoute(mData.get(position).getId_route());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        holder.route_delete.setOnClickListener(v -> {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setTitle("Delete Route")
                    .setMessage("Are you sure you want to delete this Route?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        try {
                            deleteRoute(mData.get(position).getId_route());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Realm realmDelete = Realm.getDefaultInstance();
                        final Route route1 = realmDelete
                                .where(Route.class)
                                .equalTo("id_route", mData.get(position).getId_route())
                                .findFirst();

                        realmDelete.beginTransaction();
                        route1.deleteFromRealm();
                        Log.i("After Transaction from Realm 1", "Deleted");
                        realmDelete.commitTransaction();
                        realmDelete.close();
                        notifyDataSetChanged();
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

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
        ImageView routeDropDownButton;
        ImageView routeDropUpButton;
        LinearLayout controlLinearLayout;
        View dividerView;
        TextView adminTag;
        ImageButton route_delete;
        ImageButton routeEdit;

        ViewHolder(View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.route_name);
            routeDescription = itemView.findViewById(R.id.route_description);
            startPoint = itemView.findViewById(R.id.route_start_point);
            routeDropDownButton = itemView.findViewById(R.id.arrow_down);
            routeDropUpButton = itemView.findViewById(R.id.arrow_up);
            route_delete = itemView.findViewById(R.id.route_delete);
            routeEdit = itemView.findViewById(R.id.route_edit);
            controlLinearLayout = itemView.findViewById(R.id.controls_button);
            dividerView = itemView.findViewById(R.id.divider);
            adminTag = itemView.findViewById(R.id.admin_tag);

            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {

        }
    }

    //TO DO: IMPLEMENT DELETE ROUTE FEATURE
    private void deleteRoute(long routeId) throws JSONException {

        JSONObject json = new JSONObject();
        json.put("id_route", routeId);
        try {
            FutureTask<String> task = new FutureTask((Callable<String>) () -> {
                JSONObject threadResponse = Requests.getResponse("deleteRoute", json);
                return threadResponse.toString();
            });
            new Thread(task).start();
            Log.i("Response", task.get());
        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }
    }

    //TO DO: IMPLEMENT EDIT ROUTE FEATURE
    private void editRoute(long routeId) throws JSONException {
        Intent intent = new Intent(context, EditRouteActivity.class);
        intent.putExtra("id", routeId);
        context.startActivity(intent);
    }

}
