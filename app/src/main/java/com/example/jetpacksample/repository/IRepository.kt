package com.example.jetpacksample.repository

import androidx.paging.PagingData
import com.example.jetpacksample.bean.ArticleItem
import kotlinx.coroutines.flow.Flow

interface IRepository {

    fun getHomeArticles(pageSize: Int): Flow<PagingData<ArticleItem>>

}