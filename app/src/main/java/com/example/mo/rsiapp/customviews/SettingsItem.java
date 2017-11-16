package com.example.mo.rsiapp.customviews;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mo.rsiapp.NavActivity;
import com.example.mo.rsiapp.R;
import com.example.mo.rsiapp.datamanaging.DisplayInfoManager;
import com.example.mo.rsiapp.datamanaging.FetchingManager;
import com.example.mo.rsiapp.datamanaging.JSONFetcher;

/**
 * Created by mo on 12/10/17.
 */

public class SettingsItem implements SeekBar.OnSeekBarChangeListener{
    public static final String TAG = "NavAreaItem";
    private String name;
    private String label;
    private int sliderValue = 0;
    private View view;

    private String labelBase = "%s överstiger %d%%";
    private SeekBar slider;


    public SettingsItem(String name)
    {
        this.name = name;
        this.label = DisplayInfoManager.getRoadConditionInfoByName(name, "label");
    }


    public String getLabel(){
        return label;
    }

    public String getName(){
        return name;
    }

    public void initComponents(View view){
        this.view = view;
        slider = view.findViewById(R.id.settings_slider);
        slider.setOnSeekBarChangeListener(this);
        updateLabel();
    }

    public void updateLabel(){
        TextView labelView = view.findViewById(R.id.settings_item_header);
        String formattedLabel = String.format(labelBase, label, sliderValue);
        labelView.setText(formattedLabel);
    }

    public void onClick(View view, Context context) {
/*        Log.d(TAG, "onClick: clicked");
        NavActivity.openLoadingScreen();
        FetchingManager.fetchForecast(areaID, FetchingManager.latestForecastTime, JSONFetcher.FETCH_FORECAST);
        NavActivity.navActivity.closeNav();*/
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        sliderValue = progress;
        updateLabel();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
