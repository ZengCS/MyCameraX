package com.zcs.app.camerax.roundimg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.zcs.app.camerax.R;
import com.zcs.app.camerax.utils.LogUtil;

public class CustomRoundedImageView extends AppCompatImageView {
    private static final int DEFAULT_RADIUS = 0;
    private float ltRadius;
    private float rtRadius;
    private float rbRadius;
    private float lbRadius;
    // 默认不是圆形
    private boolean isCircle = false;

    float width, height;
    private Path mPath;

    public CustomRoundedImageView(Context context) {
        this(context, null);
        init(context, null);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPath = new Path();
        // 读取配置
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomRoundedImageView);
        float radius = ta.getDimensionPixelOffset(R.styleable.CustomRoundedImageView_radius, DEFAULT_RADIUS);
        ltRadius = ta.getDimensionPixelOffset(R.styleable.CustomRoundedImageView_left_top_radius, DEFAULT_RADIUS);
        rtRadius = ta.getDimensionPixelOffset(R.styleable.CustomRoundedImageView_right_top_radius, DEFAULT_RADIUS);
        rbRadius = ta.getDimensionPixelOffset(R.styleable.CustomRoundedImageView_right_bottom_radius, DEFAULT_RADIUS);
        lbRadius = ta.getDimensionPixelOffset(R.styleable.CustomRoundedImageView_left_bottom_radius, DEFAULT_RADIUS);
        isCircle = ta.getBoolean(R.styleable.CustomRoundedImageView_isCircle, false);

        // 如果四个角的值没有设置，那么就使用通用的radius的值。
        if (DEFAULT_RADIUS == ltRadius) {
            ltRadius = radius;
        }
        if (DEFAULT_RADIUS == rtRadius) {
            rtRadius = radius;
        }
        if (DEFAULT_RADIUS == rbRadius) {
            rbRadius = radius;
        }
        if (DEFAULT_RADIUS == lbRadius) {
            lbRadius = radius;
        }
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        LogUtil.d("onMeasure() called with: width = [" + width + "], height = [" + height + "]");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //这里做下判断，只有图片的宽高大于设置的圆角距离的时候才进行裁剪
        float maxLeft = Math.max(ltRadius, lbRadius);
        float maxRight = Math.max(rtRadius, rbRadius);
        float minWidth = maxLeft + maxRight;
        float maxTop = Math.max(ltRadius, rtRadius);
        float maxBottom = Math.max(lbRadius, rbRadius);
        float minHeight = maxTop + maxBottom;
        if (width >= minWidth && height > minHeight) {
            //四个角：右上，右下，左下，左上
            mPath.moveTo(ltRadius, 0);
            mPath.lineTo(width - rtRadius, 0);
            // 使用贝塞尔曲线解决圆角图表毛边问题
            mPath.quadTo(width, 0, width, rtRadius);

            mPath.lineTo(width, height - rbRadius);
            mPath.quadTo(width, height, width - rbRadius, height);

            mPath.lineTo(lbRadius, height);
            mPath.quadTo(0, height, 0, height - lbRadius);

            mPath.lineTo(0, ltRadius);
            mPath.quadTo(0, 0, ltRadius, 0);

            canvas.clipPath(mPath);
        }
        super.onDraw(canvas);
    }
}