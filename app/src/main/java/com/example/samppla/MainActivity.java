package com.example.samppla;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends Activity implements View.OnTouchListener {


    private int X_MOVE_GAP = 60; // 좌우로 움직임 간격
    private int IMAGE_COUNT = 3; // 전체 이미지 수

    private int[] mImageIds = new int[IMAGE_COUNT];
    private int[] mImageIds2 = new int[IMAGE_COUNT];
    private int mCurrentIndex = 0;

    String setPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/data/";
    String WebPath1 = "http://ad32131.net/xe/page_uZaS42";
    String WebPath2 = "http://ad32131.net/xe/page_vpRY09";
    String WebPath3 = "http://ad32131.net/xe/page_GKrb15";


    up_key upk;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    Handler handler = new Handler();
    ImageView rbtn, cbtn, lbtn, hbtn, cfbtn, pic_btn, ack_btn;
    ImageView s1, s2, s3;

    private String PhoneNumber;
    private long millis;
    private long now;
    String regId;
    ArrayList<String> idList = new ArrayList<String>();

    // 푸시메세지용 api코드
    public static final String PROPERTY_REG_ID = "registration_id";

    // SharedPreferences에 저장할 때 key 값으로 사용됨.
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "ad32131";
    String senderid; //가져올키값


    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;

    String regid;
    private TextView mDisplay;
//푸시메세지용 api코드 끝


    Intent i;


    GestureDetector mGestureDetector;

    int swi = 0;
    private WebView mWebView1;
    WebView mWebView2;
    WebView mWebView3;
    ViewFlipper flipper;
    float xAtDown;
    float xAtUp;
    boolean page_ch = false;
    long lastModified;
    Date filedate;
    Calendar cal = Calendar.getInstance() ;
    long todayMil = cal.getTimeInMillis() ;     // 현재 시간(밀리 세컨드)
    long oneDayMil = 24*60*60*1000 ;
    long diffMil;
    int diffDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //vf = (ViewFlipper) findViewById(R.id.viewFlipper);
        //vf.setOnTouchListener(this);

        Calendar fileCal = Calendar.getInstance() ;
        TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PhoneNumber = systemService.getLine1Number();

        if (PhoneNumber != null && PhoneNumber.equals("")) {
            PhoneNumber = PhoneNumber.substring(PhoneNumber.length() - 10, PhoneNumber.length());
            PhoneNumber = "0" + PhoneNumber;
        }
        PhoneNumber = "0" + PhoneNumber.substring(3, PhoneNumber.length());


        if (PhoneNumber.length() == 10) {
            PhoneNumber = "0" + PhoneNumber;

        } else if (PhoneNumber.length() == 9) {
            PhoneNumber = "01" + PhoneNumber;

        } else if (PhoneNumber.length() == 8) {
            PhoneNumber = "010" + PhoneNumber;

        }

        setPath = setPath + this.getPackageName();

        File d = new File(setPath);
        // 디렉토리 존재 여부 판단
        if (d.isDirectory()) {

            //Toast.makeText(this,"OK 디렉토리가 있습니다.", Toast.LENGTH_SHORT).show();
        } else {

            //Toast.makeText(this, setPath, Toast.LENGTH_SHORT).show();
            d.mkdir();
        }


        File setting = new File(setPath + "/" + "rec_key");

        lastModified = setting.lastModified();
        filedate = new Date(lastModified);
        fileCal.setTime(filedate);
        diffMil = todayMil - fileCal.getTimeInMillis(); //시간계산
        int diffDay = (int)(diffMil/oneDayMil); //날짜계산
        if(diffDay > 4 ){
            setting.delete() ;
        }
        if (setting.isFile()) {

            try {
                FileInputStream fis = new FileInputStream(setPath + "/" + "rec_key");
                try {
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    senderid = new String(buffer);

                    fis.close();
                    upk = new up_key();
                    upk.execute(AddrInfo.addr + AddrInfo.cont1_path + AddrInfo.up_key + "?number=" + PhoneNumber + "&key=" + senderid.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        } else {

            registerDevice();


        }


        rbtn = (ImageView) findViewById(R.id.rbtn);
        cbtn = (ImageView) findViewById(R.id.cbtn);
        lbtn = (ImageView) findViewById(R.id.lbtn);
        hbtn = (ImageView) findViewById(R.id.hbtn);
        cfbtn = (ImageView) findViewById(R.id.cfbtn);
        pic_btn = (ImageView) findViewById(R.id.pic_btn);
        ack_btn = (ImageView) findViewById(R.id.ack_btn);
        s1 = (ImageView) findViewById(R.id.s1);
        s2 = (ImageView) findViewById(R.id.s2);
        s3 = (ImageView) findViewById(R.id.s3);



        s1.setBackgroundResource(R.drawable.s_on);
        s2.setBackgroundResource(R.drawable.s_off);
        s3.setBackgroundResource(R.drawable.s_off);

        s1.bringToFront();
        s2.bringToFront();
        s3.bringToFront();



        //this.get


        //ViewPager에 설정할 Adapter 객체 생성

        //ListView에서 사용하는 Adapter와 같은 역할.

        //다만. ViewPager로 스크롤 될 수 있도록 되어 있다는 것이 다름

        //PagerAdapter를 상속받은 CustomAdapter 객체 생성

        //CustomAdapter에게 LayoutInflater 객체 전달

        setLayout();

        mWebView1.getSettings().setJavaScriptEnabled(true);
        mWebView1.loadUrl(WebPath1);
        mWebView1.setWebViewClient(new WebViewClientClass());

        mWebView2.getSettings().setJavaScriptEnabled(true);
        mWebView2.loadUrl(WebPath2);
        mWebView2.setWebViewClient(new WebViewClientClass());


        mWebView3.getSettings().setJavaScriptEnabled(true);
        mWebView3.loadUrl(WebPath3);
        mWebView3.setWebViewClient(new WebViewClientClass());

        flipper=(ViewFlipper)findViewById(R.id.viewFlipper1);
        flipper.setOnTouchListener((View.OnTouchListener) this);
        mWebView1.setOnTouchListener((View.OnTouchListener) this);
        mWebView2.setOnTouchListener((View.OnTouchListener) this);
        mWebView3.setOnTouchListener((View.OnTouchListener) this);


    }
    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void setLayout(){
        mWebView1= (WebView) findViewById(R.id.sqkweb1);
        mWebView2= (WebView) findViewById(R.id.sqkweb2);
        mWebView3= (WebView) findViewById(R.id.sqkweb3);
    }


    public boolean onTouch(View v, MotionEvent event) {
        if(v !=mWebView1 &&v !=mWebView2 && v !=mWebView3 && v!=flipper) return false;
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            xAtDown=event.getX();
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            xAtUp = event.getX();
            if(xAtDown > xAtUp){
                //flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));
                //flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
                swi++;
                if(swi < 3) {
                    flipper.showNext();
                    page_ch = true;
                }
                else{
                    Toast.makeText(this, "마지막 페이지 입니다."+swi, Toast.LENGTH_SHORT).show();
                    swi--;
                }
            }
            else if(xAtDown < xAtUp){
                //flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
                //flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));

                swi--;

                if(swi >- 1) {
                    flipper.showPrevious();
                    page_ch = true;
                }
                else{
                    Toast.makeText(this, "첫번째 페이지 입니다."+ swi, Toast.LENGTH_SHORT).show();
                    swi++;
                }
            }


        }
        if(page_ch == true) {
            switch (swi) {
                case 0:
                    s1.setBackgroundResource(R.drawable.s_on);
                    s2.setBackgroundResource(R.drawable.s_off);
                    s3.setBackgroundResource(R.drawable.s_off);
                    page_ch = false;
                    break;
                case 1:
                    s1.setBackgroundResource(R.drawable.s_off);
                    s2.setBackgroundResource(R.drawable.s_on);
                    s3.setBackgroundResource(R.drawable.s_off);
                    page_ch = false;
                    break;
                case 2:
                    s1.setBackgroundResource(R.drawable.s_off);
                    s2.setBackgroundResource(R.drawable.s_off);
                    s3.setBackgroundResource(R.drawable.s_on);
                    page_ch = false;
                    break;

            }
        }
        return true;
    }

    int length = 0;



/*
    @Override
    public boolean onTouch(View v, MotionEvent event) {

    if(event.getAction() ==MotionEvent.ACTION_MOVE ){
        length = event.getHistorySize();

        float x0;
        float x1;

        if (length > 0)
        {

            x0 = event.getHistoricalX(0 );

            x1 = event.getHistoricalX(length-1 );

            Log.i("length", String.valueOf(length));
            Log.i("x0", String.valueOf(x0));
            Log.i("x1", String.valueOf(x1));

            if ((int)(x1 - x0) > (X_MOVE_GAP)) {
                vf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
                vf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                vf.showPrevious();
                switch (swi){
                    case 0:
                        s1.setBackgroundResource(R.drawable.s_off);
                        s2.setBackgroundResource(R.drawable.s_off);
                        s3.setBackgroundResource(R.drawable.s_on);
                        swi=2;
                        break;
                    case 1:
                        s1.setBackgroundResource(R.drawable.s_on);
                        s2.setBackgroundResource(R.drawable.s_off);
                        s3.setBackgroundResource(R.drawable.s_off);
                        swi = 0;
                        break;
                    case 2:
                        s1.setBackgroundResource(R.drawable.s_off);
                        s2.setBackgroundResource(R.drawable.s_on);
                        s3.setBackgroundResource(R.drawable.s_off);
                        swi = 1;
                        break;

                }

            }
            else if ((int)(x1 - x0) < -(X_MOVE_GAP) ) {
                vf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                vf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                vf.showNext();
                switch (swi){
                    case 0:
                        s1.setBackgroundResource(R.drawable.s_off);
                        s2.setBackgroundResource(R.drawable.s_on);
                        s3.setBackgroundResource(R.drawable.s_off);
                        swi=1;
                        break;
                    case 1:
                        s1.setBackgroundResource(R.drawable.s_off);
                        s2.setBackgroundResource(R.drawable.s_off);
                        s3.setBackgroundResource(R.drawable.s_on);
                        swi=2;
                        break;
                    case 2:
                        s1.setBackgroundResource(R.drawable.s_on);
                        s2.setBackgroundResource(R.drawable.s_off);
                        s3.setBackgroundResource(R.drawable.s_off);
                        swi = 0;
                        break;

                }
            }
            SystemClock.sleep(300);
        }





    }

        return true;
    }

*/


    private class up_key extends AsyncTask<String, Integer,String> {


        @Override
        protected String doInBackground(String... urls) {


            try {
                Log.e( urls[0] ,"eeeeeeeeeee");

                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);

                    conn.setUseCaches(false);

                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        for(;;) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();

                            if (line == null) break;

                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                        }

                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                Log.e("eeeeeeeeeeeeeee","eeeeeeeeeee");
                ex.printStackTrace();


            }

            return "ok";


        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e("ICELANCER", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }



    public void onClick(View view){
        int position;

        switch(view.getId()){


            case R.id.lbtn:

                i = new Intent(MainActivity.this,  inquire.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);



                break;
            case R.id.cbtn:

                i = new Intent(MainActivity.this, center2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


                break;
            case R.id.rbtn:
                i = new Intent(MainActivity.this, right2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


                break;
            case R.id.hbtn:
                /*
                i = new Intent( MainActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                */

                break;
            case R.id.cfbtn:

                i = new Intent( MainActivity.this, Sa_Title_Activity1.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;

            case R.id.s1:
                s1.setBackgroundResource(R.drawable.s_on);
                s2.setBackgroundResource(R.drawable.s_off);
                s3.setBackgroundResource(R.drawable.s_off);
                // vf.setDisplayedChild(0);
                flipper.setDisplayedChild(0);
                swi =0;

                break;
            case R.id.s2:
                s1.setBackgroundResource(R.drawable.s_off);
                s2.setBackgroundResource(R.drawable.s_on);
                s3.setBackgroundResource(R.drawable.s_off);
                // vf.setDisplayedChild(1);
                flipper.setDisplayedChild(1);
                swi =1;
                break;
            case R.id.s3:
                s1.setBackgroundResource(R.drawable.s_off);
                s2.setBackgroundResource(R.drawable.s_off);
                s3.setBackgroundResource(R.drawable.s_on);
                // vf.setDisplayedChild(2);
                flipper.setDisplayedChild(2);
                swi =2;
                break;



        }
    }

    private void registerDevice() {

        RegisterThread registerObj = new RegisterThread();
        registerObj.start();

    }

    private void println(String msg) {
        final String output = msg;
        handler.post(new Runnable() {
            public void run() {
                Log.d(TAG, output);

            }
        });
    }

    class RegisterThread extends Thread {
        public void run() {

            try {
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                regId = gcm.register(GCMInfo.PROJECT_ID);
                println("푸시 서비스를 위해 단말을 등록했습니다.");



                try {
                    //FileOutputStream fos = openFileOutput(setPath + "/" + "rec_key.txt", Context.MODE_WORLD_WRITEABLE);
                    FileOutputStream fos = new  FileOutputStream(setPath + "/" + "rec_key");

                    fos.write(regId.getBytes());
                    fos.close();

                }  catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                i = new Intent( MainActivity.this ,  MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                MainActivity.this.finish();



                // 등록 ID 리스트에 추가 (현재는 1개만)
                idList.clear();
                idList.add(regId);

            } catch (Exception ex) {
                ex.printStackTrace();
            }



        }
    }





}
