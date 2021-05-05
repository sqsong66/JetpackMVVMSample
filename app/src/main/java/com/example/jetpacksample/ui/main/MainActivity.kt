package com.example.jetpacksample.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.jetpacksample.R
import com.example.jetpacksample.base.ui.BaseActivity
import com.example.jetpacksample.databinding.ActivityMainBinding
import com.example.jetpacksample.utils.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun initBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initEvent(savedInstanceState: Bundle?) {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)

        // 请求banner数据
        mainViewModel.requestBannerData()

        mainViewModel.bannerList.observe(this) {
            showToast("Banner size: ${it.size}")
        }
    }

}