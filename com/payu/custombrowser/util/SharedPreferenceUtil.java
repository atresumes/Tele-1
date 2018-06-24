package com.payu.custombrowser.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import java.util.HashMap;
import java.util.Map;

public class SharedPreferenceUtil {
    public static void addStringToSharedPreference(Context context, String spFileName, String spKey, String spValue) {
        Editor editor = context.getSharedPreferences(spFileName, 0).edit();
        editor.putString(spKey, spValue);
        editor.commit();
    }

    public static void addIntToSharedPreference(Context context, String spFileName, String spKey, int spValue) {
        Editor editor = context.getSharedPreferences(spFileName, 0).edit();
        editor.putInt(spKey, spValue);
        editor.commit();
    }

    public static void addBooleanToSharedPreference(Context context, String spFileName, String spKey, boolean spValue) {
        Editor editor = context.getSharedPreferences(spFileName, 0).edit();
        editor.putBoolean(spKey, spValue);
        editor.commit();
    }

    public static String getStringFromSharedPreference(Context context, String spFileName, String spKey, String defaultValue) {
        return context.getSharedPreferences(spFileName, 0).getString(spKey, defaultValue);
    }

    public static int getIntFromSharedPreference(Context context, String spFileName, String spKey, int defaultValue) {
        return context.getSharedPreferences(spFileName, 0).getInt(spKey, defaultValue);
    }

    public static boolean getBooleanFromSharedPreference(Context context, String spFileName, String spKey, boolean defaultValue) {
        return context.getSharedPreferences(spFileName, 0).getBoolean(spKey, defaultValue);
    }

    public static Map<String, ?> getSharedPrefMap(Context context, String prefName) {
        HashMap<String, Object> sharedPrefHashMap = new HashMap();
        return context.getSharedPreferences(prefName, 0).getAll();
    }

    public static void removeAllFromSharedPref(Context context, String prefName) {
        context.getSharedPreferences(prefName, 0).edit().clear().commit();
    }
}
