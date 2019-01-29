package com.gr03.amos.bikerapp.NetworkLayer;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SocketUtility {

    public static boolean hasSocketError(JSONObject obj){
        boolean error = false;
        try {
            if(obj.has("error") && obj.getString("error").equals("connection_timeout")){
                error = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return error;
    }

    public static void displayToastOnTimeout(Context ctx, JSONObject obj){
        if(hasSocketError(obj)){
            Toast.makeText(ctx, "NetworkTimeout: Please try again later.", Toast.LENGTH_LONG);
        }
    }


    public static void displayToastOnNoResponse(Context ctx){
        Toast.makeText(ctx, "No response: Check your network", Toast.LENGTH_LONG);
    }
}
