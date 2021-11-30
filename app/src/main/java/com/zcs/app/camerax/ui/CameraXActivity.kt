package com.zcs.app.camerax.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.safframework.delegate.extras.extraDelegate
import com.zcs.app.camerax.BuildConfig
import com.zcs.app.camerax.base.BaseActivity
import com.zcs.app.camerax.config.CustomCameraConfig
import com.zcs.app.camerax.databinding.ActivityCameraXBinding
import com.zcs.app.camerax.utils.LogUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("RestrictedApi")
class CameraXActivity : BaseActivity() {
    val binding: ActivityCameraXBinding by lazy {
        ActivityCameraXBinding.inflate(layoutInflater)
    }

    val mType: Int by extraDelegate("type", CustomCameraConfig.CAMERA_PHOTO)

    // 是否需要倒计时
    private val withTimeout: Boolean = true

    // 最长录制时长:3分钟
    private val maxTime: Float by extraDelegate("maxTime", 3 * 60 * 1000F)

    private val imageCapture by lazy(LazyThreadSafetyMode.NONE) {
        ImageCapture.Builder()
            .setTargetAspectRatio(CustomCameraConfig.mAspectRatio)
            // .setTargetRotation(binding.surfacePreview.display.rotation)
            .build()
    }

    private val videoCapture by lazy(LazyThreadSafetyMode.NONE) {
        VideoCapture.Builder()//录像用例配置
            .setBitRate(CustomCameraConfig.mBitRate)
//                .setAudioBitRate(CameraxConfig.mAudioBitRate)
//                .setVideoFrameRate(CameraxConfig.mVideoFrameRate)
            .setMaxResolution(CustomCameraConfig.mMaxResolution)
            // .setTargetResolution(Size(1280, 720))
            // .setTargetAspectRatio(CameraxConfig.mAspectRatio) //设置高宽比 不能和setTargetResolution共存
            // .setTargetRotation(binding.surfacePreview.display.rotation)//设置旋转角度
            .build()
    }

    private val preview by lazy(LazyThreadSafetyMode.NONE) {
        Preview.Builder()
            .setTargetAspectRatio(CustomCameraConfig.mAspectRatio)
            .build()
    }

