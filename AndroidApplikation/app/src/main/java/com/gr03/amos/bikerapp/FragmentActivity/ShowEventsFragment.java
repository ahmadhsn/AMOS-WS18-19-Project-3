package com.gr03.amos.bikerapp.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gr03.amos.bikerapp.Models.Address;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.ShowEventRecylerViewAdapter;

import io.realm.Realm;
import io.realm.RealmResults;


public class ShowEventsFragment extends Fragment {
    RecyclerView showEventsRecyclerView;
    ShowEventRecylerViewAdapter showEventRecylerViewAdapter;
    private ImageView eventFilterImage;

    private TextView resultText;



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
        resultText = view.findViewById(R.id.result);

        eventFilterImage.setOnClickListener(v -> showInputDialog());

        Realm.init(container.getContext());
        Realm realm = Realm.getDefaultInstance();

        Requests.getJsonResponse("getEvents", container.getContext());
        RealmResults<Event> events = realm.where(Event.class).findAll();

        showEventsRecyclerView = view.findViewById(R.id.showEvents);
        showEventsRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        showEventRecylerViewAdapter = new ShowEventRecylerViewAdapter(container.getContext(), events);
        showEventsRecyclerView.setAdapter(showEventRecylerViewAdapter);

        return view;
    }

    protected void showInputDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.event_filter_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> resultText.setText("Hello, " + editText.getText()))
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


}
