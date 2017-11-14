package com.example.mo.rsiapp.datamanaging;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import static java.nio.file.Paths.get;

/**
 * Created by mo on 13/11/17.
 */

public class DisplayInfoManager {
    public static final String TAG = "DisplayInfo";

    // The categories that are to be viewable in the app
    public static ArrayList<String> viewCategories = new ArrayList<String>(){{
        add("roadcondition");
        add("roadfriction");
        add("roadtemperature");
        add("slipincidents");
        add("roadtreatment");
    }};

    public static ArrayList<HashMap<String, String>> roadConditionInfo = new ArrayList<>();
    public static HashMap<String, String> saltColors = new HashMap<>();

    public static void initData(){
        roadConditionInfo = initRoadConditionInfoArray();
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

    public static ArrayList<HashMap<String, String>> initRoadConditionInfoArray(){
        ArrayList<HashMap<String, String>> roadConditionInfo = new ArrayList<>();

        HashMap<String, String> map = new HashMap<>();

        map.put("name", "Dry");
        map.put("color", "#99cc66");
        map.put("label", "Torrt");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "Moist");
        map.put("color", "#8eb1e6");
        map.put("label", "Fuktigt");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "Wet");
        map.put("color", "#2a70d9");
        map.put("label", "Vått");
        roadConditionInfo.add(map);

        //[{ name: 'Dry', color: '#99cc66', stroke: 'rgba(0,0,0,0.2)', label: 'Torrt' },
        // { name: 'Moist', color: '#8eb1e6', stroke: 'rgba(0,0,0,0.2)', label: 'Fuktigt' },
        // { name: 'Wet', color: '#2a70d9', stroke: 'rgba(0,0,0,0.2)', label: 'VÃ¥tt' },
        // { name: 'LightSnow', color: '#66ffff', stroke: 'rgba(0,0,0,0.2)', label: 'LÃ¤tt snÃ¶' },
        // { name: 'Snow', color: '#00c0c0', stroke: 'rgba(0,0,0,0.2)', label: 'SnÃ¶' },
        // { name: 'DriftingSnow', color: '#156262', stroke: 'rgba(0,0,0,0.2)', label: 'SnÃ¶drev' },
        // { name: 'Slipperiness', color: '#cc66cc', stroke: 'rgba(0,0,0,0.2)', label: 'Halka' },
        // { name: 'Hazardous', color: '#e96605', stroke: 'rgba(0,0,0,0.2)', label: 'SvÃ¥r halka' }];

        map = new HashMap<>();
        map.put("name", "LightSnow");
        map.put("color", "#66ffff");
        map.put("label", "Lätt snö");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "Snow");
        map.put("color", "#00c0c0");
        map.put("label", "Snö");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "DriftingSnow");
        map.put("color", "#156262");
        map.put("label", "Snö drev");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "Slipperiness");
        map.put("color", "#cc66cc");
        map.put("label", "Halka");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "Hazardous");
        map.put("color", "#e96605");
        map.put("label", "Svår halka");
        roadConditionInfo.add(map);

        return roadConditionInfo;
    }
}
