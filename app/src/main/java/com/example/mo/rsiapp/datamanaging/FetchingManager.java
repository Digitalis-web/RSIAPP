package com.example.mo.rsiapp.datamanaging;

import android.util.Log;

import com.example.mo.rsiapp.NavActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader; import java.io.IOException; import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


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

    public static long closestHourTime = 0;
    public static long chartOneTime = 0;
    public static long chartTwoTime = 0;
    public static long chartThreeTime = 0;

    // Variables relevant to the latest forecast fetch
    public static ArrayList<String> categories = new ArrayList<>();
    public static ArrayList<JSONObject> categorizedData = new ArrayList<>();

    public static void fetchAreas() {
        Log.d(TAG, "fetchAndControlData: fetching data");
        clearOldData();
        JSONFetcher JF = new JSONFetcher(false);
        JF.execute(areasUrl);
    }

    public static void fetchForecast(String areaID, long time) {
        clearOldData();
        String url = forecastUrl + areaID + "@" + time;
        Log.d(TAG, "fetchAndControlData: fetching data from : " + url);
        JSONFetcher JF = new JSONFetcher(true);
        JF.execute(url);
    }

    public static void parseAreasData(JSONObject data) {
        Log.d(TAG, "parseAreasData: " + data.toString());
        try {

            JSONArray areasArr = data.getJSONArray("areas");

            Log.d(TAG, "parseAreasData: areas" + areasArr.toString());
            for(int i = 0; i < areasArr.length(); i++){
                JSONObject obj = areasArr.getJSONObject(i);
                areasName.add(obj.get("name").toString());
                areasID.add(obj.get("id").toString());
                //Log.d(TAG, "parseAreasData: id: " + obj.get("id"));
                //Log.d(TAG, "parseAreasData: name: " + obj.get("name"));
            }

            JSONArray forecastsObj = data.getJSONArray("forecasts");

            for(int i = 0; i < forecastsObj.length(); i++) {
                JSONObject obj = forecastsObj.getJSONObject(i);

                latestForecastTime = Long.parseLong(obj.get("creation_time").toString());
                //latestForecastTime = 1485680400; // TEMP DEBUG
                Log.d(TAG, "parseAreasData: lastforecast: " + latestForecastTime);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        NavActivity.navActivity.updateNavItems();
        NavActivity.searchBar.updateList(areasName);
    }

    public static String getAreaNameFromID(String areaID){
        int index = areasID.indexOf(areaID);
        if(index != -1) {
            String areaName = areasName.get(index);
            return areaName;
        }
        else {
            return "";
        }
    }

    public static void parseForecastData(JSONObject data){
        Log.d(TAG, "parseData: length of data: " + data.toString().length());
        String areaID = "" ;
        try {
            String time = data.get("times").toString();
            areaID = data.get("area").toString();
            Log.d(TAG, "parseData: " + time);


            // If there is any data for this area
            if(data.has("data")){
                Log.d(TAG, "parseForecastData: data finns");
                String d = data.get("data").toString();


                ArrayList<JSONObject> routeData = getAllDataByRouteID(data, 0); // picks out the relevant data
                categories = findAllCategories(routeData);

                for(int i = 0; i < categories.size(); i++){
                    JSONObject dataObj = findDataByCategory(routeData, categories.get(i));
                    categorizedData.add(dataObj);
                    //Log.d(TAG, "parseForecastData: " + dataObj.toString());
                }

                //Log.d(TAG, "parseForecastData: roadcond_data: " + getDataForCategory("roadcondition").getJSONArray("series"));
                //getDataPoint("roadcondition", 0);




                //Log.d(TAG, "parseData: " + dataObj.toString());

            }
            else {
                Log.d(TAG, "parseForecastData: data finns INTE");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        closestHourTime = getClosestHourTime();
        chartOneTime = closestHourTime;
        chartTwoTime = closestHourTime + 4*3600;
        chartThreeTime = closestHourTime + 8*3600;
        NavActivity.openForecast(areaID);
    }

    public static HashMap<String, Long> getDataPoint(String category, long time){

        HashMap<String, Long> values = new HashMap<>();
        try {
            JSONObject data = getDataForCategory(category);
            JSONArray seriesArray = data.getJSONArray("series");

            for(int i = 0; i < seriesArray.length(); i++){
                //Log.d(TAG, "getDataPoint: " + seriesArray.get(i));
                //Log.d(TAG, "getDataPoint: " + seriesArray.getJSONObject(i));
                JSONObject seriesItem = seriesArray.getJSONObject(i);
                String name = seriesItem.getString("name");
                JSONArray seriesData = seriesItem.getJSONArray("data");
                ////Log.d(TAG, "getDataPoint: data : " + seriesData);

                for(int n = 0; n < seriesData.length(); n++){
                    JSONObject timePointObj = seriesData.getJSONObject(n);
                    long pointTime = timePointObj.getLong("x");
                    Long value = timePointObj.getLong("y");
                    //Log.d(TAG, "getDataPoint: value: " + value);

                    if(pointTime == time){
                        values.put(name, value);
                    }

                }

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;
    }

    public static long getClosestHourTime(){
        //long unixTime = System.currentTimeMillis() / 1000L;
        long unixTime = latestForecastTime+3600;
        //Log.d(TAG, "getClosestHourTime: unixTime: " + unixTime);

        long hourRest = unixTime % 3600;
        long closestHourTime = unixTime - hourRest; // Floored hour

        // if current time more than XX:30
        if(hourRest >= 1800) {
            closestHourTime += 3600;
        }
        //Log.d(TAG, "getClosestHourTime: cloests: " + closestHourTime);


        return closestHourTime;

    }

    public static JSONObject getDataForCategory(String category){

        int index = categories.indexOf(category);
        Log.d(TAG, "getDataForCategory: " + categories.toString());
        JSONObject obj = null;
        if(index != -1){
            obj = categorizedData.get(index);
        }
        else {
            Log.d(TAG, "getDataForCategory: Category does not exist");
        }

        return obj;

    }

    public static JSONObject findDataByCategory(ArrayList<JSONObject> dataList, String category) {

        try {
            // Loops over all the given data and find the one that matched the given category
            for(int i = 0; i < dataList.size(); i++){
                JSONObject dataItem = dataList.get(i);
                String layer = dataItem.get("layer").toString();

                if(layer.equals(category)){
                    return dataItem;
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
    // Finds the type of data that is available for a given route
    public static ArrayList<String> findAllCategories(ArrayList<JSONObject> dataList){
        ArrayList<String> categories = new ArrayList<>();

        try {
            for(int i = 0; i < dataList.size(); i++){
                JSONObject dataItemObj = dataList.get(i);
                String category = dataItemObj.get("layer").toString();
                Log.d(TAG, "findAllCategories: categories found: " + category);
                categories.add(category);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return categories;
    }
    // Finds the data for a given route
    public static ArrayList<JSONObject> getAllDataByRouteID(JSONObject data, int id){

        ArrayList<JSONObject> matchedData = new ArrayList<>(); // The array to be returned

        try {
            JSONArray dataArr = data.getJSONArray("data");
            // loops over all the items in the data list and adds the relevant ones to the matchedData array
            for(int i = 0; i < dataArr.length();i++) {
                //Log.d(TAG, "parseForecastData: value : " + dataArr.get(i));
                JSONObject dataItem = dataArr.getJSONObject(i);
                //JSONArray dataItemArr = dataArr.getJSONArray(i);
                JSONObject dataRouteObj = dataItem.getJSONObject("route");
                int route = dataRouteObj.getInt("route_id");

                // adds the items that match the given route_id
                if(route == id){
                    matchedData.add(dataItem);
                }
                //Log.d(TAG, "parseForecastData: route_id : + " + dataRouteObj.get("route_id"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return matchedData;



    }

    public static void clearOldData(){
        categories.clear();
        categorizedData.clear();
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
