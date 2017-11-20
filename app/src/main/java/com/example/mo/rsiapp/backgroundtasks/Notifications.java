package com.example.mo.rsiapp.backgroundtasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.mo.rsiapp.NavActivity;
import com.example.mo.rsiapp.R;

/**
 * Created by mo on 19/11/17.
 */

public class Notifications {
    public static void sendNotification(Context context, String title, String contentText) {

        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_menu_camera)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                        .setContentText(contentText)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(context, NavActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;


        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //When you issue multiple notifications about the same type of event, it’s best practice for your app to try to update an existing notification with this new information, rather than immediately creating a new notification. If you want to update this notification at a later date, you need to assign it an ID. You can then use this ID whenever you issue a subsequent notification. If the previous notification is still visible, the system will update this existing notification, rather than create a new one. In this example, the notification’s ID is 001//

        //NotificationManager.notify().


        mNotificationManager.notify(001, mBuilder.build());
    }

}
