package com.academiaexpresssystem.Server;

import org.json.JSONObject;

public class RequestPerformer {

    private OnRequestPerformedListener listener;
    private JSONObject[] objects;

    public RequestPerformer listener(OnRequestPerformedListener listener) {
        this.listener = listener;
        return this;
    }

    public RequestPerformer objects(JSONObject... objects) {
        this.objects = objects;
        return this;
    }

    public RequestExecutor orders() {
        return new RequestExecutor(new OrdersRequest(), listener, objects);
    }

    public RequestExecutor assigned() {
        return new RequestExecutor(new AssignedRequest(), listener, objects);
    }

    public RequestExecutor updateUser() {
        return new RequestExecutor(new UpdateUserRequest(), listener, objects);
    }

    public RequestExecutor attachOrder(String id) {
        return new RequestExecutor(new AttachOrderRequest(id), listener, objects);
    }

    public RequestExecutor completeOrder(String id) {
        return new RequestExecutor(new UpdateOrderRequest(id), listener, objects);
    }

    public RequestExecutor auth() {
        return new RequestExecutor(new AuthRequest(), listener, objects);
    }
}
