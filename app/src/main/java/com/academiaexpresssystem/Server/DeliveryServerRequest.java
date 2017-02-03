package com.academiaexpresssystem.Server;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;

public class DeliveryServerRequest {
    private static DeliveryServerRequest request;

    static String apiToken;
    static Context context;
    public static String baseUrl = "http://academia-delivery.herokuapp.com";

    static String PREFERENCE_NAME = "DeliverySystemAppPrefs";
    static String TOKEN_ENTITY = "auth_token";

    public static RequestQueue queue;

    public static DeliveryServerRequest with(Context context) {
        DeliveryServerRequest.context = context;
        if (request == null) {
            synchronized (DeliveryServerRequest.class) {
                if (request == null) {
                    request = new DeliveryServerRequest(context);
                }
            }
        }
        return request;
    }

    private DeliveryServerRequest(Context context) {
        DeliveryServerRequest.context = context;

        if (queue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
            Network network = new BasicNetwork(new OkHttpStack());
            queue = new RequestQueue(cache, network);
            queue.start();
        }
    }

    public RequestPerformer authorize() throws IllegalArgumentException {
        if(DeviceInfoStore.getToken(context).equals("null")) {
            throw new IllegalArgumentException();
        }
        apiToken = "?api_token=" + DeviceInfoStore.getToken(context);
        return new RequestPerformer();
    }

    public RequestPerformer skipAuth() {
        return new RequestPerformer();
    }
}
