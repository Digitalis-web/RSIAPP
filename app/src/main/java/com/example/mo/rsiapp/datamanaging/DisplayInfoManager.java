package com.example.mo.rsiapp.datamanaging;

import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.example.mo.rsiapp.NavActivity;
import com.example.mo.rsiapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mo on 13/11/17.
 */

public class DisplayInfoManager {
    public static final String TAG = "DisplayInfo";

    // The categories that are to be viewable in the app, other categories will not be shown
    public static ArrayList<String> viewCategories = new ArrayList<String>(){{
        add("roadcondition");
        //add("roadfriction");
        add("roadtemperature");
        //add("slipincidents");
        //add("roadtreatment");
    }};

    public static ArrayList<HashMap<String, String>> roadConditionInfo = new ArrayList<>();
    public static HashMap<String, String> saltColors = new HashMap<>();

    public static void initData(){
        initRoadConditionInfoArray();
        saltColors = initSaltColors();
    }

    public static HashMap<String, String> initSaltColors(){
        HashMap<String, String> map = new HashMap<String, String>();

        for(int i = 0; i <= 24; i++){
            String key = i + "g salt";
            String redAndGreen = Integer.toHexString(255-i*10);
            String hexColor =  "#" + redAndGreen + redAndGreen + "FF";
            map.put(key, hexColor);
        }

        return map;
    }

    public static String getSaltColor(String key){
        String color = saltColors.get(key);
        Log.d(TAG, "getSaltColor: color: " + color);
        // fallback color
        if (color == null) {
            color = "#0000FF";
        }
        return color;
    }

    public static String getRoadConditionInfoByName(String name, String type){

        for(int i = 0; i < DisplayInfoManager.roadConditionInfo.size(); i++) {
            HashMap<String, String> map = DisplayInfoManager.roadConditionInfo.get(i);
            if (map.get("name").equals(name)) {
                return map.get(type);
            }
        }

        return "";
    }

    public static String getLayerLabel(String name){
        String label = "";
        switch (name){
            case "Dry":
                label = "Torrt";
                break;
            case "Moist":
                label = "Fuktigt";
                break;
            case "Wet":
                label = "Vått";
                break;
            case "LightSnow":
                label = "Lätt snö";
                break;
            case "Snow":
                label = "Snö";
                break;
            case "DriftingSnow":
                label = "Snö drev";
                break;
            case "Slipperiness":
                label = "Halka";
                break;
            case "Hazardous":
                label = "Svår halka";
                break;
        }
        return label;

    }

    public static String getCategoryLabel(String category){
        String label = "";
        switch (category){
            case "roadcondition":
                label = "Väglag";
                break;
            case "roadfriction":
                label = "Friktion";
                break;
            case "roadtemperature":
                label = "Yttemperatur";
                break;
            case "slipincidents":
                label = "Halkrapporter";
                break;
            case "roadtreatment":
                label = "Åtgärder";
                break;
        }
        return label;

    }

    public static void putRoadConditionRow(String name, String color){
        HashMap<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("color", color);
        map.put("label", getLayerLabel(name));
        roadConditionInfo.add(map);
    }

    public static void initRoadConditionInfoArray(){

        String dry = String.valueOf(ResourcesCompat.getColor(NavActivity.navActivity.getResources(), R.color.colorDry, null));
        String moist = String.valueOf(ResourcesCompat.getColor(NavActivity.navActivity.getResources(), R.color.colorMoist, null));
        String wet = String.valueOf(ResourcesCompat.getColor(NavActivity.navActivity.getResources(), R.color.colorWet, null));
        String lightSnow = String.valueOf(ResourcesCompat.getColor(NavActivity.navActivity.getResources(), R.color.colorLightSnow, null));
        String snow = String.valueOf(ResourcesCompat.getColor(NavActivity.navActivity.getResources(), R.color.colorSnow, null));
        String driftingSnow = String.valueOf(ResourcesCompat.getColor(NavActivity.navActivity.getResources(), R.color.colorDriftingSnow, null));
        String slipperiness = String.valueOf(ResourcesCompat.getColor(NavActivity.navActivity.getResources(), R.color.colorSlipperiness, null));
        String hazardous = String.valueOf(ResourcesCompat.getColor(NavActivity.navActivity.getResources(), R.color.colorHazardous, null));

        putRoadConditionRow("Dry", dry);
        putRoadConditionRow("Moist", moist);
        putRoadConditionRow("Wet", wet);
        putRoadConditionRow("LightSnow", lightSnow);
        putRoadConditionRow("Snow", snow);
        putRoadConditionRow("DriftingSnow", driftingSnow);
        putRoadConditionRow("Slipperiness", slipperiness);
        putRoadConditionRow("Hazardous", hazardous);

        //[{ name: 'Dry', color: '#99cc66', stroke: 'rgba(0,0,0,0.2)', label: 'Torrt' },
        // { name: 'Moist', color: '#8eb1e6', stroke: 'rgba(0,0,0,0.2)', label: 'Fuktigt' },
        // { name: 'Wet', color: '#2a70d9', stroke: 'rgba(0,0,0,0.2)', label: 'VÃ¥tt' },
        // { name: 'LightSnow', color: '#66ffff', stroke: 'rgba(0,0,0,0.2)', label: 'LÃ¤tt snÃ¶' },
        // { name: 'Snow', color: '#00c0c0', stroke: 'rgba(0,0,0,0.2)', label: 'SnÃ¶' },
        // { name: 'DriftingSnow', color: '#156262', stroke: 'rgba(0,0,0,0.2)', label: 'SnÃ¶drev' },
        // { name: 'Slipperiness', color: '#cc66cc', stroke: 'rgba(0,0,0,0.2)', label: 'Halka' },
        // { name: 'Hazardous', color: '#e96605', stroke: 'rgba(0,0,0,0.2)', label: 'SvÃ¥r halka' }];
    }
}
