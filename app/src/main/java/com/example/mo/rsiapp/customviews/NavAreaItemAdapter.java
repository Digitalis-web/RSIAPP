package com.example.mo.rsiapp.customviews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mo.rsiapp.R;

import java.util.ArrayList;

/**
 * Created by mo on 12/10/17.
 */


public class NavAreaItemAdapter extends BaseAdapter {
    // ArrayList<String> name, company, email, id, status;
    ArrayList<NavAreaItem> navItemsList;
    Context c;
    private final String TAG = "NavAdapter";

    public NavAreaItemAdapter(Context c, ArrayList<NavAreaItem> list) {
        navItemsList = list;
        this.c = c;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return navItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return navItemsList.get(position);
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
            row = inflater.inflate(R.layout.nav_area_item, parent, false);
        } else {
            row = convertView;
        }

        NavAreaItem navItem = navItemsList.get(position);
        TextView name = (TextView) row.findViewById(R.id.nav_item_header);
        name.setText(navItem.getName());
        /*ClientDetails detail = clientArrayList.get(position);
        TextView email = (TextView) row.findViewById(R.id.tvClientEmail);
        email.setText(detail.email);
        TextView id = (TextView) row.findViewById(R.id.tvClientID);
        id.setText("ID : " + detail.id);
        TextView company = (TextView) row
                .findViewById(R.id.tvClientCompanyName);
        company.setText(detail.company);
        TextView status = (TextView) row.findViewById(R.id.tvClientStatus);
        status.setText("Status:" + detail.status);*/
        return row;
    }

}