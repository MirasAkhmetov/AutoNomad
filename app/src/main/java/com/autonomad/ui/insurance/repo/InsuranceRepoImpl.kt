package com.autonomad.ui.insurance.repo

import com.autonomad.data.models.Page
import com.autonomad.data.models.insurance.InsuranceHistory
import com.autonomad.utils.ApiGarageF
import okhttp3.ResponseBody
import retrofit2.Response

class InsuranceRepoImpl : InsuranceRepo {
    private val apiService = ApiGarageF.apiService

    override suspend fun getMyFavourites(): Response<Page<InsuranceHistory>> {
       return apiService.getInsuranceFavoriteList()
    }

    override suspend fun getMyHistory():  Response<Page<InsuranceHistory>> {
        return apiService.getInsuranceHistory()
    }

    override suspend fun deleteDriver(id: Int): Response<ResponseBody> {
        return apiService.deleteDriverItem(id)
    }

    override suspend fun addToFavourites(insuranceHistory: InsuranceHistory) {
        TODO("Not yet implemented")
    }

    override suspend fun sendRequest() {
        TODO("Not yet implemented")
    }
}