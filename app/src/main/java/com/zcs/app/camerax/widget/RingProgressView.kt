package com.zcs.app.camerax.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.zcs.app.camerax.utils.MeasureUtil.dip2px

/**
 * 圆环进度条
 */
class RingProgressView(context: Context?, attrs: AttributeSet?) : View(context, attrs),
    OnTouchListener {
    private var mProgress = 0f // 0-1 浮点数

    // 画圆所在的距形区域
    private val mRectF: RectF = RectF()
    private val mProgressRectF: RectF = RectF()
    private val mPaint: Paint = Paint()
    private val mCirclePaint: Paint = Paint()
    private var mRadius = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = measuredWidth
        var height = measuredHeight
        if (width != height) {
            val min = Math.min(width, height)
            width = min
            height = min
        }
        // 位置
        mRectF.left = 0f // 左上角x
        mRectF.top = 0f // 左上角y
        mRectF.right = width.toFloat() // 左下角x
        mRectF.bottom = height.toFloat() // 右下角y
        mRadius = width / 2
        val widthOffset = dip2px(context, CIRCLE_LINE_STROKE_WIDTH) / 2
        mProgressRectF.left = mRectF.left + widthOffset
        mProgressRectF.top = mRectF.top + widthOffset
        mProgressRectF.right = mRectF.right - widthOffset
        mProgressRectF.bottom = mRectF.bottom - widthOffset
    }

    private fun init() {
        setOnTouchListener(this)

        // 设置画笔相关属性
        mPaint.isAntiAlias = true
        mPaint.strokeWidth =
            dip2px(context, CIRCLE_LINE_STROKE_WIDTH).toFloat()
        mPaint.style = Paint.Style.STROKE // 空心
        mPaint.color = COLOR_PROGRESS

        // 设置画笔相关属性
        mCirclePaint.isAntiAlias = true
        mCirclePaint.style = Paint.Style.FILL
        mCirclePaint.color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制背景
        canvas.drawColor(Color.TRANSPARENT)
        // 绘制外圆
        canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), mRadius.toFloat(), mCirclePaint)

        // 绘制进度
        if (mProgress < 0 || mProgress > 1) return
        val startAngle = -90f
        val sweepAngle = mProgress * 360
        canvas.drawArc(mProgressRectF, startAngle, sweepAngle, false, mPaint)
    }

    fun setProgress(progress: Float) {
        mProgress = progress
        this.invalidate()
    }

    fun setProgressNotInUiThread(progress: Int) {
        mProgress = progress.toFloat()
        this.postInvalidate()
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return if (isClickable && isFocusable) {
            if (isClickable && isFocusable) {
                if (event.action == MotionEvent.ACTION_DOWN) { // 按下时,图片缩放
                    scaleX = SCALE
                    scaleY = SCALE
                } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL || event.actionMasked == MotionEvent.ACTION_CANCEL) {
                    scaleX = 1f
                    scaleY = 1f
                }
            }
            false
        } else {
            super.onTouchEvent(event)
        }
    }

    companion object {
        private const val CIRCLE_LINE_STROKE_WIDTH = 6f
        private const val COLOR_PROGRESS = -0x11004f51
        private const val SCALE = 0.95f
    }

    init {
        init()
    }
}