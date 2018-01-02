package com.drkj.wishfuldad.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.drkj.wishfuldad.R;

/**
 * Created by yuhongmiao on 2017/4/24.
 */

public class ArcScaleView extends View {

    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;
    private int radius;
    private Path mArcPath;

    private Paint mScaleLinePaint;//刻度线画笔
    private Paint mScaleTextPaint;//刻度线画笔
    private Paint mIndicatorPaint;//刻度线画笔
    private Paint mSelectedScaleTextPaint;//刻度线画笔

    private double initAngle;
    public ArcScaleView(Context context) {
        super(context);
        init();
    }

    public ArcScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleTextPaint.setTextSize(30);
        mIndicatorPaint = new Paint();
        mSelectedScaleTextPaint = new Paint();
        mSelectedScaleTextPaint.setTextSize(50);
        mSelectedScaleTextPaint.setTextAlign(Paint.Align.CENTER);
//        mIndicatorPaint.setColor(Color.RED);
        mArcPath = new Path();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();

        mHeight = getMeasuredHeight();

        mCenterX = mWidth / 2;

        mCenterY = mHeight;

        radius = Math.min(mWidth/2, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint1 = new Paint();
        paint1.setColor(getResources().getColor(R.color.toolbar_color));
//        canvas.drawArc(new RectF(mWidth/2-radius, mHeight-radius, mWidth/2+radius, mHeight+radius), 180, 180, false, paint1);


        mArcPath.reset();
        mArcPath.arcTo(new RectF(mWidth/2-radius, mHeight-radius, mWidth/2+radius, mHeight+radius), 180, 180, false);
        canvas.drawPath(mArcPath,paint1);
        Paint paint2 = new Paint();
        paint2.setColor(getResources().getColor(R.color.white));
        canvas.drawArc(new RectF(mWidth/2-radius/2, mHeight-radius/2, mWidth/2+radius/2, mHeight+radius/2), 180, 180, false, paint2);
        drawScale(canvas);
        drawIndicator(canvas);
        drawSelectedScale(canvas);
    }

    double currentAngle;

