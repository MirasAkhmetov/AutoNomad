package com.autonomad.data.repo

import com.autonomad.data.models.penalty.SaveRequest
import com.autonomad.network.ApiGarage
import com.autonomad.utils.ApiCaller
import kotlinx.coroutines.CoroutineDispatcher

class PenaltiesRepoImpl(private val apiService: ApiGarage, dispatcher: CoroutineDispatcher) : PenaltiesRepo,
    ApiCaller(dispatcher) {

    override suspend fun searchPenaltiesByUin(uin: String) = apiCallConverted {
        apiService.penaltiesSearch(uin)
    }

    override suspend fun saveDriver(id: Int, save: Boolean) = apiService.saveDriverRequest(id, SaveRequest(save))
}