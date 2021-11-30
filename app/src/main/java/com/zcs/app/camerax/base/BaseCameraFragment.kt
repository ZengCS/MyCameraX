package com.zcs.app.camerax.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zcs.app.camerax.adapter.CommonRecyclerAdapter
import com.zcs.app.camerax.config.CustomCameraConfig
import com.zcs.app.camerax.databinding.FragmentVideoPhotoBinding
import com.zcs.app.camerax.entity.MediaEntity
import com.zcs.app.camerax.isImage
import com.zcs.app.camerax.isVideo
import com.zcs.app.camerax.ui.CameraXActivity
import com.zcs.app.camerax.utils.LogUtil
import java.io.File

abstract class BaseCameraFragment : Fragment() {
    var adapter: CommonRecyclerAdapter<File>? = null
    lateinit var binding: FragmentVideoPhotoBinding

    lateinit var mItems: ArrayList<File>

    private val rxPermissions: RxPermissions by lazy(LazyThreadSafetyMode.NONE) {
        RxPermissions(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoPhotoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val intent: Intent? = it.data
                    val media: MediaEntity? = intent?.getSerializableExtra("media") as MediaEntity?
                    media?.let {
                        val path = media.path
                        if (!TextUtils.isEmpty(path)) {
                            // showMessage("拍摄成功：$path")
                            notifyItemInserted(path)
                        }
                    }
                }
            }
        // 子类重写此方法即可
        onInit()
    }

    private fun notifyItemInserted(path: String) {
        val file = File(path)
        mItems.add(file)
        adapter?.notifyItemInserted(mItems.size)
    }

    @SuppressLint("CheckResult")
    fun openCamera(type: Int) {
        rxPermissions.request(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe {
            if (it) {
                // 授权成功
                LogUtil.d("授权成功，启动相机")

                val intent = Intent(requireContext(), CameraXActivity::class.java)
                intent.putExtra("type", type)
                cameraResultLauncher.launch(intent)
            } else {
                // 授权失败
                showMessage("授权失败，无法启动相机。")
            }
        }
    }

    private fun loadMediaList(type: Int): ArrayList<File> {
        val picList = ArrayList<File>()
        val dir = requireContext().externalCacheDir
        if (dir != null) {
            val fileList = dir.listFiles()
            if (fileList.isNotEmpty()) {
                when (type) {
                    CustomCameraConfig.CAMERA_PHOTO -> {
                        for (file in fileList) {
                            if (file.isImage())
                                picList.add(file)
                        }
                    }
                    CustomCameraConfig.CAMERA_VIDEO -> {
                        for (file in fileList) {
                            if (file.isVideo())
                                picList.add(file)
                        }
                    }
                }
            }
        }
        return picList
    }

    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    fun showMessage(msg: String, withExit: Boolean = false) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(msg)
        builder.setPositiveButton(if (withExit) "退出" else "我知道了") { dialog, _ ->
            dialog.dismiss()
            if (withExit)
                requireActivity().finish()
        }
        builder.show()
    }

    open fun onInit() {
        binding.btnOpenCamera.setOnClickListener {
            openCamera(bindCameraType())
        }

        mItems = loadMediaList(bindCameraType())
    }

    abstract fun bindCameraType(): Int

    abstract fun initAdapter()
}