package com.roadstatusinfo.mo.rsiapp.customviews;

/**
 * Created by mo on 12/10/17.
 */

/*
public class NavAreaItemAdapter extends BaseAdapter implements ListView.OnItemClickListener {
    // ArrayList<String> name, company, email, id, status;
    ArrayList<NavAreaItem> navItemsList;
    Context c;
    private final String TAG = "NavAdapter";

    public NavAreaItemAdapter(Context c, ArrayList<NavAreaItem> list, ListView listView) {
        navItemsList = list;
        this.c = c;
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
        TextView name = (TextView) row.findViewById(R.id.nav_item_header);
        name.setText(navItem.getName());

        return row;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        NavAreaItem clickedItem = getItem(i);
        Log.d(TAG, "onItemClick: clicked: " +  i + " l " + l);
        clickedItem.onClick(view, c);

    }*/
//}