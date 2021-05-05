package com.example.jetpacksample.http

import com.example.jetpacksample.bean.ArticleData
import com.example.jetpacksample.bean.BannerData
import com.example.jetpacksample.bean.BaseBean
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("article/list/{page}/json")
    suspend fun getHomeArticles(@Path("page") page: Int): BaseBean<ArticleData>

    @GET("banner/json")
    suspend fun getHomeBanner(): BaseBean<List<BannerData>>

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }

}