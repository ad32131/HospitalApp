package com.example.samppla;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 황정선 on 2015-11-02.
 */
class msgAdapter extends BaseAdapter {
    Context context;     // 현재 화면의 제어권자
    int layout;              // 한행을 그려줄 layout
    ArrayList<msg_conte> al;     // 다량의 데이터
    LayoutInflater inf; // 화면을 그려줄 때 필요

    public msgAdapter(Context context, int layout, ArrayList<msg_conte> al) {
        this.context = context;
        this.layout = layout;
        this.al = al;
        this.inf = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() { // 총 데이터의 개수를 리턴
        return al.size();
    }
    @Override
    public Object getItem(int position) { // 해당번째의 데이터 값
        return al.get(position);
    }
    @Override
    public long getItemId(int position) { // 해당번째의 고유한 id 값
        return position;
    }
    @Override // 해당번째의 행에 내용을 셋팅(데이터와 레이아웃의 연결관계 정의)
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        //ImageView limg= (ImageView)convertView.findViewById(R.id.limg);
        TextView lmsg_con=(TextView)convertView.findViewById(R.id.lmsg_con);
        TextView ltime_con =(TextView)convertView.findViewById(R.id.ltime_con);
        //ImageView rimg= (ImageView)convertView.findViewById(R.id.rimg);
        TextView rmsg_con=(TextView)convertView.findViewById(R.id.rmsg_con);
        TextView rtime_con =(TextView)convertView.findViewById(R.id.rtime_con);
        TextView date = (TextView)convertView.findViewById(R.id.date);

        msg_conte m = al.get(position);

        //limg.setImageResource(m.limg);
        lmsg_con.setText(m.lmsg_con);




        //rimg.setImageResource(m.rimg);


        if(m.date.toString().equals("")) {
         date.setHeight(0);

        }
        else {
            date.setText(m.date);
        }



        if( !(m.lmsg_con.isEmpty()) ){ //비어있지않을경우


            if( Integer.valueOf(m.ltime_con.toString().substring(0,2)) > 12 ) {
                ltime_con.setText( "오후 "+String.valueOf(Integer.valueOf(m.ltime_con.toString().substring(0,2))-12)+":" +
                        m.ltime_con.toString().substring(3,5))  ;
            }
            else{
                ltime_con.setText( "오전 "+ m.ltime_con.toString().substring(0,2)+":" +
                        m.ltime_con.toString().substring(3,5)  )  ;
            }
        }

        else{



        }


        if(m.rmsg_con.isEmpty() ) {
            rmsg_con.setText("");
            rtime_con.setText("");
        }



        else{



            rmsg_con.setText(m.rmsg_con);
            rmsg_con.setBackgroundResource(R.drawable.acltalk);

            if( Integer.valueOf(m.rtime_con.toString().substring(0, 2)) > 12 ) {
                rtime_con.setText( "오후 "+String.valueOf(Integer.valueOf(m.rtime_con.toString().substring(0,2))-12)+";" +
                       m.rtime_con.toString().substring(3,5) )  ;
            }
            else{
                rtime_con.setText(  "오전 "+ m.rtime_con.toString().substring(0,2)+":" +
                        m.rtime_con.toString().substring(3,5)  )  ;
            }

        }



        lmsg_con.setBackgroundResource(R.drawable.acrtalk);



        return convertView;
    }
}