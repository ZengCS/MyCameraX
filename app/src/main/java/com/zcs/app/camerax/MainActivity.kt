package com.zcs.app.camerax

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.zcs.app.camerax.base.BaseActivity
import com.zcs.app.camerax.config.CameraxConfig
import com.zcs.app.camerax.databinding.ActivityMainBinding
import com.zcs.app.camerax.utils.LogUtil

@SuppressLint("RestrictedApi")
class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var videoResultLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        binding.btnPhoto.setOnClickListener {
            onPhoto()
        }
        binding.btnRecord.setOnClickListener {
            onVideo()
        }

        videoResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val intent: Intent? = it.data
                    val videoPath: String? = intent?.getStringExtra("videoPath")
                    if (!TextUtils.isEmpty(videoPath))
                        showMessage("视频录制成功：$videoPath")
                }
            }
    }

    @SuppressLint("CheckResult")
    private fun onVideo() {
        rxPermissions.request(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe {
            if (it) {
                // 授权成功
                LogUtil.d("授权成功，启动相机")
                startCamera(CameraxConfig.CAMERA_VIDEO_ONLY)
            } else {
                // 授权失败
                showMessage("授权失败，无法启动相机。")
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun onPhoto() {
        rxPermissions.request(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe {
            if (it) {
                // 授权成功
                LogUtil.d("授权成功，启动相机")
                startCamera(CameraxConfig.CAMERA_PHOTO_ONLY)
            } else {
                // 授权失败
                showMessage("授权失败，无法启动相机。")
            }
        }
    }

    private fun startCamera(type: Int) {
        val intent = Intent(this, CameraXActivity::class.java)
        intent.putExtra("type", type)
        videoResultLauncher.launch(intent)
    }
}
