package com.drkj.wishfuldad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.DataBean;
import com.drkj.wishfuldad.bean.PeeDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganlong on 2017/12/20.
 */

public class PeeRecordView extends View {
    List<String> times = new ArrayList<>();
    List<String> days = new ArrayList<>();
    private float mLastX = 0;
    private float mLastY = 0;
    protected int mMinPositionX = 0, mMaxPositionX = 0;
    protected int mMinPositionY = 0, mMaxPositionY = 0;
    private List<DataBean> data = new ArrayList<>();
    Paint linePaint;
    Paint rectPaint;
    Paint textPaint;
    Paint paint;
    PeeRecordLayout layout;
    private int scrollx;
    private int scrolly;
    private boolean flag = true;
    public PeeRecordView(Context context, PeeRecordLayout layout) {
        super(context);
        this.layout = layout;
        initdatas();
    }

    public PeeRecordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initdatas();
    }

    public PeeRecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initdatas();
    }

    private void initdatas() {
//        days.add("09-01");
//        days.add("09-02");
//        days.add("09-03");
//        days.add("09-04");
//        days.add("09-05");
//        days.add("09-06");
//        days.add("09-07");

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
        paint.setColor(getResources().getColor(R.color.data_view_text_color));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(getResources().getColor(R.color.line_color));
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(getResources().getColor(R.color.toolbar_color));
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(getResources().getColor(R.color.white));
//        for (int i=10;i>0;i--){
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//            canvas.drawText(i+"",50,(10-i)*50+100,paint);
//        }
        int h = canvas.getHeight() / 10;
        int w = canvas.getWidth() / 5;
        int height = canvas.getHeight();
        float textH = paint.descent() - paint.ascent();
        for (int j = 0; j < days.size(); j++) {
//            canvas.drawText(days.get(j), w / 2 + j * w, h / 2, paint);
            canvas.drawLine(w / 2 + j * w, 0, w / 2 + j * w, 24 * h + textH, linePaint);
            for (DataBean bean : data) {
                if (TextUtils.equals(bean.getDate(), days.get(j))) {
                    String time = bean.getTime();
                    String hour = time.substring(0, 2);
                    String minute = time.substring(3);
                    int ho = Integer.parseInt(hour);
                    int min = Integer.parseInt(minute);
                    canvas.drawRect(w / 2 + j * w - 60, (ho) * h - textH/2 + h / 60 * min, w / 2 + j * w + 60, (ho) * h  + textH/2 + h / 60 * min, rectPaint);
                    canvas.drawText(time, w / 2 + j * w, (ho) * h  + textH/2 + h / 60 * min-5, textPaint);
                    scrollx = w / 2 + j * w-canvas.getWidth()/2;
                    scrolly = (int) (ho * h + 15 + textH + h / 60 * min) - height / 2;
                }
            }
        }
        if (flag){
            flag = false;
            if (layout != null)
                layout.scroll(scrollx, scrolly);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMinPositionX = 0;
        mMaxPositionX = w / 5 * 2;
        mMinPositionY = 0;
        mMaxPositionY = h / 10 * 15;
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
        if (y < mMinPositionY) {
            y = mMinPositionY;
        }
        if (y > mMaxPositionY) {
            y = mMaxPositionY;
        }
        if (x != getScrollX() || y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    public void setData(List<DataBean> data) {
        this.data = data;
        invalidate();
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
