package com.example.mo.rsiapp.customviews;

import android.view.View;

import com.example.mo.rsiapp.NavActivity;
import com.example.mo.rsiapp.datamanaging.FetchingManager;
import com.example.mo.rsiapp.datamanaging.JSONFetcher;

/**
 * Created by mo on 12/10/17.
 */

public class NavAreaItem implements View.OnClickListener{
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

    @Override
    public void onClick(View view) {
        NavActivity.openLoadingScreen();
        FetchingManager.fetchForecast(areaID, FetchingManager.latestForecastTime, JSONFetcher.FETCH_FORECAST);
        NavActivity.searchBar.clearText();
        NavActivity.navActivity.closeNav();

    }
}
