package com.zcs.app.camerax.utils

import android.util.Log

object LogUtil {
    private const val TAG = "camerax/d"

    fun i(msg: String) {
        Log.i(TAG, msg)
    }

    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    fun w(msg: String) {
        Log.w(TAG, msg)
    }

    fun e(msg: String) {
        Log.e(TAG, msg)
    }
}