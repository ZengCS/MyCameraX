package com.zcs.app.camerax.utils

import android.graphics.Bitmap
import com.zcs.app.camerax.utils.LogUtil.d
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by ZengCS on 2018/12/21.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
object BitmapUtils {
    /**
     * 保存Bitmap到SDCard
     *
     * @param bitmap
     * @param filePath 图片保存路径
     */
    @JvmStatic
    fun saveBitmap(bitmap: Bitmap?, filePath: String?, quality: Int): Boolean {
        val success: Boolean
        val file = File(filePath)
        if (file.exists()) {
            val delete = file.delete()
            if (delete) {
                d("旧文件已删除")
            }
        }
        val dir = file.parentFile
        if (!dir.exists()) {
            val mkdirs = dir.mkdirs()
            if (!mkdirs) {
                return false
            }
        }
        success = try {
            val out = FileOutputStream(file)
            if (bitmap != null && !bitmap.isRecycled) bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                quality,
                out
            )
            out.flush()
            out.close()
            true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
        return success
    }
}