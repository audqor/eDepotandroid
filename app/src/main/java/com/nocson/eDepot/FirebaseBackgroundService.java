package com.nocson.eDepot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class FirebaseBackgroundService extends WakefulBroadcastReceiver {
    String Title,Content;
    private static final String TAG = "FirebaseService";


    @Override
    public void onReceive(Context context, Intent intent1) {
//        Log.d(TAG, "I'm in!!!");
//
//        if (intent1.getExtras() != null) {
//            for (String key : intent1.getExtras().keySet()) {
//                Object value = intent1.getExtras().get(key);
//                Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
//                if(key.equalsIgnoreCase("gcm.notification.title") && value != null) {
//                    Title = value.toString();
//                }
//                if(key.equalsIgnoreCase("gcm.notification.body") && value != null) {
//                    Content = value.toString();
//                }
//
//            }
//            Intent intent = null;
//        intent = new Intent(context, EzMain.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("notificationId", 789);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        int smallIcon = R.mipmap.ic_launcher;
//            NotificationCompat.Builder notification =  new NotificationCompat.Builder(context)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(Title)
//                .setContentText(Content)
//                .setAutoCancel(true)
////                .setNumber(10)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setVibrate(new long[0])
//                .setCategory(Notification.CATEGORY_MESSAGE)
//                .setPriority(Notification.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent)
//                .setWhen(System.currentTimeMillis())
//                .setVisibility(Notification.VISIBILITY_PUBLIC);
//            NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//            mNotificationManager.notify(789 /* ID of notification */, notification.build());
//        }


    }
}
