package com.autonomad.utils

import com.autonomad.network.ApiChat

object ApiChatFactory {
    val apiService: ApiChat by lazy {
        retrofit.create(ApiChat::class.java)
    }

    private val retrofit by lazy {
        createRetrofit(Constants.BASE_URL_CHAT, HttpClient.tokenClient)
    }
}