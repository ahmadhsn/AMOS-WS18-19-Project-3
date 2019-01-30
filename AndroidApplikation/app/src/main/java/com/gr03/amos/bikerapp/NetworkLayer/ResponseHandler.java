package com.gr03.amos.bikerapp.NetworkLayer;

import org.json.JSONObject;

/**
 * Interface to handle reponses of HTTP-Requests. Every activity or fragment sending a Request and expecting a Response
 * has to implement this interface.
 */
public interface ResponseHandler {
    /**
     * Contains handling of response. The responses are matched to the requests using the urlTail.
     *
     * @param response JSON Object of Response
     * @param urlTail urlTail of Request (used to match responses and requests)
     */
    void onResponse(JSONObject response, String urlTail);
}
