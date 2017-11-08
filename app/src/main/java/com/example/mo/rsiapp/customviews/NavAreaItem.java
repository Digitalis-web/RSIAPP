package com.example.mo.rsiapp.customviews;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.mo.rsiapp.NavActivity;
import com.example.mo.rsiapp.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by mo on 12/10/17.
 */

public class NavAreaItem {
    private String name;
    private boolean isAddButton;

    public NavAreaItem(String name, boolean isAddButton)
    {
        this.isAddButton = isAddButton;
        this.name = name;
    }


    public boolean isAddButton(){
        return isAddButton;
    }

    public String getName(){
        return name;
    }

    public void onClick(View view, Context context) {
        /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout popupView = (LinearLayout)inflater.inflate(R.layout.nav_area_popup, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupView.addView(NavActivity.searchBar);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);*/


    }

}
