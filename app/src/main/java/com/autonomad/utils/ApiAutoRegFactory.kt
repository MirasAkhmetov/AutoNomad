package com.autonomad.utils

import com.autonomad.network.ApiAutoReg
import retrofit2.Retrofit

class ApiAutoRegFactory private constructor() {

    val apiService: ApiAutoReg
        get() = retrofit.create(ApiAutoReg::class.java)

    init {
        retrofit = createRetrofit(Constants.BASE_URL_AUTO_REQ, HttpClient.loggingClient)
    }

    companion object {
        private var apiAutoRegFactory: ApiAutoRegFactory? = null
        private lateinit var retrofit: Retrofit

        val instance: ApiAutoRegFactory
            get() {
                if (apiAutoRegFactory == null) {
                    apiAutoRegFactory = ApiAutoRegFactory()
                }
                return apiAutoRegFactory as ApiAutoRegFactory
            }
    }
}