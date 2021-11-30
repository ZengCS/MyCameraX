package com.zcs.app.camerax.task;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import com.zcs.app.camerax.CustomApplication;
import com.zcs.app.camerax.utils.BitmapUtils;
import com.zcs.app.camerax.utils.LogUtil;

import java.io.File;
import java.util.HashMap;

/**
 * Created by ZengCS on 2019/7/12.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 * <p>
 * 获取网络视频第一帧异步任务
 */
public class LoadVideoFirstFrameTask extends AsyncTask<String, Void, Bitmap> {
    private static final String cacheDir = CustomApplication.getContext().getCacheDir().getAbsolutePath();
    private Callback mCallback;

    public LoadVideoFirstFrameTask(Callback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params != null && params.length > 0) {
            String url = params[0];
            return getNetVideoBitmap(url);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (mCallback != null)
            mCallback.onPostExecute(bitmap);
    }

    public interface Callback {
        void onPostExecute(Bitmap bitmap);
    }

    private static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            if (videoUrl.startsWith("http://")
                    || videoUrl.startsWith("https://")
                    || videoUrl.startsWith("widevine://")) {
                retriever.setDataSource(videoUrl, new HashMap());
            } else {
                retriever.setDataSource(videoUrl);
            }
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
            if (bitmap != null && !bitmap.isRecycled()) {
                String filePath = cacheDir.concat(File.separator).concat(String.valueOf(videoUrl.hashCode())).concat(".jpg");
                LogUtil.d("filePath = " + filePath);
                BitmapUtils.saveBitmap(bitmap, filePath, 40);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    public static String getCacheFirstFrame(String videoUrl) {
        String filePath = cacheDir.concat(File.separator).concat(String.valueOf(videoUrl.hashCode())).concat(".jpg");
        File file = new File(filePath);
        if (file.exists()) {
            LogUtil.d("存在本地缓存图片：" + filePath);
            return filePath;
        }
        return "";
    }
}
