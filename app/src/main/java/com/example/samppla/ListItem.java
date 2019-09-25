package com.example.samppla;

/**
 * Created by 황정선 on 2015-10-31.
 */
public class ListItem {

    public String[] mData;

    public ListItem(String[] data ){


        mData = data;
    }

    public ListItem(String lmsg, String ldate, String ltime,String iden,String rmsg,String rdate,String rtime){

        mData = new String[7];
        mData[0] = lmsg;
        mData[1] = ldate;
        mData[2] = ltime;
        mData[3] = iden;
        mData[4] = rmsg;
        mData[5] = rdate;
        mData[6] = rtime;

    }

    public String[] getData(){
        return mData;
    }

    public String getData(int index){
        return mData[index];
    }

    public void setData(String[] data){
        mData = data;
    }
    public String sizeget(){
        return mData[7];
    }


}
