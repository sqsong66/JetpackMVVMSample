package com.example.jetpacksample.http

import com.example.jetpacksample.R
import com.example.jetpacksample.base.BaseApplication
import com.example.jetpacksample.utils.CommonUtils
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class ApiException constructor(val code: Int, val showMessage: String?, val errorMessage: String?) :
    Exception() {

    companion object {
        const val ERROR_EMPTY = -1000
        const val ERROR_NETWORK = -1
        const val ERROR_TIMEOUT = -2
        const val ERROR_UNKNOWN = -3
        const val ERROR_DATA_PARSE = -4

        fun parseException(error: Throwable): ApiException {
            if (error is ApiException) return error

            val code: Int
            val message: String
            val context = BaseApplication.getInstance().applicationContext
            if (error is UnknownHostException || error is ConnectException) {
                code = ERROR_NETWORK
                message = if (!CommonUtils.isNetworkAvailable(context)) {
                    context.getString(R.string.text_network_disconnect)
                } else {
                    context.getString(R.string.text_network_connect_fail)
                }
            } else if (error is HttpException) {
                code = error.code()
                message = context.getString(R.string.text_connect_server_fail)
            } else if (error is JsonParseException || error is JsonSyntaxException) {
                code = ERROR_DATA_PARSE
                message = context.getString(R.string.text_error_data_parse)
            } else if (error is ConnectException || error is TimeoutException
                || error is SocketException || error is SocketTimeoutException
            ) {
                code = ERROR_TIMEOUT
                message = if (error is TimeoutException || error is SocketTimeoutException) {
                    context.getString(R.string.text_network_connect_fail)
                } else {
                    context.getString(R.string.text_connect_server_fail)
                }
            } else {
                code = ERROR_UNKNOWN
                message = error.message ?: ""
            }
            return ApiException(code, message, message)
        }
    }


}
