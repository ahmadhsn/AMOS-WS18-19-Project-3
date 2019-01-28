package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.NetworkLayer.HttpPostTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import io.realm.Realm;

public class EditRouteActivity extends AppCompatActivity {
    Intent intent;
    Long routeId;
    Button updateRoute;
    EditText route_name, route_description, route_start_street, route_start_hnumber, route_start_postcode, route_start_city, route_start_bstate, route_start_country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.activity_edit_route);
        intent = getIntent();
        routeId = intent.getLongExtra("id", 0);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        final Route route = realm.where(Route.class).equalTo("id_route", routeId).findFirst();

        route_name = (EditText)findViewById(R.id.route_name);
        route_description = (EditText)findViewById(R.id.route_description);

        route_name.setText(route.getName());
        route_description.setText(route.getDescription());

    }

    boolean isTextEmpty(EditText text){
        CharSequence string = text.getText().toString();
        return TextUtils.isEmpty(string);
    }


    public void updateRoute(View view) throws JSONException {

        if (isTextEmpty(route_name)) {
            Log.i("VALIDATIONROUTE", "route name is empty");
            route_name.setError("Please enter a route name");
            return;
        }
        if (isTextEmpty(route_description)) {
            Log.i("VALIDATIONROUTE", "route description is empty");
            route_description.setError("Please enter a route discription");
            return;
        }

        JSONObject json = new JSONObject();
        json.put("id_route", routeId);
        json.put("name", route_name.getText().toString());
        json.put("description", route_description.getText().toString());

        Log.i("EditRouteActivity", route_name + " " + routeId);

        try {
            JSONObject response = Requests.getResponse("updateRoute", json, "POST", getApplicationContext());
            if(response == null){
                Intent intent = new Intent(this, ShowEventActivity.class);
                startActivity(intent);
                return;
            }

            Intent intent = new Intent(this, ShowEventActivity.class);
            startActivity(intent);

            if (response.has("routeUpdate")) {
                String statusRoute = (String) response.get("routeUpdate");
                if (statusRoute.equals("successfullUpdation")) {
                    Toast.makeText(getApplicationContext(), "Successfully updated Route.", Toast.LENGTH_LONG).show();
                }
                if (statusRoute.equals("InvalidRequestBody")){
                    Toast.makeText(getApplicationContext(),"Invalid Request Body",Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }
        super.onBackPressed();
    }
    public void cancel(View view){
        finish();
    }



}