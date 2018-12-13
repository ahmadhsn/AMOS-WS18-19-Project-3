package com.gr03.amos.bikerapp;

import android.content.Context;
import android.util.Log;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.gson.JsonObject;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.Models.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;

public class Requests {
    public static JSONObject getResponse(String urlTail, JSONObject json) {
        return Requests.getResponse(urlTail, json, "POST");
    }

    public static JSONObject getResponse(String urlTail, JSONObject json, String method) {
        try {
            URL url = new URL("http://10.0.2.2:8080/RESTfulWebserver/services/" + urlTail);

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestMethod(method);
            if (json != null) {
                DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
                printout.writeBytes(json.toString());
                printout.flush();
                printout.close();
            }

            InputStream in = urlConn.getInputStream();
            JSONObject response = new JSONObject(new java.util.Scanner(in).useDelimiter("\\A").next());

            urlConn.disconnect();

            return response;

        } catch (Exception e) {
            Log.i("Could not execute!", e.toString());
            return null;
        }
    }

    public static void getJsonResponse(String urlTail, Context context) {
        try {
            JsonObject jsonObject = new GetJson().AsJSONObject("http://10.0.2.2:8080/RESTfulWebserver/services/" + urlTail);
            JSONObject obj = new JSONObject(String.valueOf(jsonObject));
            String eventString = obj.getJSONObject("eventCreation").getString("events");

            JSONArray object = new JSONArray(eventString); // parse the array

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(Event.class, object);
            realm.commitTransaction();
            realm.close();

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getJsonResponseForFriends(String urlTail, Context context) {
        try {
            JsonObject jsonObject = new GetJson().AsJSONObject("http://10.0.2.2:8080/RESTfulWebserver/services/" + urlTail);
            JSONObject obj = new JSONObject(String.valueOf(jsonObject));
            String eventString = obj.getString("friends");

            JSONArray object = new JSONArray(eventString); // parse the array

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(Friend.class, object);
            realm.commitTransaction();
            realm.close();

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

    }
}