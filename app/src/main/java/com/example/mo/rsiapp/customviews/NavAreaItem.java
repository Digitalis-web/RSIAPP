package com.example.mo.rsiapp.customviews;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.mo.rsiapp.NavActivity;
import com.example.mo.rsiapp.R;
import com.example.mo.rsiapp.datamanaging.FetchingManager;
import com.example.mo.rsiapp.datamanaging.JSONFetcher;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.example.mo.rsiapp.NavActivity.openForecast;

/**
 * Created by mo on 12/10/17.
 */

public class NavAreaItem {
    public static final String TAG = "NavAreaItem";
    private String name;
    private String areaID;

    public NavAreaItem(String name, String areaID)
    {
        this.name = name;
        this.areaID = areaID;
    }



    public String getName(){
        return name;
    }

    public void onClick(View view, Context context) {
        Log.d(TAG, "onClick: clicked");
        NavActivity.openLoadingScreen();
        FetchingManager.fetchForecast(areaID, FetchingManager.latestForecastTime, JSONFetcher.FETCH_FORECAST);
        NavActivity.navActivity.closeNav();
    }

}
