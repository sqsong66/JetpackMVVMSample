package com.example.jetpacksample.bean

class BaseBean<T> {
    var errorCode: Int = 0
    var errorMsg: String? = null
    val data: T? = null
}