package com.roadstatusinfo.mo.rsiapp.datamanaging;

import android.util.Log;

import com.roadstatusinfo.mo.rsiapp.NavActivity;
import com.roadstatusinfo.mo.rsiapp.backgroundtasks.Alarm;
import com.roadstatusinfo.mo.rsiapp.backgroundtasks.Notifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.roadstatusinfo.mo.rsiapp.datamanaging.FetchingManager.unixToHumanTime;


/**
 * Created by mo on 19/11/17.
 */

public class Forecast {
    // Variables relevant to the latest forecast fetch
    public ArrayList<String> categories = new ArrayList<>();
    public ArrayList<JSONObject> categorizedData = new ArrayList<>();
    public String areaID = "";


    public long latestForecastImportTime = 0;
    public String latestForecastImportTimeLabel = "";

    private static final String TAG = "Forecast";

    private JSONObject data;

    public Forecast(){

    }

    // Finds the data for a given route
    public ArrayList<JSONObject> getAllDataByRouteID(JSONObject data, int id){

        ArrayList<JSONObject> matchedData = new ArrayList<>(); // The array to be returned

        try {
            JSONArray dataArr = data.getJSONArray("data");
            // loops over all the items in the data list and adds the relevant ones to the matchedData array
            for(int i = 0; i < dataArr.length();i++) {
                JSONObject dataItem = dataArr.getJSONObject(i);
                JSONObject dataRouteObj = dataItem.getJSONObject("route");
                int route = dataRouteObj.getInt("route_id");

                // adds the items that match the given route_id
                if(route == id){
                    matchedData.add(dataItem);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return matchedData;
    }

    // requestedLayers specifies which layers that should be returned. If null all layers will be returned
    public HashMap<String, Long> getDataPoint(String category, long time, Set<String> requestedLayers){

        if(data == null){
            if(NavActivity.navActivity != null) {
                NavActivity.navActivity.displayConnectError();
                FetchingManager.fetchAreas(JSONFetcher.FETCH_AREAS);
            }
        }

        HashMap<String, Long> values = new HashMap<>();
        try {
            JSONObject data = getDataForCategory(category);
            JSONArray seriesArray = data.getJSONArray("series");

            for(int i = 0; i < seriesArray.length(); i++){
                JSONObject seriesItem = seriesArray.getJSONObject(i);
                String name = seriesItem.getString("name");
                JSONArray seriesData = seriesItem.getJSONArray("data");

                for(int n = 0; n < seriesData.length(); n++){
                    JSONObject timePointObj = seriesData.getJSONObject(n);
                    long pointTime = timePointObj.getLong("x");
                    Long value = timePointObj.getLong("y");

                    if(pointTime == time){

                        if(requestedLayers == null) {
                            values.put(name, value);
                        }
                        else if(requestedLayers.contains(name)){
                            values.put(name, value);
                        }
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;
    }

    // Finds the type of data that is available for a given route or area
    public ArrayList<String> findAllCategories(ArrayList<JSONObject> dataList){
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

    public JSONObject findDataByCategory(ArrayList<JSONObject> dataList, String category) {

        try {
            // Loops over all the given data and find the one that matched the given category
            for(int i = 0; i < dataList.size(); i++){ JSONObject dataItem = dataList.get(i);
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

    public JSONObject getDataForCategory(String category){

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

    public int getTotalLengthForRoute(int routeID) {

        try {
            JSONArray routeArr = data.getJSONArray("routes");

            // loops over all the items in the data list and adds the relevant ones to the matchedData array
            for(int i = 0; i < routeArr.length();i++) {
                JSONObject routeItem = routeArr.getJSONObject(i);
                //JSONObject dataRouteObj = routeItem.getJSONObject("total_length");
                int route = routeItem.getInt("route_id");

                if(route == routeID){
                    int totalLength = routeItem.getInt("total_length");
                    Log.d(TAG, "getTotalLengthForRoute: totallength: " + totalLength);
                    return totalLength;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;


    }

    public void controlData(){

        String category = "roadcondition";
        Log.d(TAG, "controlData: controlling data");

        int routeLength = getTotalLengthForRoute(0);

        Set<String> savedSettings = StorageManager.getSettings(Alarm.currentAlarmContext);
        HashMap<String, Integer> trackedAreasValues = new HashMap<>();

        for(String savedStr : savedSettings){
            String[] split = savedStr.split(",");
            String layer = "";
            boolean enabled = false;
            int value = 0;

            if(split.length >= 3){
                layer = split[0];
                enabled = split[1].equals("1");
                value = Integer.parseInt(split[2]);
                if(enabled){
                    trackedAreasValues.put(layer, value);
                }
            }


/*            Log.d(TAG, "controlData: layer: " + layer);
            Log.d(TAG, "controlData: enabled: " + enabled);
            Log.d(TAG, "controlData: value: " + value);*/

        }

        //long currentTime = FetchingManager.getClosestHourTime();
        //HashMap<String, Long> values = new HashMap<>();


        ArrayList<String> notifyLayers = new ArrayList<>(); // the layers that are over the threshold
        ArrayList<Integer> notifyValues = new ArrayList<>();
        ArrayList<String> notifyTimes = new ArrayList<>();


        ArrayList<Long> controlTimes = new ArrayList<Long>(){{
            add(FetchingManager.chartOneTime);
            add(FetchingManager.chartTwoTime);
            add(FetchingManager.chartThreeTime);
        }};


        outerLoop:
        for(int n = 0; n < controlTimes.size(); n++) {
            long time = controlTimes.get(n);
            HashMap<String, Long> dataPoints =  getDataPoint("roadcondition", time, trackedAreasValues.keySet());
            String timeLabel = unixToHumanTime(time);
/*            Log.d(TAG, "controlData: datapoints: " + dataPoints.toString());
            Log.d(TAG, "controlData: --------------------------------" + time);*/

            Set<String> dataPointsKeys = dataPoints.keySet();

            for(String layer : dataPointsKeys){
                Long value = dataPoints.get(layer);
                int percent = (int) (((value*1.0) / routeLength)*100);
                int maxValue = trackedAreasValues.get(layer);
                if(percent > 0){
                    if(percent >= maxValue){
                        //if(!notifyLayers.contains(layer)) {
                        notifyLayers.add(layer);
                        notifyValues.add(percent);
                        notifyTimes.add(timeLabel);
                        //}
                    }
                }

/*                Log.d(TAG, "controlData: kör123");
                Log.d(TAG, "controlData: value2: " + value );
                Log.d(TAG, "controlData: layer: " + layer );
                Log.d(TAG, "controlData: percent: " + percent );
                Log.d(TAG, "controlData: area: " + areaID );*/

            }



        }

        if(notifyLayers.size() > 0) {
            String message = "";
            String areaName = FetchingManager.getAreaNameFromID(areaID);
            for(int i = 0; i < notifyLayers.size(); i++){
                String layer = notifyLayers.get(i);
                int percent = notifyValues.get(i);
                String timeLabel = notifyTimes.get(i);

                String label = DisplayInfoManager.getLayerLabel(layer);
                message +=  label + " når " + percent + "%  " + timeLabel + "\n";
            }

            Notifications.sendNotification(Alarm.currentAlarmContext, "Ny prognos för " + areaName + " visar", message, areaID);
            Log.d(TAG, "controlData: sending notification for " + areaName);
        }


    }

    public void parseData(JSONObject data, boolean updateUI){
        this.data = data;

        Log.d(TAG, "parseData: length of data: " + data.toString().length());
        int routeLength = 0;
        try {
            String time = data.get("times").toString();
            areaID = data.get("area").toString();
            Log.d(TAG, "parseData: " + time);



            // If there is any data for this area
            if(data.has("data")){
                Log.d(TAG, "parseForecastData: data finns");
                //String d = data.get("data").toString();

                routeLength = getTotalLengthForRoute(0);

                ArrayList<JSONObject> routeData = getAllDataByRouteID(data, 0); // picks out the relevant data
                categories = findAllCategories(routeData);

                for(int i = 0; i < categories.size(); i++){
                    JSONObject dataObj = findDataByCategory(routeData, categories.get(i));
                    categorizedData.add(dataObj);
                    //Log.d(TAG, "parseForecastData: " + dataObj.toString());
                }

                latestForecastImportTime = Long.parseLong(data.get("import_time").toString());
                latestForecastImportTimeLabel  = unixToHumanTime(latestForecastImportTime);
            }
            else {
                if(NavActivity.navActivity != null) {
                    NavActivity.navActivity.displayError("Kunde inte hämta område", "Data ej tillgängligt för valt område");
                }
                Log.d(TAG, "parseForecastData: data finns INTE");
                return;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        FetchingManager.closestHourTime = FetchingManager.getClosestHourTime();

        if(updateUI) {
            NavActivity.openForecast(areaID, routeLength, this);
        } else {
            controlData();
            Log.d(TAG, "parseData: controlling data");
        }
    }
}
