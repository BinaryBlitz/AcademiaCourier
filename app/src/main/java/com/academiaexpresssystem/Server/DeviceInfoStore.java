package com.academiaexpresssystem.Server;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("unused")
public class DeviceInfoStore {

    public static void saveToken(String token) {
        SharedPreferences prefs = DeliveryServerRequest.context.getSharedPreferences(
                DeliveryServerRequest.PREFERENCE_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(DeliveryServerRequest.TOKEN_ENTITY, token).apply();
    }

    public static void resetToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                DeliveryServerRequest.PREFERENCE_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(DeliveryServerRequest.TOKEN_ENTITY, "null").apply();
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                DeliveryServerRequest.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(DeliveryServerRequest.TOKEN_ENTITY, "null");
    }

    public static void saveId(String token) {
        SharedPreferences prefs = DeliveryServerRequest.context.getSharedPreferences(
                DeliveryServerRequest.PREFERENCE_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString("user_id", token).apply();
    }

    public static void resetId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                DeliveryServerRequest.PREFERENCE_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString("user_id", "null").apply();
    }

    public static String getId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                DeliveryServerRequest.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("user_id", "null");
    }
}
