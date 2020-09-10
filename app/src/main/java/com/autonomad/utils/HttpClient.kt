package com.autonomad.utils

import android.content.Context
import com.autonomad.BuildConfig
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object HttpClient {
    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val loggingClient: OkHttpClient
        get() = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) addInterceptor(chuckInterceptor ?: loggingInterceptor)
        }.build()

    val headerClient: OkHttpClient?
        get() = if (Methods.key.isNotEmpty()) tokenClient else null

    val tokenClient by lazy {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) addInterceptor(chuckInterceptor ?: loggingInterceptor)
        }.addInterceptor {
            val requestBuilder = it.request().newBuilder().addHeader("Authorization", Methods.key)
            it.proceed(requestBuilder.build())
        }.build()
    }

    private var context: Context? = null

    fun provideContext(context: Context) {
        this.context = context
    }

    private val chuckInterceptor: ChuckInterceptor?
        get() = if (context != null) ChuckInterceptor(
            context
        ) else null
}