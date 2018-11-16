package com.gr03.amos.bikerapp;

import android.util.Log;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Requests {

    public static JSONObject getResponse(String urlTail, JSONObject json) {
        try {
            URL url = new URL("http://10.0.2.2:8080/RESTfulWebserver/services/" + urlTail);

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestMethod("POST");

            DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
            printout.writeBytes(json.toString());
            printout.flush();
            printout.close();

            InputStream in = urlConn.getInputStream();
            JSONObject response = new JSONObject(new java.util.Scanner(in).useDelimiter("\\A").next());

            urlConn.disconnect();

            return response;

        } catch (Exception e) {
            Log.i("Could not execute!", e.toString());
            return null;
        }
    }

    public static List<List<String>> getJsonResponse(String urlTail) {
        List<List<String>> renderedlist = new ArrayList<>();
        try {
            JsonObject jsonObject = new GetJson().AsJSONObject("http://10.0.2.2:8080/RESTfulWebserver/services/" + urlTail);
            JSONObject obj = new JSONObject(String.valueOf(jsonObject));
            String eventString = obj.getJSONObject("eventCreation").getString("event");

            JSONArray object = new JSONArray(eventString); // parse the array
            for (int i = 0; i < object.length(); i++) { // iterate over the array
                List<String> list = new ArrayList<>();
                JSONObject objectJSON = object.getJSONObject(i);
                list.add(objectJSON.getString("name"));
                list.add(objectJSON.getString("description"));
                list.add(objectJSON.getString("time"));
                list.add(objectJSON.getString("date"));
                list.add(objectJSON.getString("id_event"));
                renderedlist.add(list);
            }

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return renderedlist;
    }

}
