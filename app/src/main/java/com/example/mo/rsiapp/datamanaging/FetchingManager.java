package com.example.mo.rsiapp.datamanaging;

import android.content.Context;
import android.util.Log;

import com.example.mo.rsiapp.NavActivity;
import com.example.mo.rsiapp.backgroundtasks.Alarm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;


/**
 * Created by mo on 23/07/17.
 */

public class FetchingManager {
    ///public static final String SERVER_IP = "64.147.114.20";
    public static final String SERVER_IP = "163.172.101.14";
    public static final String VERIFY_KEY_IP = "146.185.150.217";

    private static String url = "http://163.172.101.14:8000/api/ogJu1VCu09HpHD6VbHX34jChdoKz2fR5/area/1427@1497772800";
    //private static String forecastUrl = "http://163.172.101.14:8000/api/ogJu1VCu09HpHD6VbHX34jChdoKz2fR5/area/";
    private static String forecastUrl = "http://" + SERVER_IP + "/api/area/";
    private static String areasUrl = "http://" + SERVER_IP + "/api//forecasts";
    private static String TAG = "FetchingManager";
    public static ArrayList<String> areasName = new ArrayList<>();
    public static ArrayList<String> areasID = new ArrayList<>();
    public static long latestForecastTime = 0;

    public static long closestHourTime = 0;
    public static long chartOneTime = 0;
    public static long chartTwoTime = 0;
    public static long chartThreeTime = 0;
    public static String chartOneTimeLabel = "";
    public static String chartTwoTimeLabel = "";
    public static String chartThreeTimeLabel = "";


    public static void fetchAreas(int fetchMode) {
        clearOldData();
        JSONFetcher JF = new JSONFetcher(fetchMode);
        JF.execute(areasUrl);
    }

    public static void fetchForecast(String areaID, long time, int fetchMode) {
        clearOldData();
        String url = forecastUrl + areaID + "@" + time;
        Log.d(TAG, "fetchAndControlData: fetching data from : " + url);
        JSONFetcher JF = new JSONFetcher(fetchMode);
        JF.execute(url);
    }

    public static void parseAreasData(JSONObject data, boolean updateUI) {
        //Log.d(TAG, "parseAreasData: " + data.toString());
        if (data != null) { // if no data was fetched
            areasID.clear();
            areasName.clear();
            try {

                JSONArray areasArr = data.getJSONArray("areas");

                Log.d(TAG, "parseAreasData: areas" + areasArr.toString());
                for (int i = 0; i < areasArr.length(); i++) {
                    JSONObject obj = areasArr.getJSONObject(i);
                    areasName.add(obj.get("name").toString());
                    areasID.add(obj.get("id").toString());
                    //Log.d(TAG, "parseAreasData: id: " + obj.get("id"));
                    //Log.d(TAG, "parseAreasData: name: " + obj.get("name"));
                }

                JSONArray forecastsObj = data.getJSONArray("forecasts");

                JSONObject obj = forecastsObj.getJSONObject(0);
                latestForecastTime = Long.parseLong(obj.get("creation_time").toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (updateUI) {
                NavActivity.navActivity.updateNavItems();
                NavActivity.searchBar.updateList(areasName);

                if (!NavActivity.favoriteForecastOpened) {
                    NavActivity.navActivity.viewFavoriteForecast();
                }
            } else {
                checkForNewForecast();
            }
        } else {
            if (updateUI) {
                NavActivity.navActivity.displayError("Anslutning misslyckades", "Kunde inte hämta data, vänligen kontrollera internetanslutningen och försök igen");
            }

        }
    }

    public static String getAreaNameFromID(String areaID) {
        int index = areasID.indexOf(areaID);
        if (index != -1) {
            String areaName = areasName.get(index);
            return areaName;
        } else {
            return "";
        }
    }

    public static long getClosestHourTime() {
        long unixTime = System.currentTimeMillis() / 1000L;
        //long unixTime = latestForecastTime+3600;
        //Log.d(TAG, "getClosestHourTime: unixTime: " + unixTime);
        Log.d(TAG, "getClosestHourTime: time: " + unixTime);
        Log.d(TAG, "getClosestHourTime: time: " + unixToHumanTime(unixTime));

        long hourRest = unixTime % 3600;
        long closestHourTime = unixTime - hourRest; // Floored hour

        // if current time more than XX:30
        if (hourRest >= 1800) {
            closestHourTime += 3600;
        }
        //Log.d(TAG, "getClosestHourTime: cloests: " + closestHourTime);

        chartOneTime = closestHourTime;
        chartTwoTime = closestHourTime + 4 * 3600;
        chartThreeTime = closestHourTime + 8 * 3600;

        chartOneTimeLabel = unixToHumanTime(chartOneTime);
        chartTwoTimeLabel = unixToHumanTime(chartTwoTime);
        chartThreeTimeLabel = unixToHumanTime(chartThreeTime);

        Log.d(TAG, "getClosestHourTime: usingtime: " + unixToHumanTime(closestHourTime));
        Log.d(TAG, "getClosestHourTime: usingtime: " + (closestHourTime));

        return closestHourTime;
    }

    public static String unixToHumanTime(long unixTime) {
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
        String humanTime = dateFormat.format(date);

        return humanTime;
    }


    public static void clearOldData() {
        //categories.clear();
        //categorizedData.clear();
    }

    public static void verifyKey(String key, int fetchMode){
        Log.d(TAG, "verifyKey: running");
        KeyVerifier kv = new KeyVerifier(fetchMode);
        kv.execute(key);
    }

    // checks if there is new forecast available and fetches the new forecast if there is a new one available
    public static void checkForNewForecast() {
        Log.d(TAG, "-----------------");
        Log.d(TAG, "checkifnew1: " + latestForecastTime);

        long lastControlledTime = StorageManager.getLastControlledForecastTime(Alarm.currentAlarmContext);
        Log.d(TAG, "checkifnew2: " + lastControlledTime);

        // if there is a new forecast to be controlled
        if (lastControlledTime != latestForecastTime) {
            //if (true) { // tmp
            Set<String> watchedAreas = StorageManager.getWatchedAreas(Alarm.currentAlarmContext);
            Log.d(TAG, "checkForNewForecast: control:  " + watchedAreas.toString());
            for (String area : watchedAreas) {
                Log.d(TAG, "fetchAndControlData: area: " + area);
                fetchForecast(area, latestForecastTime, JSONFetcher.FETCH_FORECAST_IN_BACKGROUND);

            }
            StorageManager.setLastControlledForecastTime(latestForecastTime, Alarm.currentAlarmContext);
        } else {
            //Notifications.sendNotification(Alarm.currentAlarmContext,"test", "no prognos", "0");
        }
    }


    public static void fetchAndControlData(Context context) {
        Log.d(TAG, "fetchAndControlData: running");
        if (StorageManager.getNotificationsEnabled(context)) {
            Log.d(TAG, "fetchAndControlData: notifications are enabled");
            fetchAreas(JSONFetcher.FETCH_AREAS_IN_BACKGROUND);
        }
    }
}
