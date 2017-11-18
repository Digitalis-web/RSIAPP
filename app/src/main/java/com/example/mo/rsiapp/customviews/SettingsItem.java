package com.example.mo.rsiapp.customviews;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mo.rsiapp.R;
import com.example.mo.rsiapp.datamanaging.DisplayInfoManager;

import java.util.Set;

/**
 * Created by mo on 12/10/17.
 */

public class SettingsItem implements SeekBar.OnSeekBarChangeListener, CheckBox.OnCheckedChangeListener{
    public static final String TAG = "NavAreaItem";
    private String name;
    private String labelName;
    private int sliderValue = 0;
    private View view;

    private String labelBase = "%s överstiger %d%%";
    private SeekBar slider;
    private CheckBox checkbox;
    private boolean enabled = false; // if notifications for this layer is enabled


    public SettingsItem(String name)
    {
        this.name = name;
        this.labelName = DisplayInfoManager.getRoadConditionInfoByName(name, "label");
    }


    public void setSavedSettings(Set<String> savedSettings){
        for(String savedStr : savedSettings){
            String[] split = savedStr.split(",");

            String name;
            boolean enabled;
            int value;

            if(split.length >= 3) {
                name = split[0];
                if(name.equals(getName())){
                    enabled = split[1].equals("1");
                    value = Integer.parseInt(split[2]);
                    setEnabled(enabled);
                    setSliderValue(value);
                }
            }

        }
    }


    public String getLabelName(){
        return labelName;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public void setSliderValue(int value){
        slider.setProgress(value);
        sliderValue = value;
    }

    public void setEnabled(boolean value){
        checkbox.setChecked(value);
        enabled = value;
    }

    public int getSliderValue(){
        return sliderValue;
    }

    public String getName(){
        return name;
    }

    public void initComponents(View view){
        this.view = view;
        slider = view.findViewById(R.id.settings_slider);
        checkbox = view.findViewById(R.id.settings_item_check);

        slider.setOnSeekBarChangeListener(this);
        checkbox.setOnCheckedChangeListener(this);
        updateLabel();
    }

    public void updateLabel(){
        //CheckBox labelView = view.findViewById(R.id.settings_item_check);
        String formattedLabel = String.format(labelBase, labelName, sliderValue);
        checkbox.setText(formattedLabel);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        sliderValue = progress;
        Log.d(TAG, "onProgressChanged: prog: " + progress + " user: " + fromUser);
        //slider.setProgress(progress);
        updateLabel();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        this.enabled = b;
    }
}
