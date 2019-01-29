package com.gr03.amos.bikerapp.NetworkLayer;


import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;

public class RealmResponseHandler implements ResponseHandler {

    private String jsonName;
    private Context context;
    private Class typeClass;

    public RealmResponseHandler(Class typeClass, String jsonName, Context context){
        this.typeClass = typeClass;
        this.jsonName = jsonName;
        this.context = context;
    }

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if(SocketUtility.hasSocketError(response)){
            Toast.makeText(context, "No response from server.", Toast.LENGTH_LONG);
            return;
        }
        try {
            JSONArray jsonString = response.getJSONArray(jsonName);

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(typeClass, jsonString);
            realm.commitTransaction();
            realm.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
