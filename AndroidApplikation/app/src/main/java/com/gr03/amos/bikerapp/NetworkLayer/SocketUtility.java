package com.gr03.amos.bikerapp.NetworkLayer;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SocketUtility {

    /**
     * Checks if server could not be reached because of timeout.
     *
     * @param obj JSONObject of Response
     * @return true if timeout error, false if not
     */
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

    /**
     * Method checks if Response contains timeout error message and displays a Toast if it does.
     * @param ctx Context of application
     * @param obj JSONObject of Response
     */
    public static void displayToastOnTimeout(Context ctx, JSONObject obj){
        if(hasSocketError(obj)){
            Toast.makeText(ctx, "NetworkTimeout: Please try again later.", Toast.LENGTH_LONG);
        }
    }

}
