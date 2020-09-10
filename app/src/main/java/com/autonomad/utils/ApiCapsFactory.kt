package com.autonomad.utils

import com.autonomad.network.ApiCaps
import retrofit2.Retrofit

class ApiCapsFactory private constructor() {

    val apiService: ApiCaps
        get() = retrofit.create(ApiCaps::class.java)


    init {
        retrofit = createRetrofit(Constants.BASE_URL_AUTH, HttpClient.loggingClient)
    }

    companion object {
        private var apiCapsFactory: ApiCapsFactory? = null
        private lateinit var retrofit: Retrofit

        val instance: ApiCapsFactory
            get() {
                if (apiCapsFactory == null) {
                    apiCapsFactory = ApiCapsFactory()
                }
                return apiCapsFactory as ApiCapsFactory
            }
    }
}

object ApiCapsSingle {
    private val apiCaps by lazy {
        createRetrofit(Constants.BASE_URL_AUTH, HttpClient.loggingClient).create(ApiCaps::class.java)
    }

    private var apiHeader: ApiCaps? = null

    private fun setHeader() {
        val client = HttpClient.headerClient ?: return
        apiHeader = createRetrofit(Constants.BASE_URL_AUTH, client).create(ApiCaps::class.java)
    }

    val apiService: ApiCaps
        get() {
            apiHeader ?: setHeader()
            return apiHeader ?: apiCaps
        }
}