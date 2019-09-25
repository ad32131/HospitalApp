package com.example.samppla;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class title extends Activity {

    String setPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/data/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_lay);
        setPath = setPath + this.getPackageName();
        File setting = new File(setPath +"/"+ "title_not");

        if (setting.isFile()) {

            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {


                @Override
                public void run() {

                    Intent i;
                    i = new Intent(title.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    title.this.finish();

                }
            }, 3000);

        }

        else {
            DialogSimple();

        }

    }
    String checkda;
    boolean ch = false;


    private void DialogSimple(){




        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);


        alt_bld.setMessage(  "* 개인정보보호를 위해 휴대폰의 전화번호외에는 성명, 주소 등 일체의 개인정보를 수집하지 않습니다.\n" +
                "* WIFI외에 3G/4G네트워크를 이용할 경우 가입하신 요금제에 따라 데이터 통화료가 발생할 수 있습니다.");

        alt_bld.setCancelable(false);

        LinearLayout settingLayout = new LinearLayout(title.this) ;
        settingLayout.setOrientation(LinearLayout.VERTICAL) ;

        final CheckBox radOptionTen = new CheckBox(title.this) ;
        radOptionTen.setText("다시는 이창을 띄우지 않습니다.") ;
        radOptionTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch == false){
                    ch = true;

                }
                else{
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
                                checkda = "y";
                                FileOutputStream fos = new FileOutputStream(setPath + "/" + "title_not");
                                fos.write(checkda.getBytes());
                                fos.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        dialog.cancel();

                        Intent i;
                        i = new Intent(title.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        title.this.finish();

                    }
                });



                          AlertDialog alert = alt_bld.create();
                        // Title for AlertDialog
                        alert.setTitle("안내메세지");


                        // Icon for AlertDialog
                         alert.setIcon(R.mipmap.ic_launcher);
                          alert.show();

    }



                    }







