package com.example.mo.rsiapp.customviews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mo.rsiapp.R;
import com.example.mo.rsiapp.datamanaging.StorageManager;

import static android.R.attr.textColor;
import static com.example.mo.rsiapp.datamanaging.StorageManager.addWatchedArea;

/**
 * Created by mo on 08/11/17.
 */

public class WatchAreaButton extends android.support.v7.widget.AppCompatButton implements AppCompatButton.OnClickListener {
    public final String TAG = "WatchAreaButton";

    private Context context;
    private String areaID = "";
    private boolean isAddButton = true; // if the button should add or remove the area from watched areas

    public WatchAreaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOnClickListener(this);
    }

    public void init(String areaID){
        this.areaID = areaID;

        isAddButton = !StorageManager.areaIsWatched(areaID);
        Log.d(TAG, "init: isaddbutton " + isAddButton);

        // If the area is watched, the color of the button will be changed
        int bgColor;
        String text = "";
        if(!isAddButton){
            bgColor = ResourcesCompat.getColor(context.getResources(), R.color.colorYellow, null);
            text = "Sluta bevaka område";
        }
        else {
            bgColor = ResourcesCompat.getColor(context.getResources(), R.color.colorBlue, null);
            text = "Bevaka område";

        }
        setBackgroundColor(bgColor);
        setText(text);

    }

    @Override
    public void onClick(View view) {
        if(isAddButton) {
            Log.d(TAG, "onClick: adding area " + areaID);
            StorageManager.addWatchedArea(areaID);
        }
        else {
            Log.d(TAG, "onClick: removing area " + areaID);
            StorageManager.removeWatchedArea(areaID);
        }

        init(areaID);
    }
}
