package com.roadstatusinfo.mo.rsiapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.roadstatusinfo.mo.rsiapp.backgroundtasks.Alarm;
import com.roadstatusinfo.mo.rsiapp.backgroundtasks.Notifications;
import com.roadstatusinfo.mo.rsiapp.customviews.InstantAutoComplete;
import com.roadstatusinfo.mo.rsiapp.customviews.NavAreaItem;
import com.roadstatusinfo.mo.rsiapp.datamanaging.DisplayInfoManager;
import com.roadstatusinfo.mo.rsiapp.datamanaging.FetchingManager;
import com.roadstatusinfo.mo.rsiapp.datamanaging.Forecast;
import com.roadstatusinfo.mo.rsiapp.datamanaging.JSONFetcher;
import com.roadstatusinfo.mo.rsiapp.datamanaging.KeyVerifier;
import com.roadstatusinfo.mo.rsiapp.datamanaging.StorageManager;

import java.util.ArrayList;
import java.util.Set;

import static com.roadstatusinfo.mo.rsiapp.R.menu.nav;
import static com.roadstatusinfo.mo.rsiapp.datamanaging.FetchingManager.latestForecastTime;

public class NavActivity extends AppCompatActivity
        implements DrawerLayout.DrawerListener, LoginFragment.OnFragmentInteractionListener, ForecastFragment.OnFragmentInteractionListener, LoadingFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, StartPageFragment.OnFragmentInteractionListener {

    public static InstantAutoComplete searchBar;
    private static final String TAG = "NavActivity";
    public static NavActivity navActivity;
    private LinearLayout navDrawerList;
    private DrawerLayout navDrawer;

    public static boolean favoriteForecastOpened = false;
    public static boolean openedFromNotification = false;
    ArrayList<NavAreaItem> navAreaItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navActivity = this;

        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, navDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navDrawer.addDrawerListener(toggle);
        toggle.syncState();

        //StorageManager.clearRSIKey();

        navDrawer.addDrawerListener(this);

        //Log.d(TAG, "onCreate: watched areas: " + StorageManager.getWatchedAreas().toString());

        getSupportActionBar().setDisplayShowTitleEnabled(false); // hide title in action bar


        searchBar = (InstantAutoComplete) findViewById(R.id.search_area);

        DisplayInfoManager.initData();
        FetchingManager.setCurrentIp(this);

        //StorageManager.clearWatchedAreas();
        //StorageManager.clearRSIKey();

        Notifications.initNotificationsChannel(this);

        //Log.d(TAG, "onCreate: " + StorageManager.getLastControlledForecastTime(this));
        //StorageManager.setLastControlledForecastTime(0, this);
        //StorageManager.clearSettings();
        SettingsFragment.initDefaultSettingsIfNonExists(); // inits default settings if there are no settings

        Intent intent = getIntent();
        // if the application is opened by clicking on a notification
        if (intent.hasExtra("area_id") && intent.hasExtra("latest_forecast_time")) {
            String areaID = intent.getStringExtra("area_id");
            long forecastTime = intent.getLongExtra("latest_forecast_time", latestForecastTime);
            FetchingManager.latestForecastTime = forecastTime;
            FetchingManager.fetchForecast(areaID, forecastTime, JSONFetcher.FETCH_FORECAST);
            openLoadingScreen();
            openedFromNotification = true;
            //favoriteForecastOpened = true; // stops favorite forecast from being opened
        } else {
            Alarm.setAlarm(this); // starts the background task
        }

        if(!StorageManager.getRSIKey().isEmpty()){
            FetchingManager.verifyKey(StorageManager.getRSIKey(), KeyVerifier.ON_START);
        }
        else {
            openLogin();
        }


    }

    public void showSearchBar() {
        View nav = findViewById(R.id.search_edit_frame);
        nav.setVisibility(View.VISIBLE);
    }

    public void hideSearchBar() {
        View nav = findViewById(R.id.search_edit_frame);
        nav.setVisibility(View.INVISIBLE);
    }

    public void openInitial() {

        if (StorageManager.getFavoriteArea().isEmpty()) {
            // open startpage if there is no favorite area
            openStartPage();
        } else {
            if (FetchingManager.latestForecastTime != 0) { // if this is 0, it means the latestforecast time still hasn't been fetched from the server
                if(!openedFromNotification) {
                    favoriteForecastOpened = false;
                    viewFavoriteForecast();
                }
            }
        }
    }

    public void viewFavoriteForecast() {
        if (StorageManager.keyIsVerified()) {
            if (!NavActivity.favoriteForecastOpened) { // if a forecast hasn't been automatically opened yet
                favoriteForecastOpened = true;
                String favoriteArea = StorageManager.getFavoriteArea();
                if (!favoriteArea.isEmpty()) {
                    FetchingManager.fetchForecast(favoriteArea, FetchingManager.latestForecastTime, JSONFetcher.FETCH_FORECAST);
                }

            }
        }

    }

    public void testNofitication(View view) {
        //Notifications.sendNotification(this);
    }

    //@Override
    //public void onSaveInstanceState(Bundle outState){
        //outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        //super.onSaveInstanceState(outState);
    //}

    public void closeNav() {
        navDrawer.closeDrawers();
    }


    public void updateNavItemsFavorite(String newFavorite) {
        for (NavAreaItem navItem : navAreaItems) {
            boolean isNewFavorite = navItem.getAreaID().equals(newFavorite);
            navItem.setIsFavorite(isNewFavorite);
        }

    }

    public void updateNavItems() { // updates the list of watched areas
        Set<String> watchedAreas = StorageManager.getWatchedAreas();

        navAreaItems.clear();
        for (String areaID : watchedAreas) {
            String areaName = FetchingManager.getAreaNameFromID(areaID);
            navAreaItems.add(new NavAreaItem(areaName, areaID));
        }

        navDrawerList = (LinearLayout) findViewById(R.id.nav_view);
        navDrawerList.removeAllViews();
        //navDrawerList.setAdapter(new NavAreaItemAdapter(this, navAreaItems, navDrawerList));
        LayoutInflater inflater = LayoutInflater.from(this);

        for (NavAreaItem navItem : navAreaItems) {
            View row = inflater.inflate(R.layout.nav_area_item, navDrawerList, false);

            navItem.initComponents(row);
            navDrawerList.addView(row);
        }

        String favoriteArea = StorageManager.getFavoriteArea();
        updateNavItemsFavorite(favoriteArea);

    }

    public void displayConnectError() {
        displayError("Anslutningsfel", "Kunde inte hämta data från servern, vänligen försök igen");
    }

    public void displayError(String errorTitle, String errorMessage) {

        AlertDialog builder;
        builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom).create();

        builder.setTitle(errorTitle);
        builder.setMessage(errorMessage);
        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.setIcon(android.R.drawable.ic_menu_info_details);
        builder.show();
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

    public void openStartPage() {
        StartPageFragment fragment = new StartPageFragment().newInstance();
        FragmentManager manager = navActivity.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_layout, fragment, fragment.getTag()).commit();
    }

    public void openSettings(View v) {
        SettingsFragment fragment = new SettingsFragment().newInstance();
        FragmentManager manager = navActivity.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_layout, fragment, fragment.getTag()).commit();
        closeNav();
    }

    public void manualFetchAreas(View v) {
        //FetchingManager.fetchAreas(true);
    }

    public void manualFetchData(View v) {
        //FetchingManager.fetchForecast(0);
    }

    public void manualCancelAlarm(View v) {
        //Alarm.cancelAlarm(this);
    }

    public void manualStartAlarm(View v) {
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

    public static void openForecast(String areaID, Forecast forecast) {
        // in case app has closed by user while fetching data in other thread
        try {
            ForecastFragment.viewedForecast = forecast; // passed statically is ok here cause there will only ever be one instance of ForecastFragment at the time
            ForecastFragment fragment = new ForecastFragment().newInstance(areaID);
            FragmentManager manager = navActivity.getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_layout, fragment, fragment.getTag()).commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static void openLogin() {
        LoginFragment fragment = new LoginFragment().newInstance();
        FragmentManager manager = navActivity.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_layout, fragment, fragment.getTag()).commit();
    }


    public static void openLoadingScreen() {
        LoadingFragment fragment = new LoadingFragment().newInstance();
        FragmentManager manager = navActivity.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_layout, fragment, fragment.getTag()).commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (slideOffset != 0) {
            searchBar.removeFocusAndKeyboard();
        }

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    public void launchRSI(View view) {
        String URL = "http://www.roadstatus.info/app";

        if(StorageManager.getCountry(this).equals("norway")){
            URL = "http://www.roadstatus.info/norway";
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(browserIntent);
    }


}
