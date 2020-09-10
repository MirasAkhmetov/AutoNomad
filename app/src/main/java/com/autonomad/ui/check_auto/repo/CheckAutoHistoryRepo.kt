package com.autonomad.ui.check_auto.repo

import com.autonomad.data.models.Page
import com.autonomad.data.models.check_auto.*
import okhttp3.ResponseBody
import retrofit2.Response

interface CheckAutoHistoryRepo {
    suspend fun getGarageCars(limit: Int = 1000, offset: Int = 0): Response<Page<GarageCar>>
    suspend fun deleteCar(id: Int): Response<ResponseBody>
    suspend fun getOrders(limit: Int = 1000, offset: Int = 0): Response<Page<CheckAutoHistory>>
    suspend fun getCheckAutoDetailTicket(id: Int): Response<DetailTicket>
    suspend fun getCarByVinDetail(vin: String): Response<Report>
    suspend fun createOrder(createOrder: CreateOrder): Response<CreateOrderStatus>
    suspend fun checkOrderStatus(id: Int): Response<CreateOrderStatus>
}