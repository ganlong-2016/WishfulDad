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
 * Created by ganlong on 2017/12/28.
 */

public class PeeRecordDayView extends View {
    Paint paint;
    protected int mMinPositionX = 0, mMaxPositionX = 0;
    List<String> days = new ArrayList<>();
    public PeeRecordDayView(Context context) {
        super(context);
        init();
    }

    public PeeRecordDayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PeeRecordDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(getResources().getColor(R.color.data_view_text_color));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMinPositionX = 0;
        mMaxPositionX = w/5*2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int h = canvas.getHeight();
        int w = canvas.getWidth()/5;
        for (int j= 0;j<days.size();j++) {
            canvas.drawText(days.get(j), w / 2 + j * w, h / 2, paint);
        }
    }
    @Override
    public void scrollTo(int x, int y) {
        if (x < mMinPositionX) {
            x = mMinPositionX;
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        if (x > mMaxPositionX) {
            x = mMaxPositionX;
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        if (x != getScrollX()) {
            super.scrollTo(x,0);
        }
    }
    public void setDays(List<String> days) {
        this.days = days;
        invalidate();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
