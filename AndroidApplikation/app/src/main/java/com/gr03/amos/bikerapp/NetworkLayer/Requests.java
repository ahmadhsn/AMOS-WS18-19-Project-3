package com.gr03.amos.bikerapp.NetworkLayer;

import android.content.Context;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Models.Event;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.Models.Route;
import com.gr03.amos.bikerapp.Models.User;
import com.gr03.amos.bikerapp.Models.Message;
import com.gr03.amos.bikerapp.NetworkLayer.DefaultResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.HttpTask;
import com.gr03.amos.bikerapp.NetworkLayer.RealmResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class Requests {

    public static final String HOST = "10.0.2.2";
    public static final String PORT = "8080";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static OkHttpClient client = new OkHttpClient();

    public static void executeRequest(ResponseHandler handler, String method, String urlTail){
        new HttpTask(handler,method, urlTail).execute();
    }


    public static void executeRequest(ResponseHandler handler, String method, String urlTail, JSONObject json){
        new HttpTask(handler,method, urlTail).execute(json.toString());
    }


    public static JSONObject getResponse(String urlTail, JSONObject json, String method) {
        return getResponse(urlTail, json, method, null, true);
    }

    public static JSONObject getResponse(String urlTail, JSONObject json, String method, Context context) {
        return getResponse(urlTail, json, method, context, true);
    }

    public static JSONObject getResponse(String urlTail, JSONObject json, String method, Context context, boolean handleTimeout) {
        JSONObject response = null;
        try {
            HttpTask currTask = new HttpTask(new DefaultResponseHandler(), method, urlTail);
            if (json == null) {
                response = currTask.execute().get();
            } else {
                response = currTask.execute(json.toString()).get();
            }

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();

        }

        if(context != null && SocketUtility.hasSocketError(response)){
            Toast.makeText(context, "No response from server.", Toast.LENGTH_LONG).show();
        }

        return response;
    }

    private static void executeRealmResponse(String urlTail, String jsonName, Class type, Context context) {
        RealmResponseHandler handler = new RealmResponseHandler(type, jsonName, context);

        executeRequest(handler, "GET", urlTail);
    }

    public static void getJsonResponse(String urlTail, Context context) {
        executeRealmResponse(urlTail, "event", Event.class, context);
    }

    public static void getJsonResponseForRoutes(String urlTail, Context context) {
        executeRealmResponse(urlTail, "route", Route.class, context);
    }

    public static void getJsonResponseForFriends(String urlTail, Context context) {
        executeRealmResponse(urlTail, "friends", Friend.class, context);
    }

    public static void getJsonResponseForFriendsRoutes(String urlTail, Context context) {
        executeRealmResponse(urlTail, "friend", Friend.class, context);

    }

    public static void getJsonResponseForUser(String urlTail, Context context) {
        executeRealmResponse(urlTail, "user", User.class, context);
    }

    public static void getJsonResponseForChat(String urlTail, int chatId, Context context) {
        executeRealmResponse(urlTail + "/" + chatId, "Chat", Message.class, context);
    }


    public static String getUrl(String urlTail) {
        return "http://" + HOST + ":" + PORT + "/RESTfulWebserver/services/" + urlTail;
    }


}