package com.fenghaha.zscy.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.fenghaha.zscy.R;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Created by FengHaHa on2018/5/25 0025 20:53
 */
public class RedDot extends View {
    public final int RECOMMENDED_RADIUS = dp2px(50);
    private static final String TAG = "RedDot";
    private float mMaxDragLength;
    private float mMinRate;
    private Paint mPaint;
    private Path mPath;
    private Circle mCircleMain;
    private Circle mCircleCopy;

    private boolean dragAble;

    public RedDot(Context context) {
        this(context, null);
    }

    public RedDot(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedDot(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RedDot);
        int circleColor = typedArray.getColor(R.styleable.RedDot_circleColor, Color.RED);
        mMinRate = typedArray.getFloat(R.styleable.RedDot_minRate, 0.2f);
        mMaxDragLength = typedArray.getDimension(R.styleable.RedDot_maxDragLength, dp2px(200));
        dragAble = typedArray.getBoolean(R.styleable.RedDot_dragAble, true);
        typedArray.recycle();

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(circleColor);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public boolean isDragAble() {
        return dragAble;
    }

    public void setDragAble(boolean dragAble) {
        this.dragAble = dragAble;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: widthSize = " + widthSize + "  heightSize = " + heightSize);

        if (widthMode == heightMode && widthMode == MeasureSpec.AT_MOST) {
            widthSize = heightSize = min(min(widthSize, heightSize), RECOMMENDED_RADIUS);
        } else if (widthMeasureSpec == MeasureSpec.AT_MOST) {
            widthSize = heightSize;
        } else if (heightMeasureSpec == MeasureSpec.AT_MOST) {
            heightSize = widthSize;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCircleMain == null) initCircle();
        canvas.drawCircle(mCircleMain.cx, mCircleMain.cy, mCircleMain.radius, mPaint);
        if (dragAble) {
            canvas.drawCircle(mCircleCopy.cx, mCircleCopy.cy, mCircleCopy.radius, mPaint);
            resetPath();
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!dragAble) {
            return super.onTouchEvent(event);
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    float x = event.getX();
                    float y = event.getY();
                    return true;//getDistance(mCircleMain.cx, mCircleMain.cy, x, y) <= mCircleMain.radius;
                case MotionEvent.ACTION_MOVE:
                    mCircleCopy.cx = event.getX();
                    mCircleCopy.cy = event.getY();
                    double rate = getDistance(mCircleMain.cx, mCircleMain.cy,
                            mCircleCopy.cx, mCircleCopy.cy) / mMaxDragLength;
                    if (rate > mMinRate) {
                        if (rate < 1 - mMinRate) {
                            int sum = mCircleCopy.radius + mCircleMain.radius;
                            mCircleCopy.radius = (int) (sum * rate);
                            mCircleMain.radius = sum - mCircleCopy.radius;
                        } else {
                            mCircleMain.radius = (int) ((mCircleCopy.radius + mCircleMain.radius) / (1 + mMinRate));
                            //main.r:copy.r = 1:minRate
                            // 原r = （mCircleCopy.radius + mCircleMain.radius） / (1 + minRate)
                            mCircleMain.cx = mCircleCopy.cx;
                            mCircleMain.cy = mCircleCopy.cy;
                            mCircleCopy.radius = (int) (mCircleMain.radius * mMinRate);
                            //scrollTo((int)mCircleCopy.cx,(int)mCircleCopy.cy);
                        }
                    }
                    invalidate();
                    return true;

                case MotionEvent.ACTION_UP:
                    mCircleMain.radius = (int) ((mCircleCopy.radius + mCircleMain.radius) / (1 + mMinRate));
                    mCircleCopy.cx = mCircleMain.cx;
                    mCircleCopy.cy = mCircleMain.cy;
                    mCircleCopy.radius = (int) (mCircleMain.radius * mMinRate);
                    invalidate();
                    return true;

                default:
                    return false;
            }
        }
    }

    public void setCircle(@ColorInt int color) {
        mPaint.setColor(color);
        invalidate();
    }


    private void resetPath() {
        float dx = mCircleCopy.cx - mCircleMain.cx;
        float dy = mCircleCopy.cy - mCircleMain.cy;
        double do1o2 = getDistance(mCircleCopy.cx, mCircleCopy.cy, mCircleMain.cx, mCircleMain.cy);
        double a1 = atan(dy / dx);
        double a2 = acos(mCircleMain.radius / do1o2);

        double da1 = a2 - a1;
        float x1 = (float) (mCircleMain.cx + mCircleMain.radius * cos(da1));
        float y1 = (float) (mCircleMain.cy - mCircleMain.radius * sin(da1));

        double da2 = a1 + a2 - PI / 2;
        float x2 = (float) (mCircleMain.cx - mCircleMain.radius * sin(da2));
        float y2 = (float) (mCircleMain.cy + mCircleMain.radius * cos(da2));

        float x3 = (float) (mCircleCopy.cx - mCircleCopy.radius * cos(da1));
        float y3 = (float) (mCircleCopy.cy + mCircleCopy.radius * sin(da1));

        float x4 = (float) (mCircleCopy.cx + mCircleCopy.radius * sin(da2));
        float y4 = (float) (mCircleCopy.cy - mCircleCopy.radius * cos(da2));

        float controlX = (mCircleCopy.cx + mCircleMain.cx) / 2;
        float controlY = (mCircleCopy.cy + mCircleMain.cy) / 2;

        mPath.reset();
        mPath.moveTo(x1, y1);
        mPath.lineTo(x2, y2);
        mPath.quadTo(controlX, controlY, x3, y3);
        mPath.lineTo(x4, y4);
        mPath.quadTo(controlX, controlY, x1, y1);
        mPath.close();
    }

    private double getDistance(float x1, float y1, float x2, float y2) {
        return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2));
    }


    private void initCircle() {
        mCircleMain = new Circle(getWidth() / 2, getHeight() / 2, min(getWidth(), getHeight()) / 2);
        mCircleCopy = mCircleMain.clone();
        mCircleCopy.radius = (int) (mMinRate * mCircleMain.radius);
    }

    private int dp2px(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public void setmMinRate(float mMinRate) {
        this.mMinRate = mMinRate;
    }

    public void setmMaxDragLength(float mMaxDragLength) {
        this.mMaxDragLength = mMaxDragLength;
    }

    private class Circle implements Cloneable {
        float cx, cy;
        int radius;

        Circle(float x, float y, int radius) {
            this.cx = x;
            this.cy = y;
            this.radius = radius;
        }

        @Override
        public Circle clone() {
            try {
                return (Circle) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}