package com.drkj.wishfuldad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.drkj.wishfuldad.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganlong on 2017/12/20.
 */

public class PeeRecordTimeView extends View{
    List<String> times = new ArrayList<>();
    protected int mMinPositionY = 0, mMaxPositionY = 0;
    private float mLastY = 0;
    Paint paint;
    public PeeRecordTimeView(Context context) {
        super(context);
        initdatas();
    }

    public PeeRecordTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initdatas();
    }

    public PeeRecordTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initdatas();
    }

    private void initdatas() {
        times.add("0:00");
        times.add("1:00");
        times.add("2:00");
        times.add("3:00");
        times.add("4:00");
        times.add("5:00");
        times.add("6:00");
        times.add("7:00");
        times.add("8:00");
        times.add("9:00");
        times.add("10:00");
        times.add("11:00");
        times.add("12:00");
        times.add("13:00");
        times.add("14:00");
        times.add("15:00");
        times.add("16:00");
        times.add("17:00");
        times.add("18:00");
        times.add("19:00");
        times.add("20:00");
        times.add("21:00");
        times.add("22:00");
        times.add("23:00");
        times.add("24:00");

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(getResources().getColor(R.color.line_color));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int h = canvas.getHeight()/10;
        int w = canvas.getWidth();
        float textH = paint.descent()-paint.ascent();
        for (int i=0;i<times.size();i++){
            canvas.drawText(times.get(i),w/2,(i*h)+textH,paint);
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMinPositionY = 0;
        mMaxPositionY = h/10*15;
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float currentY = event.getY();
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                mLastY = currentY;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float moveY = mLastY -currentY;
//                mLastY = currentY;
////                offsetLeftAndRight((int) (moveX));
//                scrollBy( 0, (int)moveY);
//                break;
//        }
//        return true;
//    }
    @Override
    public void scrollTo(int x, int y) {

        if (y<mMinPositionY){
            y=mMinPositionY;
        }
        if (y>mMaxPositionY){
            y=mMaxPositionY;
        }
        if (y!=getScrollY()) {
            super.scrollTo(x, y);
        }
    }
}
