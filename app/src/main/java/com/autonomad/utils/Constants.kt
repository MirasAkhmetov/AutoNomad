package com.autonomad.utils

import com.autonomad.BuildConfig

object Constants {
    const val BASE_URL_IMAGES = "https://auto-nomad.s3.amazonaws.com"
    const val BASE_URL_METEO_CURRENCY = "https://meteo-currency.api.auto-nomad.kz/api/v1/"

    private const val currentApis = true
    private val prodApis = !BuildConfig.DEBUG || currentApis
    val BASE_URL_AUTH = if (prodApis) "https://caps.api.auto-nomad.kz/api/" else "http://caps.test.auto-nomad.kz:8000/api/"
    val BASE_URL_GARAGE = if (prodApis) "https://garage.api.auto-nomad.kz/api/" else "http://garage.test.auto-nomad.kz:8000/api/"
    val BASE_URL_AUTO_REQ = if (prodApis) "https://auto-requests.api.auto-nomad.kz/" else "http://auto-requests.test.auto-nomad.kz:8000/"
    val BASE_URL_SHOP = if (prodApis) "https://shop.api.auto-nomad.kz/" else "http://shop.test.auto-nomad.kz:8000/"
    val BASE_URL_CHAT = if (prodApis) "https://chat.api.auto-nomad.kz" else "http://chat.test.auto-nomad.kz:8000/"
    val BASE_URL_CHAT_SOCKET = if (prodApis) "wss://ws-chat.api.auto-nomad.kz/ws/dialogue/" else "ws://ws-chat.test.auto-nomad.kz:8000/ws/dialogue/"
}