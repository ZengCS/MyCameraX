package com.zcs.app.camerax.task

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.AsyncTask
import com.zcs.app.camerax.CustomApplication
import com.zcs.app.camerax.utils.BitmapUtils.saveBitmap
import com.zcs.app.camerax.utils.LogUtil
import java.io.File
import java.util.*

/**
 * Created by ZengCS on 2019/7/12.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 *
 *
 * 获取网络视频第一帧异步任务
 */
class LoadVideoFirstFrameTask(private val mCallback: Callback?) :
    AsyncTask<String, Void, Bitmap?>() {
    override fun doInBackground(vararg params: String): Bitmap? {
        if (params.isNotEmpty()) {
            val url = params[0]
            return getNetVideoBitmap(url)
        }
        return null
    }

    override fun onPostExecute(bitmap: Bitmap?) {
        mCallback?.onPostExecute(bitmap)
    }

    interface Callback {
        fun onPostExecute(bitmap: Bitmap?)
    }

    companion object {
        private val cacheDir: String =
            CustomApplication.context.cacheDir.absolutePath

        private fun getNetVideoBitmap(videoUrl: String): Bitmap? {
            var bitmap: Bitmap? = null
            val retriever = MediaMetadataRetriever()
            try {
                //根据url获取缩略图
                if (videoUrl.startsWith("http://")
                    || videoUrl.startsWith("https://")
                    || videoUrl.startsWith("widevine://")
                ) {
                    retriever.setDataSource(videoUrl, HashMap())
                } else {
                    retriever.setDataSource(videoUrl)
                }
                //获得第一帧图片
                bitmap = retriever.frameAtTime
                if (bitmap != null && !bitmap.isRecycled) {
                    val filePath =
                        cacheDir + File.separator + videoUrl.hashCode().toString() + ".jpg"
                    LogUtil.d("filePath = $filePath")
                    saveBitmap(bitmap, filePath, 40)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                retriever.release()
            }
            return bitmap
        }

        fun getCacheFirstFrame(videoUrl: String): String {
            val filePath = cacheDir + File.separator + videoUrl.hashCode().toString() + ".jpg"
            val file = File(filePath)
            if (file.exists()) {
                LogUtil.d("存在本地缓存图片：$filePath")
                return filePath
            }
            return ""
        }
    }
}