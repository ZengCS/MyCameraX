package com.zcs.app.camerax.ui

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zcs.app.camerax.R
import com.zcs.app.camerax.adapter.CommonRecyclerAdapter
import com.zcs.app.camerax.base.BaseCameraFragment
import com.zcs.app.camerax.config.CustomCameraConfig
import com.zcs.app.camerax.isAddCamera
import java.io.File

/**
 * 照片列表页面
 */
class PhotoListFragment : BaseCameraFragment() {
    override fun bindCameraType() = CustomCameraConfig.CAMERA_PHOTO

    override fun onInit() {
        super.onInit()
        initAdapter()
    }

    override fun initAdapter() {
        if (adapter == null) {
            adapter = object : CommonRecyclerAdapter<File>(R.layout.item_video_photo, mItems) {
                override fun convert(holder: BaseViewHolder, item: File) {
                    holder.setVisible(R.id.ivCameraAdd, item.isAddCamera())
                    val ivThumb = holder.getView<ImageView>(R.id.ivThumb)

                    if (!item.isAddCamera()) {
                        Glide.with(requireContext()).load(item.absolutePath).into(ivThumb)
                    }
                    ivThumb.setOnClickListener {
                        if (item.isAddCamera()) {
                            openCamera(bindCameraType())
                        } else {
                            openPicture(item, holder.bindingAdapterPosition)
                        }
                    }
                }
            }
            binding.rvGrid.adapter = adapter
        } else {
            adapter?.setNewInstance(mItems)
        }
    }

    private fun openPicture(file: File, position: Int) {
        showMessage("[$position] - [${file.absolutePath}]")
    }
}