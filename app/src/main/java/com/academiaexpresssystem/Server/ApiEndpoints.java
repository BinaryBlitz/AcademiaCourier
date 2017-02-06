package com.academiaexpresssystem.Server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndpoints {
    @GET("orders/assigned")
    Call<JsonArray> assigned(@Query("api_token") String token);

    @PATCH("orders/{id}/assign")
    Call<JsonObject> assignOrder(@Path("id") int id, @Query("api_token") String token);

    @PATCH("orders/{id}")
    Call<JsonObject> updateOrder(@Path("id") int id, @Query("api_token") String token);

    @PATCH("courier")
    Call<JsonObject> updateUser(@Body JsonObject user, @Query("api_token") String token);

    @GET("orders")
    Call<JsonArray> getOrders(@Query("api_token") String token);

    @POST("authenticate")
    Call<JsonObject> auth(@Body JsonObject user);
}
