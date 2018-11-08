package com.gr03.amos.bikerapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Requests {

    public static JSONObject getResponse(String urlTail, JSONObject json) {
        try {
            URL url = new URL("http://10.0.2.2:8080/RESTfulWebserver/services/" + urlTail);

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestMethod("POST");


            DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
            printout.writeBytes(json.toString());
            printout.flush ();
            printout.close ();

            InputStream in = urlConn.getInputStream();
            JSONObject response = new JSONObject(new java.util.Scanner(in).useDelimiter("\\A").next());

            urlConn.disconnect();

            return response;

        } catch(Exception e) {
            Log.i("Could not execute!", e.toString());
            return null;
        }
    }

}
