package com.example.samppla;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 황정선 on 2015-11-10.
 */
public class CalendarAdapter extends BaseAdapter
{
    private ArrayList<DayInfo> mDayList;
    private Context mContext;
    private int mResource;
    private LayoutInflater mLiInflater;
    private int day_appoint;
    private String day_appoint_time;
    private String day_appoint_date;
    private int month_aa;
    /**
     * Adpater 생성자
     *
     * @param context
     *            컨텍스트
     * @param textResource
     *            레이아웃 리소스
     * @param dayList
     *            날짜정보가 들어있는 리스트
     */
    public CalendarAdapter(Context context, int textResource, ArrayList<DayInfo> dayList,int ata,String ata_time,String ata_date,int mont)
    {
        this.mContext = context;
        this.mDayList = dayList;
        this.mResource = textResource;
        this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.day_appoint = ata;
        this.day_appoint_time = ata_time;
        this.day_appoint_date = ata_date;
        this.month_aa = mont;

    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mDayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mDayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DayInfo day = mDayList.get(position);

        DayViewHolde dayViewHolder;

        if(convertView == null)
        {
            if((position == day_appoint)  && (month_aa == Integer.valueOf(day_appoint_date) ) ) {
                convertView = mLiInflater.inflate(R.layout.day_app, null);
            }
            else{
                convertView = mLiInflater.inflate(mResource, null);
            }

            if(position % 7 == 6)
            {
                convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP()+getRestCellWidthDP(), getCellHeightDP()));
            }
            else
            {
                convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP(), getCellHeightDP()));
            }


            dayViewHolder = new DayViewHolde();


                dayViewHolder.llBackground = (RelativeLayout) convertView.findViewById(R.id.day_cell_ll_background);


                    dayViewHolder.tvDay = (TextView) convertView.findViewById(R.id.day_cell_tv_day);

                    convertView.setTag(dayViewHolder);
        }
        else
        {
            dayViewHolder = (DayViewHolde) convertView.getTag();
        }

        if(day != null)
        {

            if ((position == day_appoint) && (month_aa == Integer.valueOf(day_appoint_date) ) ){
                final SpannableStringBuilder abc =  new SpannableStringBuilder("\n"+ day_appoint_time + "시");
                abc.setSpan( new AbsoluteSizeSpan(45), 1, abc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
                final SpannableStringBuilder abc2 =  new SpannableStringBuilder("\n" + "예약");
                abc2.setSpan( new AbsoluteSizeSpan(45), 1, abc2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

                dayViewHolder.tvDay.setText( day.getDay()  );
                dayViewHolder.tvDay.append(abc);
                dayViewHolder.tvDay.append(abc2);
            }
            else {
                dayViewHolder.tvDay.setText(day.getDay());

            }


            if(day.isInMonth())
            {
                if(position % 7 == 0)
                {
                    dayViewHolder.tvDay.setTextColor(Color.RED);
                }
                else if(position % 7 == 6)
                {
                    dayViewHolder.tvDay.setTextColor(Color.BLUE);
                }
                else
                {
                    dayViewHolder.tvDay.setTextColor(Color.BLACK);

                }
            }
            else
            {
                dayViewHolder.tvDay.setTextColor(Color.GRAY);
            }

        }

        return convertView;
    }

    public class DayViewHolde
    {
        public RelativeLayout llBackground;
        public TextView tvDay;
        public TextView tvDay2;

    }

    private int getCellWidthDP()
    {
//      int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int cellWidth = 2000/7;

        return cellWidth;
    }

    private int getRestCellWidthDP()
    {
//      int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int cellWidth = 2000%7;

        return cellWidth;
    }

    private int getCellHeightDP()
    {
//      int height = mContext.getResources().getDisplayMetrics().widthPixels;
        int cellHeight = 1500/6;

        return cellHeight;
    }

}
