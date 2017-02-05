package com.academiaexpresssystem.Server;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("unused")
public class DeviceInfoStore {

    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(
                ServerConfig.INSTANCE.getPrefsName(), Context.MODE_PRIVATE);
        prefs.edit().putString(ServerConfig.INSTANCE.getTokenEntity(), token).apply();
    }

    public static void resetToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                ServerConfig.INSTANCE.getPrefsName(), Context.MODE_PRIVATE);
        prefs.edit().putString(ServerConfig.INSTANCE.getTokenEntity(), "null").apply();
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                ServerConfig.INSTANCE.getPrefsName(), Context.MODE_PRIVATE);
        return prefs.getString(ServerConfig.INSTANCE.getTokenEntity(), "null");
    }

    public static void saveId(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(
                ServerConfig.INSTANCE.getPrefsName(), Context.MODE_PRIVATE);
        prefs.edit().putString(ServerConfig.INSTANCE.getUserIdEntity(), token).apply();
    }

    public static void resetId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                ServerConfig.INSTANCE.getPrefsName(), Context.MODE_PRIVATE);
        prefs.edit().putString(ServerConfig.INSTANCE.getUserIdEntity(), "null").apply();
    }

    public static String getId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                ServerConfig.INSTANCE.getPrefsName(), Context.MODE_PRIVATE);
        return prefs.getString(ServerConfig.INSTANCE.getUserIdEntity(), "null");
    }
}
