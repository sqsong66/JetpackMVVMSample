package com.example.jetpacksample.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.jetpacksample.bean.ArticleItem
import com.example.jetpacksample.http.ApiService
import com.example.jetpacksample.repository.source.HomeArticleSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JetpackRepository @Inject constructor(private val apiService: ApiService) : IRepository {

    override fun getHomeArticles(pageSize: Int): Flow<PagingData<ArticleItem>> =
        Pager(config = PagingConfig(pageSize = pageSize, initialLoadSize = pageSize)) {
            Log.e("sqsong", "JetpackRepository ApiService: $apiService")
            HomeArticleSource(apiService = apiService)
        }.flow

}