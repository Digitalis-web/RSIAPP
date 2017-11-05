package com.example.mo.rsiapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.mo.rsiapp.backgroundtasks.Alarm;
import com.example.mo.rsiapp.customviews.InstantAutoComplete;
import com.example.mo.rsiapp.customviews.NavAreaItem;
import com.example.mo.rsiapp.customviews.NavAreaItemAdapter;
import com.example.mo.rsiapp.datamanaging.FetchingManager;

import java.util.ArrayList;

import static com.example.mo.rsiapp.R.menu.nav;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ForecastFragment.OnFragmentInteractionListener, LoadingFragment.OnFragmentInteractionListener {

    public static InstantAutoComplete searchBar;
    private static String TAG = "NavActivity";
    public static NavActivity navActivity;
    private ListView navDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navActivity = this;

        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        ArrayList<NavAreaItem> navAreaItems = new ArrayList<>();
        /*navAreaItems.add(new NavAreaItem("test1"));
        navAreaItems.add(new NavAreaItem("test2"));
        navAreaItems.add(new NavAreaItem("test2"));
        navAreaItems.add(new NavAreaItem("test2"));
        navAreaItems.add(new NavAreaItem("test2"));
        navAreaItems.add(new NavAreaItem("test2"));
        navAreaItems.add(new NavAreaItem("test2"));*/

        navDrawerList = (ListView) findViewById(R.id.nav_view);

        navDrawerList.setAdapter(new NavAreaItemAdapter(this, navAreaItems));

        getSupportActionBar().setDisplayShowTitleEnabled(false); // hide title in action bar


        searchBar = (InstantAutoComplete) findViewById(R.id.search_area);
        FetchingManager.fetchAreas();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void manualFetchAreas(View v) {
        FetchingManager.fetchAreas();
    }

    public void manualFetchData(View v) {
        //FetchingManager.fetchForecast(0);
    }
    public void manualCancelAlarm(View v) {
        Alarm.cancelAlarm(this);
    }

    public void manualStartAlarm(View v){
        Alarm.setAlarm(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void openForecast(){
        ForecastFragment fragment = new ForecastFragment().newInstance("", "");
        FragmentManager manager = navActivity.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentLayout, fragment, fragment.getTag()).commit();
    }

    public static void openLoadingScreen(){
        LoadingFragment fragment = new LoadingFragment().newInstance();
        FragmentManager manager = navActivity.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentLayout, fragment, fragment.getTag()).commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_forecast) {
            openForecast();
        } else if (id == R.id.nav_settings) {

        }
       // else if (id == R.id.nav_slideshow) {
        //}
        /*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }
}
