package com.roadstatusinfo.mo.rsiapp.datamanaging;

import android.os.AsyncTask;
import android.util.Log;

import com.roadstatusinfo.mo.rsiapp.ForecastFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by mo on 05/09/17.
 */

public class JSONFetcher extends AsyncTask<String, Void, JSONObject> {
    public String TAG = "JSONFETCHING";
    //public boolean forecastFetch;
    private int fetchMode = 0;


    public static final short FETCH_FORECAST = 0;
    public static final short FETCH_AREAS = 1;
    public static final short FETCH_AREAS_IN_BACKGROUND = 2;
    public static final short FETCH_FORECAST_IN_BACKGROUND = 3;
    public static final short FETCH_AREAS_AND_REOPEN_FORECAST = 4;


    public JSONFetcher(int fetchMode){
        //Log.d(TAG, "JSONFetcher: creating object " + updateUI);
        this.fetchMode = fetchMode;


        //this.forecastFetch = forecastFetch;


        //this.urlStr = url;
    }



    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: ");
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String urlStr = params[0];

        Log.d(TAG, "doinbackground");
        HttpURLConnection urlConnection = null;
        String jsonString = "";
        JSONObject JSONObj = null;
        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
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

            jsonString = sb.toString();
            JSONObj = new JSONObject(jsonString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "doinbackground2");
        return JSONObj;
    }

    @Override
    protected void onPostExecute(JSONObject result){
        //Log.d(TAG, "onPPostExecute: " + result.toString().substring(result.toString().length()-59));
        Log.d(TAG, "onPPostExecute: " + fetchMode);
        if (fetchMode == FETCH_FORECAST) {
            //FetchingManager.parseForecastData(result, true);
            Forecast forecast = new Forecast();
            forecast.parseData(result, true);
        }
        else if (fetchMode == FETCH_AREAS_AND_REOPEN_FORECAST) {
            FetchingManager.parseAreasData(result, true, ForecastFragment.viewedForecast.areaID);
        }
        else if (fetchMode == FETCH_AREAS) {
            FetchingManager.parseAreasData(result, true, "");
        }
        else if (fetchMode == FETCH_AREAS_IN_BACKGROUND) {
            FetchingManager.parseAreasData(result, false, "");
        }
        else if (fetchMode == FETCH_FORECAST_IN_BACKGROUND) {
            //FetchingManager.parseForecastData(result, false);
            Forecast forecast = new Forecast();
            forecast.parseData(result, false);
        }

    }
}
