package com.example.samppla;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class inquire extends Activity {

    SlidingDrawer drawer;
    ImageView rbtn,cbtn,lbtn,hbtn,cfbtn,pic_btn,ack_btn,sba;
    Intent i;
    Bitmap bmImg;
    //back task;
    phpDown update;
    uploadmsg new_msg;
    EditText msg_enter;
    ArrayList<ListItem> listItem= new ArrayList<ListItem>();
    private String PhoneNumber;
    String lmsgt;


   // ArrayAdapter<ListItem> msg_Adapter;
   ArrayList<msg_conte> al = new ArrayList<msg_conte>();

    Bitmap originalImg;
    Matrix updownInversion;
    Bitmap updownInversionImg;
    int count;
    String setPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/data/";
    RelativeLayout bt_1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta);

        setPath = setPath + this.getPackageName();
        drawer = (SlidingDrawer)findViewById(R.id.sdme);

        update = new phpDown();
        updownInversion = new Matrix();

        originalImg = BitmapFactory.decodeResource(getResources(), R.drawable.obtn);
        updownInversionImg = Bitmap.createBitmap(originalImg, 0, 0,
                originalImg.getWidth(), originalImg.getHeight(), updownInversion, false);

        Intent intent2 = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
// 패키지 네임과 클래스 네임 설정
        intent2.putExtra("badge_count_package_name", this.getPackageName());
        intent2.putExtra("badge_count_class_name",  "com.example.samppla.title");
        //해당패키지네임
