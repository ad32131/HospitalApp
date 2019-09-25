package com.example.samppla;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 황정선 on 2015-11-09.
 */
public class GcmIntentService extends IntentService {
    public static final String TAG = "icelancer";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    int count=1;
    String setPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/data/";

    public GcmIntentService() {
//        Used to name the worker thread, important only for debugging.
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.

                sendNotification(extras.get("tag").toString(),extras.get("data").toString(),extras.get("From").toString() );
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        count++;
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String tag,String msg,String push) {
        setPath = setPath + this.getPackageName();

        if( tag.equals("all") ) {
            if (!(TextUtils.isEmpty(push))) {
                try {

                    FileOutputStream fos = new FileOutputStream(setPath + "/" + "push");
                    fos.write(push.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                push = "-1";
            }
        }
        if( tag.equals("one") ){
            if (!(TextUtils.isEmpty(push))) {
                try {

                    FileOutputStream fos = new FileOutputStream(setPath + "/" + "push_one");
                    fos.write(push.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                push = "-1";
            }


        }
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentTitle("병원관리앱 안내")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());



        File setting = new File(setPath +"/"+ "count.txt");

        if (setting.isFile()) { //카운트에 해당하는파일이있다면 읽어서 count에 정수형태로 저장한다.

            try {
                FileInputStream fis = new FileInputStream(setPath + "/" + "count.txt");
                try {
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    count = Integer.parseInt(new String(buffer));

                    fis.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            count++;

            try {

                FileOutputStream fos = new FileOutputStream(setPath + "/" + "count.txt");
                fos.write(String.valueOf(count).getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
            else{ //없다면 강제열어서 1을 넣어준다.

            try {

                FileOutputStream fos = new FileOutputStream(setPath + "/" + "count.txt");
                count = 1;
                fos.write(String.valueOf(count).getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }



            }






        Intent intent2 = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
// 패키지 네임과 클래스 네임 설정
        intent2.putExtra("badge_count_package_name", this.getPackageName());
        intent2.putExtra("badge_count_class_name",  "com.example.samppla.title");
        //해당패키지네임
// 업데이트 카운트
        intent2.putExtra("badge_count", count);
        sendBroadcast(intent2);
    }
}
