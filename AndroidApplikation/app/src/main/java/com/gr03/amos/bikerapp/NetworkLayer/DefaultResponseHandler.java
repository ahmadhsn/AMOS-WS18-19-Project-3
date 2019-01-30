package com.gr03.amos.bikerapp.NetworkLayer;

import org.json.JSONObject;

/**
 * Default Response Handler for Responses without expected JSON response and without activity specific behaviour.
 * The Response is ignored and nothing happens after receiving it.
 */
public class DefaultResponseHandler implements ResponseHandler {
    @Override
    public void onResponse(JSONObject response, String urlTail) {
        //TODO: implement default behaviour
    }
}
