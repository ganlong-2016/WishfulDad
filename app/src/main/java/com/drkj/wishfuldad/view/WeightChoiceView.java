package com.drkj.wishfuldad.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.drkj.wishfuldad.R;

/**
 * Created by ganlong on 2017/12/22.
 */

public class WeightChoiceView extends View {
    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;
    private int radius;

    private float currentAngle = 0;
    private double initAngle;
    private Path mArcPath;
    private Paint mArcPaint;// 弧线画笔
    private Paint mArcPaint2;// 弧线画笔2
    private Paint mScaleLinePaint;//刻度线画笔
    private TextPaint mScaleTextPaint;//刻度值画笔
    private TextPaint mSelectedTextPaint;//选中刻度线画笔
    private Paint mIndicatorPaint;//指针画笔
    private Paint mMaskPaint;//画遮罩层画笔
    private SelectScaleListener selectScaleListener;//选择器监听
    private String mScaleUnit = "单位";//刻度单位
    private float mEvenyScaleValue = 1;//每滑动多少度,值得增加
    private int mScaleNumber = 30;//刻度数量
    private int mScaleSpace = 1;//刻度间距
    private int mScaleMin = 20;//刻度最小值
    private int mScaleMax = 201;//刻度最大值
    private int mDrawLineSpace = 1;//画线间距
    private int mDrawTextSpace = 5;//画刻度值的间隔

    private int mArcLineColor = Color.RED;//圆1颜色
    private int mArcLineColor2 = Color.RED;//圆2颜色
    private int mScaleLineColor = Color.BLUE;//刻度线颜色
    private int mIndicatorColor = Color.GREEN;
    private int mScaleTextColor = Color.BLACK;//刻度文字颜色
    private int mSelectTextColor = Color.BLACK;//选中刻度文字颜色

    public WeightChoiceView(Context context) {
        this(context, null);

    }

