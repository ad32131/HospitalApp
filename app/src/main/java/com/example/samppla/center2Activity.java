package com.example.samppla;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class center2Activity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static int SUNDAY = 1;
    public static int MONDAY = 2;
    public static int TUESDAY = 3;
    public static int WEDNSESDAY = 4;
    public static int THURSDAY = 5;
    public static int FRIDAY = 6;
    public static int SATURDAY = 7;

    String setPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/data/";

    private TextView mTvCalendarTitle;
    private GridView mGvCalendar;


    private ArrayList<DayInfo> mDayList;
    private CalendarAdapter mCalendarAdapter;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    int posa;
    String daa,daa_time;
    Intent i;
    private String PhoneNumber;
    phpDown get_appoi;
    uploadmsg up_appoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        setPath = setPath + this.getPackageName();

        Button bLastMonth = (Button) findViewById(R.id.gv_calendar_activity_b_last);
        Button bNextMonth = (Button) findViewById(R.id.gv_calendar_activity_b_next);

        mTvCalendarTitle = (TextView) findViewById(R.id.gv_calendar_activity_tv_title);
        mGvCalendar = (GridView) findViewById(R.id.gv_calendar_activity_gv_calendar);


        bLastMonth.setOnClickListener(center2Activity.this);
        bNextMonth.setOnClickListener(this);
        mGvCalendar.setOnItemClickListener(this);

        mDayList = new ArrayList<DayInfo>();


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

        get_appoi = new phpDown();
        get_appoi.execute( AddrInfo.addr+ AddrInfo.cont1_path + AddrInfo.get_appoi + "?number=\"" + PhoneNumber + "\"");

        File setting = new File(setPath +"/"+ "title_not3");
        if (setting.isFile()) {

        }else{
            DialogSimple();
        }

    }



    String checkda3;
    boolean ch = false;

    private void DialogSimple(){




        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);


        alt_bld.setMessage( "* 대략적인 증상을 미리 알려주시면 보다 정확한 진찰에 도움이 되며 예약없이 병원을 방문하셔도 됩니다.\n" +
                "   * 개인정보보호를 위해 진료예약은 1:1상담을 통해서 하실 수 있으며 대략적인 증상과 희망 내원 일자 및 시간을 요청하시기 바랍니다.\n" +
                "   * 1:1상담을 통해 요청하신 예약일자를 확인하실 수 있습니다.");

        alt_bld.setCancelable(false);

        LinearLayout settingLayout = new LinearLayout(center2Activity.this) ;
        settingLayout.setOrientation(LinearLayout.VERTICAL) ;

        final CheckBox radOptionTen = new CheckBox(center2Activity.this) ;
        radOptionTen.setText("다시는 이창을 띄우지 않습니다.") ;
        radOptionTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch == false) {
                    ch = true;

                } else {
                    ch = false;

                }
                // TODO Auto-generated method stub
            }
        }) ;
        settingLayout.addView(radOptionTen) ;
        alt_bld.setView(settingLayout);


        alt_bld.setNegativeButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button

                        if (ch == true) {
                            try {
                                checkda3 = "y";
                                FileOutputStream fos = new FileOutputStream(setPath + "/" + "title_not3");
                                fos.write(checkda3.getBytes());
                                fos.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        dialog.cancel();



                    }
                });



        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("안내메세지");


        // Icon for AlertDialog
        alert.setIcon(R.mipmap.ic_launcher);
        alert.show();

    }


    private class uploadmsg extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... urls) {


            try {
                Log.e(urls[0], "eeeeeeeeeee");

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

                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();

                            if (line == null) break;

                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                        }

                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                Log.e("eeeeeeeeeeeeeee", "eeeeeeeeeee");
                ex.printStackTrace();


            }

            return "ok";


        }
    }





    private class phpDown extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... urls) {

            StringBuilder jsonHtml = new StringBuilder();

            try {


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

                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();

                            if (line == null) break;

                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");

                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {

                ex.printStackTrace();


            }
            Log.e("여기야 여기 여기라고!", jsonHtml.toString());
            return jsonHtml.toString();


        }


        protected void onPostExecute(String str) {


            try {

                JSONObject root = new JSONObject(str);

                JSONArray ja = root.getJSONArray("results");

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);


                    daa = jo.getString("date");


                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("aaaa", "예외발생2");
            }




            /*
            textView1.setText("보낸질문:"+listItem.get(1).getData(0).toString()+
                         "\n날짜:"+ listItem.get(1).getData(1).toString()+
                          "\n시간:"+listItem.get(1).getData(2).toString()+
                          "\n번호:"+listItem.get(1).getData(3).toString()+
                           "\n답장:"+listItem.get(1).getData(4).toString()+
                           "\n날짜:"+listItem.get(1).getData(5).toString()+
                           "\n시간:"+listItem.get(1).getData(6).toString()
           );
            */
            //Toast.makeText(center2Activity.this, "예약하신 날짜 는 "+date + " 입니다.", Toast.LENGTH_LONG).show();

            // Toast.makeText(center2Activity.this, "예약하신 날짜 는 " + date.toString().substring(5, 7) + "월" + date.toString().substring(8, 10) + "일" + date.toString().substring(11, 16) + " 입니다.", Toast.LENGTH_LONG).show();
            //
            // date.toString().substring(8,10)

            if (!(TextUtils.isEmpty(daa))) {
                try {
                    FileOutputStream fos = new FileOutputStream(setPath + "/" + "appoint");
                    fos.write(daa.toString().substring(8, 10).getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    FileOutputStream fos = new FileOutputStream(setPath + "/" + "appoint_time");
                    fos.write(daa.toString().substring(11, 13).getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    FileOutputStream fos = new FileOutputStream(setPath + "/" + "appoint_date");
                    fos.write(daa.toString().substring(5, 7).getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                daa = "-1";
            }







        }



        // adapter
        // 다량의 데이터


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lbtn:

                i = new Intent(center2Activity.this, inquire.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


                break;
            case R.id.cbtn:
                /*
                i = new Intent(center2Activity.this, center2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                */

                break;
            case R.id.rbtn:
                i = new Intent(center2Activity.this, right2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


                break;
            case R.id.hbtn:
                i = new Intent(center2Activity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


                break;
            case R.id.cfbtn:

                break;
            case R.id.gv_calendar_activity_b_last:
                mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
            case R.id.gv_calendar_activity_b_next:
                mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
        }
    }

    private Calendar getLastMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }


    @Override
    protected void onResume() {
        super.onResume();

        // 이번달 의 캘린더 인스턴스를 생성한다.
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);
    }


    private void getCalendar(Calendar calendar) {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();

        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH) + "");

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH) + "");

        if (dayOfMonth == SUNDAY) {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth - 1) - 1;


        // 캘린더 타이틀(년월 표시)을 세팅한다.
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        DayInfo day;

        Log.e("DayOfMOnth", dayOfMonth + "");

        for (int i = 0; i < dayOfMonth - 1; i++) {
            int date = lastMonthStartDay + i;
            day = new DayInfo();

            day.setInMonth(false);

            mDayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);

            mDayList.add(day);
        }


        initCalendarAdapter(dayOfMonth - 2, mThisMonthCalendar.get(Calendar.MONTH) +1);
    }


    private Calendar getNextMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {

        /*
        int min = 0, max = 0;

        for (int a = 0; a < 41; a++) {
            if (max < Integer.valueOf(mDayList.get(a).getDay()))
                max = Integer.valueOf(mDayList.get(a).getDay());
            else {
                max = a;
                a = 41;
            }
        }


        for (int a = max + 1; a < 41; a++) {
            if (min < Integer.valueOf(mDayList.get(a).getDay()))
                min = Integer.valueOf(mDayList.get(a).getDay());
            else {
                min = a - 1;
                a = 41;
            }
        }

        if (((max - 1) < position) && (position < (min + 1))) {
            //Toast.makeText(center2Activity.this, String.valueOf(String.valueOf(mDayList.get(max).getDay())) +"~"+ String.valueOf(String.valueOf(mDayList.get(min).getDay())) +" "+String.valueOf(mDayList.get(position).getDay()),Toast.LENGTH_LONG ).show();
            Toast.makeText(center2Activity.this, mThisMonthCalendar.get(Calendar.YEAR) + "-" + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "-" + String.valueOf(mDayList.get(position).getDay()), Toast.LENGTH_LONG).show();
            posa = position;


            // up_appoi  = new uploadmsg();
            // up_appoi.execute("http://www.ad32131.net/xe/cont1/get_appoi.php?number=\"" + PhoneNumber + "\"" + "&date=" + "\"" + PhoneNumber + "\"" );
        }


        //this,String.valueOf(position) +" "+ String.valueOf(arg3)

        */
    }


    private void initCalendarAdapter(int ddat,int mont) {

        String sen_day = "0";
        String sen_time = "0";
        String sen_date = "0";
        int ddat_time =0;
        File setting = new File(setPath + "/" + "appoint");
        if (setting.isFile()) {

            try {
                FileInputStream fis = new FileInputStream(setPath + "/" + "appoint");

                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                sen_day = new String(buffer);
                fis.close();



            } catch (IOException e) {
                e.printStackTrace();
            }

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(setPath + "/" + "appoint_time");
                byte[] buffer = new byte[0];

                buffer = new byte[fis.available()];
                fis.read(buffer);
                sen_time = new String(buffer);
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }

            try {
                fis = new FileInputStream(setPath + "/" + "appoint_date");
                byte[] buffer = new byte[0];

                buffer = new byte[fis.available()];
                fis.read(buffer);
                sen_date = new String(buffer);
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }




            if( !(sen_day.isEmpty()) ) {
                ddat = Integer.valueOf(sen_day) + ddat;

            }
            else {


            }
            mCalendarAdapter = new CalendarAdapter(this, R.layout.day, mDayList, ddat,sen_time, sen_date ,mont );
            mGvCalendar.setAdapter(mCalendarAdapter);
        }
        else{
            mCalendarAdapter = new CalendarAdapter(this, R.layout.day, mDayList, -1,"","",mont );
            mGvCalendar.setAdapter(mCalendarAdapter);
        }

    }





}
