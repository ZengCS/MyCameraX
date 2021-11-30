package com.zcs.app.camerax.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatImageView

class ScalableImageView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(
    context!!, attrs, defStyle
), OnTouchListener {
    private fun init() {
        setOnTouchListener(this)
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
        private const val SCALE = 0.95f
    }

    init {
        init()
    }
}