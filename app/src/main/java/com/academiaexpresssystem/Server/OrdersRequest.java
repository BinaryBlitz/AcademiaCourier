package com.academiaexpresssystem.Server;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrdersRequest implements Request {

    @Override
    public void execute(final OnRequestPerformedListener listener, final JSONObject... params) {
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(
                com.android.volley.Request.Method.GET,
                DeliveryServerRequest.baseUrl +
                        ServerURLs.ordersUrl + DeliveryServerRequest.apiToken,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
