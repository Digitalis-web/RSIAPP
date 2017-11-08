package com.example.mo.rsiapp.customviews;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mo.rsiapp.R;

import java.util.ArrayList;

import static android.R.attr.name;

/**
 * Created by mo on 12/10/17.
 */


public class NavAreaItemAdapter extends BaseAdapter implements ListView.OnItemClickListener {
    // ArrayList<String> name, company, email, id, status;
    ArrayList<NavAreaItem> navItemsList;
    Context c;
    private final String TAG = "NavAdapter";

    public NavAreaItemAdapter(Context c, ArrayList<NavAreaItem> list, ListView listView) {
        navItemsList = list;
        this.c = c;
        list.add(new NavAreaItem("", true));
        listView.setOnItemClickListener(this);


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return navItemsList.size();
    }

    @Override
    public NavAreaItem getItem(int position) {
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

        int bgColor = 0;
        int visibility = 0;

        if(navItem.isAddButton()){
            bgColor = ResourcesCompat.getColor(c.getResources(), R.color.colorGrey, null);

            Log.d(TAG, "getView: row: " + row);
            Log.d(TAG, "getView: runnig is button");
            visibility = View.VISIBLE;

        //    row.setBackgroundColor(color);
        }
        else {
            TextView name = (TextView) row.findViewById(R.id.nav_item_header);
            name.setText(navItem.getName());
            bgColor = ResourcesCompat.getColor(c.getResources(), R.color.colorLightBlue, null);
            visibility = View.INVISIBLE;
        }

        ImageView addIcon = (ImageView) row.findViewById(R.id.nav_add_icon);
        addIcon.setVisibility(visibility);

        LinearLayout innerContainer = (LinearLayout) row.findViewById(R.id.nav_item_inner_container);
        innerContainer.setBackgroundColor(bgColor);



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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        NavAreaItem clickedItem = getItem(i);
        Log.d(TAG, "onItemClick: clicked: " +  i + " l " + l);
        clickedItem.onClick(view, c);

    }
}