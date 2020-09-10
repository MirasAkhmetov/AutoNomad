package com.autonomad.utils

import com.autonomad.network.ApiAutoReg
import retrofit2.Retrofit

object ApiMyRegFactory {

    val apiService: ApiAutoReg
        get() = apiChuck ?: apiLogcat

    private val apiLogcat: ApiAutoReg by lazy { create() }
    private var apiChuck: ApiAutoReg? = null

    private val retrofit: Retrofit by lazy { buildRetrofit() }

    private fun create() = retrofit.create(ApiAutoReg::class.java)

    private fun buildRetrofit() = createRetrofit(Constants.BASE_URL_AUTO_REQ, HttpClient.loggingClient)
}
