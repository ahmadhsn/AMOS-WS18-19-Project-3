package com.gr03.amos.bikerapp.NetworkLayer;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Async Task to execute Http Requests based on OkHttp Client.
 */
public class HttpTask extends AsyncTask<String, Void, JSONObject> {

    static MediaType jsonMedia = MediaType.parse("application/json; charset=utf-8");
    static int CODE_NOTFOUND = 404;
    static int CODE_UNAUTHORIZED = 401;

    private OkHttpClient client;
    private String urlTail;
    private String method;
    private boolean connectionTimeout;
    private Response response;
    private JSONObject responseJson;
    private ResponseHandler handler;
    private boolean hasRealmUpdate;
    private ResponseHandler realmHandler;

    /**
     * Constructor for HttpTask.
     *
     * @param handler specifies Response handling
     * @param method HTTP Method ("GET", "POST", "PUT")
     * @param urlTail urlTail (servicename) of request
     */
    public HttpTask(ResponseHandler handler, String method, String urlTail) {
        super();
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .build();
        this.method = method;
        this.urlTail = urlTail;
        this.connectionTimeout = false;
        this.handler = handler;
        this.hasRealmUpdate = false;
    }

    /**
     * Constructor for HttpTask with Realm updates.
     *
     * @param handler specifies Response handling
     * @param method HTTP Method ("GET", "POST", "PUT")
     * @param urlTail urlTail (servicename) of request
     */
    public HttpTask(ResponseHandler handler, String method, String urlTail, ResponseHandler realmHandler) {
        super();
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .build();
        this.method = method;
        this.urlTail = urlTail;
        this.connectionTimeout = false;
        this.handler = handler;
        this.realmHandler = realmHandler;
        this.hasRealmUpdate = true;
    }


    /**
     * Sends in background HTTP Request to server.
     *
     * @param params if response contains JSON Body params[0] is the JSON Object
     * @return JSON Object of response
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        Request request = null;
        if (method == "GET") {
            request = new Request.Builder()
                    .url(Requests.getUrl(urlTail))
                    .build();
        } else {
            String json = params[0];
            RequestBody body = RequestBody.create(jsonMedia, json);

            if (method == "PUT") {
                request = new Request.Builder()
                        .url(Requests.getUrl(urlTail))
                        .put(body)
                        .build();
            } else if (method == "POST") {
                request = new Request.Builder()
                        .url(Requests.getUrl(urlTail))
                        .post(body)
                        .build();
            }
        }

        try {
            this.response = client.newCall(request).execute();
            Log.i("Response", response.toString());
            this.responseJson = new JSONObject(response.body().string());
        } catch (SocketTimeoutException ex) {
            connectionTimeout = true;
        } catch (IOException |JSONException e) {
            e.printStackTrace();
        }

        return responseJson;
    }

    /**
     * Handle response after executing. If the Client could not connect to the server, the response contains a specified JSON Object.
     * Forwards response handling to ResponseHandler (specified in constructor).
     *
     * @param obj JSONObject of Response
     */
    @Override
    protected void onPostExecute(JSONObject obj) {
        if (connectionTimeout) {
            Log.d("CONNECTION_TIMEOUT", "Found connection Timeout");
            responseJson = new JSONObject();
            try {
                responseJson.put("error", "connection_timeout");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(response.code() == CODE_UNAUTHORIZED){
            try {
                responseJson = new JSONObject();
                responseJson.put("error", "unauthorized");
                Log.d("UNAUTHORIZED_REQUEST", "found unauthorized request");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(response.code() == CODE_NOTFOUND){
            try {
                responseJson = new JSONObject();
                responseJson.put("error", "not_found");
                Log.d("NOTFOUND_REQUEST", "Service with url " + urlTail + " not found.");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //execute Realm update before other Response handling if necessary
        if(hasRealmUpdate){
            realmHandler.onResponse(responseJson, urlTail);
        }
        handler.onResponse(responseJson, urlTail);
    }

}
