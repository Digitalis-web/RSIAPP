package com.example.mo.rsiapp.backgroundtasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.mo.rsiapp.datamanaging.FetchingManager;

/**
 * Created by mo on 23/07/17.
 */
public class Alarm extends BroadcastReceiver
{
    private final String TAG = "Alarm";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("debugging", "kör on reciveve");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        Log.d(TAG, "onReceive: makeing toast");
        // Put here YOUR code.
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
        //FetchingManager.fetchAndControlData();

        wl.release();
    }

    public static void setAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 20, pi); // Millisec * Second * Minute
        Log.i("debugging", "setting repeating in set alarm");
    }

    public static void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}

