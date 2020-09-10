package com.autonomad.utils

import com.autonomad.network.ApiMeteoCurrency

object ApiMeteoCurrencyFactory {
    val apiService by lazy { create() }

    private fun create(): ApiMeteoCurrency {
        val retrofit = createRetrofit(Constants.BASE_URL_METEO_CURRENCY, HttpClient.loggingClient)
        return retrofit.create(ApiMeteoCurrency::class.java)
    }
}