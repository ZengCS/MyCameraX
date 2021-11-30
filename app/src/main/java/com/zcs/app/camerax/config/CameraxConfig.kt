package com.zcs.app.camerax.config

import android.util.Size


object CameraxConfig {
    const val CAMERA_PHOTO_ONLY = 0
    const val CAMERA_VIDEO_ONLY = 1
    const val CAMERA_BOTH = 2

    var mAspectRatio = androidx.camera.core.AspectRatio.RATIO_16_9
    var mBitRate = 1280 * 720 * 2
    var mAudioBitRate = 1024
    var mMaxResolution = Size(1280, 720)
    var mVideoFrameRate = 30
    var mCameraType = CAMERA_VIDEO_ONLY
}