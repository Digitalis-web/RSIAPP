package com.example.mo.rsiapp.customviews;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.mo.rsiapp.R;
import com.example.mo.rsiapp.datamanaging.DisplayInfoManager;

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
    private Switch checkbox;
    private boolean enabled = false; // if notifications for this layer is enabled
    private boolean savedSettingsSet = false;

    private boolean savedEnabled = false;
    private int savedSliderValue = 0;


    public SettingsItem(String name)
    {
        this.name = name;
        this.labelName = DisplayInfoManager.getRoadConditionInfoByName(name, "label");
    }

    public void setFromSavedSettings(){
        if(!savedSettingsSet) {
            savedSettingsSet = true;
            this.sliderValue = savedSliderValue;
            this.enabled = savedEnabled;
        }
    }

    public void setSavedValues(boolean enabled, int savedSliderValue){
        this.savedEnabled = enabled;
        this.savedSliderValue = savedSliderValue;
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
        setFromSavedSettings();
    }

    public void updateLabel(){
        //CheckBox labelView = view.findViewById(R.id.settings_item_check);
        String formattedLabel = String.format(labelBase, labelName, sliderValue);
        checkbox.setText(formattedLabel);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        sliderValue = progress;
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
