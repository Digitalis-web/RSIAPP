package com.roadstatusinfo.mo.rsiapp.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.roadstatusinfo.mo.rsiapp.R;

import java.util.ArrayList;

/**
 * Created by mo on 12/10/17.
 */


public class SettingsItemAdapterOld extends BaseAdapter  {
    // ArrayList<String> name, company, email, id, status;
    ArrayList<SettingsItem> settingsItems;
    Context c;
    private final String TAG = "NavAdapter";


    public SettingsItemAdapterOld(Context c, ArrayList<SettingsItem> list, ListView listView) {
        settingsItems = list;

        this.c = c;
        //listView.setOnItemClickListener(this);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return settingsItems.size();
    }

    @Override
    public SettingsItem getItem(int position) {
        // TODO Auto-generated method stub
        return settingsItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View row = null;
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            row = inflater.inflate(R.layout.settings_item, parent, false);
        } else {
            row = convertView;
        }

        SettingsItem settingsItem = settingsItems.get(position);

        settingsItem.initComponents(row);
        // to prevent listview recycling issue
        settingsItem.setEnabled(settingsItem.isEnabled());
        settingsItem.setSliderValue(settingsItem.getSliderValue());

        //parent.setupSavedSettings();

        return row;
    }

/*    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        NavAreaItem clickedItem = getItem(i);
        Log.d(TAG, "onItemClick: clicked: " +  i + " l " + l);
        clickedItem.onClick(view, c);

    }*/
}