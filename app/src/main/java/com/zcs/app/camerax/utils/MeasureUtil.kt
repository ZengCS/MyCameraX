package com.zcs.app.camerax.utils

import android.content.Context

/**
 * 说明：度量工具类
 */
object MeasureUtil {
    /**
     * 说明：根据手机的分辨率将dp转成为px
     */
    @JvmStatic
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 说明：根据手机的分辨率将sp转成为px
     */
    @JvmStatic
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}