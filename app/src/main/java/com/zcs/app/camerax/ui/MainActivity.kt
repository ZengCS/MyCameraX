package com.zcs.app.camerax.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.zcs.app.camerax.base.BaseActivity
import com.zcs.app.camerax.databinding.ActivityMainBinding

@SuppressLint("RestrictedApi")
class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        rxPermissions.request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe {
            if (it) {
                initViewPager2()
            } else {
                // 授权失败
                showMessage("授权失败，无法读取相册。", true)
            }
        }
    }

    private fun initViewPager2() {
        val tabs = listOf(
            "图片", "视频"
        )
        val mFragments = listOf<Fragment>(
            PhotoListFragment(),
            VideoListFragment()
        )

        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 2
            }

            override fun createFragment(position: Int): Fragment {
                return mFragments[position]
            }
        }
        // 取消滑动阴影
        val child: View = binding.viewPager2.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        // 将TabLayout与ViewPager2关联
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2,
            false,
            true
        ) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}
