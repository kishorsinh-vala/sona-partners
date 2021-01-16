package com.loginiusinfotech.sonapartner.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Pref {

    public static final String PREFERENCENAME = "loginiusinfotech_sonapartner";

    public static Boolean getPrefDate(Context context, String key, Boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
        Boolean result = sharedPreferences.getBoolean(key, defaultValue);
        return result;
    }

    public static void setPrefDate(Context context, String key, Boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
        Editor prefsPrivateEditor = sharedPreferences.edit();
        prefsPrivateEditor.putBoolean(key, value);
        prefsPrivateEditor.apply();
    }

    public static String getPrefDate(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(key, defaultValue);
        return result;
    }

    public static void setPrefDate(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
        Editor prefsPrivateEditor = sharedPreferences.edit();
        prefsPrivateEditor.putString(key, value);
        prefsPrivateEditor.apply();
    }

    public static int getPrefDate(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
        int result = sharedPreferences.getInt(key, defaultValue);
        return result;
    }

    public static void setPrefDate(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
        Editor prefsPrivateEditor = sharedPreferences.edit();
        prefsPrivateEditor.putInt(key, value);
        prefsPrivateEditor.apply();
    }

    public static void ClearAllPref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
        Editor prefsPrivateEditor = sharedPreferences.edit();
        prefsPrivateEditor.clear();
        prefsPrivateEditor.apply();
    }
}
