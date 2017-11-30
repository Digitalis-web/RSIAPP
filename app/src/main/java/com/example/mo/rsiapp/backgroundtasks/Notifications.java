package com.example.mo.rsiapp.backgroundtasks;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.mo.rsiapp.NavActivity;
import com.example.mo.rsiapp.R;

/**
 * Created by mo on 19/11/17.
 */

public class Notifications {

    public static final String TAG = "Notifications";

    public static final String NOTIFICATIONS_CHANNEL = "RSI_NOTIFICATIONS";


    public static void sendNotification(Context context, String title, String contentText, int ID) {

        //String channelID = NotificationChannel.DEFAULT_CHANNEL_ID;

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setChannel(NOTIFICATIONS_CHANNEL);
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, NOTIFICATIONS_CHANNEL);
/*                .setSmallIcon(R.drawable.ic_rsi_snowflake_2)
                .setContentTitle(title)
                .setStyle(new Notification.BigTextStyle().bigText(contentText))
                .setAutoCancel(true).build();*/
        }
        else {
            builder = new Notification.Builder(context);
        }

        initNotification(builder, title, contentText, context);
        Notification notification = builder.build();

/*        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notifications)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                        .setContentText(contentText)
                        .setAutoCancel(true)
                ;*/

        //mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;


        // Gets an instance of the NotificationManager service//
        mNotificationManager.notify(ID, notification);
    }

    public static void initNotification(Notification.Builder builder, String title, String contentText, Context context){
        Intent resultIntent = new Intent(context, NavActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        builder.setSmallIcon(R.drawable.ic_rsi_snowflake_2);
        builder.setContentTitle(title);
        builder.setStyle(new Notification.BigTextStyle().bigText(contentText));
        builder.setContentText(contentText);
        builder.setTicker(title);
        builder.setAutoCancel(true).build();
    }

    public static void initNotificationsChannel(Context context){
        Log.d(TAG, "sendNotification: runnings");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // The user-visible name of the channel.
            CharSequence name = "RSI notifications";
            // The user-visible description of the channel.
            String description = "Notifications for RSI application";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(NOTIFICATIONS_CHANNEL, name, importance);
            mChannel.setDescription(description);
            //mChannel.enableLights(true);
/*            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});*/
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


}
