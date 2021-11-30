package com.zcs.app.camerax.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zcs.app.camerax.utils.MeasureUtil;

/**
 * 圆环进度条
 */
public class RingProgressView extends View implements View.OnTouchListener {
    private float mProgress = 0;// 0-1 浮点数
    private static final float CIRCLE_LINE_STROKE_WIDTH = 6;
    private static final int COLOR_PROGRESS = 0xEEFFB0AF;

    // 画圆所在的距形区域
    private final RectF mRectF, mProgressRectF;
    private final Paint mPaint, mCirclePaint;
    private int mRadius = 0;

    public RingProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRectF = new RectF();
        mProgressRectF = new RectF();
        mPaint = new Paint();
        mCirclePaint = new Paint();

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }
        // 位置
        mRectF.left = 0;// 左上角x
        mRectF.top = 0;// 左上角y
        mRectF.right = width; // 左下角x
        mRectF.bottom = height; // 右下角y
        mRadius = width / 2;

        int widthOffset = MeasureUtil.dip2px(getContext(), CIRCLE_LINE_STROKE_WIDTH) / 2;
        mProgressRectF.left = mRectF.left + widthOffset;
        mProgressRectF.top = mRectF.top + widthOffset;
        mProgressRectF.right = mRectF.right - widthOffset;
        mProgressRectF.bottom = mRectF.bottom - widthOffset;
    }

    private void init() {
        setOnTouchListener(this);

        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(MeasureUtil.dip2px(getContext(), CIRCLE_LINE_STROKE_WIDTH));
        mPaint.setStyle(Paint.Style.STROKE);// 空心
        mPaint.setColor(COLOR_PROGRESS);

        // 设置画笔相关属性
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        canvas.drawColor(Color.TRANSPARENT);
        // 绘制外圆
        canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), mRadius, mCirclePaint);

        // 绘制进度
        if (mProgress < 0 || mProgress > 1) return;

        float startAngle = -90;
        float sweepAngle = mProgress * 360;
        canvas.drawArc(mProgressRectF, startAngle, sweepAngle, false, mPaint);
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        this.invalidate();
    }

    public void setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }

    private static final float SCALE = 0.95f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isClickable() && isFocusable()) {
            if (isClickable() && isFocusable()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下时,图片缩放
                    setScaleX(SCALE);
                    setScaleY(SCALE);
                } else if (event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_CANCEL
                        || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
                    setScaleX(1f);
                    setScaleY(1f);
                }
            }
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }
}