package com.example.samppla;

/**
 * Created by 황정선 on 2015-11-02.
 */
class msg_conte { // 자바 빈 (java Bean)
    int limg; // 사진 - res/drawable
    String lmsg_con = "";
    boolean isDomestic;
    String ltime_con = "";
    int rimg; // 사진 - res/drawable
    String rmsg_con = "";
    String rtime_con = "";
    String date = "";
    // 생성자가 있으면 객체 생성시 편리하다
    public msg_conte(int limg, String lmsg_con, String ltime_con,
                     boolean isDomestic,
                     int rimg, String rmsg_con, String rtime_con,String date) {
        this.limg = limg;
        this.lmsg_con = lmsg_con;
        this.ltime_con = ltime_con;
        this.isDomestic = isDomestic;
        this.rimg = rimg;
        this.rmsg_con = rmsg_con;
        this.date = date;
        this.rtime_con = rtime_con;
    }
    public msg_conte() {}// 기존 코드와 호환을 위해서 생성자 작업시 기본생성자도 추가
}