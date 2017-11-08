package com.example.mo.rsiapp.customviews;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by mo on 08/11/17.
 */

public class WatchAreaButton extends android.support.v7.widget.AppCompatButton implements AppCompatButton.OnClickListener {
    public final String TAG = "WatchAreaButton";

    public WatchAreaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");

    }
}
