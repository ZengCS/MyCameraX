package com.zcs.app.camerax.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

abstract class CommonRecyclerAdapter<T>(@LayoutRes resId: Int, data: ArrayList<T>) :
    BaseQuickAdapter<T, BaseViewHolder>(resId, data)