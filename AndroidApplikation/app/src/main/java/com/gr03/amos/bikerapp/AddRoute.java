package com.gr03.amos.bikerapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Models.RouteParticipation;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;

public class AddRoute extends AppCompatActivity implements ResponseHandler {
    private static Context context;

    EditText routeName;
    EditText routeDescription;
    EditText startState;
    EditText startCountry;
    EditText startCity;
    EditText startStreet;
    EditText startPostcode;
    EditText startHouseNr;
    EditText endState;
    EditText endCountry;
    EditText endCity;
    EditText endStreet;
    EditText endPostcode;
    EditText endHouseNr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_add_route);

        routeName = findViewById(R.id.routeName);
        routeDescription = findViewById(R.id.routeDescription);

        startState = findViewById(R.id.startState);
        startCountry = findViewById(R.id.startCountry);
        startCity = findViewById(R.id.startCity);
        startStreet = findViewById(R.id.startStreet);
        startPostcode = findViewById(R.id.startPostcode);
        startHouseNr = findViewById(R.id.startHouseNr);

        endState = findViewById(R.id.endState);
        endCountry = findViewById(R.id.endCountry);
        endCity = findViewById(R.id.endCity);
        endStreet = findViewById(R.id.endStreet);
        endPostcode = findViewById(R.id.endPostcode);
        endHouseNr = findViewById(R.id.endHouseNr);
    }

    public void addRoute(View view) {
        if (checkEnteredData()) {
            return;
        }
        Requests.executeRequest(this, "POST", "createRoute", generateRequestJSON(), getApplicationContext());
        finish();
    }

    void addRouteToMyList(int routId) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RouteParticipation routeParticipation = realm.createObject(RouteParticipation.class);
        routeParticipation.setIdRoute(routId);
        routeParticipation.setIdUser(SaveSharedPreference.getUserID(context));
        realm.commitTransaction();
        realm.close();
        Requests.getJsonResponseForRoutes("getRoutes", context);
    }

    boolean isTextEmpty(EditText text) {
        CharSequence string = text.getText().toString();
        return TextUtils.isEmpty(string);
    }


    boolean checkEnteredData() {
        boolean isDataNotSet = false;
        if (isTextEmpty(routeName)) {
            routeName.setError("Route name is required!");
            isDataNotSet = true;
        }
        if (isTextEmpty(routeDescription)) {
            routeDescription.setError("Route description is required!");
            isDataNotSet = true;
        }

        if (isTextEmpty(startPostcode)) {
            startPostcode.setError("Integer Postcode is required!");
            isDataNotSet = true;
        }

        if (isTextEmpty(startState)) {
            startState.setError("State is a required field!");
            isDataNotSet = true;
        }
        if (isTextEmpty(startCountry)) {
            startCountry.setError("Country is a required field!");
            isDataNotSet = true;
        }
        if (isTextEmpty(startCity)) {
            startCity.setError("City is a required field!");
            isDataNotSet = true;
        }

        if (isTextEmpty(endPostcode)) {
            endPostcode.setError("Integer Postcode is required!");
            isDataNotSet = true;
        }

        if (isTextEmpty(endState)) {
            endState.setError("State is a required field!");
            isDataNotSet = true;
        }
        if (isTextEmpty(endCountry)) {
            endCountry.setError("Country is a required field!");
            isDataNotSet = true;
        }
        if (isTextEmpty(endCity)) {
            endCity.setError("City is a required field!");
            isDataNotSet = true;
        }

        return isDataNotSet;
    }

    private JSONObject generateRequestJSON() {
        JSONObject startAddress = new JSONObject();
        JSONObject route = new JSONObject();
        JSONObject endAddress = new JSONObject();
        System.out.print(route);
        JSONObject requestJSON = new JSONObject();
        try {
            route.put("name", routeName.getText().toString());
            route.put("description", routeDescription.getText().toString());
            route.put("id_user", SaveSharedPreference.getUserID(this));
            startAddress.put("street", startStreet.getText().toString());
            startAddress.put("housenumber", startHouseNr.getText().toString());
            startAddress.put("country", startCountry.getText().toString());
            startAddress.put("state", startState.getText().toString());
            startAddress.put("city", startCity.getText().toString());
            startAddress.put("postcode", startPostcode.getText().toString());


            endAddress.put("street", endStreet.getText().toString());
            endAddress.put("housenumber", endHouseNr.getText().toString());
            endAddress.put("country", endCountry.getText().toString());
            endAddress.put("state", endState.getText().toString());
            endAddress.put("city", endCity.getText().toString());
            endAddress.put("postcode", endPostcode.getText().toString());

            requestJSON.put("end_address", endAddress);
            requestJSON.put("start_address", startAddress);
            requestJSON.put("route", route);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestJSON;
    }

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        SocketUtility.checkRequestSuccessful(getApplicationContext(),response);
    }
}

