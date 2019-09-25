package com.example.samppla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 황정선 on 2015-10-29.
 */
public class MyReceiver extends BroadcastReceiver {

    public long millis;
    public long now;
    public String strNow;
    public Calendar cal;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)){
            //시간계산
            cal = Calendar.getInstance();


            cal.setTimeInMillis(millis);
            // 현재 시간을 msec으로 구한다.
            long now = System.currentTimeMillis();
            // 현재 시간을 저장 한다.
            Date date = new Date(now);
            // 시간 포맷으로 만든다.
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            strNow = sdfNow.format(date);
        }
    }
    public Calendar getCal(){

        return cal;
    }
    public String getNow(){
        return strNow;
    }
}
