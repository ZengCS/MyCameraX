package com.zcs.app.camerax;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

public class CustomApplication extends Application {
    private static CustomApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        /**
         * TODO 解决android7.0以上版本传递URI问题
         * @Modify by zzy@sxw.cn on 2018/2/8
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        mApplication = null;
    }

    /**
     * 返回上下文
     *
     * @return
     */
    public static Context getContext() {
        return mApplication;
    }
}