// 업데이트 카운트
        intent2.putExtra("badge_count", 0);
        sendBroadcast(intent2);

        try {

            FileOutputStream fos = new FileOutputStream(setPath + "/" + "count.txt");
            count = 0;
            fos.write(String.valueOf(count).getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        msg_enter = (EditText)findViewById(R.id.msg_enter);
        rbtn = (ImageView)findViewById(R.id.rbtn);
        cbtn = (ImageView)findViewById(R.id.cbtn);
        lbtn = (ImageView)findViewById(R.id.lbtn);
        hbtn = (ImageView)findViewById(R.id.hbtn);
        cfbtn = (ImageView)findViewById(R.id.cfbtn);
        pic_btn = (ImageView)findViewById(R.id.pic_btn);
        ack_btn = (ImageView)findViewById(R.id.ack_btn);
        sba = (ImageView)findViewById(R.id.sba);
        bt_1 = (RelativeLayout)findViewById(R.id.bt_1);

        sba.setImageBitmap(updownInversionImg);

        TelephonyManager systemService = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        PhoneNumber = systemService.getLine1Number();

        if(PhoneNumber!=null && PhoneNumber.equals("")){
            PhoneNumber = PhoneNumber.substring(PhoneNumber.length()-10,PhoneNumber.length());
            PhoneNumber = "0"+PhoneNumber;
        }
        PhoneNumber =  "0" + PhoneNumber.substring(3,PhoneNumber.length());



        if(PhoneNumber.length() == 10){
            PhoneNumber = "0" + PhoneNumber;

        }
        else if(PhoneNumber.length() == 9){
            PhoneNumber = "01" + PhoneNumber;

        }
        else if(PhoneNumber.length() == 8){
            PhoneNumber = "010" + PhoneNumber;


        }


        update.execute(AddrInfo.addr + AddrInfo.cont1_path + AddrInfo.view_msg +   "?number=\"" + PhoneNumber + "\"" );

        Log.e("1111111", "11111111111");


        // ListView 로 메세지전달
        //    1. 다량의 데이터
        //    2. Adapter (데이터와 view의 연결 관계를 정의)
        //    3. AdapterView (ListView)

        RelativeLayout touchInterceptor = (RelativeLayout)findViewById(R.id.ratlay);
        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (msg_enter.isFocused()) {
                        Rect outRect = new Rect();
                        msg_enter.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                            msg_enter.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        }
                    }
                }
                return false;
            }
        });


        ListView listView1 = (ListView)findViewById(R.id.listView1);
        listView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (msg_enter.isFocused()) {
                        Rect outRect = new Rect();
                        msg_enter.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            msg_enter.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        }
                    }
                }
                return false;
            }
        });

        msg_enter.requestFocus();


        File setting = new File(setPath +"/"+ "title_not2");
        if (setting.isFile()) {

        }else{
            DialogSimple();
        }

    }




    String checkda2;
    boolean ch = false;

    private void DialogSimple(){




        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);


        alt_bld.setMessage( "* 정확한 진단을 위해서는 직접 병원방문을 하셔야 하며 본 1:1상담은 일반적인 답변이고 법적 책임이 없습니다.\n" +
                "* 진료관계상 답변이 늦어질 수 있으며 진료시간 이외에는 답변이 어렵습니다.\n" +
                "* 진료예약시 대략적인 증상과 희망 내원 시간을 명시하여 주시기 바랍니다.");

        alt_bld.setCancelable(false);

        LinearLayout settingLayout = new LinearLayout(inquire.this) ;
        settingLayout.setOrientation(LinearLayout.VERTICAL) ;

        final CheckBox radOptionTen = new CheckBox(inquire.this) ;
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
                                checkda2 = "y";
                                FileOutputStream fos = new FileOutputStream(setPath + "/" + "title_not2");
                                fos.write(checkda2.getBytes());
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



    public void onClick(View view){
        switch(view.getId()){
            case R.id.bt_1:

                break;
            case R.id.lbtn:
                /*
                i = new Intent(inquire.this,  inquire.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                this.finish();
                */
                break;

            case R.id.cbtn:

                i = new Intent(inquire.this,  center2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                this.finish();

                break;
            case R.id.rbtn:
                i = new Intent(inquire.this, right2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                this.finish();
                break;
            case R.id.hbtn:
                i = new Intent( inquire.this ,  MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                this.finish();
                break;
            case R.id.ack_btn:




                if( msg_enter.getText().length() < 2 ){
                    Toast.makeText(this, "2문자 이하는 보내실수없습니다. ", Toast.LENGTH_LONG).show();
                    break;
                }
                new_msg = new uploadmsg();
                lmsgt =  java.net.URLEncoder.encode(new String(msg_enter.getText().toString()));
                //lmsgt = msg_enter.getText().toString();

                //Toast.makeText(this, "http://www.ad32131.net/xe/cont1/upload.php?lmsg=\"" + lmsgt + "\"" + "&number=" + "\"" + PhoneNumber + "\"",Toast.LENGTH_LONG).show();
                //Log.e("http://www.ad32131.net/xe/cont1/upload.php?lmsg=\"" + lmsgt + "\"" + "&number=" + "\"" + PhoneNumber + "\"" , "adbasdfadsfasdf");
                new_msg.execute(AddrInfo.addr + AddrInfo.cont1_path + AddrInfo.upload + "?lmsg=\"" + lmsgt + "\"" + "&number=" + "\"" + PhoneNumber + "\"");
                /*
                String aac = "http://www.ad32131.net/xe/cont1/upload.php?lmsg=" + msg_enter.getText()
                           + ""
                        ;
                        */
                //Toast.makeText(this,aac,Toast.LENGTH_LONG).show();

                //new_msg.execute("");

                i = new Intent( inquire.this ,  inquire.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                this.finish();


                break;
            case R.id.msg_enter:
                drawer.close();
                break;
            case R.id.sba:


                updownInversion.setScale(1, -1);
                updownInversionImg = Bitmap.createBitmap(updownInversionImg, 0, 0,
                        updownInversionImg.getWidth(), updownInversionImg.getHeight(), updownInversion, false);
                sba.setImageBitmap(updownInversionImg);





                break;
        }
    }

    public class msgAdapter extends BaseAdapter{

        Context context;     // 현재 화면의 제어권자
        int layout;              // 한행을 그려줄 layout
        ArrayList<ListItem> al;     // 다량의 데이터
        LayoutInflater inf; // 화면을 그려줄 때 필요
        public msgAdapter(Context context, int layout, ArrayList<ListItem> al){
            this.context = context;
            this.layout = layout;
            this.al = al;
            this.inf = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inf.inflate(layout, null);


           // ImageView limg = (ImageView)convertView.findViewById(R.id.rimg);
            //ImageView rimg = (ImageView)convertView.findViewById(R.id.limg);
            TextView lmsg_con=(TextView)convertView.findViewById(R.id.lmsg_con);
            TextView ltime_con =(TextView)convertView.findViewById(R.id.ltime_con);
            TextView rmsg_con=(TextView)convertView.findViewById(R.id.rmsg_con);
            TextView rtime_con=(TextView)convertView.findViewById(R.id.rtime_con);

            ListItem m = al.get(position);

            //iv.setImageResource(m.img);
           // rmsg_con.setText(m.mData[0]);
            rtime_con.setText("일시"+ m.mData[1]+m.mData[2] );

            return convertView;
        }
    }
    private class uploadmsg extends AsyncTask<String, Integer,String> {


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

    private class phpDown extends AsyncTask<String, Integer,String>{



        @Override
        protected String doInBackground(String... urls) {

            StringBuilder jsonHtml = new StringBuilder();

            try{


                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                // 연결되었으면.
                if(conn != null){
                    conn.setConnectTimeout(10000);

                    conn.setUseCaches(false);

                   // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){

                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();

                            if(line == null) break;

                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");

                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch(Exception ex){

                ex.printStackTrace();


            }
            Log.e("여기야 여기 여기라고!", jsonHtml.toString()  );
            return jsonHtml.toString();



        }



        protected void onPostExecute(String str){

            String lmsg;
            String ldate;
            String ltime;
            String iden;
            String rmsg;
            String rdate;
            String rtime;
            try{
                Log.e("aaaa","1111");
                JSONObject root = new JSONObject(str);
                Log.e("aaaa","2222");
                JSONArray ja = root.getJSONArray("results");
                Log.e("aaaa","3333");
                for(int i=0; i<ja.length(); i++){
                    Log.e("aaaa","4444");
                    JSONObject jo = ja.getJSONObject(i);

                    lmsg = jo.getString("lmsg");
                    ldate = jo.getString("ldate");
                    ltime = jo.getString("ltime");
                    iden = jo.getString("iden");
                    rmsg = jo.getString("rmsg");
                    rdate = jo.getString("rdate");
                    rtime = jo.getString("rtime");
                    listItem.add(new ListItem(lmsg,ldate,ltime,iden,rmsg,rdate,rtime));

                }
            }catch(JSONException e){
                e.printStackTrace();
                Log.e("aaaa","예외발생2");
            }
            msgAdapter adat = new msgAdapter(
                    getApplicationContext(), // 현재 화면의 제어권자
                    R.layout.row,             // 한행을 그려줄 layout
                    listItem);
            Log.e("1111111","22222222222");

            //ListView list = (ListView)findViewById(R.id.listView1);

            //list.setAdapter(adat);

            //txtView.setText(listItem.get(listItem.size()-1).getData(3).toString());
            //txtView.setText(listItem.get(listItem.size()-1).getData(3).toString() );

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



            /*
            Toast.makeText(inquire.this, listItem.get(0).getData(0).toString() +
                    listItem.get(0).getData(1).toString()+
                    listItem.get(0).getData(2).toString()+
                    listItem.get(0).getData(3).toString()+
                    listItem.get(0).getData(4).toString()+
                    listItem.get(0).getData(5).toString()+
                    listItem.get(0).getData(6).toString()  ,Toast.LENGTH_LONG).show();
                    */


            String temp="";

            for(int p=0; p< (listItem.size()); p++ ) {


                //Toast.makeText(inquire.this, String.valueOf(p) + "번째 작동" + " " + tmp_date.toString() + " " + listItem.get(p).getData(1).toString() + " " + listItem.get(p).getData(5).toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(inquire.this,String.valueOf(tmp_date.toString().length()) , Toast.LENGTH_LONG).show();

                /*
                if((tmp_date == (tmp_date2=listItem.get(p).getData(1).toString())) || (tmp_date == (tmp_date3 = listItem.get(p).getData(5).toString())) ){
                    Toast.makeText(inquire.this, String.valueOf(p)+"번째 작동" ,Toast.LENGTH_LONG).show();
                }
                */

                if( !(temp.equals(listItem.get(p).getData(1).toString())) ) {
                    al.add(new msg_conte(R.drawable.ad, listItem.get(p).getData(0),
                            listItem.get(p).getData(2),
                            true, R.drawable.ad,
                            listItem.get(p).getData(4),
                            listItem.get(p).getData(6), listItem.get(p).getData(1) ) );
                    temp = listItem.get(p).getData(1).toString();
                }
                else{
                    al.add(new msg_conte(R.drawable.ad, listItem.get(p).getData(0),
                            listItem.get(p).getData(2),
                            true, R.drawable.ad,
                            listItem.get(p).getData(4),
                            listItem.get(p).getData(6), "" ) );
                }


            }

            // adapter
            com.example.samppla.msgAdapter adapter = new com.example.samppla.msgAdapter(
                    getApplicationContext(), // 현재 화면의 제어권자
                    R.layout.row,             // 한행을 그려줄 layout
                    al);                     // 다량의 데이터

            ListView lv = (ListView)findViewById(R.id.listView1);
            lv.setAdapter(adapter);

            // 이벤트 처리
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Log.d("test", "아이템클릭, postion : " + position +
                            ", id : " + id);
                }
            });


        }


    }



}
