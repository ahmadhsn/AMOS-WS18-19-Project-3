package com.gr03.amos.bikerapp.NetworkLayer;

import android.content.Context;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Models.Chat;
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
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import okhttp3.Response;

public class Requests {

    public static final String HOST = "10.0.2.2";
    public static final String PORT = "8080";


    /**
     * Execute a request without JSON body as an async task in the background. Response handling is specified in the given ResponseHandler.
     *
     * @param handler specifies ResponseHandling
     * @param method  HTTP method ("GET", "POST", "PUT")
     * @param urlTail urlTail (servicename) of Request
     */
    public static void executeRequest(ResponseHandler handler, String method, String urlTail, Context context) {
        new HttpTask(handler, method, urlTail, context).execute();
    }

    /**
     * Execute a request with JSON body as an async task in the background. Response handling is specified in the given ResponseHandler.
     *
     * @param handler specifies ResponseHandling
     * @param method  HTTP method ("GET", "POST", "PUT")
     * @param urlTail urlTail (servicename) of Request
     * @param json    json Object for request
     */
    public static void executeRequest(ResponseHandler handler, String method, String urlTail, JSONObject json, Context context) {
        new HttpTask(handler, method, urlTail, context).execute(json.toString());
    }


    /**
     * Temporary method to handle old requests as a synch task. Response handling is done on return of the method.
     *
     * @param urlTail urlTail (servicename) of Request
     * @param json    json Object for request
     * @param method  HTTP method ("GET", "POST", "PUT")
     * @return JSON Object of response
     */
    public static JSONObject getResponse(String urlTail, JSONObject json, String method) {
        return getResponse(urlTail, json, method, null);
    }


    /**
     * Temporary method to handle old requests as a synch task with timeoutHandling. Response handling is done on return of the method.
     * On Timeout a toast is displayed if context is given.
     *
     * @param urlTail urlTail (servicename) of Request
     * @param json    json Object for request
     * @param method  HTTP method ("GET", "POST", "PUT")
     * @param context
     * @return JSON Object of response
     */
    public static JSONObject getResponse(String urlTail, JSONObject json, String method, Context context) {
        JSONObject response = null;
        try {
            HttpTask currTask = new HttpTask(new DefaultResponseHandler(), method, urlTail, context);
            if (json == null) {
                response = currTask.execute().get();
            } else {
                response = currTask.execute(json.toString()).get();
            }

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();

        }

        if (context != null) {
            SocketUtility.checkRequestSuccessful(context, response);
        }

        return response;
    }

    /**
     * Executes a GET Request as an asynch background. To handle the response the RealmResponseHandler is used.
     * After receiving the response the realmResponseHandler updates the Realm Database.
     *
     * @param urlTail  urlTail (servicename) of request
     * @param jsonName string of json Object in response
     * @param type     Class of RealmModel
     * @param context  Context of application to init realm
     */
    private static void executeRealmResponse(String urlTail, String jsonName, Class type, Context context, ResponseHandler handler) {
        if (handler == null) {
            handler = new DefaultResponseHandler();
        }
        RealmResponseHandler realmHandler = new RealmResponseHandler(type, jsonName, context);

        new HttpTask(handler, "GET", urlTail, realmHandler, context).execute();
    }

    public static void getJsonResponseForEvents(String urlTail, int userId,  Context context, ResponseHandler handler) {
        executeRealmResponse(urlTail + "/" + userId, "event", Event.class, context, handler);
    }

    public static void getJsonResponseForRoutes(String urlTail, Context context) {
        executeRealmResponse(urlTail, "route", Route.class, context, null);
    }

    public static void getJsonResponseForRoutes(String urlTail, Context context, ResponseHandler handler) {
        executeRealmResponse(urlTail, "route", Route.class, context, handler);
    }

    public static void getJsonResponseForFriends(String urlTail, Context context, ResponseHandler handler) {
        executeRealmResponse(urlTail, "friends", Friend.class, context, handler);
    }

    public static void getJsonResponseForFriendsRoutes(String urlTail, Context context, ResponseHandler handler) {
        executeRealmResponse(urlTail, "friend", Friend.class, context, handler);
    }

    public static void getJsonResponseForUser(String urlTail, Context context) {
        executeRealmResponse(urlTail, "user", User.class, context, null);
    }

    public static void getJsonResponseForChat(String urlTail, int chatId, Context context, ResponseHandler handler) {
        executeRealmResponse(urlTail + "/" + chatId, "Chat", Message.class, context, handler);
    }

    public static void getJSONResponseForChats(int userId, Context context, ResponseHandler handler){
        executeRealmResponse( "getChats/" + userId, "chat", Chat.class, context, handler);

    }

    public static String getUrl(String urlTail) {
        return "http://" + HOST + ":" + PORT + "/RESTfulWebserver/services/" + urlTail;
    }


}