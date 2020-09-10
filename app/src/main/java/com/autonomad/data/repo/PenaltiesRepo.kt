package com.autonomad.data.repo

import com.autonomad.data.models.penalty.PenaltySearch
import com.autonomad.data.models.penalty.SaveRequest
import com.autonomad.utils.Status
import retrofit2.Response

interface PenaltiesRepo {
    suspend fun searchPenaltiesByUin(uin: String): Status<PenaltySearch>?
    suspend fun saveDriver(id: Int, save: Boolean): Response<SaveRequest>
}