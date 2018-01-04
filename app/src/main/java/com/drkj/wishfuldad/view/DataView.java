package com.drkj.wishfuldad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.TibeiDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganlong on 2017/12/19.
 */

public class DataView extends View {
    private float mLastX = 0;
    List<String> times = new ArrayList<>();
    //最小可滑动值、最大可滑动值
    protected int mMinPosition = 0, mMaxPosition = 0;
    private List<TibeiDataBean> list = new ArrayList<>();
    Paint linePaint;
    Paint rectPaint;
    Paint textPaint;
    Paint paint;
    DataLayout layout;
    private int scrollx;
    private boolean flag = true;
    public DataView(Context context,DataLayout layout) {
        super(context);
        this.layout = layout;
        initData();
    }

    public DataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public DataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
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
        paint.setColor(getResources().getColor(R.color.data_view_text_color));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(getResources().getColor(R.color.line_color));
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(getResources().getColor(R.color.toolbar_color));
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(getResources().getColor(R.color.white));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float textH = paint.measureText("10");
//        for (int i=10;i>0;i--){
//            canvas.drawText(i+"",50,(10-i)*50+100,paint);
//        }
        int h = canvas.getHeight() / 11;
        int w = canvas.getWidth() / 6;
        for (int j = 0; j <= 23; j++) {
            canvas.drawText(times.get(j), w / 2 + j * w, h / 2, paint);
            canvas.drawLine(w / 2 + j * w, h, w / 2 + j * w, 11 * h, linePaint);

            for (TibeiDataBean bean : list) {
                if (bean.getTime() == j  && bean.getNumber() > 0 && bean.getNumber() < 11) {
                    canvas.drawRect(w / 2 + j * w - 30, h * (11 - bean.getNumber()), w / 2 + j * w + 30, h * (11 - bean.getNumber()) + textH, rectPaint);
                    canvas.drawText(bean.getNumber() + "", w / 2 + j * w, h * (11 - bean.getNumber()) + textH - 5, textPaint);
                   scrollx = w / 2 + j * w-canvas.getWidth()/2;
                }
            }
        }
        if (flag){
            flag = false;
            scrollTo(scrollx,0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = currentX;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = mLastX - currentX;
                mLastX = currentX;
//                offsetLeftAndRight((int) (moveX));
                scrollBy((int) (moveX), 0);
                break;

        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMinPosition = 0;
        mMaxPosition = w / 6 * 18;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (x < mMinPosition) {
//            goStartEdgeEffect(x);
            x = mMinPosition;
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        if (x > mMaxPosition) {
//            goEndEdgeEffect(x);
            x = mMaxPosition;
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        if (x != getScrollX()) {
            super.scrollTo(x, y);
        }
    }

    public void setData(List<TibeiDataBean> list) {
        this.list = list;
        invalidate();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
