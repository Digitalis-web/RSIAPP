package com.example.mo.rsiapp.datamanaging;

import android.os.AsyncTask;
import android.util.Log;

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
    public boolean forecastFetch;

    public JSONFetcher(boolean forecastFetch){
        Log.d(TAG, "JSONFetcher: creating object");
        this.forecastFetch = forecastFetch;

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
            System.out.println("JSON: " + jsonString.substring(jsonString.length()-59));
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
        Log.d(TAG, "onPPostExecute: " + result.toString().substring(result.toString().length()-59));
        if(forecastFetch){
            FetchingManager.parseForecastData(result);
        }
        else {
            FetchingManager.parseAreasData(result);
        }

    }
}
