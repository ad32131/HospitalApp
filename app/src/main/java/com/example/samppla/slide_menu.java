package com.example.samppla;

/**
 * Created by 황정선 on 2015-11-13.
 */

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class slide_menu extends LinearLayout {

    private Context context;
    private Scroller scroller;
    private float density;
    private int scrollSizeWidth;


    public slide_menu(Context context) {
        super(context);
        this.context = context;
        init();
    }


    public slide_menu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    private void init(){
        scroller = new Scroller(context);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        int screenWidth = disp.getWidth();

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.density = metrics.density;

        scrollSizeWidth = (int)(screenWidth-(48*density));




    }



//	@Override
//    public boolean onTouchEvent(MotionEvent event) {
//        final int action = event.getAction();
//        if((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP){
//            animationStart();
//        }
//        return true;
//    }


    public void scroll(){
        animationStart();
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            // Scroller
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }

    private void animationStart(){
        if (scroller.getCurrX() < 0)
            scroller.startScroll(scroller.getCurrX(), scroller.getCurrY(), -1*scroller.getCurrX(), 0, 500);
        else
            scroller.startScroll(scroller.getCurrX(), scroller.getCurrY(), -scrollSizeWidth, 0, 500);

        invalidate();
    }
}
