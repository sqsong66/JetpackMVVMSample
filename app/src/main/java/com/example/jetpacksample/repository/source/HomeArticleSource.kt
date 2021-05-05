package com.example.jetpacksample.repository.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jetpacksample.bean.ArticleItem
import com.example.jetpacksample.http.ApiException
import com.example.jetpacksample.http.ApiService
import java.lang.Exception

class HomeArticleSource(private val apiService: ApiService): PagingSource<Int, ArticleItem>() {

    override fun getRefreshKey(state: PagingState<Int, ArticleItem>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleItem> {
        val index = params.key ?: 0
        return try {
            val response = apiService.getHomeArticles(index)
            if (response.errorCode == 0 && response.data != null) {
                val dataList = response.data.datas
                val prevKey = if (index == 0) null else index - 1
                val nextKey = if (response.data.over) null else index + 1
                if (index == 0&& dataList.isEmpty()) {
                    LoadResult.Error(ApiException(ApiException.ERROR_EMPTY, "Empty", "Empty"))
                } else {
                    LoadResult.Page(data = dataList, prevKey = prevKey, nextKey = nextKey)
                }
            } else {
                LoadResult.Error(ApiException(response.errorCode, response.errorMsg, response.errorMsg))
            }
        } catch (ex: Exception) {
            LoadResult.Error(ApiException.parseException(ex))
        }
    }
}