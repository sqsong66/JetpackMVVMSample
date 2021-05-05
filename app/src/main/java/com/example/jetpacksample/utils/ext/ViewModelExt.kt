package com.example.jetpacksample.utils.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpacksample.bean.BaseBean
import com.example.jetpacksample.http.ApiException
import com.example.jetpacksample.http.ResponseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** 普通从服务器请求数据结果。
 * [block] 请求接口挂起函数；该函数会在IO子线程中执行，返回的结果是[BaseBean]子类型
 * [result] 请求结果回调函数；
 *  */
fun <T> ViewModel.requestDataFromServer(
    block: suspend () -> BaseBean<T>,
    result: (ResponseResult<T?>) -> Unit
) {
    viewModelScope.launch {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                val blockResult = block()
                if (blockResult.errorCode == 0) {
                    blockResult.data
                } else {
                    throw ApiException(blockResult.errorCode, blockResult.errorMsg, blockResult.errorMsg)
                }
            }
        }.onSuccess {
            result(ResponseResult.success(it))
        }.onFailure {
            it.printStackTrace()
            result(ResponseResult.failure(it))
        }
    }
}
