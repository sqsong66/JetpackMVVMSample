package com.example.jetpacksample.http

import android.annotation.SuppressLint
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

object OkHttpClientFactory {

    private const val DEFAULT_TIME_OUT = 30000L

    fun createNormalOkHttpClient(headerInterceptor: Interceptor? = null, timeoutMillis: Long = DEFAULT_TIME_OUT): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder().apply {
            connectTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
            readTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
            writeTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
            addInterceptor(loggingInterceptor)
            if (headerInterceptor != null) addInterceptor(headerInterceptor)
        }.build()
    }

    @SuppressLint("CustomX509TrustManager")
    fun createUnsafeOkHttpClient(
            headerInterceptor: Interceptor? = null,
            timeoutMillis: Long = DEFAULT_TIME_OUT
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
            // if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        val builder = OkHttpClient.Builder()
                .connectTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMillis, TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .followRedirects(true)
                .retryOnConnectionFailure(true)
                .followSslRedirects(true)
        if (headerInterceptor != null) {
            builder.addInterceptor(headerInterceptor)
        }

        try {
            val x509TrustManager = object : X509TrustManager {
                override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                ) {
                }

                override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
            val trustManagerArray = arrayOf(x509TrustManager)
            val sslContext = SSLContext.getInstance("SSL").apply {
                init(null, trustManagerArray, SecureRandom())
            }
            return builder.sslSocketFactory(sslContext.socketFactory, trustManagerArray[0])
                    .hostnameVerifier(HostnameVerifier { _, _ -> true })
                    .build()
        } catch (e: Exception) {
            e.printStackTrace()
            return builder.build()
        }
    }

}