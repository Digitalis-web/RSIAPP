package com.roadstatusinfo.mo.rsiapp.customviews;

import android.graphics.drawable.Drawable;
import android.support.design.widget.CheckableImageButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.roadstatusinfo.mo.rsiapp.NavActivity;
import com.roadstatusinfo.mo.rsiapp.R;
import com.roadstatusinfo.mo.rsiapp.datamanaging.FetchingManager;
import com.roadstatusinfo.mo.rsiapp.datamanaging.JSONFetcher;
import com.roadstatusinfo.mo.rsiapp.datamanaging.StorageManager;

/**
 * Created by mo on 12/10/17.
 */

public class NavAreaItem implements View.OnClickListener{
    public static final String TAG = "NavAreaItem";
    private String name;
    private String areaID;
    private View view;
    private CheckableImageButton favoriteButton;
    private boolean isFavorite = false;

    public NavAreaItem(String name, String areaID)
    {
        this.name = name;
        this.areaID = areaID;
    }


    public void initComponents(View view){
        this.view = view;
        TextView name = view.findViewById(R.id.nav_item_header);
        name.setText(getName());
        view.setOnClickListener(this);

        favoriteButton = view.findViewById(R.id.favorite_area_button);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAreaFavorite();
            }
        });
    }

    public void makeAreaFavorite(){
        StorageManager.setFavoriteArea(areaID);
    }

    public void setIsFavorite(boolean isFavorite){
        Drawable icon;
        if(isFavorite){
            icon = ContextCompat.getDrawable(NavActivity.navActivity, R.drawable.ic_star_filled);
        }
        else {
            icon = ContextCompat.getDrawable(NavActivity.navActivity, R.drawable.ic_star_empty);
        }
        this.isFavorite = isFavorite;
        favoriteButton.setImageDrawable(icon);
    }

    public boolean isFavorite(){
        return isFavorite;
    }

    public String getAreaID() {
        return areaID;
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
