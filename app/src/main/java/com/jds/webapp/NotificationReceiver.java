package com.jds.webapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.jds.webapp.Fragment.FragmentArticle;
import com.jds.webapp.Fragment.FragmentMain;

/**
 * Created by JDS on 02/04/2015.
 */
public class NotificationReceiver extends BroadcastReceiver {
    int YOUR_PI_REQ_CODE=1, YOUR_NOTIF_ID=1;
    ArticlePersistence persistence;



    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, we'll just display a message
        Toast.makeText(context, "Matome Running", Toast.LENGTH_SHORT).show();
        Log.v("Notification", "Update Notif");
        String title =intent.getExtras().getString("title");
        Intent notificationIntent = new Intent(context, FragmentMain.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                YOUR_PI_REQ_CODE, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = context.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.news))
                .setTicker("Notifikasi Matome")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Notifikasi Matome")
                .setContentText(title);

        Notification n = builder.build();

        nm.notify(YOUR_NOTIF_ID, n);
    }
}
