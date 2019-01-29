package com.gr03.amos.bikerapp.NetworkLayer;

import org.json.JSONObject;

public interface ResponseHandler {
    void onResponse(JSONObject response, String urlTail);
}
