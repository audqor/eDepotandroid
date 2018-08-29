package com.nocson.eDepot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class EzAlertMessagingService extends com.google.firebase.messaging.FirebaseMessagingService  {

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
    //
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        cnt+=1;
        EzCommonHandler.setBadge(this,cnt);

        String title  ;
        String content ;
        boolean sound = true;
        boolean vibrate= true;


        if( remoteMessage.getNotification() == null   ){
            title = remoteMessage.getData().get("title");
            content = remoteMessage.getData().get("message");
            sound =Boolean.parseBoolean(remoteMessage.getData().get("sound"));
            vibrate = Boolean.parseBoolean(remoteMessage.getData().get("vibrate"));
        }
        else{
            title = remoteMessage.getNotification().getTitle();
            content = remoteMessage.getNotification().getBody();

        }
        //추가한것
        sendNotification(title,content,sound,vibrate);
    }



    static int cnt =0;




    public void sendNotification(String title,String content,boolean sound, boolean vibrate) {
        Intent intent = null;


        intent = new Intent(this, EzMain.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("notificationId", 1);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
//                .setDefaults(Notification.DEFAULT_ALL )
//                .setVibrate(new long[0])
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setVisibility(Notification.VISIBILITY_PUBLIC);
        int defaults = Notification.DEFAULT_LIGHTS;
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (sound) {
            defaults |= Notification.DEFAULT_SOUND;
        }



        notificationBuilder.setDefaults(defaults);
         NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification nt = notificationBuilder.build();
        nt.flags = Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(1 /* ID of notification */, nt );





    }
}
