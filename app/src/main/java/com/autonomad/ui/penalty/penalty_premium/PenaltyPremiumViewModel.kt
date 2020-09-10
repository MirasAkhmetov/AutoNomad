package com.autonomad.ui.penalty.penalty_premium

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.penalty.Driver
import com.autonomad.utils.*

class PenaltyPremiumViewModel : DisposableViewModel() {
    private val apiService = ApiGarageF.apiService

    private val mDrivers = MutableLiveData<Status<List<Driver>>>()
    val drivers: LiveData<Status<List<Driver>>> = mDrivers

    fun loadDrivers() {
        apiService.getDrivers().subscribeOnIo().subscribe({
            mDrivers.value = it.listToStatus()
        }, {
            mDrivers.value = it.localizedMessage.fail
        }).disposeOnCleared()
    }
}