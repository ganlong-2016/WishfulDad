package com.drkj.wishfuldad.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.DataBean;
import com.drkj.wishfuldad.bean.PeeDataBean;

import java.util.List;

/**
 * Created by ganlong on 2017/12/20.
 */

public class PeeRecordLayout extends ViewGroup {
    private PeeRecordView recordView;
    private PeeRecordTimeView timeView;
    private PeeRecordDayView dayView;
    int wi;
    int hi;
    int w1;
    int h1;
    int w2;
    int h2;
    int w3;
    int h3;
    private float mLastX = 0;
    private float mLastY = 0;
    private List<PeeDataBean> list;
    Bitmap bitmap;
    Bitmap bitmap2;
    public PeeRecordLayout(Context context) {
        super(context);
        initChild();
    }

    public PeeRecordLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChild();
    }

    public PeeRecordLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChild();
    }
    private void initChild(){
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        recordView = new PeeRecordView(getContext());
        timeView = new PeeRecordTimeView(getContext());
        dayView = new PeeRecordDayView(getContext());
        recordView.setLayoutParams(layoutParams);
        timeView.setLayoutParams(layoutParams);
        dayView.setLayoutParams(layoutParams);
        addView(dayView);
        addView(recordView);
        addView(timeView);
        //设置ViewGroup可画
        setWillNotDraw(false);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_left_button);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_right_button);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        wi = w/6;
        hi = h/11;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        w3 = r-l-wi;
        h3 = b-t;
        dayView.layout(wi,0,w3,hi);
        timeView.layout(0,hi,wi,h3);
        recordView.layout(wi,hi,w3,h3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        w= canvas.getWidth();
//        h = canvas.getHeight();


        w1 = bitmap.getWidth()/2;
        h1 = bitmap.getHeight()/3*2;
        canvas.drawBitmap(bitmap,wi/2-w1,hi/2-h1,null);


        w2 = bitmap2.getWidth()/2;
        h2 = bitmap2.getHeight()/3*2;
        canvas.drawBitmap(bitmap2,wi*5+wi/2-w2,hi/2-h2,null);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = currentX;
                mLastY = currentY;
                float x = event.getX();
                float y = event.getY();
                if (x>wi/2-w1&&x<wi/2+w1&&y>hi/2-h1&&y<hi/2+h1){
                    recordView.scrollBy(-recordView.getWidth()/5,0);
                }
                if (x>wi*5+wi/2-w2&&x<wi*5+wi/2+w2&&y>hi/2-h2&&y<hi/2+h2){
                    recordView.scrollBy(recordView.getWidth()/5,0);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = mLastX -currentX;
                float moveY = mLastY -currentY;
                mLastX = currentX;
                mLastY = currentY;
                if (currentX>0&&currentX<wi&&currentY>hi){
                    timeView.scrollBy(0,(int)moveY);
                    recordView.scrollBy(0, (int)moveY);
                }
                if (currentX>wi&&currentX<w3&&currentY>hi){
                    dayView.scrollBy((int)(moveX),0);
                    recordView.scrollBy((int)(moveX), (int)moveY);
                    timeView.scrollBy(0,(int)moveY);
                }
                if (currentX>wi&&currentX<w3&&currentY<hi){
                    dayView.scrollBy((int)(moveX),0);
                    recordView.scrollBy((int)(moveX),0);
                }

                break;
        }
        return true;
    }

    public void setData(List<DataBean> list){
        recordView.setData(list);
    }

    public void setDays(List<String> days){
        dayView.setDays(days);
        recordView.setDays(days);
    }

}
