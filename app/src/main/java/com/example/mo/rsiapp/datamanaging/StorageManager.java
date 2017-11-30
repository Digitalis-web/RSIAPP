package com.example.mo.rsiapp.datamanaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mo.rsiapp.NavActivity;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by mo on 08/11/17.
 */

public class StorageManager {
    public static final String WATCHED_AREAS_KEY =  "watchedAreas";
    public static final String LAST_CONTROLLED_FORECAST_KEY =  "lastForecast";
    public static final String SETTINGS_KEY =  "settings";
    public static final String NOTIFICATIONS_ENABLED_KEY =  "notifications";
    public static final String RSI_KEY_KEY =  "rsi_key";
    public static final String TAG =  "StorageManager";

    public static final String PREF_NAME =  "areasData";


    public static boolean getNotificationsEnabled(Context context){
        String enabledStr = getString(NOTIFICATIONS_ENABLED_KEY, context);
        boolean enabled = enabledStr.equals("1");
        return enabled;
    }

    public static String getRSIKey(){
        return getString(RSI_KEY_KEY);
    }
    public static void saveRSIKey(String key){
        saveString(RSI_KEY_KEY, key);
    }

    public static void saveNotificationsEnabled(boolean enabled){
        String str = enabled ? "1" : "0";
        saveString(NOTIFICATIONS_ENABLED_KEY, str);
    }

    public static boolean areaIsWatched(String areaID) {
        Set<String> set = getWatchedAreas();
        return set.contains(areaID);
    }

    public static void saveSettings(Set<String> settingsSet){
        saveStringSet(SETTINGS_KEY, settingsSet);
    }

    public static Set<String> getSettings(){
        return getSettings(NavActivity.navActivity);
    }

    public static Set<String> getSettings(Context context){
        return getStringSet(SETTINGS_KEY, context);
    }

    public static Set<String> getWatchedAreas(Context context) {
        Set<String> set = getStringSet(WATCHED_AREAS_KEY, context);
        Log.d(TAG, "getWatchedAreas: watched: " + set.toString());
        return set;
    }

    public static void setLastControlledForecastTime(long value, Context context){
        saveLong(LAST_CONTROLLED_FORECAST_KEY, value, context);
    }

    public static long getLastControlledForecastTime(Context context){
        return getLong(LAST_CONTROLLED_FORECAST_KEY, context);
    }

    public static Set<String> getWatchedAreas() {
        Set<String> set = getStringSet(WATCHED_AREAS_KEY);
        Log.d(TAG, "getWatchedAreas: watched: " + set.toString());
        return set;
    }

    public static void removeWatchedArea(String areaID){
        Set<String> set = getStringSet(WATCHED_AREAS_KEY);
        set.remove(areaID);
        saveStringSet(WATCHED_AREAS_KEY, set);
        NavActivity.navActivity.updateNavItems();
    }


    public static void clearWatchedAreas() {
        Set<String> set = new HashSet<>();
        saveStringSet(WATCHED_AREAS_KEY, set);
        NavActivity.navActivity.updateNavItems();
    }

    public static void addWatchedArea(String areaID){
        Set<String> set = getStringSet(WATCHED_AREAS_KEY);
        set.add(areaID);
        saveStringSet(WATCHED_AREAS_KEY, set);
        NavActivity.navActivity.updateNavItems();
    }

    public static void saveStringSet(String key, Set<String> set) {
        SharedPreferences settings = NavActivity.navActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.apply();
        editor.putStringSet(key, set);
        editor.apply();
    }

    public static Set<String> getStringSet(String key, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        Set<String> set = sharedPref.getStringSet(key, new HashSet<String>());
        return set;
    }

    public static Set<String> getStringSet(String key) {
        Context context = NavActivity.navActivity;
        return getStringSet(key, context);
    }

    public static long getLong(String key, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        long value = sharedPref.getLong(key, 0);
        return value;
    }

    public static void saveLong(String key, long value, Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.apply();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void saveString(String key, String value) {
        SharedPreferences settings = NavActivity.navActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.apply();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        String value = sharedPref.getString(key, "");
        return value;
    }

    public static String getString(String key) {
        return getString(key, NavActivity.navActivity);
    }
}
