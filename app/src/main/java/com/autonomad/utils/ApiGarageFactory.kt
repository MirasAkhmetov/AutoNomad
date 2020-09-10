package com.autonomad.utils

import com.autonomad.network.ApiGarage
import retrofit2.Retrofit

class ApiGarageFactory private constructor() {

    val apiService: ApiGarage
        get() = retrofit.create(ApiGarage::class.java)


    init {
        retrofit = createRetrofit(Constants.BASE_URL_GARAGE, HttpClient.loggingClient)
    }

    companion object {
        private var ApiGarageFactory: ApiGarageFactory? = null
        private lateinit var retrofit: Retrofit

        val instance: ApiGarageFactory
            get() {
                if (ApiGarageFactory == null) {
                    ApiGarageFactory = ApiGarageFactory()
                }
                return ApiGarageFactory as ApiGarageFactory
            }
    }
}

object ApiGarageF {
    private val apiGarage by lazy {
        createRetrofit(Constants.BASE_URL_GARAGE, HttpClient.loggingClient).create(ApiGarage::class.java)
    }

    private var apiHeader: ApiGarage? = null

    private fun setHeader() {
        val client = HttpClient.tokenClient
        apiHeader = createRetrofit(Constants.BASE_URL_GARAGE, client).create(ApiGarage::class.java)
    }

    val apiService: ApiGarage
        get() {
            apiHeader ?: setHeader()
            return apiHeader ?: apiGarage
        }
}