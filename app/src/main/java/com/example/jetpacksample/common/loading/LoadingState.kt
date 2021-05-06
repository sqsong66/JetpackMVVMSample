package com.example.jetpacksample.common.loading

interface RetryCallback {
    fun retry(type: Int = 0) // 0 - 错误按钮回调  空白页按钮回调
}

enum class LayoutType {
    LOADING,
    EMPTY,
    SUCCESS,
    ERROR,
}

data class LoadingState(
        val layoutType: LayoutType,
        val errorMsg: String? = "",
        val errorDesc: String? = "",
        var emptyMsg: String? = "",
        var emptyDesc: String? = "",
        var emptyBtnText: String? = "") {
    companion object {

        fun loadingState(): LoadingState = LoadingState(LayoutType.LOADING)

        fun emptyState(emptyMsg: String? = "暂无数据", emptyDesc: String? = "", emptyBtnText: String? = ""): LoadingState =
                LoadingState(LayoutType.EMPTY, emptyMsg = emptyMsg, emptyDesc = emptyDesc, emptyBtnText = emptyBtnText)

        fun successState(): LoadingState = LoadingState(LayoutType.SUCCESS)

        fun errorState(errorMessage: String? = "", errorDesc: String? = ""/*"数据加载出错,请稍后重试!"*/): LoadingState {
            return LoadingState(LayoutType.ERROR, errorMessage, errorDesc)
        }

    }
}