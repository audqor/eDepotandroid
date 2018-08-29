package com.nocson.eDepot;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

public class EzSplash extends Activity {

    Intent nextActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ez_splash);

        nextActivity = EzCommonHandler.GetNextActivity(getApplicationContext());


        boolean isNotify = false;
        if(getIntent().getExtras()!= null) {
            if(getIntent().getExtras().getString("tickerText") != null) {
                isNotify = true;
            }

          //  Log.d("ddddd", getIntent().getExtras().getString("tickerText"));
        }

        if( nextActivity.getComponent().getClassName().equals( "com.nocson.eDepot.EzMain"))
        {

            nextActivity.putExtra("isNotify",isNotify);

            startActivity(nextActivity);
           finish();
        }
        else
        {
            Handler handler = new Handler()
            {
                @Override
                public void handleMessage(Message msg) {

                    startActivity(nextActivity);
                    finish();
                }
            };
            handler.sendEmptyMessageDelayed(0,1000);
        }

//        Intent intent = getIntent();
//        String action = intent.getAction();
//        String type = intent.getType();
//        if (Intent.ACTION_SEND.equals(action) && type != null) {
//            handleSendImage(intent); // Handle single image being sent
//        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
//            handleSendMultipleImages(intent); // Handle multiple images being sent
//        }

    }
//    void handleSendImage(Intent intent) {
//        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
//        uploadImage(imageUri);
//    }
//
//    void handleSendMultipleImages(Intent intent) {
//        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//        if (imageUris != null) {
//            for (Integer i = 0; i < imageUris.size(); i++) {
//                uploadImage(imageUris.get(i));
//            }
//        }
//    }
//    void uploadImage(Uri imageUri)
//    {
//        final Uri iUri = imageUri;
//        Cursor cursor = getContentResolver().query(iUri,null, null, null, null);
//        cursor.moveToFirst();
//        long size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
//        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//        String name = cursor.getString(nameIndex);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        String fPath =  cursor.getString(column_index);
//        cursor.close();
//        final   EzUploader  uploadInfo = new EzUploader();
//        uploadInfo.uri = imageUri;
//        uploadInfo.FileName = name;
//        uploadInfo.Size = size;
//        uploadInfo.SourcePath = fPath;
//        new Thread() {
//            public void run() {
//                uploadInfo.doInBackground(null);
//            }
//        }.start();
//    }

}
