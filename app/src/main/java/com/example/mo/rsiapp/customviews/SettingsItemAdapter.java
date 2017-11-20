package com.example.mo.rsiapp.customviews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.mo.rsiapp.R;
import com.example.mo.rsiapp.datamanaging.StorageManager;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by mo on 12/10/17.
 */


public class SettingsItemAdapter extends BaseAdapter  {
    // ArrayList<String> name, company, email, id, status;
    ArrayList<SettingsItem> settingsItems;
    Context c;
    private final String TAG = "NavAdapter";

    Set<String> savedSettings;

    public SettingsItemAdapter(Context c, ArrayList<SettingsItem> list, ListView listView) {
        settingsItems = list;
        savedSettings = StorageManager.getSettings();
        Log.d(TAG, "SettingsItemAdapter: SAVED: " + savedSettings);

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
        settingsItem.setSliderValue(settingsItem.getSliderValue());
        settingsItem.setEnabled(settingsItem.isEnabled());
        settingsItem.initFromSavedSettings(savedSettings);

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