package com.zcs.app.camerax.config

import android.content.Context
import android.util.Size


object CustomCameraConfig {
    const val CAMERA_PHOTO = 0
    const val CAMERA_VIDEO = 1
    const val CAMERA_BOTH = 2

    var mAspectRatio = androidx.camera.core.AspectRatio.RATIO_16_9
    var mBitRate = 1280 * 720 * 2
    var mAudioBitRate = 1024
    var mMaxResolution = Size(1280, 720)
    var mVideoFrameRate = 30
}