    private void drawScale(Canvas canvas) {
//        Paint mScaleLinePaint = new Paint();
        PathMeasure mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mArcPath, false);
        float[] pos = new float[2];
        float[] tan = new float[2];
        for (int i = 1; i <= 120; i++) {
            int scale = 19;
            float percentage = i / (float) 120;//所占百分比
            mPathMeasure.getPosTan(mPathMeasure.getLength() * percentage, pos, tan);
            double atan2 = Math.atan2(tan[1], tan[0]);
            double angle = calcArcAngle(atan2) + 90;//刻度线垂直切线，所以旋转90°
            float startX = pos[0];
            float startY = pos[1];
                float endX = pos[0];
                float endY = pos[1];
                if ((scale+i)%10==0){
                    endX = pos[0]+80;
                        canvas.save();
                        canvas.rotate((float) (angle + 270), pos[0], pos[1]);
                        String mScaleText = (scale+i)/10 + "Kg";
                        float scaleTextLength = mScaleTextPaint.measureText(mScaleText, 0, mScaleText.length());
                        canvas.drawText(mScaleText, pos[0] - scaleTextLength/2, pos[1] + 130, mScaleTextPaint);
                        canvas.restore();
                }else {
                    endX = pos[0]+50;
                }

//
//            if (scale >= 2 && scale % 10 == 0) {
//                float startX = pos[0];
//                float startY = pos[1];
//                float endX = pos[0];
//                float endY = pos[1];
//                if (scale % 10 == 0) {
//                    endX = pos[0] + 80;
////                    mScaleLinePaint.setStrokeWidth(10);
////                    mScaleLinePaint.setColor(getResources().getColor(R.color.black));
//                    if (currentAngle >= (-(120 / 2) * 1)) {
//                        canvas.save();
//                        canvas.rotate((float) (angle + 270), pos[0], pos[1]);
//                        String mScaleText = scale + "Kg";
//                        float scaleTextLength = mScaleTextPaint.measureText(mScaleText, 0, mScaleText.length());
//                        canvas.drawText(mScaleText, pos[0] - scaleTextLength / 2, pos[1] - 130, mScaleTextPaint);
//                        canvas.restore();
//                    }
//                } else {
////                    mScaleLinePaint.setColor(getResources().getColor(R.color.black));
////                    mScaleLinePaint.setStrokeWidth(5);
//                    endX = pos[0] + 50;
//                }

                canvas.save();
                canvas.rotate((float) angle, pos[0], pos[1]);
                canvas.drawLine(startX, startY, endX, endY, mScaleLinePaint);
                canvas.restore();

//            }
        }
    }
    /**
     * 画指示器
     *
     * @param canvas
     */
    private void drawIndicator(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_zhi);
        canvas.drawBitmap(bitmap,mWidth/2-bitmap.getWidth()/2,mHeight-radius/2-bitmap.getHeight(),mIndicatorPaint);
        canvas.drawLine(mWidth/2,mHeight-radius/2,mWidth/2,mHeight-radius/2-80,mIndicatorPaint);
    }
    /**
     * 画选中的刻度值
     *
     * @param canvas
     */
    private void drawSelectedScale(Canvas canvas) {
        canvas.drawText("5Kg",mWidth/2,mHeight-radius/8,mSelectedScaleTextPaint);
//        int selectedScale = Math.round(currentAngle + mScaleMin + (mScaleNumber / 2) * mScaleSpace);
//        if (selectScaleListener != null) {
//            selectScaleListener.selectScale(selectedScale);
//        }
//        String selectedScaleText = selectedScale + mScaleUnit;
//        float selectedScaleTextLength = mSelectedTextPaint.measureText(selectedScaleText, 0, selectedScaleText.length());
//        canvas.drawText(selectedScaleText, mCenterX - selectedScaleTextLength / 2, mCenterY + 100, mSelectedTextPaint);
    }
    private double calcArcAngle(double arc) {
        double angle = arc * 180.0 / Math.PI;
        return angle;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                float mDownX = event.getX();

                float mDownY = event.getY();

                initAngle = computeAngle(mDownX, mDownY);
                if (mDownX > mCenterX) {
                    initAngle = 180 - initAngle;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float mTouchX = event.getX();
                float mTouchY = event.getY();
                if (isTouch(mTouchX, mTouchY)) {
                    double moveAngle = computeAngle(mTouchX, mTouchY);
                    if (mTouchX > mCenterX) {
                        moveAngle = 180 - moveAngle;
                    }
                    double tempAngle = moveAngle - initAngle;
                    long addValue = Math.round(tempAngle );
                    currentAngle += addValue;
                    if (currentAngle >= -(120 / 2) * 1) {
                        invalidate();

                    } else {
                        currentAngle = currentAngle - addValue;

                    }
                    initAngle = moveAngle;
                    return true;
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 计算角度
     *
     * @param touchX
     * @param touchY
     * @retuen 旋转角度
     */
    private double computeAngle(float touchX, float touchY) {
        double atan2 = Math.atan2(touchY, touchX);
        double taperedEdge = Math.sqrt(Math.pow(touchX - mCenterX, 2) + Math.pow(touchY - mCenterY, 2));//计算斜边
        double sin = (touchY - mCenterY) / taperedEdge;
        double asin = Math.asin(sin);
        double calcArcAngle = calcArcAngle(asin);
        return calcArcAngle;
    }

    /**
     * 是否在触摸范围内
     *
     * @param touchX 触摸点x坐标
     * @param touchY 触摸点y坐标
     */
    private boolean isTouch(float touchX, float touchY) {
        float x = touchX - mCenterX;
        float y = touchY - mCenterY;
        return Math.pow(x, 2) + Math.pow(y, 2) < Math.pow(radius, 2);
    }

}