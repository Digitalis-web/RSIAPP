package com.example.mo.rsiapp.datamanaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mo.rsiapp.NavActivity;
import com.example.mo.rsiapp.R;

import java.util.HashSet;
import java.util.Set;

import static android.R.attr.defaultValue;
import static android.R.attr.value;

/**
 * Created by mo on 08/11/17.
 */

public class StorageManager {
    public static final String WATCHED_AREAS_KEY =  "watchedAreas";
    public static final String TAG =  "StorageManager";

    public static boolean areaIsWatched(String areaID) {
        Set<String> set = getWatchedAreas();
        return set.contains(areaID);
    }

    public static Set<String> getWatchedAreas() {
        Set<String> set = getStringSet(WATCHED_AREAS_KEY);
        return set;
    }

    public static void removeWatchedArea(String areaID){
        Set<String> set = getStringSet(WATCHED_AREAS_KEY);
        set.remove(areaID);
        saveStringSet(WATCHED_AREAS_KEY, set);
        NavActivity.navActivity.initNavItems();
    }

    public static void addWatchedArea(String areaID){
        Set<String> set = getStringSet(WATCHED_AREAS_KEY);
        set.add(areaID);
        saveStringSet(WATCHED_AREAS_KEY, set);
        NavActivity.navActivity.initNavItems();
    }

    public static void saveStringSet(String key, Set<String> set) {
        SharedPreferences settings = NavActivity.navActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.apply();
        editor.putStringSet(key, set);
        editor.apply();
        Log.d(TAG, "saveStringSet: watched areas: " + StorageManager.getWatchedAreas().toString());
    }

    public static Set<String> getStringSet(String key) {
        SharedPreferences sharedPref = NavActivity.navActivity.getPreferences(Context.MODE_PRIVATE);
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        Set<String> set = sharedPref.getStringSet(key, new HashSet<String>());
        return set;
    }

    public static void saveString(String key, String value) {
        SharedPreferences settings = NavActivity.navActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.apply();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key) {
        SharedPreferences sharedPref = NavActivity.navActivity.getPreferences(Context.MODE_PRIVATE);
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        String value = sharedPref.getString(key, "");
        return value;
    }
}
