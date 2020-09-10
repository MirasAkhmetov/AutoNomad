package com.autonomad.ui.shop.model

import retrofit2.Response

sealed class NetResult<out T> {
    data class Success<out T>(val data: T) : NetResult<T>()
    data class Error(val errorMessage: String) : NetResult<Nothing>()
}

fun <T> Response<T>.getResult(): NetResult<T> {
    return when (code()) {
        200, 201 -> {
            val data = body()
            if (data != null) NetResult.Success(data)
            else NetResult.Error("body() is null")
        }
        404 -> NetResult.Error("404")
        500 -> NetResult.Error("500")
        else -> NetResult.Error("Ошибка")
    }
}