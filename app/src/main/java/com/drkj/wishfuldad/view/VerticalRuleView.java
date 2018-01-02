package com.drkj.wishfuldad.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.drkj.wishfuldad.R;

/**
 * Created by ganlong on 2017/12/23.
 */

public class VerticalRuleView extends View {

    private int mWidth;
    private int mHeight;
//    //当前刻度值
    protected float mCurrentScale = 0;

    //最小最大刻度值(以0.1为单位)
    private int mMinScale = 200, mMaxScale = 1200;

    private Paint linePaint;
    private Paint textPaint;
    private float mLastY = 0;
//    //长度、最小可滑动值、最大可滑动值
    protected int mLength, mMinPosition = 0, mMaxPosition = 0;
    //刻度间隔
    private int mInterval = 10;
    //一半宽度
    private int mHalfHeight = 0;
    //速度获取
    protected VelocityTracker mVelocityTracker;
    //惯性最大最小速度
    protected int mMaximumVelocity, mMinimumVelocity;
    //控制滑动
    protected OverScroller mOverScroller;
    private int drawLine,longLine,shortLine,textSize;
    private CallBack callBack;
    public VerticalRuleView(Context context) {
        super(context);
        init();
    }



    public VerticalRuleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VerticalRuleView, 0, 0);
        drawLine = typedArray.getDimensionPixelSize(R.styleable.VerticalRuleView_drawLine, drawLine);
        longLine = typedArray.getDimensionPixelSize(R.styleable.VerticalRuleView_longLine, longLine);
        shortLine = typedArray.getDimensionPixelSize(R.styleable.VerticalRuleView_shortLine, shortLine);
        textSize = typedArray.getDimensionPixelSize(R.styleable.VerticalRuleView_textSize, textSize);
        typedArray.recycle();
    }

    public VerticalRuleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }
    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        linePaint.setColor(Color.RED);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);

        //配置速度
        mVelocityTracker = VelocityTracker.obtain();
        mMaximumVelocity = ViewConfiguration.get(getContext())
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(getContext())
                .getScaledMinimumFlingVelocity();

        mOverScroller = new OverScroller(getContext());
        mInterval = drawLine;

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mWidth = getMeasuredWidth();
//        mHeight = getMeasuredHeight();
    }
    //获取控件宽高，设置相应信息
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLength = (mMaxScale-mMinScale)*mInterval;
        mHalfHeight = h/2;
        mMinPosition = 0;
        mMaxPosition = mLength;
        mWidth = w;
        mHeight = h;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int h = mHeight/60;
        for (float i = mMaxScale; i >= mMinScale; i--){
                if (i % 10 == 0) {
                    canvas.drawLine(mWidth-longLine, (mMaxScale-i)*mInterval+mHalfHeight, mWidth-2, (mMaxScale-i)*mInterval+mHalfHeight, linePaint);
                    canvas.drawText(String.valueOf((int)(i / 10)), 10, (mMaxScale-i)*mInterval+(textPaint.descent()-textPaint.ascent())/2+mHalfHeight, textPaint);
                } else {
                    canvas.drawLine(mWidth-shortLine, (mMaxScale-i)*mInterval+mHalfHeight, mWidth-2, (mMaxScale-i)*mInterval+mHalfHeight, linePaint);
                }
        }
        canvas.drawLine(mWidth-2,mHalfHeight,mWidth-2,(mMaxScale-mMinScale)*mInterval+mHalfHeight,linePaint);
//        for (int i = mMaxScale;i>mMinScale;i--){
//            if (i%10==0){
//                canvas.drawText(i/10+"",0,(mMaxScale-i+30)*h+(textPaint.descent()-textPaint.ascent())/2,textPaint);
//                canvas.drawLine(80,h*(mMaxScale-i+30),mWidth-2,h*(mMaxScale-i+30),linePaint);
//            }else {
//                canvas.drawLine(110,h*(mMaxScale-i+30),mWidth-2,h*(mMaxScale-i+30),linePaint);
//            }
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //开始速度检测
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        float currentY= event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                mLastY = currentY;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = mLastY - currentY;
                mLastY = currentY;
                scrollBy(0, (int) (moveY));
                break;
            case MotionEvent.ACTION_UP:
                //处理松手后的Fling
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                } else {
//                    scrollBackToCurrentScale();
                }
                //VelocityTracker回收
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                //VelocityTracker回收
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }
        return true;
    }
    private void fling(int vY) {
        mOverScroller.fling(0, getScrollY(), 0, vY, 0, 0, mMinPosition, mMaxPosition);
        invalidate();
    }
    //重写滑动方法，设置到边界的时候不滑。滑动完输出刻度
    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (y < mMinPosition) {
//            goStartEdgeEffect(x);
            y = mMinPosition;
        }
        if (y > mMaxPosition) {
//            goEndEdgeEffect(x);
            y = mMaxPosition;
        }
        if (y != getScrollY()) {
            super.scrollTo(0, y);
        }
        mCurrentScale = scrollYtoScale(y);
        if (callBack!=null){
            callBack.onScaleChanging(mCurrentScale);
        }
    }
    //把滑动偏移量scrollY转化为刻度Scale
    private float scrollYtoScale(int scrollY) {
        return 1200-((float) (scrollY - mMinPosition) / mLength) * 1000;
    }
    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }
    public interface CallBack{
        void onScaleChanging(float scale);
    }
    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(mOverScroller.getCurrX(), mOverScroller.getCurrY());

            invalidate();
        }
    }

}
