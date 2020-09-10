package com.autonomad.data.repo

import com.autonomad.data.models.penalty.Driver
import com.autonomad.utils.Status
import okhttp3.ResponseBody

interface GarageDriverRepo {
    suspend fun getDriver(id: Int): Status<Driver>?
    suspend fun addDriver(uin: String): Status<Int>?
    suspend fun deleteDriver(id: Int): Status<ResponseBody>?
    suspend fun setCar(carId: Int, driverId: Int, add: Boolean): Status<Boolean>?
}