    public WeightChoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        initAttr(context, attrs);
        initPath();
        initPaint();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcScaleView);
        mScaleUnit = typedArray.getString(R.styleable.ArcScaleView_scaleUnit);
        if (mScaleUnit == null) {
            mScaleUnit = "";
        }
        mEvenyScaleValue = typedArray.getFloat(R.styleable.ArcScaleView_everyScaleValue, 1);
        mScaleNumber = typedArray.getInt(R.styleable.ArcScaleView_scaleNum, 30);
        mScaleSpace = typedArray.getInt(R.styleable.ArcScaleView_scaleSpace, 1);
        mScaleMin = typedArray.getInt(R.styleable.ArcScaleView_scaleMin, 30);
        mScaleMax = typedArray.getInt(R.styleable.ArcScaleView_scaleMax, 30);
        mDrawLineSpace = typedArray.getInt(R.styleable.ArcScaleView_drawLineSpace, 1);
        mDrawTextSpace = typedArray.getInt(R.styleable.ArcScaleView_drawTextSpace, 5);

        mArcLineColor = typedArray.getColor(R.styleable.ArcScaleView_arcLineColor, Color.RED);
        mArcLineColor2 = typedArray.getColor(R.styleable.ArcScaleView_arcLineColor2, Color.RED);
        mScaleLineColor = typedArray.getColor(R.styleable.ArcScaleView_scaleLineColor, Color.RED);
        mIndicatorColor = typedArray.getColor(R.styleable.ArcScaleView_indicatorColor, Color.GREEN);
        mScaleTextColor = typedArray.getColor(R.styleable.ArcScaleView_scaleTextColor, Color.BLACK);
        mSelectTextColor = typedArray.getColor(R.styleable.ArcScaleView_selectTextColor, Color.BLACK);
    }

    private void initPaint() {
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(mArcLineColor);

        mArcPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint2.setColor(mArcLineColor2);

        mScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleLinePaint.setColor(mScaleLineColor);
        mScaleLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mScaleLinePaint.setStyle(Paint.Style.STROKE);

        mScaleTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mScaleTextPaint.setColor(mScaleTextColor);
        mScaleTextPaint.setTypeface(Typeface.SANS_SERIF);
        mScaleTextPaint.setTextSize(30);

        mSelectedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        mSelectedTextPaint.setColor(mSelectTextColor);
        mSelectedTextPaint.setTextSize(60);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorPaint.setStrokeWidth(5);

        mMaskPaint = new Paint();
        mMaskPaint.setFlags(Paint.ANTI_ALIAS_FLAG);


    }

    private void initPath() {
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
        drawArc(canvas);
        canvas.drawArc(new RectF(mWidth/2-radius/2, mHeight-radius/2, mWidth/2+radius/2, mHeight+radius/2), 180, 180, false, mArcPaint2);
        drawScale(canvas);
        drawSelectedScale(canvas);
        drawIndicator(canvas);

    }


    /**
     * 画指示器
     *
     * @param canvas
     */
    private void drawIndicator(Canvas canvas) {

        Path linePath = new Path();
        linePath.moveTo(mWidth/2 + 3, mHeight-radius/2);
        linePath.lineTo(mWidth/2, mHeight-radius+10);
        linePath.lineTo(mWidth/2 - 3, mHeight-radius/2);
        canvas.drawPath(linePath, mIndicatorPaint);

    }

    /**
     * 画选中的刻度值
     *
     * @param canvas
     */
    private void drawSelectedScale(Canvas canvas) {
        double selectedScale = Math.round(currentAngle + mScaleMin + (mScaleNumber / 2) * mScaleSpace)/10.0;
        if (selectScaleListener != null) {
            selectScaleListener.selectScale(selectedScale);
        }
        String selectedScaleText = selectedScale + mScaleUnit;
        float selectedScaleTextLength = mSelectedTextPaint.measureText(selectedScaleText, 0, selectedScaleText.length());
        canvas.drawText(selectedScaleText, mCenterX - selectedScaleTextLength / 2, mHeight - 50, mSelectedTextPaint);
    }

    /**
     * 画刻度线和刻度
     *
     * @param canvas
     */
    private void drawScale(Canvas canvas) {
        PathMeasure mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mArcPath, false);
        float[] pos = new float[2];
        float[] tan = new float[2];
        for (int i = 1; i <= mScaleNumber; i++) {
            float percentage = i / (float) mScaleNumber;//所占百分比
            mPathMeasure.getPosTan(mPathMeasure.getLength() * percentage, pos, tan);
            double atan2 = Math.atan2(tan[1], tan[0]);
            double angle = calcArcAngle(atan2) + 90;//刻度线垂直切线，所以旋转90°

            int scale = Math.round(currentAngle + mScaleMin + i * mScaleSpace);

            if (scale >= mScaleMin && scale % mDrawLineSpace == 0&&scale<mScaleMax) {
                float startX = pos[0];
                float startY = pos[1];
                float endX = 0;
                float endY = pos[1];
                if (scale % mDrawTextSpace == 0) {
                    endX = pos[0] + 80;
//                    mScaleLinePaint.setStrokeWidth(15);
                    mScaleLinePaint.setColor(mScaleLineColor);
                    if (currentAngle >= (-(mScaleNumber / 2) * mScaleSpace)) {
                        canvas.save();
                        canvas.rotate((float) (angle + 270), pos[0], pos[1]);
                        String mScaleText = scale/10+"";
                        float scaleTextLength = mScaleTextPaint.measureText(mScaleText, 0, mScaleText.length());
                        canvas.drawText(mScaleText, pos[0] - scaleTextLength / 2, pos[1] + 120, mScaleTextPaint);
                        canvas.restore();
                    }
                } else if (scale % mDrawLineSpace == 0) {
                    mScaleLinePaint.setColor(mScaleTextColor);
//                    mScaleLinePaint.setStrokeWidth(10);
                    endX = pos[0] + 50;
                }

                canvas.save();
                canvas.rotate((float) angle, pos[0], pos[1]);
                canvas.drawLine(startX, startY, endX, endY, mScaleLinePaint);
                canvas.restore();

            }
        }
    }

    /**
     * 画半圆
     *
     * @param canvas
     */
    private void drawArc(Canvas canvas) {
        int top =mHeight-radius;
        int bottom = mHeight+radius;
        int left = mWidth/2-radius;
        int right = mWidth/2+radius;
        mArcPath.reset();

        mArcPath.addArc(new RectF(left, top, right, bottom), 180, 180);

        canvas.drawPath(mArcPath, mArcPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                float mDownX = event.getX();

                float mDownY = event.getY();

                initAngle = computeAngle(mDownX, mDownY);
                if (mDownX > mCenterX) {
                    initAngle = 180-initAngle ;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float mTouchX = event.getX();
                float mTouchY = event.getY();
                if (isTouch(mTouchX, mTouchY)) {
                    double moveAngle = computeAngle(mTouchX, mTouchY);
                    if (mTouchX > mCenterX) {
                        moveAngle = 180-moveAngle  ;
                    }
                    double tempAngle = initAngle - moveAngle ;
                    long addValue = Math.round(tempAngle * mEvenyScaleValue);
                    currentAngle -= addValue;
                    if (currentAngle >= -(mScaleNumber / 2) * mScaleSpace&&currentAngle<(mScaleMax-mScaleMin)*mScaleSpace-(mScaleNumber / 2) * mScaleSpace ) {
                        invalidate();
                    } else {
                        currentAngle = currentAngle + addValue;

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

        double taperedEdge = Math.sqrt(Math.pow(touchX - mCenterX, 2) + Math.pow(touchY - mCenterY, 2));//计算斜边
        double sin = (touchY - mCenterY) / taperedEdge;
        double asin = Math.asin(sin);
        double calcArcAngle = calcArcAngle(asin);
        return calcArcAngle;
    }

    private double calcArcAngle(double arc) {
        double angle = arc * 180.0 / Math.PI;
        return angle;
    }

    /**
     * 是否在触摸范围内
     *
     * @param touchX 触摸点x坐标
     * @param touchY 触摸点y坐标
     */
    private boolean isTouch(float touchX, float touchY) {
        float x = touchX - mCenterX;
        float y = touchY - mHeight;
        return Math.pow(x, 2) + Math.pow(y, 2) < Math.pow(radius, 2);
    }

    /**
     * 选择器监听
     */
    public interface SelectScaleListener {
        void selectScale(double value);
    }

    public void setSelectScaleListener(SelectScaleListener listener) {
        this.selectScaleListener = listener;
    }


}
