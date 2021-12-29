package com.zcs.app.camerax.roundimg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.zcs.app.camerax.utils.MeasureUtil;

public class RoundedRectangleImageView extends AppCompatImageView {
    private float mRadius; // dp
    private Path mRoundedRectPath;
    private RectF mRectF;

    public RoundedRectangleImageView(Context context) {
        super(context);
        init(context);
    }

    public RoundedRectangleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoundedRectangleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mRadius = MeasureUtil.dip2px(context, 20);
        mRoundedRectPath = new Path();
        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
        mRoundedRectPath.addRoundRect(mRectF, mRadius, mRadius, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(mRoundedRectPath);
        super.onDraw(canvas);
    }
}