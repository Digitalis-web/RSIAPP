package com.roadstatusinfo.mo.rsiapp.backgroundtasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.roadstatusinfo.mo.rsiapp.datamanaging.FetchingManager;

/**
 * Created by mo on 23/07/17.
 */
public class Alarm extends BroadcastReceiver
{
    private static final String TAG = "Alarm";
    public static Context currentAlarmContext = null;
    public static boolean notificationWithSoundSent;
    public static final int ALARM_INTERVAL = 1000*60*30; // 30 minutes
    //public static final int ALARM_INTERVAL = 1000*60; // 1 minutes

    @Override
    public void onReceive(Context context, Intent intent)
    {
        currentAlarmContext = context;
        notificationWithSoundSent = false;
        //Log.i(TAG, "kör on reciveve");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();


        //Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
        FetchingManager.fetchAndControlData(context);

        wl.release();
    }

    public static void setAlarm(Context context)
    {
        //boolean alarmRunning = (PendingIntent.getBroadcast(context, 0,
            //new Intent(context, Alarm.class),
            //PendingIntent.FLAG_NO_CREATE) != null);

        //if(!alarmRunning) {
        Log.d(TAG, "setAlarm: alarm not running, startging");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_INTERVAL, pi); // Millisec * Second * Minute
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_INTERVAL, pi); // Millisec * Second * Minute
/*        }
        else {
            Log.d(TAG, "alarm already running");
        }*/
        //Log.i("debugging", "setting repeating in set alarm");
    }

    public static void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}

