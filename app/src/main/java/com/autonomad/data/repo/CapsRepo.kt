package com.autonomad.data.repo

import com.autonomad.data.models.FcmId
import com.autonomad.utils.Status

interface CapsRepo {
    suspend fun onLogIn(fcmId: String): Status<FcmId>?
}