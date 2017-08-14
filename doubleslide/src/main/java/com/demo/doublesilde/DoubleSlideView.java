package com.demo.doublesilde;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 花歹 on 2017/5/30.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class DoubleSlideView extends View {
    private Paint mWriteLinePaint;
    private Paint mPointPaint;
    private Paint mDigitPaint;
    private Paint mBlueLinePaint;

    private int mLeftText;
    private int mRightText;

    private int mPadding = 100;
    private float mMinWidth;
    private float mMaxWidth;
    private int mLineMaxWidth;
    private int mTotal;
    private int mType = 1;
    private int mMin;
    private int mMax;

    public DoubleSlideView(Context context) {
        super(context);
        init();
    }

    private void init() {
        //1、定义4只画笔
        //1、1 白线条
        mWriteLinePaint = new Paint();
        mWriteLinePaint.setColor(Color.WHITE);
        mWriteLinePaint.setStyle(Paint.Style.FILL);
        mWriteLinePaint.setAntiAlias(true);
        mWriteLinePaint.setDither(true);
        //1、2 蓝线条
        mBlueLinePaint = new Paint();
        mBlueLinePaint.setColor(Color.BLUE);
        mBlueLinePaint.setStyle(Paint.Style.FILL);
        mBlueLinePaint.setAntiAlias(true);
        mBlueLinePaint.setDither(true);
        //1、3 圆点
        mPointPaint = new Paint();
        mPointPaint.setColor(Color.WHITE);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setAntiAlias(true);
        mPointPaint.setDither(true);
        //1、4 数字
        mDigitPaint = new Paint();
        mDigitPaint.setColor(Color.WHITE);
        mDigitPaint.setStyle(Paint.Style.FILL);
        mDigitPaint.setAntiAlias(true);
        mDigitPaint.setDither(true);
        mDigitPaint.setTextSize(36);



        //数字
        mLeftText = 0;
        mRightText = 14;
        mTotal = 14;
    }

    public DoubleSlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleSlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
//        int pointCount = event.getPointerCount();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                if (downX <= mLeftX) {
                    mType = 1;
                    mLeftX = downX;
                } else if (downX >= mRightX) {
                    mType = 2;
                    mRightX = downX;
                } else {
                    mType = 0;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                switch (mType) {
                    case 1:
                        mLeftX = event.getX();
                        break;
                    case 2:
                        mRightX = event.getX();
                        break;
                    default:
                        break;
                }
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mLineMaxWidth = width - 2 * mPadding;
        mMinWidth = mPadding;
        mMaxWidth = width - mPadding;
        mLeftX = textToPositionX(mLeftText, mLineMaxWidth, mTotal);
        mRightX = textToPositionX(mRightText, mLineMaxWidth, mTotal) + (float) mMinWidth;
    }

    private float mLeftX = 0;
    private float mRightX = 0;
//    private float mCurrentTouchX = 0;

    @Override
    protected void onDraw(Canvas canvas) {

        int radius = 10;
//        float leftNum = Float.parseFloat(positionXToText(mLeftX, mLineMaxWidth, mTotal));
//        float RightNum = Float.parseFloat(positionXToText(mRightX, mLineMaxWidth, mTotal));
        int leftNum = Integer.parseInt(positionXToText(mLeftX - mMinWidth, mLineMaxWidth, mTotal));
        int rightNum = Integer.parseInt(positionXToText(mRightX - mMinWidth, mLineMaxWidth, mTotal));
        if (mLeftX < mMinWidth && mType == 1) {
            mLeftX = (float) mMinWidth;
        } else if (mLeftX > (mRightX - radius * 2) && mType == 1) {
            mLeftX = mRightX - radius * 2 - 1;
        }

        if (mRightX > mMaxWidth && mType == 2) {
            mRightX = (float) mMaxWidth;
        } else if (rightNum <= leftNum && mType == 2) {
            rightNum++;
            mRightX = textToPositionX(leftNum + 1, mLineMaxWidth, mTotal) + (float) mMinWidth;
//            Log.i("tag", mRightX + "");
            Log.i("rightx", mRightX + "");
            Log.i("leftx", mLeftX + "");
        }
        int firstCenterX = (int) mLeftX;
        int firstCenterY = getMeasuredHeight() / 2 + radius * 2;

        int secondCenterX = (int) mRightX;
        int secondCenterY = getMeasuredHeight() / 2 + radius * 2;

        //白线
        drawLine(canvas, (int) mMinWidth, firstCenterY, (int) mMaxWidth, firstCenterY, mWriteLinePaint);
        //蓝线
        drawLine(canvas, firstCenterX, firstCenterY, secondCenterX, secondCenterY, mBlueLinePaint);
        //左边的点
        drawPointer(canvas, firstCenterX, firstCenterY, radius, mPointPaint);
        //右边的点
        drawPointer(canvas, secondCenterX, secondCenterY, radius, mPointPaint);
        //左边显示的数字

        String mLeftText = positionXToText((float) (firstCenterX - mMinWidth), mLineMaxWidth, mTotal);
        drawText(canvas, mLeftText, firstCenterX, firstCenterY, radius, mDigitPaint);
        //右边显示的数字
        String mRightText = positionXToText((float) (secondCenterX - mMinWidth), mLineMaxWidth, mTotal);
        drawText(canvas, mRightText, secondCenterX, secondCenterY, radius, mDigitPaint);
    }


    private String positionXToText(float currentX, int maxLength, int total) {
        if (currentX < 0) {
            return "0";
        }
        float every = maxLength * 1f / total;
        int currentCounts = (int) (currentX / every);
        return String.valueOf(currentCounts);

    }

    private float textToPositionX(int size, int maxLength, int total) {
        return maxLength * 1f / total * size;
    }

    private void drawLine(Canvas canvas, int startX, int startY, int endX, int endY, Paint paint) {
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    private void drawPointer(Canvas canvas, int centerX, int centerY, int radius, Paint paint) {
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    private void drawText(Canvas canvas, String text, int startX, int startY, int radius, Paint paint) {
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int origin = startX - textBounds.width() / 2;
        int baseLine = (int) (startY -
                (fontMetrics.leading + fontMetrics.bottom + radius));
        canvas.drawText(text, origin, baseLine, mDigitPaint);
    }


}
