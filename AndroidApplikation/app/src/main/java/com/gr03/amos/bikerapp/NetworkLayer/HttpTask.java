package com.gr03.amos.bikerapp.NetworkLayer;

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

public class HttpTask extends AsyncTask<String, Void, JSONObject> {

    private static String HOST = "10.0.2.2";
    private static String PORT = "8080";
    static MediaType jsonMedia = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;
    private String urlTail;
    private String method;
    private boolean connectionTimeout;
    private JSONObject response;
    private ResponseHandler handler;

    public HttpTask(ResponseHandler handler, String method, String urlTail) {
        super();
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .build();
        this.method = method;
        this.urlTail = urlTail;
        this.connectionTimeout = false;
        this.handler = handler;
    }


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
            Response response = client.newCall(request).execute();
            Log.i("Response", response.toString());
            this.response = new JSONObject(response.body().string());
        } catch (SocketTimeoutException ex) {
            connectionTimeout = true;
        } catch (IOException |JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject obj) {
        if (connectionTimeout) {
            Log.d("CONNECTION_TIMEOUT", "Found connection Timeout");
            response = new JSONObject();
            try {
                response.put("error", "connection_timeout");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        handler.onResponse(response, urlTail);
    }

}
