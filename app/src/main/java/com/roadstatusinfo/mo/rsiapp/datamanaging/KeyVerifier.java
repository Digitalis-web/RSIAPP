package com.roadstatusinfo.mo.rsiapp.datamanaging;

import android.os.AsyncTask;
import android.util.Log;

import com.roadstatusinfo.mo.rsiapp.NavActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by mo on 05/09/17.
 */

public class KeyVerifier extends AsyncTask<String, Void, String> {
    public String TAG = "KeyVerifier";
    //public boolean forecastFetch;
    int fetchMode = 0;

    public static final short ON_LOGIN = 0;
    public static final short ON_START = 1;

    String key = "";



    public KeyVerifier(int fetchMode){
        this.fetchMode = fetchMode;
    }

    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: ");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        key = params[0];
        String urlStr = "http://" + FetchingManager.VERIFY_KEY_IP + "/verifykey.php";
        //String urlStr = "http://roadstatus.info/key/verify_SDGJHweqr4w5jdfJ314jaSDJaskSJDGK345Asdsadjlha_3874SDFhjzxz.php";
        //http://roadstatus.info/key/verify_SDGJHweqr4w5jdfJ314jaSDJaskSJDGK345Asdsadjlha_3874SDFhjzxz.php
        urlStr += "?rsi_key=" + key;

        URL url = null;
        String response = "";

        try {
            url = new URL(urlStr);
            HttpURLConnection urlConnection = null;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            //urlConnection.setRequestProperty("rsi_key", key);
            Log.d(TAG, "doInBackground: key: " + key);
            urlConnection.setReadTimeout(10000 /* milliseconds */ );
            urlConnection.setConnectTimeout(15000 /* milliseconds */ );
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();
            response = sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "doInBackground: response: " + response);
        return response;
    }

    @Override
    protected void onPostExecute(String result){
        boolean swedenVersion = result.contains("sweden");
        boolean norwayVersion = result.contains("norway");

        boolean keyVerified = swedenVersion || norwayVersion;

        if(keyVerified){
            StorageManager.saveNotificationsEnabled(true);
            String oldCountry = StorageManager.getCountry(NavActivity.navActivity);
            if(norwayVersion) {
                StorageManager.setCountry("norway");
            }
            else {
                StorageManager.setCountry("sweden");
            }

            if(!oldCountry.equals(StorageManager.getCountry(NavActivity.navActivity))){ // if country changed
                StorageManager.clearWatchedAreas();
                StorageManager.setAnyFavoriteArea();
                FetchingManager.setCurrentIp(NavActivity.navActivity);
            }
        }
        else {
            StorageManager.saveNotificationsEnabled(false);
        }

        if(fetchMode == ON_LOGIN) {
            if(keyVerified){
                FetchingManager.fetchAreas(JSONFetcher.FETCH_AREAS);
                NavActivity.navActivity.openInitial();
                NavActivity.navActivity.showSearchBar();
            }
            else {
                NavActivity.navActivity.displayError("Kunde inte verifiera nyckeln", "Nyckeln verkar inte stämma, vänliga kontrollera nyckeln och försök igen");
            }

        }
        else if(fetchMode == ON_START){
            if(keyVerified) {
                FetchingManager.fetchAreas(JSONFetcher.FETCH_AREAS);
                NavActivity.navActivity.openInitial();
            }
            else {
                NavActivity.navActivity.openLogin();
            }
        }



        Log.d(TAG, "onPostExecute: KEY VERIFED " + result);
        Log.d(TAG, "onPostExecute: KEY VERIFED " + keyVerified);

    }
}

