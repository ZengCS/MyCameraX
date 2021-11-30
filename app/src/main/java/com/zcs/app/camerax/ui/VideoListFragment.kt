package com.zcs.app.camerax.ui

import android.graphics.Bitmap
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zcs.app.camerax.R
import com.zcs.app.camerax.adapter.CommonRecyclerAdapter
import com.zcs.app.camerax.base.BaseCameraFragment
import com.zcs.app.camerax.config.CustomCameraConfig
import com.zcs.app.camerax.task.LoadVideoFirstFrameTask
import java.io.File

/**
 * 视频列表页面
 */
class VideoListFragment : BaseCameraFragment() {
    override fun bindCameraType() = CustomCameraConfig.CAMERA_VIDEO

    override fun onInit() {
        super.onInit()
        initAdapter()
    }

    override fun initAdapter() {
        if (adapter == null) {
            adapter = object : CommonRecyclerAdapter<File>(R.layout.item_video_photo, mItems) {
                override fun convert(holder: BaseViewHolder, item: File) {
                    val ivThumb = holder.getView<ImageView>(R.id.ivThumb)
                    holder.setVisible(R.id.ivPlay, true)
                    ivThumb.setImageResource(R.color.black_20p)
                    // ++++++++++++++ 显示视频第一帧图片1 ++++++++++++++
                    val cacheFirstFrame =
                        LoadVideoFirstFrameTask.getCacheFirstFrame(item.absolutePath)
                    if (TextUtils.isEmpty(cacheFirstFrame)) { // 本地没有缓存
                        LoadVideoFirstFrameTask { bitmap: Bitmap? ->
                            // 显示第一帧图片
                            if (bitmap != null) ivThumb.setImageBitmap(bitmap)
                        }.execute(item.absolutePath)
                    } else {
                        Glide.with(requireContext()).load(cacheFirstFrame).into(ivThumb)
                    }
                    // ++++++++++++++ 显示视频第一帧图片2 ++++++++++++++

                    ivThumb.setOnClickListener {
                        if (!TextUtils.isEmpty(item.absolutePath)) {
                            openVideoPlayer(item)
                        }
                    }
                }
            }
            binding.rvGrid.adapter = adapter
        } else {
            adapter?.setNewInstance(mItems)
        }
    }

    private fun openVideoPlayer(file: File) {
        showMessage("openVideoPlayer --> ${file.absolutePath}")
    }
}