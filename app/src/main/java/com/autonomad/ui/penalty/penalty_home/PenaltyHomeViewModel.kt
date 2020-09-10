package com.autonomad.ui.penalty.penalty_home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.penalty.Driver
import com.autonomad.utils.*

class PenaltyHomeViewModel : DisposableViewModel() {

    private val apiService = ApiGarageF.apiService

    private val mDrivers = MutableLiveData<Status<List<Driver>>>()
    var drivers: LiveData<Status<List<Driver>>> = mDrivers

    fun loadDrivers() {
        mDrivers.value = Status.loading
        apiService.getDrivers().subscribeOnIo().subscribe({
            mDrivers.value = it.listToStatus()
        }, {
            mDrivers.value = it.localizedMessage.fail
        }).disposeOnCleared()
    }

    fun deleteDriver(id: Int) {
        apiService.deleteDriver(id).subscribeOnIo().subscribe({
            if (it.isSuccessful) loadDrivers()
            else mDrivers.value = it.message().fail
        }, {
            mDrivers.value = it.localizedMessage.fail
        }).disposeOnCleared()
    }
}