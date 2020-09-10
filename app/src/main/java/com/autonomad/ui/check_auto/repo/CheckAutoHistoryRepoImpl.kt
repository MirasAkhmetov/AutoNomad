package com.autonomad.ui.check_auto.repo

import com.autonomad.data.models.Page
import com.autonomad.data.models.check_auto.*
import com.autonomad.utils.ApiGarageF
import okhttp3.ResponseBody
import retrofit2.Response

class CheckAutoHistoryRepoImpl : CheckAutoHistoryRepo {
    private val apiService = ApiGarageF.apiService

    override suspend fun getGarageCars(limit: Int, offset: Int): Response<Page<GarageCar>> {
        return apiService.getGarageCarsList(limit, offset)
    }

    override suspend fun deleteCar(id: Int): Response<ResponseBody> {
        return apiService.deleteCarFromGarage(id)
    }

    override suspend fun getOrders(limit: Int, offset: Int): Response<Page<CheckAutoHistory>> {
        return apiService.getCheckAutoOrders(limit, offset)
    }

    override suspend fun getCheckAutoDetailTicket(id: Int): Response<DetailTicket> {
        return apiService.getCheckAutoDetailTicket(id)
    }

    override suspend fun getCarByVinDetail(vin: String): Response<Report> {
        return apiService.getCarByVinDetail(vin)
    }

    override suspend fun createOrder(createOrder: CreateOrder): Response<CreateOrderStatus> {
        return apiService.createOrder(createOrder)
    }

    override suspend fun checkOrderStatus(id: Int): Response<CreateOrderStatus> {
        return apiService.checkOrderStatus(id)
    }
}