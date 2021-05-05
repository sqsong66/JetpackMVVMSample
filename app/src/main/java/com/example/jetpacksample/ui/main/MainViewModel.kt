package com.example.jetpacksample.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetpacksample.bean.BannerData
import com.example.jetpacksample.http.ApiService
import com.example.jetpacksample.utils.ext.requestDataFromServer
import com.example.jetpacksample.utils.ext.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var apiService: ApiService

    val bannerList = MutableLiveData<List<BannerData>>()

    fun requestBannerData() {
        Log.e("sqsong", "MainViewModel ApiService: $apiService")
        requestDataFromServer({ apiService.getHomeBanner() }, {
            if (it.isSuccess) {
                val resultList = it.getOrNull()
                if (resultList != null) {
                    bannerList.postValue(resultList)
                }
            } else {
                context.showToast(it.exceptionOrNull()?.showMessage)
            }
        })
    }

}