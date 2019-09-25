package com.example.samppla;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class right2Activity extends Activity {
    SlidingDrawer drawer;
    Intent i;
    String setPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/data/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_2);
        WebView web = (WebView) findViewById(R.id.wb_push);

// 자바스크립트 허용
        drawer = (SlidingDrawer)findViewById(R.id.sdme2);
        drawer.bringToFront();
        drawer.close();

        web.getSettings().setJavaScriptEnabled(true);

        setPath = setPath + this.getPackageName();

        String source = "";



        File setting = new File(setPath + "/" + "push");
        if (setting.isFile()) {

            try {
                FileInputStream fis = new FileInputStream(setPath + "/" + "push");

                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                source = new String(buffer);
                fis.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        web.loadData(source, "text/html", "UTF-8");

        web.loadData(source, "text/html; charset=UTF-8", null);

        WebSettings set = web.getSettings();
        web.getSettings().setBuiltInZoomControls(true);
        //web.getSettings().setSupportZoom(true);

        web.getSettings().setDisplayZoomControls(false);
        set.setLoadWithOverviewMode(true);
        set.setUseWideViewPort(true);



        File settt = new File(setPath +"/"+ "title_not4");
        if (settt.isFile()) {

        }else{
            DialogSimple();
        }

    }

    String checkda4;
    boolean ch = false;

    private void DialogSimple() {



        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);


        alt_bld.setMessage( "* 환자가 평소에 다니는 병원의 담당의사가 환자에 대해서 가장 잘 알고 있습니다.\n" +
                "   * 본 고객알림은 환자에게 1:1 맞춤 건강정보와 진료시기를 알려드립니다.");

        alt_bld.setCancelable(false);

        LinearLayout settingLayout = new LinearLayout(right2Activity.this) ;
        settingLayout.setOrientation(LinearLayout.VERTICAL) ;

        final CheckBox radOptionTen = new CheckBox(right2Activity.this) ;
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
                                checkda4 = "y";
                                FileOutputStream fos = new FileOutputStream(setPath + "/" + "title_not4");
                                fos.write(checkda4.getBytes());
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
            case R.id.lbtn:

                i = new Intent(right2Activity.this,  inquire.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);



                break;
            case R.id.cbtn:

                i = new Intent(right2Activity.this,  center2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);



                break;
            case R.id.rbtn:
                /*
                i = new Intent(right2Activity.this,  right2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                */
                drawer.close();
                break;
            case R.id.hbtn:
                i = new Intent( right2Activity.this ,  MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);



                break;
            case R.id.cfbtn:
                break;
            case R.id.sba:
                drawer.close();
                break;
            case R.id.lbtn2:
                drawer.close();
                break;
            case R.id.rbtn2:
                i = new Intent( right2Activity.this ,  Right_Activity2.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                this.finish();
                break;
            case R.id.cent_e:
                drawer.close();
                break;
        }
    }
}
