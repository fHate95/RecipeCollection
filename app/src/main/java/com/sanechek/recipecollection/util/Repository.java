package com.sanechek.recipecollection.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Repository {

    private SharedPreferences prefs;

    public Repository(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /* Int */
    public int getInt(String tag, int defValue) {
        return prefs.getInt(tag, defValue);
    }

    public void setInt(String tag, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(tag, value);
        editor.apply();
    }

    /* Long */
    public long getLong(String tag, long defValue) {
        return prefs.getLong(tag, defValue);
    }

    public void setLong(String tag, long value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(tag, value);
        editor.apply();
    }

    /* String */
    public String getString(String tag, String defValue) {
        return prefs.getString(tag, defValue);
    }

    public void setString(String tag, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(tag, value);
        editor.apply();
    }

    /* Boolean */
    public boolean getBoolean(String tag, boolean defaultValue) {
        return prefs.getBoolean(tag, defaultValue);
    }

    public void setBoolean(String tag, boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(tag, value);
        editor.apply();
    }

}
