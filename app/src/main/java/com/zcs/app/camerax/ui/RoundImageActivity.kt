package com.zcs.app.camerax.ui

import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.zcs.app.camerax.R
import com.zcs.app.camerax.base.BaseActivity
import com.zcs.app.camerax.databinding.ActivityRoundImageBinding
import com.zcs.app.camerax.utils.MeasureUtil

class RoundImageActivity : BaseActivity() {
    val binding: ActivityRoundImageBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRoundImageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        // Glide圆角和CenterCrop冲突，设置了CenterCrop的图片不能被圆角
        Glide.with(this).load(R.mipmap.pic_f1)
            .dontAnimate()
            .transform(RoundedCorners(MeasureUtil.dip2px(this, 20f)))
            .into(binding.pic3)

        // 正确的写法是增加CenterCrop()转换
        Glide.with(this).load(R.mipmap.pic_f1)
            .dontAnimate()
            .transform(CenterCrop(), RoundedCorners(MeasureUtil.dip2px(this, 20f)))
            .into(binding.pic4)
    }
}