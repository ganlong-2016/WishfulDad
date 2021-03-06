package com.drkj.wishfuldad.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.drkj.wishfuldad.R;
import com.drkj.wishfuldad.bean.TibeiDataBean;

import java.util.List;

/**
 * Created by ganlong on 2017/12/20.
 */

public class DataLayout extends ViewGroup implements View.OnTouchListener {

    private DataView dataView;
    Paint paint;
    Bitmap bitmap;
    Bitmap bitmap2;

    private GestureDetector gestureDetector;
    private float mLastX = 0;
    public DataLayout(Context context) {
        super(context);
        initChild(context);
    }

    public DataLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChild(context);
    }

    public DataLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChild(context);
    }

    private void initChild(Context context){
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dataView = new DataView(getContext(),this);
        dataView.setLayoutParams(layoutParams);
        addView(dataView);
        //设置ViewGroup可画
        setWillNotDraw(false);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(getResources().getColor(R.color.line_color));
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_left_button);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_right_button);
        setOnTouchListener(this);
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                mLastX = x;
                if (x>wi/2-w1&&x<wi/2+w1&&y>hi/2-h1&&y<hi/2+h1){
                    dataView.scrollBy(-dataView.getWidth()/6,0);
                }
                if (x>wi*6+wi/2-w2&&x<wi*6+wi/2+w2&&y>hi/2-h2&&y<hi/2+h2){
                    dataView.scrollBy(dataView.getWidth()/6,0);
                }
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                dataView.onDoubleTap(e);
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float currentX = e2.getX();
                float moveX = mLastX - currentX;
                mLastX = currentX;

                if (currentX > wi && currentX < w3 ) {
                    dataView.scrollBy((int) (moveX),0);
                }

                return true;
            }
        });
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        w3 = r - l - wi;
        dataView.layout(wi,0,r-l-wi,b-t);
    }
    int wi;
    int hi;
    int w1;
    int h1;
    int w2;
    int h2;
    int w3;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        w= canvas.getWidth();
//        h = canvas.getHeight();

        float textH = paint.descent()-paint.ascent();
        for (int i=10;i>0;i--){
            canvas.drawText(i+"",wi/2,(10-i)*hi+hi+textH,paint);
        }

        w1 = bitmap.getWidth()/2;
        h1 = bitmap.getHeight()/3*2;
        canvas.drawBitmap(bitmap,wi/2-w1,hi/2-h1,null);


        w2 = bitmap2.getWidth()/2;
        h2 = bitmap2.getHeight()/3*2;
        canvas.drawBitmap(bitmap2,wi*6+wi/2-w2,hi/2-h2,null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        wi = w/7;
        hi = h/11;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                float x = event.getX();
//                float y = event.getY();
//                if (x>wi/2-w1&&x<wi/2+w1&&y>hi/2-h1&&y<hi/2+h1){
//                    dataView.scrollBy(-dataView.getWidth()/6,0);
//                }
//                if (x>wi*6+wi/2-w2&&x<wi*6+wi/2+w2&&y>hi/2-h2&&y<hi/2+h2){
//                    dataView.scrollBy(dataView.getWidth()/6,0);
//                }
//                break;
//        }
//        return true;
//    }

    public void setData(List<TibeiDataBean> list){
        dataView.setData(list);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gestureDetector!=null){
            gestureDetector.onTouchEvent(event);
            return true;
        }
        return false;
    }
}
