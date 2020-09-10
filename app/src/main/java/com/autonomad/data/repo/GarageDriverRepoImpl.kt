package com.autonomad.data.repo

import com.autonomad.data.models.main_page_car.CarDrivers
import com.autonomad.data.models.penalty.DriverCheckRequest
import com.autonomad.data.models.penalty.SaveRequest
import com.autonomad.network.ApiGarage
import com.autonomad.utils.ApiCaller
import kotlinx.coroutines.CoroutineDispatcher

class GarageDriverRepoImpl(private val apiService: ApiGarage, dispatcher: CoroutineDispatcher) : GarageDriverRepo,
    ApiCaller(dispatcher) {

    override suspend fun getDriver(id: Int) = apiCallConverted { apiService.getDriver(id) }

    override suspend fun addDriver(uin: String) = apiCall {
        val created = apiService.addDriver(DriverCheckRequest(uin))
        val id = created.body()?.id
        if (created.isSuccessful && id != null) {
            val saved = apiService.saveDriver(id, SaveRequest(true))
            if (saved.isSuccessful) id
            else throw Companion.ApiException(saved.message())
        } else throw Companion.ApiException(created.message())
    }

    override suspend fun deleteDriver(id: Int) = apiCallConverted { apiService.deleteDriverSuspend(id) }

    override suspend fun setCar(carId: Int, driverId: Int, add: Boolean) = apiCall {
        val car = apiService.getCarDetails(carId)
        if (car.isSuccessful) {
            val drivers = (car.body()?.drivers?.map { it.id }) ?: emptyList()
            val result = apiService.setCarDrivers(carId, CarDrivers(if (add) drivers + driverId else drivers - driverId))
            if (result.isSuccessful) true
            else {
                var message = result.message()
                try {
                    if (result.errorBody()?.string().orEmpty().contains("list may not be empty"))
                        message = "Список привязанных к авто водителей не может быть пустым"
                } catch (e: Exception) {
                }
                throw Companion.ApiException(message)
            }
        } else throw Companion.ApiException(car.message())
    }
}