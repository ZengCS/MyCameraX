package com.zcs.app.camerax

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.VmPolicy

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        mApplication = this
        /**
         * TODO 解决android7.0以上版本传递URI问题
         * @Modify by zzy@sxw.cn on 2018/2/8
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }
    }

    /**
     * 程序终止的时候执行
     */
    override fun onTerminate() {
        super.onTerminate()
        mApplication = null
    }

    companion object {
        private var mApplication: CustomApplication? = null

        /**
         * 返回上下文
         *
         * @return
         */
        val context: Context
            get() = mApplication!!
    }
}