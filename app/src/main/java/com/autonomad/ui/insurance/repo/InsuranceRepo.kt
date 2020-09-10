package com.autonomad.ui.insurance.repo

import com.autonomad.data.models.Page
import com.autonomad.data.models.insurance.InsuranceHistory
import okhttp3.ResponseBody
import retrofit2.Response

interface InsuranceRepo {
    suspend fun getMyFavourites(): Response<Page<InsuranceHistory>>
    suspend fun getMyHistory(): Response<Page<InsuranceHistory>>
    suspend fun deleteDriver(id: Int): Response<ResponseBody>
    suspend fun addToFavourites(insuranceHistory: InsuranceHistory)
    suspend fun sendRequest()
}