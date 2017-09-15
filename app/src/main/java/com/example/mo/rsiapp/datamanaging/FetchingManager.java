package com.example.mo.rsiapp.datamanaging;

import android.util.Log;

import com.example.mo.rsiapp.NavActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.N;
import static java.nio.file.Paths.get;

/**
 * Created by mo on 23/07/17.
 */

public class FetchingManager {

    private static String url = "http://163.172.101.14:8000/api/ogJu1VCu09HpHD6VbHX34jChdoKz2fR5/area/1427@1497772800";
    private static String forecastUrl = "http://163.172.101.14:8000/api/ogJu1VCu09HpHD6VbHX34jChdoKz2fR5/area/";
    private static String areasUrl = "http://163.172.101.14:8000/api//forecasts";
    private static String TAG = "FetchingManager";
    public static ArrayList<String> areasName = new ArrayList<>();
    public static ArrayList<String> areasID = new ArrayList<>();
    public static long latestForecastTime = 0;

    public static void fetchAreas(){
        Log.d(TAG, "fetchAndControlData: fetching data");
        JSONFetcher JF = new JSONFetcher(false);
        JF.execute(areasUrl);
    }

    public static void fetchForecast(int areaID, long time){
        String url = forecastUrl + areaID + "@" + time;
        Log.d(TAG, "fetchAndControlData: fetching data from : " + url);
        JSONFetcher JF = new JSONFetcher(true);
        JF.execute(url);
    }

    public static void parseAreasData(JSONObject data) {
        Log.d(TAG, "parseAreasData: " + data.toString());
        try {
            JSONArray areasObj = data.getJSONArray("areas");

            Log.d(TAG, "parseAreasData: areas" + areasObj.toString());
            for(int i = 0; i < areasObj.length(); i++){
                JSONObject obj = areasObj.getJSONObject(i);
                areasName.add(obj.get("name").toString());
                areasID.add(obj.get("id").toString());
                //Log.d(TAG, "parseAreasData: id: " + obj.get("id"));
                //Log.d(TAG, "parseAreasData: name: " + obj.get("name"));
            }

            JSONArray forecastsObj = data.getJSONArray("forecasts");

            for(int i = 0; i < forecastsObj.length(); i++) {
                JSONObject obj = forecastsObj.getJSONObject(i);

                latestForecastTime = Long.parseLong(obj.get("creation_time").toString());
                Log.d(TAG, "parseAreasData: lastforecast: " + latestForecastTime);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        NavActivity.searchBar.updateList(areasName);
    }
    public static void parseForecastData(JSONObject data){
        Log.d(TAG, "parseData: running");
        try {
            String time = data.get("times").toString();
            Log.d(TAG, "parseData: " + time);

            // If there is any data for this area
            if(data.has("data")){
                Log.d(TAG, "parseForecastData: data finns");
                String d = data.get("data").toString();
                JSONArray dataObj = data.getJSONArray("data");
                Log.d(TAG, "parseData: " + dataObj.toString());
            }
            else {
                Log.d(TAG, "parseForecastData: data finns INTE");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        NavActivity.openForecast(NavActivity.navActivity);
    }

    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }
}
