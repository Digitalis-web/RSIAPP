package com.example.mo.rsiapp.customviews;

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

}
