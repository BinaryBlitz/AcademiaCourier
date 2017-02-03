package com.academiaexpresssystem.Server;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class UpdateUserRequest implements Request {

    @Override
    public void execute(final OnRequestPerformedListener listener, final JSONObject... params) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.PATCH,
                DeliveryServerRequest.baseUrl
                        + "/courier.json"
                        + DeliveryServerRequest.apiToken
                ,
                params[0],
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onRequestPerformedListener(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRequestPerformedListener("Error");
                    }
                }
        );
        DeliveryServerRequest.queue.add(jsObjRequest);
    }
}