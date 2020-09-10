package com.autonomad.data.repo

import com.autonomad.data.models.FcmId
import com.autonomad.network.ApiCaps
import com.autonomad.utils.ApiCaller
import kotlinx.coroutines.CoroutineDispatcher

class CapsRepoImpl(private val apiService: ApiCaps, dispatcher: CoroutineDispatcher) : CapsRepo, ApiCaller(dispatcher) {
    override suspend fun onLogIn(fcmId: String) = apiCallConverted {
        apiService.sendFcmId(FcmId(fcmId))
    }
}