package com.example.mo.rsiapp.backgroundtasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mo on 23/07/17.
 */
public class AutoStart extends BroadcastReceiver
{
    Alarm alarm = new Alarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("debugging", "kör on reciveve autostart");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            alarm.setAlarm(context);
            Log.i("debugging", "kör on reciveve autostart2");
        }
    }
}
