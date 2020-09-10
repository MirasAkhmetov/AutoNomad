package com.autonomad.ui.profile.driver_profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.autonomad.data.models.insurance.InsuranceCar
import com.autonomad.data.models.penalty.Driver
import com.autonomad.data.repo.GarageDriverRepo
import com.autonomad.utils.BaseViewModel
import com.autonomad.utils.timber
import kotlinx.coroutines.launch

class DriverPageDetailViewModel(private var driverId: Int, private var uin: String, private val repo: GarageDriverRepo) :
    BaseViewModel() {

    val cars = MutableLiveData<List<InsuranceCar>>()
    val driver = MutableLiveData<Driver>()
    val error = MutableLiveData<String>()

    fun loadData() {
        when {
            driverId > 0 -> loadDriver()
            uin.isNotEmpty() -> addDriver()
            else -> {
                timber("driverId: $driverId, uin: $uin")
                error.value = "invalidData"
            }
        }
    }

    private fun loadDriver() {
        dataLoading.value = true
        viewModelScope.launch {
            val result = repo.getDriver(driverId)
            result?.onSuccess {
                uin = target
                driver.value = this
                this@DriverPageDetailViewModel.cars.value = cars
                empty.value = cars.isEmpty()
            }
            result?.onFail {
                error.value = it
            }
            dataLoading.value = false
        }
    }

    private fun addDriver() {
        dataLoading.value = true
        viewModelScope.launch {
            val result = repo.addDriver(uin)
            result?.onSuccess {
                driverId = this
                loadDriver()
            }
            result?.onFail {
                error.value = it
                dataLoading.value = false
            }
        }
    }

    fun deleteDriver() = liveData {
        dataLoading.value = true
        val result = repo.deleteDriver(driverId)
        dataLoading.value = false
        result?.onFail { error.value = it }
        emit(result?.isSuccess)
    }

    fun setCar(carId: Int, add: Boolean) = liveData {
        val result = repo.setCar(carId, driverId, add)
        if (result?.item == true) emit(true)
        else {
            result?.onFail { error.value = it }
            emit(false)
        }
    }
}