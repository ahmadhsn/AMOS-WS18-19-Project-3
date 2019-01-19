package com.gr03.amos.bikerapp;

import android.content.Context;
import android.util.Log;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.gson.JsonObject;
import com.gr03.amos.bikerapp.Models.Address;
import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.Models.User;
import com.gr03.amos.bikerapp.Models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import io.realm.Realm;

public class Requests {

    public static final String HOST = "10.0.2.2";
    public static final String PORT = "8080";

    public static JSONObject getResponse(String urlTail, JSONObject json) {
        return Requests.getResponse(urlTail, json, "POST");
    }

    public static JSONObject getResponse(String urlTail, JSONObject json, String method) {
        try {
            URL url = new URL("http://" + HOST + ":" + PORT + "/RESTfulWebserver/services/" + urlTail);

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
            JsonObject jsonObject = new GetJson().AsJSONObject("http://" + HOST + ":" + PORT + "/RESTfulWebserver/services/" + urlTail);
            JSONObject obj = new JSONObject(String.valueOf(jsonObject));

            JSONArray eventString = obj.getJSONArray("event");

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(Event.class, eventString);
            realm.commitTransaction();
            realm.close();


        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getJsonResponseForRoutes(String urlTail, Context context) {
        try {
            JsonObject jsonObject = new GetJson().AsJSONObject("http://" + HOST + ":" + PORT + "/RESTfulWebserver/services/" + urlTail);
            JSONObject obj = new JSONObject(String.valueOf(jsonObject));

            JSONArray routeString = obj.getJSONArray("route");

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(Route.class, routeString);
            realm.commitTransaction();
            realm.close();


        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getJsonResponseForFriends(String urlTail, Context context) {
        try {
            JsonObject jsonObject = new GetJson().AsJSONObject("http://" + HOST + ":" + PORT + "/RESTfulWebserver/services/" + urlTail);
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

    public static void getJsonResponseForFriendsRoutes(String urlTail, Context context) {
        try {
            JsonObject jsonObject = new GetJson().AsJSONObject("http://" + HOST + ":" + PORT + "/RESTfulWebserver/services/" + urlTail);
            JSONObject obj = new JSONObject(String.valueOf(jsonObject));

            JSONArray friendString = obj.getJSONArray("friend");

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(Friend.class, friendString);
            realm.commitTransaction();
            realm.close();


        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getJsonResponseForUser(String urlTail, Context context) {
        try {
            JsonObject jsonObject = new GetJson().AsJSONObject("http://" + HOST + ":" + PORT + "/RESTfulWebserver/services/" + urlTail);
            JSONObject obj = new JSONObject(String.valueOf(jsonObject));

            JSONArray userString = obj.getJSONArray("user");

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(User.class, userString);
            realm.commitTransaction();
            realm.close();


        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }


    public static void getJsonResponseForChat(String urlTail, int chatId, Context context) {
        try {
            JsonObject jsonObject = new GetJson()
                    .AsJSONObject("http://" + HOST + ":" + PORT + "/RESTfulWebserver/services/" + urlTail + "/" + chatId);
            JSONObject obj = new JSONObject(String.valueOf(jsonObject));
            String messageString = obj.getString("Chat");

            JSONArray object = new JSONArray(messageString); // parse the array

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(Message.class, object);
            realm.commitTransaction();
            realm.close();

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

    }

    public static JSONObject getJSONResponse(String tail, JSONObject request, String method) {
        JSONObject response = null;

        FutureTask<String> task = new FutureTask(new Callable<String>() {
            public String call() {
                JSONObject threadResponse = Requests.getResponse(tail, request, method);
                return threadResponse.toString();
            }
        });
        new Thread(task).start();
        try {
            Log.i("Response", task.get());
            response = new JSONObject(task.get());
            Log.i("this is response", String.valueOf(response));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }
}