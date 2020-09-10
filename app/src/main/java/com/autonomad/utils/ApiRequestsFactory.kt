package com.autonomad.utils

import com.autonomad.network.ApiRequests
import com.autonomad.utils.Constants.BASE_URL_AUTO_REQ

object ApiRequestsFactory {

    val apiService: ApiRequests
        get() {
            recreate()
            return apiHeader ?: apiLogcat
        }

    private val apiLogcat: ApiRequests by lazy {
        createRetrofit(BASE_URL_AUTO_REQ, HttpClient.loggingClient).create(ApiRequests::class.java)
    }

    private var apiHeader: ApiRequests? = null

    private fun recreate() {
        val client = HttpClient.headerClient ?: return
        apiHeader = createRetrofit(BASE_URL_AUTO_REQ, client).create(ApiRequests::class.java)
    }
}