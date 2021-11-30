package com.zcs.app.camerax.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ZengCS on 2018/12/21.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class BitmapUtils {
    /**
     * 保存Bitmap到SDCard
     *
     * @param bitmap
     * @param filePath 图片保存路径
     */
    public static boolean saveBitmap(Bitmap bitmap, String filePath, int quality) {
        boolean success;
        File file = new File(filePath);
        if (file.exists()) {
            boolean delete = file.delete();
            if (delete) {
                LogUtil.d("旧文件已删除");
            }
        }
        File dir = file.getParentFile();
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                return false;
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap != null && !bitmap.isRecycled())
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            success = false;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
}