    private var cameraProvider: ProcessCameraProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initView()
        startCamera()
    }

    private fun initView() {
        when (mType) {
            CustomCameraConfig.CAMERA_PHOTO -> {
                binding.takePhoto.visibility = View.VISIBLE
                binding.takeVideo.visibility = View.INVISIBLE
                binding.ivVideoStatus.visibility = View.INVISIBLE
            }
            CustomCameraConfig.CAMERA_VIDEO -> {
                binding.takePhoto.visibility = View.INVISIBLE
                binding.takeVideo.visibility = View.VISIBLE
                binding.ivVideoStatus.visibility = View.VISIBLE
            }
        }
    }

    // 默认使用后置摄像头
    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA//当前相机

    /**
     * 打开相机
     */
    private fun startCamera() {
        if (cameraProvider == null) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                cameraProvider = cameraProviderFuture.get()
                startPreview()
            }, ContextCompat.getMainExecutor(this))
        } else {
            startPreview()
        }
    }

    /**
     * 开启预览
     */
    private fun startPreview() {
        try {
            // 解除相机之前的所有绑定
            cameraProvider?.unbindAll()
            // 绑定前面用于预览和拍照的UseCase到相机上
            // camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            when (mType) {
                CustomCameraConfig.CAMERA_PHOTO -> {
                    cameraProvider?.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                }
                CustomCameraConfig.CAMERA_VIDEO -> {
                    cameraProvider?.bindToLifecycle(this, cameraSelector, preview, videoCapture)
                }
                CustomCameraConfig.CAMERA_BOTH -> {
                    cameraProvider?.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture,
                        videoCapture
                    )
                }
            }
            // 设置用于预览的view
            preview.setSurfaceProvider(binding.surfacePreview.surfaceProvider)
        } catch (exc: Exception) {
            exc.printStackTrace()
            showMessage("相机启动失败，${exc.message}")
        }
    }

    fun switchCamera(v: View) {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startPreview()
    }

    /**
     * 拍照
     */
    fun takePhoto(v: View) {
        // 拍照保存路径
        val imagePath = "${externalCacheDir?.absolutePath}/Pic_${System.currentTimeMillis()}.jpg"
        val file = File(imagePath)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        ImageCapture.OutputFileOptions.Builder(file)
        // 开始拍照
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    println("Photo capture failed: ${exc.message}")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri
                    LogUtil.d("拍照成功，savedUri = $savedUri")
                    // val msg = "Photo capture succeeded: $savedUri"
                    // 显示拍照内容
                    // binding.ivPic.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))

                    LogUtil.d("图片保存成功 -->${output.savedUri}")
                    LogUtil.d("图片保存成功 -->$imagePath")
                    val intent = Intent()
                    intent.putExtra("path", imagePath)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            })
    }

    fun startRecord(v: View) {
        if (v.tag == 1) {
            showToast("视频录制完成")
            videoCapture.stopRecording()//停止录制
            // preview.clear()//清除预览
            v.tag = 0
            mCountDownTimer?.cancel()
            return
        }
        binding.btnSwitch.visibility = View.INVISIBLE// 录制过程中，不允许切换摄像头
        binding.ivVideoStatus.visibility = View.GONE
        binding.ivVideoStatusRecording.visibility = View.VISIBLE
        v.tag = 1
        startTimer()
        // 拍照保存路径
        val videoPath = "${externalCacheDir?.absolutePath}/V_${System.currentTimeMillis()}.mp4"
        val file = File(videoPath)
        val outputOptions = VideoCapture.OutputFileOptions.Builder(file).build()

        videoCapture.startRecording(outputOptions,
            ContextCompat.getMainExecutor(this),
            object : VideoCapture.OnVideoSavedCallback {
                override fun onVideoSaved(output: VideoCapture.OutputFileResults) {
                    LogUtil.d("视频保存成功 -->${output.savedUri}")
                    LogUtil.d("视频保存成功 -->$videoPath")
                    val intent = Intent()
                    intent.putExtra("path", videoPath)
                    setResult(RESULT_OK, intent)
                    finish()
                }

                override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                    // 保存失败的回调，可能在开始或结束录制时被调用
                    LogUtil.e("录像失败-onError: $message")
                    showMessage("录像失败，$message")
                }
            }
        )
    }

    private var mCountDownTimer: CountDownTimer? = null
    private fun startTimer() {
        binding.tvTimer.text = "00:00"
        val millisInFuture = if (withTimeout)
            maxTime.toLong() + 400
        else
            Long.MAX_VALUE

        mCountDownTimer = object : CountDownTimer(millisInFuture, 499) {
            override fun onTick(millisUntilFinished: Long) {
                val pastTime = (maxTime - millisUntilFinished).toLong()
                if (BuildConfig.DEBUG) {
                    LogUtil.d("onTick-->millisUntilFinished = $millisUntilFinished")
                    LogUtil.d("           onTick-->pastTime = $pastTime")
                }
                binding.tvTimer.text = prettyTime(pastTime)
                binding.takeVideo.setProgress(pastTime / maxTime)
            }

            override fun onFinish() {
                LogUtil.i("视频录制最长3分钟，自动结束")
                binding.takeVideo.performClick()
            }
        }
        mCountDownTimer?.start()
    }

    private val sdf by lazy(LazyThreadSafetyMode.NONE) {
        SimpleDateFormat("mm:ss", Locale.CHINA)
    }

    /**
     * 格式化时间
     */
    fun prettyTime(time: Long): String? {
        return sdf.format(Date(time.coerceAtLeast(0)))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraProvider?.shutdown()
    }

    fun closeCamera(v: View) {
        onBackPressed()
    }
}
