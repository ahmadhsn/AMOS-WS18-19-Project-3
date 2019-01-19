package com.gr03.amos.bikerapp;

import io.realm.Realm;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.gr03.amos.bikerapp.Models.RouteParticipation;

public class AddRoute extends AppCompatActivity {
    private static Context context;

    EditText routeName;
    EditText routeDescription;
    EditText startCountry;
    EditText startCity;
    EditText startStreet;
    EditText startPostcode;
    EditText startHouseNr;
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

        startCountry = findViewById(R.id.startCountry);
        startCity = findViewById(R.id.startCity);
        startStreet = findViewById(R.id.startStreet);
        startPostcode = findViewById(R.id.startPostcode);
        startHouseNr = findViewById(R.id.startHouseNr);

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
        Requests.getJSONResponse("createRoute", generateRequestJSON(), "POST");

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
            routeDescription.setError("Please add a short description of the route!");
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
        JSONObject requestJSON = new JSONObject();
        try {
            route.put("user_id", "user_id");
            route.put("name", routeName.getText().toString());
            route.put("description", routeDescription.getText().toString());

            startAddress.put("street", startStreet.getText().toString());
            startAddress.put("housenumber", startHouseNr.getText().toString());
            startAddress.put("country", startCountry.getText().toString());
            startAddress.put("state", "");
            startAddress.put("city", startCity.getText().toString());
            startAddress.put("postcode", startPostcode.getText().toString());

            endAddress.put("street", endStreet.getText().toString());
            endAddress.put("housenumber", endHouseNr.getText().toString());
            endAddress.put("country", endCountry.getText().toString());
            endAddress.put("state", "");
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
}

