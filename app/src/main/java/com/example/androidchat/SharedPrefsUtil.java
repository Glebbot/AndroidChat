package com.example.androidchat;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsUtil {
    private static final String SHARED_PREFS_NAME = "my_app_prefs";
    private static final String KEY_USER_LOGIN = "user_login";
    private static final String KEY_USER_PASSWORD = "user_password";
    private static final String KEY_CHANNEL_NAME = "channel_name";
    private static final String KEY_CHANNEL_DESCRIPTION = "channel_description";



    private static SharedPreferences getSharedPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveUserLogin(Context context, String userLogin) {
        SharedPreferences.Editor editor = getSharedPrefs(context).edit();
        editor.putString(KEY_USER_LOGIN, userLogin);
        editor.apply();
    }

    public static String getUserLogin(Context context) {
        return getSharedPrefs(context).getString(KEY_USER_LOGIN, "");
    }
    public static void saveUserPassword(Context context, String userPassword) {
        SharedPreferences.Editor editor = getSharedPrefs(context).edit();
        editor.putString(KEY_USER_PASSWORD, userPassword);
        editor.apply();
    }

    public static String getUserPassword(Context context) {
        return getSharedPrefs(context).getString(KEY_USER_PASSWORD, "");
    }
    public static void saveChannelName(Context context, String channelName) {
        SharedPreferences.Editor editor = getSharedPrefs(context).edit();
        editor.putString(KEY_CHANNEL_NAME, channelName);
        editor.apply();
    }

    public static String getChannelName(Context context) {
        return getSharedPrefs(context).getString(KEY_CHANNEL_NAME, "");
    }
    public static void saveChannelDescr(Context context, String channelDescr) {
        SharedPreferences.Editor editor = getSharedPrefs(context).edit();
        editor.putString(KEY_CHANNEL_DESCRIPTION, channelDescr);
        editor.apply();
    }

    public static String getChannelDescr(Context context) {
        return getSharedPrefs(context).getString(KEY_CHANNEL_DESCRIPTION, "");
    }
}
