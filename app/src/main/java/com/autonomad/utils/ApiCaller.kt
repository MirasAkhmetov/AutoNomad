package com.autonomad.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

open class ApiCaller(private val dispatcher: CoroutineDispatcher) {
    suspend fun <T> apiCall(request: suspend () -> T) = withContext(dispatcher) {
        try {
            request().success
        } catch (e: Exception) {
            handleException(e)
        }
    }

    suspend fun <T> apiCallConverted(request: suspend () -> Response<T>) = withContext(dispatcher) {
        try {
            request().convert()
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun handleException(e: Exception) = when (e) {
        is IOException -> "Не удалось подключиться к сети. Проверьте соединение и повторите попытку".fail
        is HttpException -> "Ошибка ${e.code()}".fail
        is ApiException -> e.apiMessage.fail
        else -> e.localizedMessage.fail
    }

    companion object {
        protected class ApiException(val apiMessage: String?) : Exception()
    }
}